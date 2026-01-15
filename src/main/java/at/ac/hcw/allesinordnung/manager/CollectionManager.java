package at.ac.hcw.allesinordnung.manager;

import at.ac.hcw.allesinordnung.model.Book;
import at.ac.hcw.allesinordnung.model.Cd;
import at.ac.hcw.allesinordnung.model.Dvd;
import at.ac.hcw.allesinordnung.model.Medium;
import at.ac.hcw.allesinordnung.persistence.JsonFileStorage;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;


public class CollectionManager {


    private final List<Medium> media = new ArrayList<>();
    private final JsonFileStorage storage;

    public CollectionManager(String filePath) {
        this.storage = new JsonFileStorage(filePath);
        media.addAll(storage.load());
        System.out.println("Sammlung wurde geladen" + media.size() + "Medien");
    }



    public List<Medium> showAllMedia () {
        return media;

    }

    public boolean add(Medium medium) {

        if (medium == null) {
            System.out.println("ERROR : Medium darf nicht null sein");
            return false;
        }

        if (media.contains(medium)) {
            System.out.println(medium.getType() +
                    ": " + medium.getTitle() +
                    "ist bereits in der Sammlung vorhanden");
        }

        media.add(medium);
        //saveToFile();
        return true;


    }

    public List<Medium> searchByTitle(String word){

        List<Medium>found = new ArrayList<>();

        for(Medium m : media){
            //if(m.getTitle().equalsIgnoreCase(word) - String muss aber exakt ident sein
            //vergiss nicht zu schauen wie du case sensitivity ignorieren kannst mit contains
            if(m.getTitle().contains(word)) {
                found.add(m);
            }
        }return found;
    }


    // fehlersuche noch adden

    public List<Medium> searchByCreator(String name){

        List<Medium>found = new ArrayList<>();

        for(Medium m : media){
            if(m.getTitle().contains(name)) {
                found.add(m);
            }


        }return found;
    }

    public List<Medium> searchByYear(int year){

        List<Medium>found = new ArrayList<>();

        for(Medium m : media){
            if(m.getYear()== year) {
                found.add(m);
            }
        }return found;
    }

    public List<Medium> searchByGenre(String genre){

        List<Medium>found = new ArrayList<>();

        for(Medium m : media){
            if(m.getGenre().equalsIgnoreCase(genre)) {
                found.add(m);
            }
        }return found;
    }




    public void editBook(Book book,String title,String creator,String genre, int year, String publisher) {

            book.setTitle(title);
            book.setCreator(creator);
            book.setGenre(genre);
            book.setYear(year);
            book.setPublisher(publisher);


            System.out.println("Deine Bearbeitung wurde gespeichert");



    }

    public void editOther(Medium medium,String title,String creator,String genre, int year, int runtime) {

            medium.setTitle(title);
            medium.setCreator(creator);
            medium.setGenre(genre);
            medium.setYear(year);

            if(medium instanceof Cd) {
                ((Cd) medium).setRuntime(runtime);
            } else if (medium instanceof Dvd) {
                ((Dvd) medium).setRuntime(runtime);

            }

        System.out.println("Deine Bearbeitung wurde gespeichert");

    }



    // evt noch vor löschung fragen ob nutzer sich sicher ist, dass er das medium löschen möchte
    // boolean rückgeb?
    public void delete(Medium medium){
        media.remove(medium);
        if (medium instanceof Book) {
            System.out.println("Dein" + medium.getType() + "wurde aus deiner Sammlung entfernt");
        }else{
            System.out.println("Deine" + medium.getType()+ "wurde aus deiner Sammlung entfernt");
        }

    }



}



