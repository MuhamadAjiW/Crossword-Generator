package solver;
import constants.ResourcePath;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CrosswordMap {
    private File file;
    public Integer mapWidth = 0;
    public Integer mapHeight = 0;
    public List<String> lines = new ArrayList<>();

    public void readMap() throws Exception {
        System.out.println("Reading map");

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = reader.readLine();

        if(line != null){
            Integer prev = line.length();
            while (line != null){
                if (line.chars().anyMatch(c -> c != 'X' && c != 'T')){
                    reader.close();
                    throw new IllegalArgumentException("Map contains illegal char");
                }

                lines.add(line);
                line = reader.readLine();

                if(line != null){
                    Integer next = line.length();
                    if(!prev.equals(next)){
                        reader.close();
                        throw new IllegalArgumentException("Map format is invalid");
                    }
                    prev = next;
                }

            }
        }

        if (lines.isEmpty()){
            reader.close();
            throw new IllegalArgumentException("Map is empty");
        }

        mapHeight = lines.size();
        mapWidth = lines.get(0).length();

        reader.close();
    }

    public CrosswordMap(String filePath){
        this.file = new File(ResourcePath.RESOURCES + filePath);
    }

    public void printMap(){
        lines.forEach(line -> System.out.println(line));
    }

    public static void main(String[] args) {
        try {
            CrosswordMap mp = new CrosswordMap("map1.txt");
            System.out.println(mp.mapHeight + " x " + mp.mapWidth);
            mp.printMap();
        } catch (Exception e){
            System.out.println("Map reading failed");
            e.printStackTrace();
        }
    }
}
