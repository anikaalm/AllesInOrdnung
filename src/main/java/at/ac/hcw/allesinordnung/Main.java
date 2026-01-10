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
            //CD
            Medium c1 = new Cd("21", "Adele", "Pop", 2011, 48);
            Medium c2 = new Cd("25", "Adele", "Pop", 2015, 48);
            Medium c3 = new Cd("33", "KJ", "Rap", 2011, 52);
            Medium c4 = new Cd("blbl", "lele", "Rap", 2000, 50);
            //DVD
            Medium d1 = new Dvd("Das Barbie Tagebuch", "Universal Pictures", "Zeichentrick", 2006, 70);

            manager.addMedium(c1);
            manager.addMedium(b1);
            manager.addMedium(c2);
            manager.addMedium(c2); // doppelt → wird ignoriert
            manager.addMedium(c3);
            manager.addMedium(c4);
            manager.addMedium(d1);

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
            manager.editBook((Book) b1, "Faust I", "Goethe", "Klassik", 2001, "Reclam");
            System.out.println("\nNach Bearbeitung:");
            manager.showAllMedia().forEach(m ->
                    System.out.println("- [" + m.getType() + "] " + m.getTitle() + " (" + m.getYear() + ") von " + m.getCreator())
            );


            //löschen
            manager.deleteMedium(d1);




            //manager.searchByCreator("Adele");
        //Fehlsuche bzw nicht vorhanden
        //manager.searchByCreator("Future");



        //manager.editBook(c1,);

    }

}
