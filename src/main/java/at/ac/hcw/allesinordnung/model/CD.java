package at.ac.hcw.allesinordnung.model;

public class CD extends Medium{

    CD(){}

    CD(String title,String author, int year){
        super(title,author,year);
    }
    @Override
    public String type() {
        return "CD";
    }
}
