import models.Pair;
import models.Point;
import models.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrosswordGeneratorComplete extends CrosswordGenerator{
    Map<String, Pair<Point, Character>> possibleVHash;
    Map<String, Pair<Point, Character>> possibleHHash;
    List<Pair<Point, Character>> possibleV;
    List<Pair<Point, Character>> possibleH;

    public CrosswordGeneratorComplete(String dictPath){
        this.dict = new Dictionary(dictPath);
    }

    public void addQuestionH(Question q, String answer, Set<String> toBeIntersections){
        crossword.addQuestionH(q, answer);

        for(int i = 0; i < q.length; i++){
            String key = "(" + q.y + "," + (q.x + i) + ")";
            if(toBeIntersections.contains(key)) {
                if(possibleVHash.containsKey(key)){
                    possibleV.remove(possibleVHash.get(key));
                    possibleVHash.remove(key);
                }
                if(possibleHHash.containsKey(key)){
                    possibleH.remove(possibleHHash.get(key));
                    possibleHHash.remove(key);
                }
                continue;
            }
            Pair<Point, Character> newPair = new Pair<Point,Character>(new Point(q.x + i, q.y), answer.charAt(i));
            possibleV.add(newPair);
            possibleVHash.put(key, newPair);
        }

    }

    public void addQuestionV(Question q, String answer, Set<String> toBeIntersections){
        crossword.addQuestionV(q, answer);

        for(int i = 0; i < q.length; i++){
            String key = "(" + (q.y + i) + "," + q.x + ")";
            if(toBeIntersections.contains(key)) {
                if(possibleVHash.containsKey(key)){
                    possibleV.remove(possibleVHash.get(key));
                    possibleVHash.remove(key);
                }
                if(possibleHHash.containsKey(key)){
                    possibleH.remove(possibleHHash.get(key));
                    possibleHHash.remove(key);
                }
                continue;
            };
            Pair<Point, Character> newPair = new Pair<Point,Character>(new Point(q.x, q.y + i), answer.charAt(i));
            possibleH.add(newPair);
            possibleHHash.put(key, newPair);
        }
    }

    public void initialize() throws Exception{
        dict.initialize();
        dict.sortWords();
        possibleVHash = new HashMap<>();
        possibleHHash = new HashMap<>();
        possibleV = new ArrayList<>();
        possibleH = new ArrayList<>();
    }
    
    @Override
    public void generate() throws Exception{
        initialize();
        crossword = new Crossword();

        List<String> unlocatedStrings = new ArrayList<>();
        for(String word : dict.wordlist){
            unlocatedStrings.add(word);
        }

        String starter = unlocatedStrings.remove(0);
        Set<String> toBeIntersections = new HashSet<>();
        addQuestionH(new Question(0, 0, starter.length()), starter, toBeIntersections);
        crossword.maxWidth = starter.length();
        crossword.maxHeight = 1;
        System.out.println("Initialized with starter: " + starter + " at (0,0)");        
        
        boolean success = false;
        boolean possible = true;
        boolean possible2 = true;
        boolean compaction = true;
        boolean vertical = true;
        while(true){
            possible = false;
            success = false;

            // System.out.println("Remaining: " + unlocatedStrings.size());
            for(String word : unlocatedStrings){
                // System.out.println("Checking on: " + word);
                
                int len = word.length();
                boolean even = (len%2 == 0);
                if(vertical){
                    for(Pair<Point, Character> p : possibleV){
                        int ypoint = p.first.loc.first;
                        int xpoint = p.first.loc.second;
                    
                        Character v = p.second;
                        success = false;

                        int i = len/2;
                        int increment = 1;
                        boolean add = true;
                        while(true){
                            if(i == len){}
                            else if(v == word.charAt(i)){
                                int y = ypoint - i;
                                int x = xpoint;

                                Boolean canAdd = true;
                                Question addedQuestion = new Question(y, x, len);
                                if (compaction && crossword.expanding(addedQuestion, word)) canAdd = false;

                                if(canAdd){
                                    toBeIntersections = new HashSet<>();
                                    for(int j = 0; j < len; j++){
                                        if(j == i) {
                                            toBeIntersections.add("(" + (y + j) + "," + x + ")");
                                            continue;
                                        };
                                        if(crossword.matrix.get("("+ (y + j) +","+ (x) +")") != null){
                                            if(crossword.matrix.get("("+ (y + j) +","+ (x) +")") != word.charAt(j)) {
                                                canAdd = false;
                                                break;
                                            };
                                            toBeIntersections.add("(" + (y + j) + "," + x + ")");
                                        }
                                        else{
                                            if(crossword.matrix.get("("+ (y + j) +","+ (x + 1) +")") != null){
                                                canAdd = false;
                                                break;
                                            }
                                            if(crossword.matrix.get("("+ (y + j) +","+ (x - 1) +")") != null){
                                                canAdd = false;
                                                break;
                                            }
                                        }
                                    }
                                    if(crossword.matrix.get("("+ (y + len) +","+ (x) +")") != null) canAdd = false;
                                    if(crossword.matrix.get("("+ (y - 1) +","+ (x) +")") != null) canAdd = false;
                                    if(canAdd) {
                                        addQuestionV(addedQuestion, word, toBeIntersections);
                                        vertical = false;
                
                                        // System.out.println("Added vertical question: " + addedQuestion.toString() + " with word: " + word);
                                        success = true;
                                        break;
                                    }
                                }
                            }

                            if(i == 0 && even) break;
                            if(i == len && !even) break;
                            
                
                            if(add){
                                i += increment;
                                add = false;
                            }
                            else{
                                i -= increment;
                                add = true;
                            }
                            increment++;
                        }

                        if(success) break;
                    }
                }
                else{
                    for(Pair<Point, Character> p : possibleH){
                        int ypoint = p.first.loc.first;
                        int xpoint = p.first.loc.second;

                        Character v = p.second;
                        success = false;

                        int i = len/2;
                        int increment = 1;
                        boolean add = true;
                        while (true){
                            if(i == len){}
                            else if(v == word.charAt(i)){
                                int y = ypoint;
                                int x = xpoint - i;

                                Boolean canAdd = true;
                                Question addedQuestion = new Question(y, x, len);
                                if (compaction && crossword.expanding(addedQuestion, word)) canAdd = false;

                                if(canAdd){
                                    toBeIntersections = new HashSet<>();
                                    for(int j = 0; j < len; j++){
                                        if(j == i) {
                                            toBeIntersections.add("(" + (y) + "," + (x + j) + ")");
                                            continue;
                                        }
                                        if(crossword.matrix.get("("+ (y) +","+ (x + j) +")") != null){
                                            if(crossword.matrix.get("("+ (y) +","+ (x + j) +")") != word.charAt(j)) {
                                                canAdd = false;
                                                break;
                                            };
                                            toBeIntersections.add("(" + (y) + "," + (x + j) + ")");
                                        }
                                        else{
                                            if(crossword.matrix.get("("+ (y + 1) +","+ (x + j) +")") != null){
                                                canAdd = false;
                                                break;
                                            }
                                            if(crossword.matrix.get("("+ (y - 1) +","+ (x + j) +")") != null){
                                                canAdd = false;
                                                break;
                                            }
                                        }
                                    }
                                    if(crossword.matrix.get("("+ (y) +","+ (x + len) +")") != null) canAdd = false;
                                    if(crossword.matrix.get("("+ (y) +","+ (x - 1) +")") != null) canAdd = false;
                                    if(canAdd) {
                                        addQuestionH(addedQuestion, word, toBeIntersections);
                                        vertical = true;
        
                                        // System.out.println("Added horizontal question: " + addedQuestion.toString() + " with word: " + word);
                                        success = true;
                                        break;
                                    }
                                }
                            }

                            if(i == 0 && even) break;
                            if(i == len && !even) break;
                
                            if(add){
                                i += increment;
                                add = false;
                            }
                            else{
                                i -= increment;
                                add = true;
                            }
                            increment++;
                        }

                        if(success) break;
                    }
                }

                if (success) {
                    possible = true;
                    possible2 = true;
                    compaction = true;
                    unlocatedStrings.remove(word);
                    break;
                };
            }

            
            if(unlocatedStrings.isEmpty()){
                System.out.println("Every word has been added successfully");
                break;
            }

            if (!possible){
                if(possible2){
                    possible2 = false;
                    vertical = !vertical;
                }
                else if (!possible && compaction){
                    compaction = false;
                    possible2 = true;
                    vertical = !vertical;
                }
                else{
                    System.out.println("Not possible to add more words");
                    break;
                }
            }
        }

        System.out.println("Done generating\n");
    }
}
