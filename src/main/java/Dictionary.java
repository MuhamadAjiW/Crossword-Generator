import java.io.File;
import java.io.FileReader;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import constants.ResourcePath;

public class Dictionary {
    private File file;
    public List<String> wordlist = new ArrayList<>();
    
    public Dictionary(String filepath){
        this.file = new File(ResourcePath.RESOURCES + filepath);
    }

    public void initialize() throws Exception{
        System.out.println("Grouping dictionary by length");

        FileReader reader = new FileReader(file);
        JSONParser parser = new JSONParser();
        JSONObject dictionary = (JSONObject) parser.parse(reader);

        List<String> words = new ArrayList<>(dictionary.keySet());
        words.sort(Comparator.comparingInt(String::length).reversed());
        
        words.forEach(word -> {
            //System.out.println(word);
            wordlist.add(word);
        });
        
    }
}
