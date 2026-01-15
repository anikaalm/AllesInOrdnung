module at.ac.hcw.allesinordnung {

    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Gson
    requires com.google.gson;

    // JavaFX darf auf Controller zugreifen
    opens at.ac.hcw.allesinordnung.controller to javafx.fxml;

    // Gson darf auf Model & Persistence zugreifen
    opens at.ac.hcw.allesinordnung.model to com.google.gson;
    opens at.ac.hcw.allesinordnung.persistence to com.google.gson;

    // Optional: Export (für saubere Modulgrenzen)
    exports at.ac.hcw.allesinordnung.controller;
    exports at.ac.hcw.allesinordnung.model;

}
