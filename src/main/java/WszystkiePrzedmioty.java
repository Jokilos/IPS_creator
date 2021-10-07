import java.util.ArrayList;
import java.util.List;

public class WszystkiePrzedmioty {
    private List<Przedmiot> przedmioty;
    private int liczbaPrzedmiotow;

    public WszystkiePrzedmioty(){
        przedmioty = new ArrayList<Przedmiot>();
        liczbaPrzedmiotow = 0;
    }

    public void dodajPrzedmiot(Przedmiot p){
        przedmioty.add(p);
        liczbaPrzedmiotow++;
    }
}
