import models.Pair;
import models.Point;
import models.Question;

import java.util.ArrayList;
import java.util.List;

//tanpa heuristik pemadatan
public class CrosswordGeneratorPlain extends CrosswordGenerator{
    List<Pair<Point, Character>> possiblePoints;

    public CrosswordGeneratorPlain(String dictPath){
        this.dict = new Dictionary(dictPath);
    }

    public void addQuestionH(Question q, String answer){
        crossword.addQuestionH(q, answer);

        for(int i = 0; i < q.length; i++){
            Pair<Point, Character> newPair = new Pair<Point,Character>(new Point(q.x + i, q.y), answer.charAt(i));
            possiblePoints.add(newPair);
        }
    }

    public void addQuestionV(Question q, String answer){
        crossword.addQuestionV(q, answer);

        for(int i = 0; i < q.length; i++){
            Pair<Point, Character> newPair = new Pair<Point,Character>(new Point(q.x, q.y + i), answer.charAt(i));
            possiblePoints.add(newPair);
        }
    }

    public void initialize() throws Exception{
        dict.initialize();
        possiblePoints = new ArrayList<>();
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
        addQuestionH(new Question(0, 0, starter.length()), starter);
        crossword.maxWidth = starter.length();
        crossword.maxHeight = 1;
        System.out.println("Initialized with starter: " + starter + " at (0,0)");        
        
        boolean success = false;
        boolean possible = true;
        boolean vertical = true;
        while(true){
            possible = false;
            success = false;

            // System.out.println("Remaining: " + unlocatedStrings.size());
            for(String word : unlocatedStrings){
                // System.out.println("Checking on: " + word);
                
                int len = word.length();
                if(vertical){
                    for(Pair<Point, Character> p : possiblePoints){
                        int ypoint = p.first.loc.first;
                        int xpoint = p.first.loc.second;
                    
                        Character v = p.second;
                        success = false;

                        for(int i = 0; i<len; i++){
                            if(v == word.charAt(i)){
                                int y = ypoint - i;
                                int x = xpoint;

                                Boolean canAdd = true;
                                Question addedQuestion = new Question(y, x, len);

                                if(canAdd){
                                    for(int j = 0; j < len; j++){
                                        if(j == i) {
                                            continue;
                                        };
                                        if(crossword.matrix.get("("+ (y + j) +","+ (x) +")") != null){
                                            if(crossword.matrix.get("("+ (y + j) +","+ (x) +")") != word.charAt(j)) {
                                                canAdd = false;
                                                break;
                                            };
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
                                        addQuestionV(addedQuestion, word);
                                        vertical = false;
                                                
                                        // System.out.println("Added vertical question: " + addedQuestion.toString() + " with word: " + word);
                                        success = true;
                                        break;
                                    }
                                }
                            }
                        }

                        if(success) break;
                    }
                }
                else{
                    for(Pair<Point, Character> p : possiblePoints){
                        int ypoint = p.first.loc.first;
                        int xpoint = p.first.loc.second;

                        Character v = p.second;
                        success = false;

                        for(int i = 0; i<len; i++){
                            if(v == word.charAt(i)){
                                int y = ypoint;
                                int x = xpoint - i;

                                Boolean canAdd = true;
                                Question addedQuestion = new Question(y, x, len);

                                if(canAdd){
                                    for(int j = 0; j < len; j++){
                                        if(j == i) {
                                            continue;
                                        }
                                        if(crossword.matrix.get("("+ (y) +","+ (x + j) +")") != null){
                                            if(crossword.matrix.get("("+ (y) +","+ (x + j) +")") != word.charAt(j)) {
                                                canAdd = false;
                                                break;
                                            };
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
                                        addQuestionH(addedQuestion, word);
                                        vertical = true;
        
                                        // System.out.println("Added horizontal question: " + addedQuestion.toString() + " with word: " + word);
                                        success = true;
                                        break;
                                    }
                                }
                            }
                        }

                        if(success) break;
                    }
                }

                if (success) {
                    possible = true;
                    unlocatedStrings.remove(word);
                    break;
                };
            }

            
            if(unlocatedStrings.isEmpty()){
                System.out.println("Every word has been added successfully");
                break;
            }

            if (!possible){
                System.out.println("Not possible to add more words");
            }
        }

        System.out.println("Done generating\n");
    }
}
