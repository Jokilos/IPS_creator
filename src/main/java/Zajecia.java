public class Zajecia {
    private Godzina rozpoczecie;
    private Godzina zakonczenie;
    private Godzina czasTrwania;
    private String zasada;
    private DzienTygodnia dzienTygodnia;


    public Zajecia(String zasada, String dzien, String roz, String za) {
        this.zasada = zasada;
        dzienTygodnia = DzienTygodnia.stringToDzien(dzien);
        rozpoczecie = Godzina.stringToGodzina(roz);
        zakonczenie = Godzina.stringToGodzina(za);
        czasTrwania = zakonczenie.odejmij(rozpoczecie);
    }

    @Override
    public String toString(){
        return "Zasada: " + zasada + " Dzień: " + dzienTygodnia + " Rozpoczęcie: " + rozpoczecie + " Zakończenie: " + zakonczenie;
    }

    public boolean equals(Zajecia obj) {
        return dzienTygodnia.equals(obj.dzienTygodnia) && rozpoczecie.equals(obj.rozpoczecie) && zakonczenie.equals(obj.zakonczenie);
    }
}
