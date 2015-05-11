package keesnapps.be.myapplication;
import android.widget.ArrayAdapter;

public class Contact {
    private int id;
    private String naam;
    private String schuld;
    private String nummer;
    private String datum;

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public Contact(){
    }

    public String getNummer() {
        return nummer;
    }

    public void setNummer(String nummer) {
        this.nummer = nummer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getSchuld() {
        return schuld;
    }

    public void setSchuld(String schuld) {
        this.schuld = schuld;
    }

    public String toString() {
        return naam;
    }

}
