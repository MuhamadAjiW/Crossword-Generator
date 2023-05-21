package models;

public class Question {
    public int x;
    public int y;
    public int length;

    public Question(int y,int x){
        this.y = y;
        this.x = x;
        this.length = 1;
    }
    public Question(int y,int x, int length){
        this.y = y;
        this.x = x;
        this.length = length;
    }
    public void addlength(){
        this.length++;
    }

    @Override
    public String toString(){
        return ("(" + y + "," + x + "), with length " + length);
    }

}
