package at.ac.hcw.allesinordnung;

import at.ac.hcw.allesinordnung.manager.CollectionManager;
import at.ac.hcw.allesinordnung.model.Book;
import at.ac.hcw.allesinordnung.model.Cd;
import at.ac.hcw.allesinordnung.model.Dvd;
import at.ac.hcw.allesinordnung.model.Medium;

public class Main {
    public static void main(String[] args) {

            // TEST TEST TEST

            CollectionManager manager = new CollectionManager();

            //Buch
            Medium b1 = new Book("Faust", "Goethe", "Klassik", 2000, "Reclam");
            //CD
            Medium c1 = new Cd("21", "Adele", "Pop", 2011, 48);
            Medium c2 = new Cd("25", "Adele", "Pop", 2015, 48);
            Medium c3 = new Cd("33", "KJ", "Rap", 2011, 52);
            Medium c4 = new Cd("blbl", "lele", "Rap", 2000, 50);
            //DVD
            Medium d1 = new Dvd("Das Barbie Tagebuch", "Universal Pictures", "Zeichentrick", 2006, 70);

            manager.add(c1);
            manager.add(b1);
            manager.add(c2);
            // Überprüfung doppelt
            manager.add(c2);
            manager.add(c3);
            manager.add(c4);
            manager.add(d1);


            manager.showAllMedia();



        //manager.searchByCreator("Adele");
        //Fehlsuche bzw nicht vorhanden
        //manager.searchByCreator("Future");



        //manager.editBook(c1,);

    }

}
