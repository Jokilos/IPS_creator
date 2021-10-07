public enum DzienTygodnia {
    PONIEDZIALEK(1),
    WTOREK(2),
    SRODA(3),
    CZWARTEK(4),
    PIATEK(5),
    SOBOTA(6),
    NIEDZIELA(7);

    int numer;

    private DzienTygodnia(int numerDnia){
        numer = numerDnia;
    }


    @Override
    public String toString() {
        switch(numer){
            case 1:
                return "Poniedziałek";
            case 2:
                return "Wtorek";
            case 3:
                return "Środa";
            case 4:
                return "Czwartek";
            case 5:
                return "Piątek";
            case 6:
                return "Sobota";
            case 7:
                return "Niedziela";
        }
        return "Dzień tygodnia";
    }

    public static DzienTygodnia stringToDzien(String dzien){
        switch (dzien) {
            case "poniedziałek":
                return DzienTygodnia.PONIEDZIALEK;
            case "wtorek":
                return DzienTygodnia.WTOREK;
            case "środa":
                return DzienTygodnia.SRODA;
            case "czwartek":
                return DzienTygodnia.CZWARTEK;
            case "piątek":
                return DzienTygodnia.PIATEK;
            case "sobota":
                return DzienTygodnia.SOBOTA;
            case "niedziela":
                return DzienTygodnia.NIEDZIELA;
        }
        return null; //cokolwiek
    }


}
