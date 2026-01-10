package at.ac.hcw.allesinordnung.model;

import java.util.Objects;

public abstract class Medium {


    private String title;
    private String creator;
    private String genre;
    private int year;

    private boolean favorite = false;  // Standard: nicht favorisiert
    private int rating = 0;            // optional, für Sternebewertung
    private String folder = "";        // optional, für Gruppierung leer = kein Ordner

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

    // Getter / Setter für favorite
    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    // Getter / Setter für rating
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    // Getter / Setter für folder
    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    /*
    Damit werden Objekte mit gleichen Daten als gleich erkannt, egal welches Objekt es ist:
    Jetzt erkennt addMedium() echte Duplikate → wird ignoriert.
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medium m)) return false;
        return year == m.getYear() &&
                title.equalsIgnoreCase(m.getTitle()) &&
                creator.equalsIgnoreCase(m.getCreator()) &&
                getType().equalsIgnoreCase(m.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title.toLowerCase(), creator.toLowerCase(), year, getType().toLowerCase());
    }

}
