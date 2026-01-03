package at.ac.hcw.allesinordnung.model;

public class Cd extends Medium{

    Cd(){}

    Cd(String title, String author, int year){
        super(title,author,year);
    }
    @Override
    public String type() {
        return "CD";
    }
}
