package at.ac.hcw.allesinordnung.model;

public class Book extends Medium {
    private final String type = "BOOK";
    private String publisher;

    public Book(){}

    public Book(String title,String creator,String genre, int year, String publisher){
        super(title,creator,genre,year);
        this.publisher = publisher;
    }

    public String getPublisher() {return publisher;}
    public void setPublisher(String publisher) {this.publisher=publisher;}

    @Override
    public String getType() {
        return type;
    }
}

