package at.ac.hcw.allesinordnung.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HeaderController {

    @FXML
    private void goToMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/at/ac/hcw/allesinordnung/menu-view.fxml")
            );

            Scene scene = new Scene(loader.load(), 900, 650);
            scene.getStylesheets().add(
                    getClass().getResource("/at/ac/hcw/allesinordnung/dark-theme.css")
                            .toExternalForm()
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSearch() {
        System.out.println("Suche gedrückt");
    }
}
