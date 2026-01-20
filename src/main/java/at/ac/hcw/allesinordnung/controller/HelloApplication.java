
package at.ac.hcw.allesinordnung.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/at/ac/hcw/allesinordnung/welcome-view.fxml")
        );



        Scene scene = new Scene(loader.load(), 600, 400);


        // Dark Theme global für die erste Szene aktivieren
        scene.getStylesheets().add(getClass()
                .getResource("/at/ac/hcw/allesinordnung/dark-theme.css")
                .toExternalForm());


        stage.setTitle("Alles in Ordnung");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }

}
