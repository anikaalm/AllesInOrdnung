package at.ac.hcw.allesinordnung.manager;

import at.ac.hcw.allesinordnung.model.Book;
import at.ac.hcw.allesinordnung.model.Cd;
import at.ac.hcw.allesinordnung.model.Dvd;
import at.ac.hcw.allesinordnung.model.Medium;
import at.ac.hcw.allesinordnung.persistence.JsonFileStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CollectionManager verwaltet alle Medien in einer polymorphen Liste
 * und speichert Änderungen automatisch in JSON.
 */
public class CollectionManager {

    private final JsonFileStorage storage;
    private final List<Medium> media;

    //Constructor
    public CollectionManager(String filePath) {
        this.storage = new JsonFileStorage(filePath);
        this.media = storage.load(); // beim Start alle Medien laden
    }

    //Anzeigen
    public List<Medium> showAllMedia() {
        return new ArrayList<>(media); // Kopie zurückgeben
    }

    //Hinzufügen
    public void addMedium(Medium m) {
        boolean exists = media.stream().anyMatch(x -> x.equals(m));
        if(exists){
            System.out.println(m.getType() + " bereits in der Sammlung vorhanden");
        } else {
            media.add(m);
            storage.save(media);
        }
    }

    //Löschen
    public void deleteMedium(Medium m) {
        media.remove(m);
        storage.save(media);
        System.out.println(m.getType() + " wurde entfernt");
    }

    //Bearbeiten
    public void editBook(Book book, String title, String creator, String genre, int year, String publisher) {
        book.setTitle(title);
        book.setCreator(creator);
        book.setGenre(genre);
        book.setYear(year);
        book.setPublisher(publisher);
        storage.save(media);
        System.out.println("Bearbeitung gespeichert");
    }

    public void editOther(Medium medium, String title, String creator, String genre, int year, int runtime) {
        medium.setTitle(title);
        medium.setCreator(creator);
        medium.setGenre(genre);
        medium.setYear(year);

        if(medium instanceof Cd) ((Cd) medium).setRuntime(runtime);
        if(medium instanceof Dvd) ((Dvd) medium).setRuntime(runtime);

        storage.save(media);
        System.out.println("Bearbeitung gespeichert");
    }

    //Suchen
    public List<Medium> searchByTitle(String title) {
        return media.stream()
                .filter(m -> m.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Medium> searchByCreator(String creator) {
        return media.stream()
                .filter(m -> m.getCreator().toLowerCase().contains(creator.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Medium> searchByGenre(String genre) {
        return media.stream()
                .filter(m -> m.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    public List<Medium> searchByYear(int year) {
        return media.stream()
                .filter(m -> m.getYear() == year)
                .collect(Collectors.toList());
    }

    public List<Medium> filterByType(String type) {
        return media.stream()
                .filter(m -> m.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    //Sortieren
    public void sortByTitle() {
        media.sort(Comparator.comparing(Medium::getTitle));
    }

    public void sortByYear() {
        media.sort(Comparator.comparingInt(Medium::getYear));
    }

    //Favoriten / Bewertung
    public List<Medium> getFavorites() {
        return media.stream()
                .filter(Medium::isFavorite)
                .toList();
    }

    public void setFavorite(Medium medium, boolean favorite) {
        for (Medium m : media) {
            if (m.equals(medium)) {
                m.setFavorite(favorite);   // ← WICHTIG: m, nicht medium
                storage.save(media);
                return;
            }
        }
    }



    public void rateMedium(Medium m, int rating) {
        m.setRating(rating);
        storage.save(media);
    }


    //Ordner / Gruppierung

    public void setFolder(Medium m, String folder) {
        m.setFolder(folder);
        storage.save(media);
    }

    public List<Medium> filterByFolder(String folder) {
        return media.stream()
                .filter(m -> m.getFolder().equalsIgnoreCase(folder))
                .collect(Collectors.toList());
    }

    //Getter
    public List<Medium> getAllMedia() {
        return new ArrayList<>(media);
    }
}
