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

    public static void main(String[] args) {
        int number = 10;
        int i = number/2;
        int increment = 1;
        boolean add = true;
        boolean even = (number%2 == 0);
        while(true){
            System.out.println(i + ", increment: " + increment);
            if(i == 0 && even) break;
            else if(i == number && !even) break;

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
    }
}
