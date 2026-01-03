package at.ac.hcw.allesinordnung.model;

public class Dvd extends Medium{

    private int runtime;

    Dvd(){}

    Dvd(String title, String creator, String genre, int year, int runtime){
        super(title,creator,genre,year);
        this.runtime = runtime;
    }

    public int getRuntime() {return runtime;}
    public void setRuntime(int runtime) {this.runtime = runtime;}

    @Override
    public String type() {
        return "DVD";
    }
}
