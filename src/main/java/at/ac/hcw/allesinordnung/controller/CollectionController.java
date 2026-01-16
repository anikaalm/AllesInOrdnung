package at.ac.hcw.allesinordnung.controller;

import at.ac.hcw.allesinordnung.manager.CollectionManager;
import at.ac.hcw.allesinordnung.model.Medium;
import at.ac.hcw.allesinordnung.model.Book;
import at.ac.hcw.allesinordnung.model.Cd;
import at.ac.hcw.allesinordnung.model.Dvd;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;


/**
 * Controller für die Hauptansicht: Medienübersicht
 */

public class CollectionController {

    private CollectionManager manager = new CollectionManager("src/main/resources/data/collection.json");

    @FXML
    private ListView<Medium> mediaListView;

    @FXML
    public void initialize() {
        showAll();
    }

    //Kategorie-Filter
    @FXML
    public void showBooks() {
        mediaListView.getItems().setAll(manager.filterByType("BOOK"));
    }

    @FXML
    public void showCds() {
        mediaListView.getItems().setAll(manager.filterByType("CD"));
    }

    @FXML
    public void showDvds() {
        mediaListView.getItems().setAll(manager.filterByType("DVD"));
    }

    @FXML
    public void showAll() {
        mediaListView.getItems().setAll(manager.showAllMedia());
    }

    //Suche
    @FXML
    public void searchByTitle() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Titel suchen");
        dialog.setContentText("Titel eingeben:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(word -> mediaListView.getItems().setAll(manager.searchByTitle(word)));
    }

    @FXML
    public void searchByCreator() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Autor/Künstler suchen");
        dialog.setContentText("Name eingeben:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> mediaListView.getItems().setAll(manager.searchByCreator(name)));
    }

    @FXML
    public void searchByYear() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Jahr suchen");
        dialog.setContentText("Jahr eingeben:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(yearStr -> {
            try {
                int year = Integer.parseInt(yearStr);
                mediaListView.getItems().setAll(manager.searchByYear(year));
            } catch (NumberFormatException e) {
                System.out.println("Ungültiges Jahr");
            }
        });
    }

    @FXML
    public void searchByGenre() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Genre suchen");
        dialog.setContentText("Genre eingeben:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(genre -> mediaListView.getItems().setAll(manager.searchByGenre(genre)));
    }

    //Bearbeiten
    @FXML
    public void editSelected() {
        Medium selected = mediaListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        TextInputDialog dialog = new TextInputDialog(selected.getTitle());
        dialog.setHeaderText("Titel bearbeiten");
        dialog.setContentText("Titel:");
        Optional<String> newTitle = dialog.showAndWait();

        if (newTitle.isPresent()) {
            if (selected instanceof Book book) {
                manager.editBook(book, newTitle.get(), book.getCreator(), book.getGenre(), book.getYear(), book.getPublisher());
            } else if (selected instanceof Cd cd) {
                manager.editOther(cd, newTitle.get(), cd.getCreator(), cd.getGenre(), cd.getYear(), cd.getRuntime());
            } else if (selected instanceof Dvd dvd) {
                manager.editOther(dvd, newTitle.get(), dvd.getCreator(), dvd.getGenre(), dvd.getYear(), dvd.getRuntime());
            }
            mediaListView.refresh();
        }
    }

    //Löschen
    @FXML
    public void deleteSelected() {
        Medium selected = mediaListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            manager.deleteMedium(selected);
            mediaListView.getItems().remove(selected);
        }
    }

    //Favorit
    @FXML
    public void toggleFavorite() {
        Medium selected = mediaListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean newFav = !selected.isFavorite();
            manager.setFavorite(selected, newFav);
            mediaListView.refresh();
        }
    }

    //Ordner
    @FXML
    public void setFolderForSelected() {
        Medium selected = mediaListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            TextInputDialog dialog = new TextInputDialog(selected.getFolder());
            dialog.setHeaderText("Ordner setzen");
            dialog.setContentText("Ordnername:");
            Optional<String> folderName = dialog.showAndWait();
            folderName.ifPresent(name -> manager.setFolder(selected, name));
            mediaListView.refresh();
        }
    }

}