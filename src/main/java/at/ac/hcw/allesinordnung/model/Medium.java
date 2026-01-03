package at.ac.hcw.allesinordnung.model;

public abstract class Medium {


    protected String title;
    protected String author;
    protected int year;

    //JSON JAVAFX
    Medium(){

    }

    Medium(String title,String author, int year){
        title=this.title;
        author=this.author;
        year=this.year;

    }

    public String getTitle() {return title;}

    public String getAuthor() {return author;}

    public int getYear() {return year;}


    public void setAuthor(String author) {this.author = author;}

    public void setTitle(String title) {this.title = title;}

    public void setYear(int year) {this.year = year;}

    // which type of medium
    public abstract String type();
}
