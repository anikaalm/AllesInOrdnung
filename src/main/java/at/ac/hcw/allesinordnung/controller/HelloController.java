package at.ac.hcw.allesinordnung.controller;

import at.ac.hcw.allesinordnung.manager.CollectionManager;
import at.ac.hcw.allesinordnung.model.Medium;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class HelloController {


    // Aktueller Label und Button
    @FXML
    private Label welcomeText;
    @FXML
    private Button helloButton;

    // TableView für Medien
    @FXML
    private TableView<Medium> mediaTable;
    @FXML
    private TableColumn<Medium, String> titleColumn;
    @FXML
    private TableColumn<Medium, String> creatorColumn;
    @FXML
    private TableColumn<Medium, String> typeColumn;

    private CollectionManager manager = new CollectionManager("src/main/resources/data/collection.json");

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
        // Tabelle laden
        loadMediaTable();
    }
    private void loadMediaTable() {
        // Spalten verbinden
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        creatorColumn.setCellValueFactory(new PropertyValueFactory<>("creator"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        mediaTable.setItems(FXCollections.observableArrayList(manager.showAllMedia()));
    }
}