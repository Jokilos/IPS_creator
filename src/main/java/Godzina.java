import java.util.Scanner;
import java.util.regex.Pattern;

public class Godzina {
    private int godzina;
    private int minuta;

    public static void main(String[] args){
        System.out.println(stringToGodzina("10:39"));
    }

    public Godzina(int godzina, int minuta){
        this.godzina = godzina;
        this.minuta = minuta;
    }

    public Godzina dodaj(Godzina other){
        int dodatkowaG = 0;
        if(minuta + other.minuta >= 60) dodatkowaG = 1;
        return new Godzina((godzina + other.godzina + dodatkowaG) % 24, (minuta + other.minuta) % 60);
    }

    public Godzina odejmij(Godzina other){ //może nie działać
        int dodatkowaG = 0;
        int minuta = (this.minuta - other.minuta) % 60;
        if(minuta < 0){
            dodatkowaG = 1;
            minuta += 60;
        }
        return new Godzina((godzina - other.godzina - dodatkowaG) % 24, minuta);
    }

    public static Godzina stringToGodzina(String godzina){
        if(godzina.length() >= 4 && godzina.length() <= 5){
            String[] data = godzina.split(":");
            return new Godzina(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
        }
        else{
            throw new RuntimeException();
        }
    }

    @Override
    public String toString() {
        String godzina, minuta;
        if(this.godzina < 10)
            godzina = "" + 0 + this.godzina;
        else
            godzina = "" + this.godzina;

        if(this.minuta < 10)
            minuta = "" + 0 + this.minuta;
        else
            minuta = "" + this.minuta;

        return "" + godzina + ":" + minuta;
    }

    public boolean equals(Godzina obj) {
        return godzina == obj.godzina && minuta == obj.minuta;
    }
}
