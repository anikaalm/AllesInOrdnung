package at.ac.hcw.allesinordnung.controller;

import at.ac.hcw.allesinordnung.manager.CollectionManager;
import at.ac.hcw.allesinordnung.model.Book;
import at.ac.hcw.allesinordnung.model.Medium;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BooksController {

    @FXML private ListView<Medium> booksList;
    @FXML private HeaderController headerController;

    private final CollectionManager manager;

    public BooksController() {
        URL dataUrl = getClass().getResource("/data/collection.json");
        String path = (dataUrl != null) ? dataUrl.getPath() : "src/main/resources/data/collection.json";
        this.manager = new CollectionManager(path);
    }

    @FXML
    public void initialize() {
        loadBooks();

        booksList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Medium item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle());
            }
        });

        if (headerController != null) {
            headerController.setOnSearch(this::applySearch);
            headerController.setHomeAction(this::goHomeFromHeader);
            headerController.setSearchPrompt("Suchen...");
        }
    }

    private void loadBooks() {
        List<Medium> onlyBooks = manager.filterByType("BOOK");
        booksList.setItems(FXCollections.observableArrayList(onlyBooks));
    }

    private void applySearch(String query) {
        if (query == null || query.isBlank()) {
            loadBooks();
            return;
        }
        String q = query.toLowerCase();
        List<Medium> filtered = manager.filterByType("BOOK").stream()
                .filter(m -> m.getTitle().toLowerCase().contains(q))
                .collect(Collectors.toList());
        booksList.setItems(FXCollections.observableArrayList(filtered));
    }

    // ------------------- Aktionen (JETZT WIE CollectionController) -------------------

    @FXML
    private void addBook() {
        String title = askText("Titel", "Titel eingeben:");
        if (title == null) return;

        String creator = askText("Autor", "Autor eingeben:");
        if (creator == null) return;

        String genre = askText("Genre", "Genre eingeben:");
        if (genre == null) return;

        Integer year = askInt("Jahr", "Jahr eingeben:");
        if (year == null) return;

        String publisher = askText("Verlag", "Verlag eingeben:");
        if (publisher == null) return;

        Book created = new Book(title, creator, genre, year, publisher);

        manager.addMedium(created);
        loadBooks();

        booksList.getSelectionModel().select(created);
        booksList.scrollTo(created);
    }

    @FXML
    private void editBook() {
        Medium selected = booksList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarn("Kein Eintrag gewählt", "Bitte wähle zuerst ein Buch aus.");
            return;
        }

        if (!(selected instanceof Book book)) {
            showWarn("Falscher Typ", "Der ausgewählte Eintrag ist kein Buch.");
            return;
        }

        String newTitle = askText("Titel bearbeiten", "Neuer Titel:", book.getTitle());
        if (newTitle == null) return;

        String newCreator = askText("Autor", "Neu:", book.getCreator());
        if (newCreator == null) return;

        String newGenre = askText("Genre", "Neu:", book.getGenre());
        if (newGenre == null) return;

        Integer newYear = askInt("Jahr", "Neu:", book.getYear());
        if (newYear == null) return;

        String newPublisher = askText("Verlag", "Neu:", book.getPublisher());
        if (newPublisher == null) return;

        manager.editBook(book, newTitle, newCreator, newGenre, newYear, newPublisher);

        booksList.refresh();
    }

    // ------------------- Navigation -------------------

    private void goHomeFromHeader() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/hcw/allesinordnung/menu-view.fxml"));
            Parent root = loader.load();
            Scene sc = new Scene(root, 900, 600);

            var css = getClass().getResource("/at/ac/hcw/allesinordnung/dark-theme.css");
            if (css != null) sc.getStylesheets().add(css.toExternalForm());

            Stage stage = (Stage) booksList.getScene().getWindow();
            stage.setScene(sc);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ------------------- Dialog-Helfer (wie CollectionController) -------------------

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
