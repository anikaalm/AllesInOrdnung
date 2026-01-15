package at.ac.hcw.allesinordnung.model;

public class Dvd extends Medium{
    private int runtime;

    public Dvd(){
        super();
    }

    public Dvd(String title, String creator, String genre, int year, int runtime){
        super(title,creator,genre,year);
        this.runtime = runtime;
    }

    public int getRuntime() {return runtime;}

    public void setRuntime(int runtime) {
        this.runtime = runtime;}

    @Override
    public String getType() {
        return "DVD";
    }

    @Override
    public String toString() {
        return super.toString() + runtime + "Min";
    }
}
