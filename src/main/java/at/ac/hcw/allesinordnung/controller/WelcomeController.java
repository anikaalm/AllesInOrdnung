package at.ac.hcw.allesinordnung.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

    public class WelcomeController {

        @FXML
        private void handleStart() {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/at/ac/hcw/allesinordnung/main-view.fxml"
                        )
                );


                Stage stage = (Stage) Stage.getWindows().filtered(w -> w.isShowing()).get(0);
                stage.setScene(new Scene(loader.load(), 800, 600));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



