package at.ac.hcw.allesinordnung.controller;

import at.ac.hcw.allesinordnung.manager.CollectionManager;
import at.ac.hcw.allesinordnung.model.*;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Optional;

public class CollectionController {

    @FXML private ListView<Medium> mediaListView;
    @FXML private HeaderController headerController;

    private final CollectionManager manager;

    public CollectionController() {
        URL dataUrl = getClass().getResource("/data/collection.json");
        String path = (dataUrl != null) ? dataUrl.getPath() : "src/main/resources/data/collection.json";
        this.manager = new CollectionManager(path);
    }

    @FXML
    public void initialize() {
        // initial laden
        showAll();

        // ListView nur Titel
        mediaListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Medium item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle());
            }
        });

        // Header anbinden (Home + Search)
        if (headerController != null) {
            headerController.setHomeAction(this::goHomeFromHeader);
            headerController.setOnSearch(this::applyQuery);
            headerController.setSearchPrompt("Suchen...");
        } else {
            System.out.println("WARN: headerController ist null in CollectionController");
        }
    }

    // ------------------- TOP Buttons (wie Books) -------------------

    @FXML
    private void addMedium() {
        ChoiceDialog<String> typeDialog = new ChoiceDialog<>("BOOK", "BOOK", "CD", "DVD");
        typeDialog.setHeaderText("Medium hinzufügen");
        typeDialog.setContentText("Typ auswählen:");

        Optional<String> typeOpt = typeDialog.showAndWait();
        if (typeOpt.isEmpty()) return;

        String type = typeOpt.get();

        String title = askText("Titel", "Titel eingeben:");
        if (title == null) return;

        String creator = askText("Autor/Künstler", "Name eingeben:");
        if (creator == null) return;

        String genre = askText("Genre", "Genre eingeben:");
        if (genre == null) return;

        Integer year = askInt("Jahr", "Jahr eingeben:");
        if (year == null) return;

        Medium created = null;

        if ("BOOK".equals(type)) {
            String publisher = askText("Verlag", "Verlag eingeben:");
            if (publisher == null) return;
            created = new Book(title, creator, genre, year, publisher);

        } else if ("CD".equals(type)) {
            Integer runtime = askInt("Laufzeit", "Laufzeit (Minuten) eingeben:");
            if (runtime == null) return;
            created = new Cd(title, creator, genre, year, runtime);

        } else if ("DVD".equals(type)) {
            Integer runtime = askInt("Laufzeit", "Laufzeit (Minuten) eingeben:");
            if (runtime == null) return;
            created = new Dvd(title, creator, genre, year, runtime);
        }

        if (created == null) return;

        manager.addMedium(created);
        showAll();

        // optional: neu eingefügtes Element auswählen
        mediaListView.getSelectionModel().select(created);
        mediaListView.scrollTo(created);
    }

    @FXML
    public void editSelected() {
        Medium selected = mediaListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarn("Kein Eintrag gewählt", "Bitte wähle zuerst ein Medium aus.");
            return;
        }

        // gemeinsame Felder
        String newTitle = askText("Titel bearbeiten", "Neuer Titel:", selected.getTitle());
        if (newTitle == null) return;

        String newCreator = askText("Autor/Künstler", "Neu:", selected.getCreator());
        if (newCreator == null) return;

        String newGenre = askText("Genre", "Neu:", selected.getGenre());
        if (newGenre == null) return;

        Integer newYear = askInt("Jahr", "Neu:", selected.getYear());
        if (newYear == null) return;

        // typ-spezifisch
        if (selected instanceof Book book) {
            String publisher = askText("Verlag", "Neu:", book.getPublisher());
            if (publisher == null) return;
            manager.editBook(book, newTitle, newCreator, newGenre, newYear, publisher);

        } else if (selected instanceof Cd cd) {
            Integer runtime = askInt("Laufzeit", "Neu (Minuten):", cd.getRuntime());
            if (runtime == null) return;
            manager.editCd(cd, newTitle, newCreator, newGenre, newYear, runtime);

        } else if (selected instanceof Dvd dvd) {
            Integer runtime = askInt("Laufzeit", "Neu (Minuten):", dvd.getRuntime());
            if (runtime == null) return;
            manager.editDvd(dvd, newTitle, newCreator, newGenre, newYear, runtime);
        }

        mediaListView.refresh();
    }

    // ------------------- Laden/Filter (kannst du behalten) -------------------

    @FXML
    public void showAll() {
        mediaListView.setItems(FXCollections.observableArrayList(manager.showAllMedia()));
    }

    @FXML
    public void showBooks() {
        mediaListView.setItems(FXCollections.observableArrayList(manager.filterByType("BOOK")));
    }

    @FXML
    public void showCds() {
        mediaListView.setItems(FXCollections.observableArrayList(manager.filterByType("CD")));
    }

    @FXML
    public void showDvds() {
        mediaListView.setItems(FXCollections.observableArrayList(manager.filterByType("DVD")));
    }

    // Header-Suche
    public void applyQuery(String q) {
        if (q == null || q.isBlank()) { showAll(); return; }

        LinkedHashSet<Medium> set = new LinkedHashSet<>();
        set.addAll(manager.searchByTitle(q));
        set.addAll(manager.searchByCreator(q));
        try {
            int year = Integer.parseInt(q.trim());
            set.addAll(manager.searchByYear(year));
        } catch (NumberFormatException ignore) {}
        set.addAll(manager.searchByGenre(q));

        mediaListView.setItems(FXCollections.observableArrayList(set));
    }

    // ------------------- Navigation -------------------

    private void goHomeFromHeader() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/hcw/allesinordnung/menu-view.fxml"));
            Parent root = loader.load();
            Scene sc = new Scene(root, 900, 600);

            var css = getClass().getResource("/at/ac/hcw/allesinordnung/dark-theme.css");
            if (css != null) sc.getStylesheets().add(css.toExternalForm());

            Stage stage = (Stage) mediaListView.getScene().getWindow();
            stage.setScene(sc);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ------------------- Dialog-Helfer -------------------

    private static String askText(String header, String content) {
        TextInputDialog d = new TextInputDialog();
        d.setHeaderText(header);
        d.setContentText(content);
        return d.showAndWait().map(String::trim).filter(s -> !s.isBlank()).orElse(null);
    }

    private static String askText(String header, String content, String preset) {
        TextInputDialog d = new TextInputDialog(preset);
        d.setHeaderText(header);
        d.setContentText(content);
        return d.showAndWait().map(String::trim).filter(s -> !s.isBlank()).orElse(null);
    }

    private static Integer askInt(String header, String content) {
        TextInputDialog d = new TextInputDialog();
        d.setHeaderText(header);
        d.setContentText(content);
        Optional<String> r = d.showAndWait();
        if (r.isEmpty()) return null;
        try { return Integer.parseInt(r.get().trim()); }
        catch (NumberFormatException e) { showWarn("Ungültige Zahl", "Bitte gib eine gültige Zahl ein."); return null; }
    }

    private static Integer askInt(String header, String content, int preset) {
        TextInputDialog d = new TextInputDialog(String.valueOf(preset));
        d.setHeaderText(header);
        d.setContentText(content);
        Optional<String> r = d.showAndWait();
        if (r.isEmpty()) return null;
        try { return Integer.parseInt(r.get().trim()); }
        catch (NumberFormatException e) { showWarn("Ungültige Zahl", "Bitte gib eine gültige Zahl ein."); return null; }
    }

    private static void showWarn(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }
}
