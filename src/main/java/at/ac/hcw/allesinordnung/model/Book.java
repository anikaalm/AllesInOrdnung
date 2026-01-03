package at.ac.hcw.allesinordnung.model;

public class Book extends Medium {
    Book(){}

    Book(String title,String author, int year){
        super(title,author,year);
    }
    @Override
    public String type() {
        return "CD";
    }
}