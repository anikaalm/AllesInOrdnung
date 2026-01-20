module at.ac.hcw.allesinordnung {

    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Gson
    requires com.google.gson;
    requires java.desktop;

    // JavaFX darf App + Controller per Reflection erzeugen
    opens at.ac.hcw.allesinordnung to javafx.graphics, javafx.fxml;
    opens at.ac.hcw.allesinordnung.controller to javafx.fxml;

    // Gson darf auf Model & Persistence zugreifen
    opens at.ac.hcw.allesinordnung.model to com.google.gson;
    opens at.ac.hcw.allesinordnung.persistence to com.google.gson;

    // Optional: Export
    exports at.ac.hcw.allesinordnung.controller;
    exports at.ac.hcw.allesinordnung.model;
}
