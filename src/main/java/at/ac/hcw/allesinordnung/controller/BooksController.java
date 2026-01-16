package at.ac.hcw.allesinordnung.controller;

import at.ac.hcw.allesinordnung.manager.CollectionManager;
import at.ac.hcw.allesinordnung.model.Medium;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BooksController {

    // ------------------- UI -------------------
    @FXML private ListView<Medium> booksList;
    @FXML private HeaderController headerController; // vom fx:include

    private final CollectionManager manager;

    public BooksController() {
        // Lade JSON aus Ressourcen (IDE + JAR kompatibel)
        URL dataUrl = getClass().getResource("/data/collection.json");
        String path = (dataUrl != null) ? dataUrl.getPath() : "src/main/resources/data/collection.json";
        this.manager = new CollectionManager(path);
    }

    @FXML
    public void initialize() {
        // Lade Bücher und zeige nur Titel
        loadBooks();

        // Zelle so definieren, dass nur der Titel angezeigt wird
        booksList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Medium item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle());
            }
        });

        // Header-Suche anbinden
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

    /** Filter für Suchfeld */
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

    // ------------------- Aktionen -------------------

    @FXML
    private void addBook() {
        showInfo("Hinzufügen", "Hier öffnest du den Dialog zum Hinzufügen (TODO).");
    }

    @FXML
    private void editBook() {
        Medium sel = booksList.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showWarn("Kein Eintrag gewählt", "Bitte wähle zuerst ein Buch aus.");
            return;
        }
        showInfo("Bearbeiten", "Hier öffnest du den Dialog zum Bearbeiten von: " + sel.getTitle());
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

    // ------------------- Dialog-Helfer -------------------

    private static void showInfo(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }

    private static void showWarn(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }
}
