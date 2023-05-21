package models;

public class Intersection {
    public int horizontalQccode;
    public int horizontalQcidx;
    public int verticalQccode;
    public int verticalQcidx;

    public Intersection(int hqc, int hqi, int vqc, int vqi){
        horizontalQccode = hqc;
        horizontalQcidx = hqi;
        verticalQccode = vqc;
        verticalQcidx = vqi;
    }

    @Override
    public String toString(){
        return ("Intersection for hq " + horizontalQccode +"(index "+ horizontalQcidx + ")" +
                ", vq " + verticalQccode +"(index "+ verticalQcidx + ")");
    }
}
