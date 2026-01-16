package at.ac.hcw.allesinordnung.controller;

import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.input.MouseEvent;

public class WelcomeController {


        @FXML
        private void handleStart(MouseEvent event) {
            System.out.println("Klick – weiter zur Hauptansicht");

            try {

                var url = getClass().getResource("/at/ac/hcw/allesinordnung/menu-view.fxml");
                if (url == null) {
                    throw new IllegalStateException("menu-view.fxml nicht gefunden. " +
                            "Erwarte: src/main/resources/at/ac/hcw/allesinordnung/menu-view.fxml");
                }

                FXMLLoader loader = new FXMLLoader(
                        /*getClass().getResource(
                                "/at/ac/hcw/allesinordnung/main-view.fxml" )*/
                        url);


                Parent root = loader.load();

                Scene newScene = new Scene(root, 900, 650);


                // Dark Theme anwenden
                newScene.getStylesheets().add(
                        getClass().getResource("/at/ac/hcw/allesinordnung/dark-theme.css")
                                .toExternalForm()
                );



                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(newScene);
                stage.show();


            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,
                        "Konnte menu-view.fxml nicht laden:\n" + e.getMessage()
                ).showAndWait();

            }
        }
}



