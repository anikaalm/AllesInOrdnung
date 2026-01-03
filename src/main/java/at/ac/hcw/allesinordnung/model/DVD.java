package at.ac.hcw.allesinordnung.model;

public class DVD extends Medium{
    DVD(){}

    DVD(String title,String author, int year){
        super(title,author,year);
    }

    @Override
    public String type() {
        return "DVD";
    }
}
