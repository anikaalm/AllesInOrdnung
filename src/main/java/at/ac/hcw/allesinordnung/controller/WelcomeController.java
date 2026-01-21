package at.ac.hcw.allesinordnung.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;


public class WelcomeController {


    @FXML private Label welcomeTitle;

    @FXML
    private void initialize() {
        // Warten, bis Scene gesetzt ist, dann binden
        Platform.runLater(() -> {
            if (welcomeTitle == null) return; // Sicherheitsnetz
            Scene scene = welcomeTitle.getScene();
            if (scene == null) return;

            double minPx   = 28;   // nie kleiner als 28px
            double maxPx   = 120;

            double divW    = 14.0; // Breite/14 -> Basisgröße
            double divH    = 9.0;


            welcomeTitle.styleProperty().bind(
                    Bindings.createStringBinding(() -> {
                        double w = scene.getWidth();
                        double h = scene.getHeight();
                        double base = Math.min(w / divW, h / divH);
                        double computed = clamp(base, minPx, maxPx);
                        return String.format("-fx-font-size: %.0fpx;", computed);
                    }, scene.widthProperty(), scene.heightProperty())
            );
        });
    }


    // Helfer
    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }



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



