import java.util.ArrayList;
import java.util.List;

public class Grupa {
    private int id_grupy;
    private String prowadzacy;
    private List<Zajecia> zajecia;

    public Grupa(String prowadzacy, int id, HtmlFile hf){
        this.prowadzacy = prowadzacy;
        id_grupy = id;
        zajecia = new ArrayList<>();
        ArrayList<String> data = hf.searchForOccurences
                ("\\b(["+ hf.plLc+ "() ]+), (\\d{1,2}:\\d{2}) - (\\d{1,2}:\\d{2})", false);
        for(String s: data){
            String[] dataZajecia = s.split("(, | - )");
            if(dataZajecia.length != 3) throw new RuntimeException();
            String dzienreg = "(poniedziałek|wtorek|środa|czwartek|piątek|sobota|niedziela)";
            String dzien = hf.groupPatternFinder(dataZajecia[0], dzienreg, 1, 0);
            zajecia.add(new Zajecia(dataZajecia[0].replace(" " + dzien, ""), dzien, dataZajecia[1], dataZajecia[2]));
        }
    }

    @Override
    public String toString(){
        return "Grupa nr." + id_grupy + " \n" + "Prowadzący:" + prowadzacy + "\n" + zajeciaToString();
    }

    public String zajeciaToString(){
        StringBuilder sb = new StringBuilder();
        if(zajecia.size() == 0)
            return "-";

        for (Zajecia z: zajecia) {
            sb.append(z);
            sb.append(" ");
            sb.append("\n");
        }
        return sb.toString();
    }
}
