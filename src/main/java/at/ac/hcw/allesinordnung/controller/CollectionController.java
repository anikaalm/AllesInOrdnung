package at.ac.hcw.allesinordnung.controller;

import at.ac.hcw.allesinordnung.manager.CollectionManager;
import at.ac.hcw.allesinordnung.model.*;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Optional;

import javafx.event.ActionEvent;

public class CollectionController {

    @FXML private ListView<Medium> mediaListView;
    @FXML private HeaderController headerController;
    @FXML private ComboBox<String> typeFilterBox; // falls du die Filter-ComboBox eingebaut hast

    private final CollectionManager manager;

    public CollectionController() {
        String path = java.nio.file.Paths.get(
                System.getProperty("user.home"),
                "allesinordnung",
                "collection.json"
        ).toString();

        this.manager = new CollectionManager(path);
    }


    @FXML
    public void initialize() {
        showAll();

        mediaListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Medium item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle());
            }
        });

        // Filter ComboBox (optional)
        if (typeFilterBox != null) {
            typeFilterBox.setItems(FXCollections.observableArrayList("Alle", "Bücher", "CDs", "DVDs"));
            typeFilterBox.setValue("Alle");
            typeFilterBox.valueProperty().addListener((obs, o, n) -> refreshByCurrentFilter());
        }

        if (headerController != null) {
            headerController.setTitle("PicassoCollective");
            headerController.setHomeAction(this::goHomeFromHeader);
            headerController.setOnSearch(this::applyQuery);
        }

    }

    // ------------------- EIN Dialog fürs Hinzufügen -------------------

    @FXML
    private void addMedium() {
        MediumFormResult r = showMediumFormDialog(
                "Medium hinzufügen",
                "BOOK",
                "", "", "", "",
                "", "" // publisher, runtime
        );
        if (r == null) return;

        Medium created = buildMediumFromForm(r);
        if (created == null) return;

        manager.addMedium(created);
        refreshByCurrentFilter();

        mediaListView.getSelectionModel().select(created);
        mediaListView.scrollTo(created);
    }

    // ------------------- EIN Dialog fürs Bearbeiten -------------------

    @FXML
    public void editSelected() {
        Medium selected = mediaListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarn("Kein Eintrag gewählt", "Bitte wähle zuerst ein Medium aus.");
            return;
        }

        String presetType = selected.getType(); // "BOOK" / "CD" / "DVD"
        String presetPublisher = "";
        String presetRuntime = "";

        if (selected instanceof Book b) presetPublisher = b.getPublisher();
        if (selected instanceof Cd c) presetRuntime = String.valueOf(c.getRuntime());
        if (selected instanceof Dvd d) presetRuntime = String.valueOf(d.getRuntime());

        MediumFormResult r = showMediumFormDialog(
                "Medium bearbeiten",
                presetType,
                selected.getTitle(),
                selected.getCreator(),
                selected.getGenre(),
                String.valueOf(selected.getYear()),
                presetPublisher,
                presetRuntime
        );
        if (r == null) return;

        // jetzt typ-spezifisch speichern
        if (selected instanceof Book book) {
            manager.editBook(book, r.title, r.creator, r.genre, r.year, r.publisher);
        } else if (selected instanceof Cd cd) {
            manager.editCd(cd, r.title, r.creator, r.genre, r.year, r.runtime);
        } else if (selected instanceof Dvd dvd) {
            manager.editDvd(dvd, r.title, r.creator, r.genre, r.year, r.runtime);
        }

        mediaListView.refresh();
    }

    // ------------------- Dialog-Bau (alles in einem Fenster) -------------------

    private MediumFormResult showMediumFormDialog(
            String dialogTitle,
            String presetType,
            String presetTitle,
            String presetCreator,
            String presetGenre,
            String presetYear,
            String presetPublisher,
            String presetRuntime
    ) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);

        ButtonType saveBtn = new ButtonType("Speichern", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Typ Dropdown
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("BOOK", "CD", "DVD");
        typeBox.setValue(presetType == null || presetType.isBlank() ? "BOOK" : presetType);

        TextField titleField = new TextField(presetTitle);
        TextField creatorField = new TextField(presetCreator);
        TextField genreField = new TextField(presetGenre);
        TextField yearField = new TextField(presetYear);

        // Typ-spezifisch
        Label publisherLabel = new Label("Verlag:");
        TextField publisherField = new TextField(presetPublisher);

        Label runtimeLabel = new Label("Laufzeit (Min):");
        TextField runtimeField = new TextField(presetRuntime);

        grid.addRow(0, new Label("Typ:"), typeBox);
        grid.addRow(1, new Label("Titel:"), titleField);
        grid.addRow(2, new Label("Autor/Künstler:"), creatorField);
        grid.addRow(3, new Label("Genre:"), genreField);
        grid.addRow(4, new Label("Jahr:"), yearField);

        // Zeile 5 wird dynamisch (Verlag ODER Laufzeit)
        // wir legen beide an und blenden um
        grid.addRow(5, publisherLabel, publisherField);
        grid.addRow(6, runtimeLabel, runtimeField);

        Runnable updateTypeFields = () -> {
            String t = typeBox.getValue();
            boolean isBook = "BOOK".equals(t);

            publisherLabel.setManaged(isBook);
            publisherLabel.setVisible(isBook);
            publisherField.setManaged(isBook);
            publisherField.setVisible(isBook);

            runtimeLabel.setManaged(!isBook);
            runtimeLabel.setVisible(!isBook);
            runtimeField.setManaged(!isBook);
            runtimeField.setVisible(!isBook);
        };

        updateTypeFields.run();
        typeBox.valueProperty().addListener((obs, oldV, newV) -> updateTypeFields.run());

        dialog.getDialogPane().setContent(grid);

        Node saveButtonNode = dialog.getDialogPane().lookupButton(saveBtn);
        saveButtonNode.addEventFilter(ActionEvent.ACTION, (ActionEvent evt) -> {
            String t = safeTrim(typeBox.getValue());
            String title = safeTrim(titleField.getText());
            String creator = safeTrim(creatorField.getText());
            String genre = safeTrim(genreField.getText());
            String yearStr = safeTrim(yearField.getText());
            String publisher = safeTrim(publisherField.getText());
            String runtimeStr = safeTrim(runtimeField.getText());

            if (title.isBlank() || creator.isBlank() || genre.isBlank() || yearStr.isBlank()) {
                showWarn("Fehlende Eingaben", "Bitte fülle Titel, Autor/Künstler, Genre und Jahr aus.");
                evt.consume();
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearStr);
            } catch (NumberFormatException ex) {
                showWarn("Ungültiges Jahr", "Bitte gib beim Jahr eine gültige Zahl ein.");
                evt.consume();
                return;
            }

            if ("BOOK".equals(t)) {
                if (publisher.isBlank()) {
                    showWarn("Fehlende Eingaben", "Bitte gib einen Verlag ein.");
                    evt.consume();
                }
            } else { // CD / DVD
                if (runtimeStr.isBlank()) {
                    showWarn("Fehlende Eingaben", "Bitte gib eine Laufzeit ein.");
                    evt.consume();
                    return;
                }
                try {
                    Integer.parseInt(runtimeStr);
                } catch (NumberFormatException ex) {
                    showWarn("Ungültige Laufzeit", "Bitte gib bei Laufzeit eine gültige Zahl ein.");
                    evt.consume();
                }
            }
        });

        Optional<ButtonType> res = dialog.showAndWait();
        if (res.isEmpty() || res.get() != saveBtn) return null;

        // Ergebnis bauen
        String t = safeTrim(typeBox.getValue());
        int year = Integer.parseInt(yearField.getText().trim());

        int runtime = 0;
        if (!"BOOK".equals(t)) runtime = Integer.parseInt(runtimeField.getText().trim());

        return new MediumFormResult(
                t,
                titleField.getText().trim(),
                creatorField.getText().trim(),
                genreField.getText().trim(),
                year,
                publisherField.getText().trim(),
                runtime
        );
    }

    private Medium buildMediumFromForm(MediumFormResult r) {
        return switch (r.type) {
            case "BOOK" -> new Book(r.title, r.creator, r.genre, r.year, r.publisher);
            case "CD" -> new Cd(r.title, r.creator, r.genre, r.year, r.runtime);
            case "DVD" -> new Dvd(r.title, r.creator, r.genre, r.year, r.runtime);
            default -> null;
        };
    }

    private static class MediumFormResult {
        final String type;
        final String title;
        final String creator;
        final String genre;
        final int year;
        final String publisher;
        final int runtime;

        MediumFormResult(String type, String title, String creator, String genre, int year, String publisher, int runtime) {
            this.type = type;
            this.title = title;
            this.creator = creator;
            this.genre = genre;
            this.year = year;
            this.publisher = publisher;
            this.runtime = runtime;
        }
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

    // ------------------- Filter / Suche -------------------

    @FXML
    public void showAll() {
        var list = manager.showAllMedia();

        list.sort(
                (a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle())
        );

        mediaListView.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    public void showBooks() {
        var list = manager.filterByType("BOOK");

        list.sort(
                (a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle())
        );

        mediaListView.setItems(FXCollections.observableArrayList(list));
    }


    @FXML
    public void showCds() {
        var list = manager.filterByType("CD");

        list.sort(
                (a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle())
        );

        mediaListView.setItems(FXCollections.observableArrayList(list));
    }


    @FXML
    public void showDvds() {
        var list = manager.filterByType("DVD");

        list.sort(
                (a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle())
        );

        mediaListView.setItems(FXCollections.observableArrayList(list));
    }


    private void refreshByCurrentFilter() {
        if (typeFilterBox == null || typeFilterBox.getValue() == null) {
            showAll();
            return;
        }
        switch (typeFilterBox.getValue()) {
            case "Bücher" -> showBooks();
            case "CDs" -> showCds();
            case "DVDs" -> showDvds();
            default -> showAll();
        }
    }


    public void applyQuery(String q) {
        if (q == null || q.isBlank()) {
            refreshByCurrentFilter();
            return;
        }

        LinkedHashSet<Medium> set = new LinkedHashSet<>();
        set.addAll(manager.searchByTitle(q));
        set.addAll(manager.searchByCreator(q));

        try {
            int year = Integer.parseInt(q.trim());
            set.addAll(manager.searchByYear(year));
        } catch (NumberFormatException ignore) {}

        set.addAll(manager.searchByGenre(q));

        var list = new java.util.ArrayList<>(set);

        list.sort(
                (a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle())
        );

        mediaListView.setItems(FXCollections.observableArrayList(list));
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

    // ------------------- Placeholder für deine Buttons -------------------

    @FXML private void deleteSelected() { showWarn("TODO", "deleteSelected() ist noch nicht implementiert."); }
    @FXML private void toggleFavorite() { showWarn("TODO", "toggleFavorite() ist noch nicht implementiert."); }
    @FXML private void setFolderForSelected() { showWarn("TODO", "setFolderForSelected() ist noch nicht implementiert."); }

    // ------------------- Warn -------------------

    private static void showWarn(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }
}
