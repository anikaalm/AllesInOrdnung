package at.ac.hcw.allesinordnung.demo;

import at.ac.hcw.allesinordnung.model.Book;
import at.ac.hcw.allesinordnung.model.Cd;
import at.ac.hcw.allesinordnung.model.Dvd;
import at.ac.hcw.allesinordnung.model.Medium;
import at.ac.hcw.allesinordnung.persistence.JsonFileStorage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StorageDemo {
    public static void main(String[] args) {
        try {
            JsonFileStorage storage = new JsonFileStorage(
                    "src/main/resources/data/collection.json"
            );


            List<Medium> media = new ArrayList<>();
            media.add(new Book(
                    "Der kleine Prinz",
                    "Saint-Exupéry",
                    "Roman",
                    1943,
                    "Reclam"
            ));

            media.add(new Cd("Thriller", "Michael Jackson", "Pop", 1982, 9));
            media.add(new Dvd("Matrix", "Wachowskis", "Sci-Fi", 1999, 136));


            storage.save(media);

            List<Medium> loaded = storage.load();
            System.out.println("Geladen: " + loaded.size());
            loaded.forEach(m ->
                    System.out.printf("- [%s] %s (%d) von %s%n",
                            m.getType(), m.getTitle(), m.getYear(), m.getCreator())
            );
        } catch (Exception e) {
            e.printStackTrace(); // zeigt die echte Ursache
        }
    }
}
