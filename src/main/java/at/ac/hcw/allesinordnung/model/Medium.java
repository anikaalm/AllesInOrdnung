package at.ac.hcw.allesinordnung.model;

public abstract class Medium {


    private String title;
    private String creator;
    private String genre;
    private int year;


    Medium(){

    }

    Medium(String title,String creator,String genre, int year){
        this.title = title;
        this.creator = creator;
        this.genre = genre;
        this.year = year;

    }

    public String getTitle() {return title;}
    public String getCreator() {return creator;}
    public String getGenre() {return genre;}
    public int getYear() {return year;}
    public void setCreator(String creator) {this.creator = creator;}
    public void setGenre(String genre) {this.genre = genre;}
    public void setTitle(String title) {this.title = title;}
    public void setYear(int year) {this.year = year;}

    // which type of medium
    public abstract String getType();
}
