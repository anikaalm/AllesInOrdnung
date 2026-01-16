package at.ac.hcw.allesinordnung;

import at.ac.hcw.allesinordnung.manager.CollectionManager;
import at.ac.hcw.allesinordnung.model.Book;
import at.ac.hcw.allesinordnung.model.Cd;
import at.ac.hcw.allesinordnung.model.Dvd;
import at.ac.hcw.allesinordnung.model.Medium;

public class Main {
    public static void main(String[] args) {

            // TEST TEST TEST

            CollectionManager manager = new CollectionManager("src/main/resources/data/collection.json");


            //Buch
            Medium b1 = new Book("Faust", "Goethe", "Klassik", 2000, "Reclam");
            Medium b2 = new Book("Before the coffee gets cold", "Toshikazu Kawaguchi", "Fantasy", 2015, "Pan Macmillan");
            Medium b3 = new Book ("Der Kleine Prinz", "Antoine de Saint-Exupery", "Klassik", 1943, "Reynal & Hitchcock" );
            Medium b4 = new Book ("Harry Potter und der Stein der Weisen", "J.K. Rowling", "Fantasy",1997, "Carlsen");
            Medium b5 = new Book ("1984", "George Orwell", "Dystopie",1949, "Secker & Warburg");
            Medium b6 = new Book ("Die Verwandlung", "Franz Kafka", "Klassik",1915, "Kurt Wolff Verlag");


            //CD
            Medium c1 = new Cd("21", "Adele", "Pop", 2011, 48);
            Medium c2 = new Cd("Thriller", "Michael Jacksons", "Pop", 1982, 42);
            Medium c3 = new Cd("Back in Black", "AC/DC", "Rock", 1980, 42);
            Medium c4 = new Cd("Random Access Memories", "Daft Puft", "Electronic", 2013, 47);
            //DVD
            Medium d1 = new Dvd("Das Barbie Tagebuch", "Universal Pictures", "Zeichentrick", 2006, 70);
            Medium d2 = new Dvd("Der König der Löwen", "Disney", "Animation", 1994, 88);
            Medium d3 = new Dvd("Inception", " Warner Bros.", "Sci-Fi", 2010, 148);
            Medium d4 = new Dvd("Forrest Gump", "Paramount Pictures", "Drama", 1994, 142);
            Medium d5 = new Dvd ("Crazy Rich Asian", "Warner Bros", "Romantik/Komödie", 2018, 120);


            manager.addMedium(c1);
            manager.addMedium(b1);
            manager.addMedium(b2);
            manager.addMedium(c2);
            manager.addMedium(c2); // doppelt → wird ignoriert
            manager.addMedium(c3);
            manager.addMedium(c4);
            manager.addMedium(d1);
            manager.addMedium(b3);
            manager.addMedium(b4);
            manager.addMedium(b5);
            manager.addMedium(b6);
            manager.addMedium(d2);
            manager.addMedium(d3);
            manager.addMedium(d4);
            manager.addMedium(d5);

            // Ausgabe aller Medien
            manager.showAllMedia().forEach(m ->
                    System.out.println("- [" + m.getType() + "] " + m.getTitle() + " (" + m.getYear() + ") von " + m.getCreator())
            );


            // Beispiel Suche
            System.out.println("\nSuche nach 'Adele':");
            manager.searchByCreator("Adele").forEach(m ->
                    System.out.println("- [" + m.getType() + "] " + m.getTitle())
            );

            // Beispiel Bearbeiten
            manager.editBook((Book) b2, "Before the coffee gets cold", "Toshikazu Kawaguchi", "science fiction", 2015, "Pan Macmillan");
            System.out.println("\nNach Bearbeitung:");
            manager.showAllMedia().forEach(m ->
                    System.out.println("- [" + m.getType() + "] " + m.getTitle() + " (" + m.getYear() + ") von " + m.getCreator())
            );


            // Doppelte Objekte erstellen (gleiche Daten, neue Instanzen)
            Medium b1Dup = new Book("Faust", "Goethe", "Klassik", 2000, "Reclam");
            Medium c1Dup = new Cd("21", "Adele", "Pop", 2011, 48);

            // Doppelte hinzufügen – sollte von addMedium erkannt werden
            manager.addMedium(b1Dup);
            manager.addMedium(c1Dup);

            //löschen
            manager.deleteMedium(d1);


            // 4 Sterne vergeben
            manager.rateMedium(b1, 4);

            // später ändern auf 5 Sterne
            manager.rateMedium(b1, 5);

            System.out.println("Bewertung: " + b1.getRating() + " Sterne");

            // Medium in Ordner "Klassiker" verschieben
            manager.setFolder(b1, "Klassiker");

            // Medium in Ordner "Pop" verschieben
            manager.setFolder(c1, "Pop");

            // Alle Medien im Ordner "Klassiker" anzeigen
            manager.filterByFolder("Klassiker").forEach(m ->
                    System.out.println("- [" + m.getType() + "] " + m.getTitle())
            );


            // Als Favorit markieren
            manager.setFavorite(b1, true);

            System.out.println("Favoriten:");
            manager.getFavorites().forEach(m ->
                    System.out.println("- " + m.getTitle())
            );


            //manager.searchByCreator("Adele");
        //Fehlsuche bzw nicht vorhanden
        //manager.searchByCreator("Future");
        //manager.editBook(c1,);

    }

}
