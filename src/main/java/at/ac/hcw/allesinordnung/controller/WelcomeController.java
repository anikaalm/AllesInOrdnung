package at.ac.hcw.allesinordnung.controller;


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
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/at/ac/hcw/allesinordnung/main-view.fxml"
                        )
                );


                Parent root = loader.load();

                Scene newScene = new Scene(root, 600, 400);


                // Dark Theme anwenden
                newScene.getStylesheets().add(
                        getClass().getResource("/at/ac/hcw/allesinordnung/dark-theme.css")
                                .toExternalForm()
                );



                Stage stage = (Stage) ((Scene) ((javafx.scene.Node) event.getSource()).getScene()).getWindow();
                stage.setScene(newScene);
                stage.show();


            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,
                        "Konnte main-view.fxml nicht laden:\n" + e.getMessage()
                ).showAndWait();

            }
        }
    }



