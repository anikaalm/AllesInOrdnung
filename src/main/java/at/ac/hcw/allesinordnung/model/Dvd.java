package at.ac.hcw.allesinordnung.model;

public class Dvd extends Medium{
    Dvd(){}

    Dvd(String title, String author, int year){
        super(title,author,year);
    }

    @Override
    public String type() {
        return "DVD";
    }
}
