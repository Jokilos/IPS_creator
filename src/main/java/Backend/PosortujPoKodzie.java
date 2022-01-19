package Backend;

import DataClass.Przedmiot;

import java.util.Comparator;

public class PosortujPoKodzie implements Comparator<Przedmiot>{

    @Override
    public int compare(Przedmiot p1, Przedmiot p2) {

        String s1 = p1.getKodPrzedmiotu(), s2 = p2.getKodPrzedmiotu();
        if (s1 == s2) {
            return 0;
        }
        if (s1 == null) {
            return -1;
        }
        if (s2 == null) {
            return 1;
        }
        return s1.compareTo(s2);
    }
}

