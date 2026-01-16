
package at.ac.hcw.allesinordnung.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MenuController {

    @FXML private TextField searchField;

    // --- Navigation helpers --------------------------------------------------

    private void gotoCollection(Node any, java.util.function.Consumer<CollectionController> afterLoad) {
        try {
            var url = getClass().getResource("/at/ac/hcw/allesinordnung/main-view.fxml");
            if (url == null) throw new IllegalStateException("main-view.fxml nicht gefunden");

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            CollectionController controller = loader.getController();

            // Optionale Nach-Initialisierung (Filter/Suche anwenden)
            if (afterLoad != null) afterLoad.accept(controller);

            Scene newScene = new Scene(root, 900, 650);
            newScene.getStylesheets().add(
                    getClass().getResource("/at/ac/hcw/allesinordnung/dark-theme.css").toExternalForm()
            );

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

    // --- Menü-Aktionen -------------------------------------------------------

    @FXML
    private void openBooks(javafx.event.ActionEvent e) {
        gotoCollection((Node) e.getSource(), CollectionController::showBooks);
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
        // TODO: Deinen Persistence-Workflow hier anstoßen
        // Beispiel: JsonFileStorage.createEmptyIfMissing(...); danach openAll:
        gotoCollection((Node) e.getSource(), CollectionController::showAll);
    }

    @FXML
    private void onSearch(javafx.event.ActionEvent e) {
        String q = searchField.getText();
        gotoCollection((Node) e.getSource(), c -> c.applyQuery(q));
    }
}
