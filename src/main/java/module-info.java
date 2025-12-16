module at.ac.hcw.allesinordnung {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.ac.hcw.allesinordnung to javafx.fxml;
    exports at.ac.hcw.allesinordnung;
}