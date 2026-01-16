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
        // prüft ob pfad gültig
        if (filePath == null || filePath.trim().isEmpty() ) {
            throw new IllegalArgumentException("Dateipfad darf nicht leer sein!");
        }
        this.storage = new JsonFileStorage(filePath);
        this.media = storage.load(); // beim Start alle Medien laden
    }

    //Anzeige aller Medien
    public List<Medium> showAllMedia() {
        if (media.isEmpty()) {
            System.out.println("Die Sammlung ist noch leer :(");

            for (Medium m : media){
                System.out.println(m);
            }
            System.out.println();
        }
        return new ArrayList<>(media); // Kopie zurückgeben
    }

    //Hinzufügen
    public void addMedium(Medium m) {

        if (m== null){
            throw new IllegalArgumentException("Es wurde kein Medium ausgewählt!");
        }

        // prüft, ob medium bereits existiert
        boolean exists = media.stream().anyMatch(x -> x.equals(m));
        if(exists){
            System.out.println( m.getType() + ": "+ m.getTitle() +" bereits in deiner Sammlung!");

        } else { // speichern & hinzufügen
            media.add(m);
            storage.save(media);
            System.out.println(m.getType() + ": " + m.getTitle() + " wurde hinzugefügt :)" );
        }
    }

    //Löschen
    public void deleteMedium(Medium m) {
        // Gültigkeit?
        if (m== null){
            throw new IllegalArgumentException("Bitte wähle ein Medium aus!");
        }

        // prüft, ob Medium überhaupt in der Sammlung vorhanden
        if (!media.contains(m)) {
            throw new IllegalStateException("Titel wurde nicht gefunden");
        }

        // löschen & speichern
        media.remove(m);
        storage.save(media);
        System.out.println(m.getType() +": "+ m.getTitle() + " wurde entfernt");
    }

    //Bearbeitet Buch
    public void editBook(Book book, String title, String creator, String genre, int year, String publisher) {
        //prüft, ob Buch existiert
        if (book == null) {
            throw new IllegalArgumentException("Bitte wähle ein Buch aus");
        }

        if (!media.contains(book)) {
            throw new IllegalStateException("Buch nicht gefunden.");
        }

        //Prüft, ob alle Eingaben gültig
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Bitte Titel eingeben");
        }
        if (creator == null || creator.trim().isEmpty()) {
            throw new IllegalArgumentException("Bitte Autor eingeben");
        }
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Bitte Genre eingeben");
        }
        if (year < 1455 || year > 2026) {
            throw new IllegalArgumentException("Bitte ein gültiges Jahr eingeben");
        }if (publisher == null || publisher.trim().isEmpty()) {
            throw new IllegalArgumentException("Bitte Verlag eingeben");
        }

        // Bearbeiten und Speichern
        book.setTitle(title);
        book.setCreator(creator);
        book.setGenre(genre);
        book.setYear(year);
        book.setPublisher(publisher);
        storage.save(media);
        System.out.println("Bearbeitung gespeichert");
    }

    // Bearbeitet CD
    public void editCD(Cd cd, String title, String creator, String genre, int year, int runtime) {
        //prüft ob CD existiert
        if (cd== null) {
            throw new IllegalArgumentException("Bitte wähle eine CD aus!");
        }

        if (!media.contains(cd)) {
            throw new IllegalStateException("CD nicht gefunden.");
        }

        //Prüft, ob alle Eingaben gültig
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Bitte Titel eingeben");
        }
        if (creator == null || creator.trim().isEmpty()) {
            throw new IllegalArgumentException("Bitte Autor eingeben");
        }
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Bitte Genre eingeben");
        }
        if (year < 1455 || year > 2026) {
            throw new IllegalArgumentException("Bitte ein gültiges Jahr eingeben");
        }if (runtime <=0) {
            throw new IllegalArgumentException("Bitte eine gültige Laufzeit eingeben");
        }

        // Bearbeiten und Speichern
        cd.setTitle(title);
        cd.setCreator(creator);
        cd.setGenre(genre);
        cd.setYear(year);
        cd.setRuntime(runtime);
        storage.save(media);
        System.out.println("Bearbeitung gespeichert");
    }

    // Bearbeitet DVD
    public void editDVD(Dvd dvd, String title, String creator, String genre, int year, int runtime) {
        //prüft ob DVD existiert
        if (dvd== null) {
            throw new IllegalArgumentException("Bitte wähle eine DVD aus");
        }

        if (!media.contains(dvd)) {
            throw new IllegalStateException("DVD nicht gefunden");
        }

        //Prüft, ob alle Eingaben gültig
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Bitte Titel eingeben");
        }
        if (creator == null || creator.trim().isEmpty()) {
            throw new IllegalArgumentException("Bitte Autor eingeben");
        }
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Bitte Genre eingeben");
        }
        if (year < 1455 || year > 2026) {
            throw new IllegalArgumentException("Bitte ein gültiges Jahr eingeben");
        }if (runtime <=0) {
            throw new IllegalArgumentException("Bitte eine gültige Laufzeit eingeben");
        }

        // Bearbeiten und Speichern
        dvd.setTitle(title);
        dvd.setCreator(creator);
        dvd.setGenre(genre);
        dvd.setYear(year);
        dvd.setRuntime(runtime);
        storage.save(media);
        System.out.println("Bearbeitung gespeichert");
    }

    //Suchen
    public List<Medium> searchByTitle(String title) {
        if (title == null ||title.trim().isEmpty()) {
            throw new IllegalArgumentException("Bitte gib einen Suchbegriff ein ! (:");
        }
        return media.stream()
                .filter(m -> m.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Medium> searchByCreator(String creator) {
        if (creator == null ||creator.trim().isEmpty()) {
            throw new IllegalArgumentException("Bitte gib einen Suchbegriff ein ! (:");
        }
        return media.stream()
                .filter(m -> m.getCreator().toLowerCase().contains(creator.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Medium> searchByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Bitte gib einen Suchbegriff ein ! (:");
        }
        return media.stream()
                .filter(m -> m.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    public List<Medium> searchByYear(int year) {
        if (year <1455 || year > 2026) {
            throw new IllegalArgumentException("Bitte gib einen Suchbegriff ein ! (:");
        }
        return media.stream()
                .filter(m -> m.getYear() == year)
                .collect(Collectors.toList());
    }

    public List<Medium> filterByType(String type) {
        if (type == null ||type.trim().isEmpty()) {
            throw new IllegalArgumentException("Bitte gib einen Suchbegriff ein ! (:");
        }
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

        if (medium == null) {
            throw new IllegalArgumentException("Medium darf nicht null sein");
        }
        if (!media.contains(medium)) {
            throw new IllegalArgumentException("Medium nicht in der Sammlung vorhanden!");
        }

        String info;

        if (favorite) {
            info = " zu Favoriten hinzugefügt! <3";
        } else {
            info = " aus Favoriten entfernt!";
        }

        System.out.println(medium.getTitle()+ ": " + info);


    }

    //Getter
    public List<Medium> getAllMedia() {
        return new ArrayList<>(media);
    }
}
