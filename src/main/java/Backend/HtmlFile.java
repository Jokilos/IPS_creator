package Backend;

import lombok.Getter;
import lombok.Setter;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HtmlFile implements Sciezka{
    private String dataLine;
    private String url;
    private String filename;
    public final String pl = "AaĄąBbCcĆćDdEeĘęFfGgHhIiJjKkLlŁłMmNnŃńOoÓóPpRrSsŚśTtUuWwVvYyQqXxZzŹźŻż";
    public final String plUc = "AĄBCĆDEĘFGHIJKLŁMNŃOÓPRSŚTUWVYQXZŹŻ";
    public final String plLc = "aąbcćdeęfghijklłmnńoóprsśtuwvyqxzźż";
    public final String special = "-_!@#$%^*()+=;:\\[\\]<>,.?/";
    public final String specialWithApo = "-'’\"_!@#$%^*()+=;:\\[\\]<>,.?/";

    public static void main(String[] args) throws IOException{
        HtmlFile Obj = new HtmlFile
                ("https://usosweb.uw.edu.pl/kontroler.php?_action=katalog2/przedmioty/pokazPrzedmiot&prz_kod=1000-112bAM2a",
                        pathToSrc + "/src/main/Sources/source.html");

    }

    public HtmlFile(){
    }

    public HtmlFile(String url, String filename) throws IOException{
        this.url = url;
        this.filename = filename;
        this.getFile();
    }

    public void changeURL(String url, String filename) throws IOException{
        this.url = url;
        this.filename = filename;
        this.getFile();
    }

    public void getFile() {
        URL url;
        try {
            // get URL content
            url = new URL(this.url);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;

            //save to this filename
            File file = new File(this.filename);

            if (!file.exists()) {
                file.createNewFile();
            }

            //use FileWriter to write file
            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));

            while ((inputLine = br.readLine()) != null) {
                bw.write(inputLine + System.lineSeparator());
            }
            bw.close();
            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getDataLine(int dataLineNum){
        File file = new File(this.filename);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            for (int i = 1; (line = br.readLine()) != null; i++) {
                if(i == dataLineNum)
                    this.dataLine = line;
            }
        }
        catch (Exception e){
            System.err.println(e.getMessage()); // handle exception
        }
    }

    public String searchForOccurence(String regex){
        File file = new File(this.filename);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                matcher = pattern.matcher(line);
                while(matcher.find()){
                    return line;
                }
            }
        }
        catch (Exception e){
            System.err.println(e.getMessage()); // handle exception
        }
        return "";
    }

    public ArrayList<String> searchForOccurences(String regex, boolean firstOnly){
        File file = new File(this.filename);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        ArrayList<String> data = new ArrayList<>();
        boolean foundMatch = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            for (int i = 1; !(firstOnly && foundMatch) && (line = br.readLine()) != null; i++) {
                matcher = pattern.matcher(line);
                while(matcher.find()){
                    data.add(matcher.group());
                    foundMatch = true;
                }
            }
        }
        catch (Exception e){
            System.err.println(e.getMessage()); // handle exception
        }
        return data;
    }

    public ArrayList<String> searchForOccurenceLineDelays(String regex, int lineDelay, String regexUntil){
        File file = new File(this.filename);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        ArrayList<String> data = new ArrayList<>();
        boolean foundMatch = false;
        ArrayList<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = "";
            for (int i = 1; !(foundMatch) && (line = br.readLine()) != null; i++) {
                matcher = pattern.matcher(line);
                while(matcher.find()){
                    foundMatch = true;
                }
            }
            for (int i = 0; i < lineDelay; i++){
                line = br.readLine();
            }
            pattern = Pattern.compile(regexUntil);
            matcher = pattern.matcher(line);
            while (matcher.find()){
                list.add(line);
                line = br.readLine();
                matcher = pattern.matcher(line);
            }
        }
        catch (Exception e){
            System.err.println(e.getMessage()); // handle exception
        }

        return list;
    }

    public String searchForOccurenceLineDelay(String regex, int lineDelay){
        File file = new File(this.filename);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        boolean foundMatch = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = "";
            for (int i = 1; !(foundMatch) && (line = br.readLine()) != null; i++) {
                matcher = pattern.matcher(line);
                while(matcher.find()){
                    foundMatch = true;
                }
            }
            for (int i = 0; i < lineDelay; i++){
                line = br.readLine();
            }
            return line;
        }
        catch (Exception e){
            System.err.println(e.getMessage()); // handle exception
        }
        return "";
    }

    public ArrayList<String> searchForOccurencesWithLineDelay(String regex, int lineDelay){
        File file = new File(this.filename);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        ArrayList<String> data = new ArrayList<>();
        boolean foundMatch = false;
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while((line = br.readLine()) != null) {
                matcher = pattern.matcher(line);
                if(matcher.find()){
                    for (int i = 0; i < lineDelay; i++){
                        line = br.readLine();
                    }
                    data.add(line);
                }
            }
            return data;
        }
        catch (Exception e){
            System.err.println(e.getMessage()); // handle exception
        }
        return data;
    }

    public String groupPatternFinder(String data, String regex, int group, int occurence){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        for(int i = 0; matcher.find(); i++) {
            if(i == occurence)
                return matcher.group(group);
        }
        return "";
    }

    public void stringListToFile(List<String> list, String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToSrc + "/src/main/Sources/" + filename));
            for(String s : list)
                writer.write(s + "\n");
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
