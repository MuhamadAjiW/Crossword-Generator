package solver;
import models.Intersection;
import models.Question;

import java.util.ArrayList;
import java.util.List;

public class CrosswordSolver {
    private CrosswordMap map;

    public List<Question> horizontalQuestions = new ArrayList();
    public List<Question> verticalQuestions = new ArrayList();
    public List<Intersection> intersections = new ArrayList<>();
    
    public CrosswordSolver(String mapPath) throws Exception{
        map = new CrosswordMap(mapPath);
        map.readMap();
    }

    public void addQuestionH(Question q){
        horizontalQuestions.add(q);
    }
    public void addQuestionV(Question q){
        verticalQuestions.add(q);
    }
    public void addIntersection(Intersection i){
        intersections.add(i);
    }

    public void printInfo(){
        System.out.println("Horizontal:");
        horizontalQuestions.forEach(question -> System.out.println(question.toString()));
        System.out.println("Vertical:");
        verticalQuestions.forEach(question -> System.out.println(question.toString()));
        System.out.println("Intersection:");
        intersections.forEach(intersection -> System.out.println(intersection.toString()));
    }

    private void getHorizontalQuestions(){
        for(int i = 0; i < map.mapHeight; i++){
            String line = map.lines.get(i);
            boolean previous = false;
            Question question = null;

            for(int j = 0; j < map.mapWidth; j++){
                if(line.charAt(j) == 'X'){
                    if(previous && question.length != 1){
                        addQuestionH(question);
                    }
                    previous = false;
                    continue;
                }

                if(previous){
                    question.addlength();
                    continue;
                }

                question = new Question(i, j);
                previous = true;
            }

            if(previous && question.length != 1){
                addQuestionH(question);
            }
        }
    }

    private void getVerticalQuestions(){
        for(int i = 0; i < map.mapWidth; i++){
            boolean previous = false;
            Question question = null;

            for(int j = 0; j < map.mapHeight; j++){
                if(map.lines.get(j).charAt(i) == 'X'){
                    if(previous && question.length != 1){
                        addQuestionV(question);
                    }
                    previous = false;
                    continue;
                }

                if(previous){
                    question.addlength();
                    continue;
                }

                question = new Question(j, i);
                previous = true;
            }

            if(previous && question.length != 1){
                addQuestionV(question);
            }
        }
    }

    private void getQuestions(){
        System.out.println("Generating questions coordinate");
        getHorizontalQuestions();
        getVerticalQuestions();
    }

    private void getIntersections(){
        System.out.println("Generating intersections coordinate");
        for(int i = 0; i < horizontalQuestions.size(); i++){
            Question hq = horizontalQuestions.get(i);

            for(int j = 0; j < verticalQuestions.size(); j++){
                Question vq = verticalQuestions.get(j);

                if( hq.x + hq.length >= vq.x &&
                    hq.x <= vq.x &&
                    vq.y + vq.length >= hq.y &&
                    vq.y <= hq.y
                ){
                    addIntersection(new Intersection(i, vq.x - hq.x, j, hq.y - vq.y));
                }
            }
        }
    }

    public void initialize(){
        getQuestions();
        getIntersections();
    }

    public static void main(String[] args) {
        try {
            CrosswordSolver c = new CrosswordSolver("map1.txt");
            c.initialize();
            c.printInfo();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
