package at.ac.hcw.allesinordnung.manager;

import at.ac.hcw.allesinordnung.model.Medium;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;


public class CollectionManager {

    private final List<Medium> media = new ArrayList<>();

    public List<Medium> showAllMedia () {
        return media;
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

    // suche oder doch schon filter??
    public List<Medium> searchByGenre(String genre){

        List<Medium>found = new ArrayList<>();

        for(Medium m : media){
            if(m.getGenre().equalsIgnoreCase(genre)) {
                found.add(m);
            }
        }return found;
    }



    public void add(Medium medium){
        media.add(medium);
    }

    public void edit(){

    }
    public void delete(Medium medium){
        media.remove(medium);

    }

}
