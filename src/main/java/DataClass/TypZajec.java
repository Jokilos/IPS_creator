package DataClass;

import Backend.HtmlFile;
import Backend.Sciezka;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TypZajec implements Sciezka {
    private List<Grupa> grupy;
    private int idZajec;
    private String typ;
    public int iloscWybranychGrup;

    public TypZajec(){
    }

    public TypZajec(int idZajec, int grupa){
        iloscWybranychGrup = 0;
        this.idZajec = idZajec;
        grupy = new ArrayList<>();

        String url = "https://usosweb.uw.edu.pl/kontroler.php?_action=katalog2/przedmioty/pokazZajecia&zaj_cyk_id="
                + idZajec + "&gr_nr=" + grupa;
        try {
            HtmlFile hf = new HtmlFile(url,
                    pathToSrc + "/src/main/Sources/zajecia.html");

            //znalezienie typu np. Wykład lub Ćwiczenia
            String reg = ">([ " + hf.pl + "]+) gr.\\d+ - Plan zajęć";
            typ = hf.groupPatternFinder(hf.searchForOccurence(reg), reg, 1, 0);

            //znalezienie prowadzącego
            String data = hf.searchForOccurenceLineDelay("Prowadzący", 3);
            String prowadzacy = hf.groupPatternFinder(data, ">(["+ hf.pl + " ]+[-" + hf.pl +"]+)<", 1, 0);

            grupy.add(new Grupa(prowadzacy, grupa, hf));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dodajZajecia(int idZajec, int grupa){
        String url = "https://usosweb.uw.edu.pl/kontroler.php?_action=katalog2/przedmioty/pokazZajecia&zaj_cyk_id="
                + idZajec + "&gr_nr=" + grupa;
        try {
            HtmlFile hf = new HtmlFile(url,
                    pathToSrc + "/src/main/Sources/zajecia.html");

            //znalezienie prowadzącego
            String data = hf.searchForOccurenceLineDelay("Prowadzący", 3);
            String prowadzacy = hf.groupPatternFinder(data, ">(["+ hf.pl + " ]+[-" + hf.pl +"]+)<", 1, 0);

            grupy.add(new Grupa(prowadzacy, grupa, hf));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString(){
        return "Rodzaj: " + typ + "\n" + "id: " + idZajec + "\n" + grupyToString();
    }

    public String grupyToString(){
        StringBuilder sb = new StringBuilder();
        if(grupy.size() == 0)
            return "-";

        for (Grupa g: grupy) {
            sb.append("\n");
            sb.append(g);
            sb.append(" ");
        }
        return sb.toString();
    }

}

