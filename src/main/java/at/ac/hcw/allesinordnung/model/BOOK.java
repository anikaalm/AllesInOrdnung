package at.ac.hcw.allesinordnung.model;

public class BOOK extends Medium {

    BOOK(){}

    BOOK(String title, String author, int year){
        super(title,author,year);
    }

    @Override
    public String type() {
        return "Book";
    }

}
