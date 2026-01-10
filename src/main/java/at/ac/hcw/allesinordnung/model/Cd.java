package at.ac.hcw.allesinordnung.model;

public class Cd extends Medium{
    private final String type = "CD";
    private int runtime;

    public Cd(){}

    public Cd(String title,String creator,String genre, int year, int runtime){
        super(title,creator,genre,year);
        this.runtime = runtime;
    }

    public int getRuntime() {return runtime;}
    public void setRuntime(int runtime) {this.runtime = runtime;}

    @Override
    public String getType() {
        return type;
    }
}
