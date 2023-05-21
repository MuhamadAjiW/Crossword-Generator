import java.util.HashMap;
import java.util.Map;

import models.Question;

public class Crossword {
    public int minWidth = 0;
    public int minHeight = 0;
    public int maxWidth = 0;
    public int maxHeight = 0;
    public Map<String, Character> matrix = new HashMap<>();

    /*
    public List<Question> horizontalQuestions = new ArrayList();
    public List<Question> verticalQuestions = new ArrayList();
    public List<String> horizontalAnswer = new ArrayList();
    public List<String> verticalAnswer = new ArrayList();
    */

    public void addQuestionH(Question q, String answer){
        /* 
        horizontalQuestions.add(q);
        horizontalAnswer.add(answer);
        */

        for(int i = 0; i < q.length; i++){
            matrix.put("(" + q.y + "," + (q.x + i) + ")", answer.charAt(i));
        }

        maxWidth = Math.max(maxWidth, q.x + q.length);
        minWidth = Math.min(minWidth, q.x);
    }
    public void addQuestionV(Question q, String answer){
        /*
        verticalQuestions.add(q);
        verticalAnswer.add(answer);
        */

        for(int i = 0; i < q.length; i++){
            matrix.put("(" + (q.y + i) + "," + (q.x) + ")", answer.charAt(i));
        }

        maxHeight = Math.max(maxHeight, q.y + q.length);
        minHeight = Math.min(minHeight, q.y);
    }

    public void print(){
        /*
        System.out.println("Horizontal:");
        horizontalQuestions.forEach(question -> System.out.println(question.toString() + ", answer: " + horizontalAnswer.get(horizontalQuestions.indexOf(question))));
        System.out.println("Vertical:");
        verticalQuestions.forEach(question -> System.out.println(question.toString() + ", answer: " + verticalAnswer.get(verticalQuestions.indexOf(question))));
        System.out.println("Intersection:");
        intersections.forEach(intersection -> System.out.println(intersection.toString()));
        */

        for(int i = minHeight; i < maxHeight; i++){
            for(int j = minWidth; j < maxWidth; j++){
                Character c = matrix.get("(" + i + "," + j + ")");
                if(c != null){
                    System.out.print(c);
                }
                else{
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
