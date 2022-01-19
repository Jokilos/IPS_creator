package DataClass;

import Backend.HtmlFile;
import Backend.Sciezka;
import lombok.Getter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter

public class Przedmiot implements Sciezka {
    private String nazwaPrzedmiotu;
    private String kodPrzedmiotu;
    private List<TypZajec> typyZajec;
    private float ECTS;
    private List<String> kierunekMSMP;
    private boolean ten_semestr;

    public static void main(String[] args){
        Przedmiot p = new Przedmiot("0000-SZD-ET-WAR9-EN", "2020Z");
        System.out.println(p);
    }

    public Przedmiot(String kodPrzedmiotu, String semestr){
        try {
            this.kodPrzedmiotu = kodPrzedmiotu;
            String url = "https://usosweb.uw.edu.pl/kontroler.php?_action=katalog2/przedmioty/pokazPrzedmiot&prz_kod=" + kodPrzedmiotu;
            HtmlFile hf = new HtmlFile(url, pathToSrc + "/src/main/Sources/przedmiot.html");

            Scanner sc = new Scanner(hf.searchForOccurenceLineDelay("ECTS", 2));
            if (sc.hasNextFloat())
                ECTS = sc.nextFloat();

            nazwaPrzedmiotu =
                    hf.groupPatternFinder
                            (hf.searchForOccurenceLineDelay("Nazwa przedmiotu", 1),
                                    ">([" + hf.specialWithApo + hf.pl + "0-9 ]+)<",
                                    1, 0);

            kierunekMSMP = new ArrayList<>();
            String dataLine = hf.searchForOccurenceLineDelay("MISMaP", 1);
            String data;
            if (dataLine != null) {
                for (int i = 0; (data = hf.groupPatternFinder(dataLine, ">([" + hf.plLc + "]+)<", 1, i)) != ""; i++) {
                    kierunekMSMP.add(data);
                }
            }

            typyZajec = parseTypyZajec(hf, kodPrzedmiotu, semestr);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<TypZajec> parseTypyZajec(HtmlFile hf, String kodPrzedmiotu, String semestr){
        ArrayList<TypZajec> zajecia = new ArrayList<>();

        HashMap<Integer, HashSet<Integer>> zajeciaGrupy = getGroups(kodPrzedmiotu, semestr);
        ArrayList<Integer> grupy = new ArrayList<>();
        boolean first = true;
        TypZajec tz = new TypZajec();

        for (int kod : zajeciaGrupy.keySet()) {
            first = true;
            System.out.println("Kod: " + kod);
            for(Integer grupa : zajeciaGrupy.get(kod)){
                System.out.println("DataClass.Grupa: " + grupa);
                if(first) {
                    tz = new TypZajec(kod, grupa);
                    first = false;
                }
                else{
                    tz.dodajZajecia(kod, grupa);
                }
            }
            zajecia.add(tz);
        }
        return zajecia;
    }

    /** To jest funkcja, która wybiera z linków kod zajęć i ilość grup dla danych zajęć w postaci HashMapy */

    public HashMap<Integer, HashSet<Integer>> getGroups(String kodPrzedmiotu, String semestr){

        String url = "https://usosweb.uw.edu.pl/kontroler.php?_action=katalog2/przedmioty/pokazPlanZajecPrzedmiotu&prz_kod="
                + kodPrzedmiotu + "&cdyd_kod=" + semestr + "&division=semester";
        ArrayList<String> list;

        try {
            HtmlFile hf = new HtmlFile(url, pathToSrc + "/src/main/Sources/plan.html");
            list = hf.searchForOccurences("https://usosweb.uw.edu.pl/kontroler.php[a-zA-Z0-9/=?&_]+gr_nr=\\d+", false);
            HashSet<Integer> kodyZajec = new HashSet<>();
            HashMap<Integer, HashSet<Integer>> kodyZajecOrazGrupy= new HashMap<>();
            HashSet<Integer> foo;
            int kod = 0, grupa = 0;

            for (String s : list) {
                Pattern pattern = Pattern.compile("https://usosweb.uw.edu.pl/kontroler.php[a-zA-Z0-9/=?&_]+zaj_cyk_id=(\\d+)&gr_nr=(\\d+)");
                Matcher matcher = pattern.matcher(s);
                if(matcher.find()){
                    kod = Integer.parseInt(matcher.group(1));
                    grupa = Integer.parseInt(matcher.group(2));
                    if(!kodyZajecOrazGrupy.containsKey(kod)){
                        foo = new HashSet<>();
                        foo.add(grupa);
                        kodyZajecOrazGrupy.put(kod, foo);
                    }
                    else{
                        foo = kodyZajecOrazGrupy.get(kod);
                        foo.add(grupa);
                        kodyZajecOrazGrupy.put(kod, foo);
                    }
                }
            }

            return kodyZajecOrazGrupy;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    @Override
    public String toString(){
        return "Przedmiot: " + nazwaPrzedmiotu + "\n" + "ECTS: " + ECTS + "\n" + "kod: " + kodPrzedmiotu + "\n" +
                "KierunkiMSMP: " + kierunkiToString() + "\n" + typyToString();
    }

    public String typyToString(){
        StringBuilder sb = new StringBuilder();
        if(typyZajec.size() == 0)
            return "-";

        for (TypZajec t : typyZajec) {
            sb.append("\n");
            sb.append(t);
            sb.append(" ");
        }
        return sb.toString();
    }

    public String kierunkiToString(){
        StringBuilder sb = new StringBuilder();
        if(kierunekMSMP.size() == 0)
            return "-";

        for (String s : kierunekMSMP) {
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString();
    }
}
