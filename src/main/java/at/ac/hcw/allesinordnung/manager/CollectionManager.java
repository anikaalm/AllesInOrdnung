// Der CollectionManager hält alle Medien (Book/Cd/Dvd) in einer Liste,
// Methoden: Anzeigen, Hinzufügen, Löschen, Bearbeiten, Suchen und Sortieren
// Änderungen sofort in die JSON-Datei gespeichert werden.
package at.ac.hcw.allesinordnung.manager;

import at.ac.hcw.allesinordnung.model.Book;
import at.ac.hcw.allesinordnung.model.Cd;
import at.ac.hcw.allesinordnung.model.Dvd;
import at.ac.hcw.allesinordnung.model.Medium;
import at.ac.hcw.allesinordnung.persistence.JsonFileStorage;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;
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

    /** Neuer, leichter Konstruktor: kein I/O */
    public CollectionManager(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Dateipfad darf nicht null oder leer sein!");
        }
        this.storage = new JsonFileStorage(filePath);
        this.media = new ArrayList<>(); // noch NICHT laden – erst in initStorage()
    }

    /**
     * Diese Methode darf I/O machen und wird EXPLIZIT nach dem Erzeugen aufgerufen.
     * Legt Ordner/Datei an (falls nötig) und lädt die Daten.
     */


    public void initStorage() {
        try {
            Path p = Path.of(storage.getFilePath());
            Path parent = p.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            if (Files.notExists(p)) {
                Files.createFile(p);
                Files.writeString(p, "[]", StandardCharsets.UTF_8);
                System.out.println(">> collection.json neu erstellt: " + p);
            }
            // Laden
            List<Medium> loaded = storage.load();
            media.clear();
            if (loaded != null) media.addAll(loaded);

        } catch (Exception e) {
            throw new RuntimeException("Konnte Datei nicht vorbereiten: " + storage.getFilePath(), e);
        }
    }



    //Anzeigen
    public List<Medium> showAllMedia() {
        if (media.isEmpty()) {
            System.out.println("Die Sammlung ist noch leer :(");
        }
        return new ArrayList<>(media); // Kopie zurückgeben
    }

    //Hinzufügen
    public void addMedium(Medium m) {

        if (m== null){
            throw new IllegalArgumentException("Medium darf nicht null sein!");
        }
        boolean exists = media.stream().anyMatch(x -> x.equals(m));
        if(exists){
            throw new IllegalStateException(m.getType() + " bereits in der Sammlung vorhanden !");

        } else {
            media.add(m);
            storage.save(media);
            System.out.println(m.getType() + ": " + m.getTitle() + "wurde hinzugefügt :)" );
        }
    }

    //Löschen
    public void deleteMedium(Medium m) {
        if (m== null){
            throw new IllegalArgumentException("Medium darf nicht null sein!");
        }
        if (!media.contains(m)) {
            throw new IllegalStateException(m.getType() + ": " + m.getTitle() + "nicht in der Sammlung gefunden");
        }
        media.remove(m);
        storage.save(media);
        System.out.println(m.getType() +": "+ m.getTitle() + " wurde entfernt");
    }

    //Bearbeiten

    //EDIT: Buch
    public void editBook(Book book, String title, String creator, String genre, int year, String publisher) {
        if (book == null) {
            throw new IllegalArgumentException("Buch darf nicht null sein!");
        }
        if (!media.contains(book)) {
            throw new IllegalStateException("Buch nicht in der Sammlung gefunden");
        }
        book.setTitle(title);
        book.setCreator(creator);
        book.setGenre(genre);
        book.setYear(year);
        book.setPublisher(publisher);
        storage.save(media);
        System.out.println("Bearbeitung gespeichert (Buch)");
    }

    //EDIT: CD
    public void editCd(Cd cd, String title, String creator, String genre, int year, int runtime) {
        if (cd == null) {
            throw new IllegalArgumentException("CD darf nicht null sein!");
        }
        if (!media.contains(cd)) {
            throw new IllegalStateException("CD nicht in der Sammlung gefunden");
        }
        cd.setTitle(title);
        cd.setCreator(creator);
        cd.setGenre(genre);
        cd.setYear(year);
        cd.setRuntime(runtime);
        storage.save(media);
        System.out.println("Bearbeitung gespeichert (CD)");
    }

    //EDIT: DVD
    public void editDvd(Dvd dvd, String title, String creator, String genre, int year, int runtime) {
        if (dvd == null) {
            throw new IllegalArgumentException("DVD darf nicht null sein!");
        }
        if (!media.contains(dvd)) {
            throw new IllegalStateException("DVD nicht in der Sammlung gefunden");
        }
        dvd.setTitle(title);
        dvd.setCreator(creator);   // bei dir heißt es 'creator' (Regisseur)
        dvd.setGenre(genre);
        dvd.setYear(year);
        dvd.setRuntime(runtime);
        storage.save(media);
        System.out.println("Bearbeitung gespeichert (DVD)");
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


    //Getter
    public List<Medium> getAllMedia() {
        return new ArrayList<>(media);
    }
}
