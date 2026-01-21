package at.ac.hcw.allesinordnung;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {


        Font loaded = Font.loadFont(
                getClass().getResourceAsStream(
                        "/at/ac/hcw/allesinordnung/fonts/PressStart2P-Regular.ttf"
                ),
                10 // Größe nur für die Registrierung, CSS bestimmt später die echte Größe
        );
        System.out.println("Loaded font: " + (loaded != null ? loaded.getName() : "null"));



                FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/at/ac/hcw/allesinordnung/welcome-view.fxml"
                )
        );

        Scene scene = new Scene(loader.load(), 900, 650);

        // Dark Theme global für die erste Szene aktivieren
        scene.getStylesheets().add(getClass() .getResource("/at/ac/hcw/allesinordnung/dark-theme.css") .toExternalForm());


        stage.setTitle("Alles in Ordnung");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
