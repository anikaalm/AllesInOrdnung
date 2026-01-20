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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Node;

import at.ac.hcw.allesinordnung.util.AppContext;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BooksController {


    // ------------------- UI -------------------
    @FXML private ListView<Medium> booksList;
    @FXML private HeaderController headerController;

    private CollectionManager manager;

    // Dropdown-Werte (kannst du beliebig anpassen)
    private static final List<String> GENRES = List.of(
            "Roman",
            "Sachbuch",
            "Fantasy",
            "Krimi",
            "Biografie",
            "Science Fiction",
            "Kinderbuch",
            "Sonstiges"
    );

    public BooksController() {}


    @FXML
    public void initialize() {
        this.manager = AppContext.getManager();
        var books = manager.filterByType("BOOK");
        booksList.setItems(FXCollections.observableArrayList(books));


        // ➜ Ab hier: Nur Titel anzeigen
        booksList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Medium item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTitle()); // nur der Titel
                }
            }
        });

        // 🔗 WICHTIG: Home-Button im Header mit dieser View verknüpfen
        if (headerController != null) {
            headerController.setHomeAction(this::goHomeFromHeader);
        } else {
            System.out.println("[Books] headerController ist null – prüfe <fx:include fx:id=\"header\" ...> und module-info.");
        }
    }





    private void loadBooks() {
        List<Medium> onlyBooks = manager.filterByType("BOOK");

        onlyBooks.sort(
                (a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle())
        );

        booksList.setItems(FXCollections.observableArrayList(onlyBooks));
    }


    private void applySearch(String query) {
        if (query == null || query.isBlank()) {
            loadBooks();
            return;
        }

        String q = query.trim().toLowerCase();

        List<Medium> filtered = manager.filterByType("BOOK").stream()
                .filter(m ->
                        containsIgnoreCase(m.getTitle(), q)
                                || containsIgnoreCase(m.getCreator(), q)
                                || containsIgnoreCase(m.getGenre(), q)
                                || (m instanceof Book b && containsIgnoreCase(b.getPublisher(), q))
                                || String.valueOf(m.getYear()).contains(q)
                )
                .collect(Collectors.toList());

        filtered.sort(
                (a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle())
        );

        booksList.setItems(FXCollections.observableArrayList(filtered));
    }

    private boolean containsIgnoreCase(String value, String q) {
        return value != null && value.toLowerCase().contains(q);
    }


    // ------------------- Aktionen -------------------

    @FXML
    private void addBook() {
        BookFormResult r = showBookFormDialog(
                "Buch hinzufügen",
                "", "", GENRES.get(0), "", ""
        );
        if (r == null) return;

        Book created = new Book(r.title, r.creator, r.genre, r.year, r.publisher);
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

        String presetGenre = (book.getGenre() != null && !book.getGenre().isBlank())
                ? book.getGenre()
                : GENRES.get(0);

        BookFormResult r = showBookFormDialog(
                "Buch bearbeiten",
                book.getTitle(),
                book.getCreator(),
                presetGenre,
                String.valueOf(book.getYear()),
                book.getPublisher()
        );
        if (r == null) return;

        manager.editBook(book, r.title, r.creator, r.genre, r.year, r.publisher);
        booksList.refresh();
    }

    // ------------------- Dialog: Formular mit Dropdown -------------------

    private BookFormResult showBookFormDialog(
            String title,
            String presetTitle,
            String presetCreator,
            String presetGenre,
            String presetYear,
            String presetPublisher
    ) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);

        ButtonType saveBtn = new ButtonType("Speichern", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField titleField = new TextField(presetTitle);
        TextField creatorField = new TextField(presetCreator);
        TextField yearField = new TextField(presetYear);
        TextField publisherField = new TextField(presetPublisher);

        ComboBox<String> genreBox = new ComboBox<>();
        genreBox.getItems().addAll(GENRES);
        // falls alter Genre-Wert nicht in Liste ist, trotzdem anzeigen
        if (presetGenre != null && !presetGenre.isBlank() && !genreBox.getItems().contains(presetGenre)) {
            genreBox.getItems().add(0, presetGenre);
        }
        genreBox.setValue((presetGenre == null || presetGenre.isBlank()) ? GENRES.get(0) : presetGenre);

        grid.addRow(0, new Label("Titel:"), titleField);
        grid.addRow(1, new Label("Autor:"), creatorField);
        grid.addRow(2, new Label("Genre:"), genreBox);
        grid.addRow(3, new Label("Jahr:"), yearField);
        grid.addRow(4, new Label("Verlag:"), publisherField);

        dialog.getDialogPane().setContent(grid);

        // Validierung: Speichern nur wenn alles ok
        Node saveButtonNode = dialog.getDialogPane().lookupButton(saveBtn);
        saveButtonNode.addEventFilter(javafx.event.ActionEvent.ACTION, evt -> {
            String t = titleField.getText() == null ? "" : titleField.getText().trim();
            String c = creatorField.getText() == null ? "" : creatorField.getText().trim();
            String g = genreBox.getValue() == null ? "" : genreBox.getValue().trim();
            String y = yearField.getText() == null ? "" : yearField.getText().trim();
            String p = publisherField.getText() == null ? "" : publisherField.getText().trim();

            if (t.isBlank() || c.isBlank() || g.isBlank() || y.isBlank() || p.isBlank()) {
                showWarn("Fehlende Eingaben", "Bitte fülle alle Felder aus.");
                evt.consume();
                return;
            }

            try {
                Integer.parseInt(y);
            } catch (NumberFormatException ex) {
                showWarn("Ungültiges Jahr", "Bitte gib beim Jahr eine gültige Zahl ein.");
                evt.consume();
            }
        });

        Optional<ButtonType> res = dialog.showAndWait();
        if (res.isEmpty() || res.get() != saveBtn) return null;

        // sicher, weil Validierung vorher
        int year = Integer.parseInt(yearField.getText().trim());

        return new BookFormResult(
                titleField.getText().trim(),
                creatorField.getText().trim(),
                genreBox.getValue().trim(),
                year,
                publisherField.getText().trim()
        );
    }

    private static class BookFormResult {
        final String title;
        final String creator;
        final String genre;
        final int year;
        final String publisher;

        BookFormResult(String title, String creator, String genre, int year, String publisher) {
            this.title = title;
            this.creator = creator;
            this.genre = genre;
            this.year = year;
            this.publisher = publisher;
        }
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

    // ------------------- Helper -------------------

    private static void showWarn(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }
}