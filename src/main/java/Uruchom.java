import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Uruchom {

    public WszystkiePrzedmioty wczytajDane(){
        try {
            String json = Files.readString(Path.of("/home/adminq/GitHub/Playtime_is_over/PO/Ips_creator/src/main/Sources/przedmioty/Baza.js"),
                    StandardCharsets.ISO_8859_1);
            System.out.println(json.substring(0, 500));
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    return null;
    }


}
