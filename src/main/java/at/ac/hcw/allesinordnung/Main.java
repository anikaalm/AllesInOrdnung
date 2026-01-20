package at.ac.hcw.allesinordnung;

import at.ac.hcw.allesinordnung.controller.HelloApplication;
import at.ac.hcw.allesinordnung.manager.CollectionManager;
import at.ac.hcw.allesinordnung.model.Book;
import at.ac.hcw.allesinordnung.model.Cd;
import at.ac.hcw.allesinordnung.model.Dvd;
import at.ac.hcw.allesinordnung.model.Medium;

public class Main {
    public static void main(String[] args) {

        HelloApplication.main(args);

            // hier die GUI starten



            /*TEST TEST TEST
            CollectionManager manager = new CollectionManager("src/main/resources/data/collection.json");


            //Buch
            Medium b1 = new Book("Before the coffee gets cold", "Toshikazu Kawaguchi", "Fantasy", 2015, "Pan Macmillan");
            Medium b2 = new Book ("Der Kleine Prinz", "Antoine de Saint-Exupery", "Klassik", 1943, "Reynal & Hitchcock" );
            //CD
            Medium c1 = new Cd("21", "Adele", "Pop", 2011, 48);
            Medium c2 = new Cd("Thriller", "Michael Jacksons", "Pop", 1982, 42);
            //DVD
            Medium d1 = new Dvd("Das Barbie Tagebuch", "Universal Pictures", "Zeichentrick", 2006, 70);
            Medium d5 = new Dvd("Crazy Rich Asian", "Warner Bros", "Romantik/Komödie", 2018, 120);


            // Ausgabe aller Medien
            manager.showAllMedia().forEach(m ->
                    System.out.println("- [" + m.getType() + "] "
                                             + m.getTitle() + " von "
                                             + m.getCreator()+ " ("
                                             + m.getYear()+")"));

            manager.addMedium(new Book("Der Drachenläufer","Khaled Hosseini", "Roman/Drama", 2003,"Bloomsbery Berlin" ));

            manager.showAllMedia().forEach(m ->
                    System.out.println("- [" + m.getType() + "] "
                                             + m.getTitle() + " von "
                                             + m.getCreator()+ " ("
                                             + m.getYear()+")"));

             */










    }

}
