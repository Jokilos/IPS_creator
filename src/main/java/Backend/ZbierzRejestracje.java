package Backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class ZbierzRejestracje implements Sciezka{

    public final String URL_Rejestracji = "https://usosweb.uw.edu.pl/kontroler.php?_action=news/rejestracje/rejJednostki&jed_org_kod=";
    public final String URL_StronyKodow = "https://usosweb.uw.edu.pl/kontroler.php?_action=katalog2/przedmioty/szukajPrzedmiotu&method=rej&rej_kod=";
    public final String showAll = "&cp_showDescriptions=0&cp_showGroupsColumn=0&cp_cdydsDisplayLevel=2&f_tylkoWRejestracji=0&f_obcojezyczne=0&kierujNaPlanyGrupy=0&tabd59e_offset=0&tabd59e_limit=500&tabd59e_order=2a1a";
    public final String findClassCode = "https://usosweb.mimuw.edu.pl/kontroler.php?_action=katalog2/przedmioty/pokazPrzedmiot&prz_kod=";
    public static void main(String[] args) {
        ZbierzRejestracje zr = new ZbierzRejestracje();
        zr.getClassCodes(zr.getRegistrationCodes());
    }

    public ArrayList<String> getRegistrationCodes(){
        String inneStrony[] = new String[]{ "mimuw", "fuw", "chem.uw", "wnpism.uw", "wne.uw"};
        ArrayList<String> kodyStronKodowSpec = new ArrayList<>();
        ArrayList<String> kodyRejestracji = new ArrayList<>();
        ArrayList<String> kodyRejestracjiTemp;
        kodyStronKodowSpec.add("10000000");
        kodyStronKodowSpec.add("11000000");
        kodyStronKodowSpec.add("12000000");
        kodyStronKodowSpec.add("21000000");
        kodyStronKodowSpec.add("24000000");

        try {
            HtmlFile hf = new HtmlFile("https://usosweb.uw.edu.pl/kontroler.php?_action=news/rejestracje/index",
                    pathToSrc + "/src/main/Sources/kody_rej.html");

            ArrayList<String> kodyStronKodow = hf.searchForOccurences("jed_org_kod=\\d+", false);

            kodyStronKodow.replaceAll(s -> hf.groupPatternFinder(s, "jed_org_kod=(\\d+)", 1, 0));

            System.out.println(kodyStronKodow);

            for(String s : kodyStronKodow){ // strony .uw.
                hf.changeURL(this.URL_Rejestracji + s,
                        pathToSrc + "/src/main/Sources/strona_rej.html");
                System.out.println(s);
                kodyRejestracjiTemp = hf.searchForOccurences("rej_kod=[" + hf.special + hf.pl + "\\d]+[&']", false);
                kodyRejestracjiTemp.replaceAll(st -> hf.groupPatternFinder(st, "rej_kod=([" + hf.special + hf.pl + "\\d]+)[&']", 1, 0));
                System.out.println(kodyRejestracjiTemp);
                for(String kod : kodyRejestracjiTemp)
                    kodyRejestracji.add(kod);
            }

            for(int i = 0; i < kodyStronKodowSpec.size(); i++){ // strony niestandardowe np .mimuw.
                hf.changeURL("https://usosweb." + inneStrony[i] + ".edu.pl/kontroler.php?_action=news/rejestracje/rejJednostki&jed_org_kod=" + kodyStronKodowSpec.get(i),
                        pathToSrc + "/src/main/Sources/strona_rej.html");

                kodyRejestracjiTemp = hf.searchForOccurences("rej_kod=[" + hf.special + hf.pl + "\\d]+[&']", false);
                kodyRejestracjiTemp.replaceAll(st -> hf.groupPatternFinder(st, "rej_kod=([" + hf.special + hf.pl + "\\d]+)[&']", 1, 0));

                for(String kod : kodyRejestracjiTemp)
                    kodyRejestracji.add(kod);
            }

            //usuń powtorki
            /*Set<String> hs = new LinkedHashSet<String>();
            hs.addAll(kodyRejestracji);
            kodyRejestracji.clear();
            kodyRejestracji.addAll(hs);*/

            //zapisz do pliku
            hf.stringListToFile(kodyRejestracji, "kodyRejestracji");

            return kodyRejestracji;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getClassCodes(ArrayList<String> regCodes){
        Set<String> classCodesTemp = new HashSet<>();
        ArrayList<String> classCodes = new ArrayList<>();
        HtmlFile hf = new HtmlFile();

        try{
            for(String s : regCodes){
                hf.changeURL(URL_StronyKodow + s + showAll, pathToSrc + "/src/main/Sources/kody.html");
                classCodes = hf.searchForOccurences("prz_kod=([" + hf.special + hf.pl + "\\d]+)[&']", false);
                for(String kod : classCodes) {
                    System.out.println(kod.substring("prz_kod=".length(), kod.length() - 1));
                    classCodesTemp.add(kod.substring("prz_kod=".length(), kod.length() - 1));
                }
                System.out.println("Obecna liczba:" + classCodesTemp.size());
            }

            classCodes.clear();
            classCodes.addAll(classCodesTemp);

            hf.stringListToFile(classCodes, "kodyPrzedmiotów");

            return classCodes;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
