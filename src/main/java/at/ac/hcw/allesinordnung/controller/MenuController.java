//Navigation zwischen den Seiten (Bücher, CDs, DVDs, Alle),
// Initialisierung des Headers, Szenenwechsel (FXML laden, Scene setzen)
package at.ac.hcw.allesinordnung.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MenuController {

    @FXML private HeaderController headerController;

    @FXML
    private void initialize() {
        // Im Menü: "Willkommen :)" anzeigen
        if (headerController != null) {
            headerController.setTitle("Willkommen :)");
            headerController.setWelcomeVisible(true);
        }
    }


    // Gemeinsamer Loader für die Medienübersicht (main-view.fxml)
    private void gotoCollection(Node any, java.util.function.Consumer<CollectionController> afterLoad) {
        try {
            var url = getClass().getResource("/at/ac/hcw/allesinordnung/collection-view.fxml");
            if (url == null) throw new IllegalStateException("collection-view.fxml nicht gefunden");

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            CollectionController controller = loader.getController();


            if (controller == null) {
                throw new IllegalStateException("Controller aus collection-view.fxml ist null");
            }

            if (afterLoad != null) afterLoad.accept(controller);


            Scene newScene = new Scene(root, 900, 650);
            var css = getClass().getResource("/at/ac/hcw/allesinordnung/dark-theme.css");
            if (css != null) newScene.getStylesheets().add(css.toExternalForm());

            Stage stage = (Stage) any.getScene().getWindow();
            stage.setScene(newScene);
            stage.setTitle("Medienübersicht");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Seitenwechsel fehlgeschlagen:\n" + ex.getMessage())
                    .showAndWait();
        }
    }

    // ------------------------------------------------------------
    // NEU: Direkter Loader für die Bücher-Seite (books-view.fxml)
    // ------------------------------------------------------------
    private void gotoBooks(Node any) {
        try {
            var url = getClass().getResource("/at/ac/hcw/allesinordnung/books-view.fxml");
            if (url == null) throw new IllegalStateException("books-view.fxml nicht gefunden");

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            Scene newScene = new Scene(root, 900, 650);
            var css = getClass().getResource("/at/ac/hcw/allesinordnung/dark-theme.css");
            if (css != null) newScene.getStylesheets().add(css.toExternalForm());

            Stage stage = (Stage) any.getScene().getWindow();
            stage.setScene(newScene);
            stage.setTitle("Bücher");
            stage.show();

            // Debug-Hinweis in der Konsole:
            System.out.println("DEBUG: books-view.fxml wurde geladen");
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Seitenwechsel (Bücher) fehlgeschlagen:\n" + ex.getMessage())
                    .showAndWait();
        }
    }

    // --------------------- Menü-Aktionen ------------------------

    @FXML
    private void openBooks(javafx.event.ActionEvent e) {
        // ALT (führt immer zur Medienübersicht):
        // gotoCollection((Node) e.getSource(), CollectionController::showBooks);

        // NEU (führt zur Bücher-Seite, wie in deinem Mockup):
        gotoBooks((Node) e.getSource());
    }

    @FXML
    private void openDvds(javafx.event.ActionEvent e) {
        gotoCollection((Node) e.getSource(), CollectionController::showDvds);
    }

    @FXML
    private void openCds(javafx.event.ActionEvent e) {
        gotoCollection((Node) e.getSource(), CollectionController::showCds);
    }

    @FXML
    private void openAll(javafx.event.ActionEvent e) {
        gotoCollection((Node) e.getSource(), CollectionController::showAll);
    }

    @FXML
    private void createFile(javafx.event.ActionEvent e) {
        gotoCollection((Node) e.getSource(), CollectionController::showAll);
    }

    // Falls du eine Suche im Menü hast, die direkt in die Übersicht springen soll:
    @FXML
    private void onSearch(javafx.event.ActionEvent e) {
        // Wenn die Suche im Menü bleiben und in die Übersicht leiten soll:
        // String q = searchField.getText();
        // gotoCollection((Node) e.getSource(), c -> c.applyQuery(q));

        // Wenn die Suche für Bücher gelten soll, könntest du stattdessen gotoBooks(e) aufrufen
        // und die Suche in BooksController verarbeiten (später).
        gotoBooks((Node) e.getSource());
    }
}
