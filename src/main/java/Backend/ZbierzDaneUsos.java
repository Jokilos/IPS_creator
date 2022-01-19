package Backend;

import DataClass.Przedmiot;
import DataClass.WszystkiePrzedmioty;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import java.io.*;
import java.util.*;


public class ZbierzDaneUsos implements Sciezka{
    public static void main(String[] args){
        //Backend.ZbierzRejestracje zb = new Backend.ZbierzRejestracje();
        //zb.getClassCodes(zb.getRegistrationCodes());
        ArrayList<String> codes = codesFromFile();
        List<String> codesSorted = codes.subList(1, codes.size());
        Collections.sort(codesSorted);
        System.out.println(codesSorted);
        HtmlFile hf = new HtmlFile();
        hf.stringListToFile(codesSorted, "kodyPrzedmiotów");
        getJsonFiles(codes);
    }

    public static ArrayList<String> codesFromFile(){
        try {
            File file = new File(pathToSrc + "/src/main/Sources/kodyPrzedmiotów");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            ArrayList<String> codes = new ArrayList<>();

            for (int i = 1; (line = br.readLine()) != null; i++) {
                codes.add(line.trim());
            }
            Set<String> s = new HashSet<>();
            s.addAll(codes);
            codes.clear();
            codes.addAll(s);
            System.out.println(codes.size());
            return codes;
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    public static void getJsonFiles(ArrayList<String> codes){
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<WszystkiePrzedmioty> ja = moshi.adapter(WszystkiePrzedmioty.class);

        String code = "";
        String semestr = "2021Z";
        WszystkiePrzedmioty wp = new WszystkiePrzedmioty();

        for (int i = 0; i < codes.size(); i++) {
            code = codes.get(i).trim();
            System.out.println("" + i + ": " + code);
            Przedmiot p = new Przedmiot(codes.get(i).trim(), semestr);
            wp.dodajPrzedmiot(p);
        }

        try{
            JSONObject preetyjson = new JSONObject(ja.toJson(wp));
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToSrc + "/src/main/Sources/przedmioty/Baza.js"));
            writer.write(preetyjson.toString(2));
            writer.close();
        }
        catch(JSONException | IOException e){
            e.printStackTrace();
        }

    }

    public static ArrayList<String> getClassesCodes(){
        String url = "https://usosweb.mimuw.edu.pl/kontroler.php?_action=katalog2/przedmioty/szukajPrzedmiotu&method=rej&rej_kod=1000-2021-INF&callback=g_0c2fe8e2";
        try {
            HtmlFile hf = new HtmlFile(url, pathToSrc + "/src/main/Sources/kody.html");
            ArrayList<String> codes = hf.searchForOccurencesWithLineDelay("<td style='vertical-align:middle' >", 1);

            return codes;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
