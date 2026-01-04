package at.ac.hcw.allesinordnung.model;

public class Cd extends Medium{

    private int runtime;

    Cd(){}

    Cd(String title,String creator,String genre, int year, int runtime){
        super(title,creator,genre,year);
        this.runtime = runtime;
    }

    public int getRuntime() {return runtime;}
    public void setRuntime(int runtime) {this.runtime = runtime;}

    @Override
    public String getType() {
        return "CD";
    }
}
