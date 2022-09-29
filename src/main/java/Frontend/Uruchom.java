package Frontend;

import Backend.ListSwapper;
import Backend.PosortujPoKodzie;
import Backend.Sciezka;
import DataClass.*;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import kotlin.Pair;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Uruchom implements Sciezka {

    private WszystkiePrzedmioty wp;
    private ArrayList<Przedmiot> wybranePrzedmioty;
    Scanner inpSc = new Scanner(System.in);


    public Uruchom(){
        try {
            Path baza =
                    Paths.get(pathToSrc + "/src/main/Sources/przedmioty/Baza.json");
            String json = Files.readString(baza);

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<WszystkiePrzedmioty> ja = moshi.adapter(WszystkiePrzedmioty.class);

            this.wp = ja.fromJson(json);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        Uruchom u = new Uruchom();
        u.posortujPrzedmiotyPoKodzie();
        u.zbierzPrzedmioty();
        u.znajdzWarunkiDlaPrzedmiotow();
    }

    public void zbierzPrzedmioty(){
        boolean zbierajPrzedmioty = true, podajIlosc = true;
        int liczba = 0;
        String nextLine = "";
        System.out.println("Witaj w kreatorze planu studiów! ");
        wybranePrzedmioty = new ArrayList<Przedmiot>();

        while(zbierajPrzedmioty){
            if(podajIlosc) {
                liczba = wczytajLiczbę("Ile chiałbyś dodać przedmotów?");
                podajIlosc = false;
            }

            System.out.println("Podaj kod przedmiotu z serwisu USOS:");
            if(inpSc.hasNextLine()) nextLine = inpSc.nextLine();
            try {
                Przedmiot znaleziony = szukajPrzedmiotu(nextLine);
                System.out.println("Znaleziony przedmiot to: " + znaleziony.getNazwaPrzedmiotu());
                System.out.println("Czy chodziło o ten przedmiot?");
                if(takLubNie()){
                    wybranePrzedmioty.add(znaleziony);
                }
            }
            catch (ClassNotFoundException e){
                System.out.println("Nie znaleziono danego przedmiotu");
            }
            if(--liczba <= 0){
                System.out.println("Czy chcesz dalej dodawać przedmioty?");
                if(takLubNie()){
                    podajIlosc = true;
                }
                else{
                    zbierajPrzedmioty = false;
                }
            }
        }
        System.err.println(new Throwable().getStackTrace()[0].getMethodName() + " completed");
    }

    public void znajdzWarunkiDlaPrzedmiotow(){
        ArrayList<ArrayList<Integer>> lista_perm = new ArrayList<>();

        for(Przedmiot p : wybranePrzedmioty){

            System.out.println("Ustal preferencje grup dla przedmiotu: " + p.getNazwaPrzedmiotu());
            for(TypZajec tz : p.getTypyZajec()){

            }
        }
    }

//    public <T, L extends List<T>>
//    void ustawPreferencje(L list){
//        for(T elem : list){
//            System.out.println(elem);
//        }
//
//        ListSwapper ls = new ListSwapper(list.size());
//        boolean zbierajPreferencje = true;
//        int indeks = 0;
//
//        if ();
//        for(int i = 0; i < list.size() && zbierajPreferencje; i++){
//
//            if(i == 0)
//                indeks = wczytajLiczbę("Podaj nr grupy o najwyższym priorytecie:");
//            else
//                indeks = wczytajLiczbę("Podaj nr grupy o priorytecie " + i + ":");
//
//            Pair<Integer, Integer> zamien = ls.registerSingleSwap(i, indeks);
//            listSwap(list, zamien);
//        }
//    }

    public static <E, L extends List<E>>
    void listSwap(L list, Pair<Integer, Integer> swap){
      E foo = list.get(swap.getFirst());
      list.set(swap.getFirst(), list.get(swap.getSecond()));
      list.set(swap.getSecond(), foo);
    }

    public Przedmiot szukajPrzedmiotu(String kodPrzedmiotu) throws ClassNotFoundException{
        System.err.println("Szukanie przedmiotu o kodzie:" + kodPrzedmiotu);
        int indeks = szukajIndeksuPrzedmiotu(0, wp.getPrzedmioty().size() - 1, kodPrzedmiotu);
        if(indeks < 0 || indeks > wp.getPrzedmioty().size() - 1)
            throw new ClassNotFoundException("Class Not Found.");
        else
            return wp.getPrzedmioty().get(indeks);
    }

    public int szukajIndeksuPrzedmiotu(int l, int r, String kodPrzedmiotu){
        if (r >= l) {
            int mid = l + (r - l) / 2;

            if (wp.getPrzedmioty().get(mid).getKodPrzedmiotu().compareTo(kodPrzedmiotu) == 0) {
                return mid;
            }

            //lewa część listy
            if (wp.getPrzedmioty().get(mid).getKodPrzedmiotu().compareTo(kodPrzedmiotu) > 0) {
                return szukajIndeksuPrzedmiotu(l, mid - 1, kodPrzedmiotu);
            }

            //prawa część listy
            return szukajIndeksuPrzedmiotu(mid + 1, r, kodPrzedmiotu);
        }

        return -1;
    }

    public boolean takLubNie(){
        while(true){
            System.out.println("[T/N]?");
            String answer = inpSc.nextLine();
            if(answer.length() > 0) {
                if (answer.charAt(0) == 'T' || answer.charAt(0) == 't') {
                    return true;
                } else if (answer.charAt(0) == 'N' || answer.charAt(0) == 'n') {
                    return false;
                }
            }
        }
    }

    public int wczytajLiczbę(String query){
        System.out.println(query);
        while(!inpSc.hasNextInt()){
            System.out.println(query);
            inpSc.nextLine();
            System.err.println("Nieprawidłowy format liczby");
        }
        Integer res = null;
        do {
            try {
                res = Integer.parseInt(inpSc.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Nieprawidłowy format liczby");
            }
        } while(res == null);
        return res;
    }

    private void posortujPrzedmiotyPoKodzie()
    {
        Collections.sort(wp.getPrzedmioty(), new PosortujPoKodzie());
        for(Przedmiot p : wp.getPrzedmioty()){
            System.out.println(p.getKodPrzedmiotu());
        }
    }

}
