package models;

public class Point {
    public Pair<Integer, Integer> loc;

    public Point(int x, int y){
        this.loc = new Pair<Integer,Integer>(y, x);
    }

    @Override
    public String toString(){
        return "(" + loc.first + "," + loc.second + ")";
    }
}
