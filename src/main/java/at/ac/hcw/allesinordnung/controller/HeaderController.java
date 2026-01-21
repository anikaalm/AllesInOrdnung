package at.ac.hcw.allesinordnung.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Label;

import java.util.function.Consumer;

public class HeaderController {

    @FXML private Button homeButton;
    @FXML private Button searchButton;
    @FXML private TextField searchField;
    @FXML private Label welcomeTitle;



    public void setTitle(String text) {
        if (welcomeTitle != null) {
            welcomeTitle.setText(text);
            setWelcomeVisible(true);
        }
    }

    public void setWelcomeVisible(boolean visible) {
        if (welcomeTitle != null) {
            welcomeTitle.setVisible(visible);
            welcomeTitle.setManaged(visible); // nimmt bei false keinen Platz ein
        }
    }


    // Aktionen, die von außen gesetzt werden
    private Runnable homeAction;          // Home-Button
    private Consumer<String> onSearch;    // Suchaktion

    @FXML
    private void initialize() {
        // Home-Button klick
        homeButton.setOnAction(e -> {
            if (homeAction != null) homeAction.run();
        });

        // Such-Button klick
        searchButton.setOnAction(e -> triggerSearch());

        // Enter-Taste im Suchfeld
        searchField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) triggerSearch();
        });


    }

    private void triggerSearch() {
        if (onSearch != null) onSearch.accept(searchField.getText());
    }

    // ----- Methoden, die von außen aufgerufen werden -----
    public void setHomeAction(Runnable r) {
        this.homeAction = r;
    }

    public void setOnSearch(Consumer<String> c) {
        this.onSearch = c;
    }

    public void setSearchPrompt(String prompt) {
        if (searchField != null) searchField.setPromptText(prompt);
    }

    public String getSearchText() {
        return searchField != null ? searchField.getText() : "";
    }
}