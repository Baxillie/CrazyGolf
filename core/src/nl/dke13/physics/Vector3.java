package nl.dke13.physics;

/**
 * Created by Daniel on 15.04.2016.
 */
public class Vector3 {

    private double x,y,z;

    public Vector3(double x, double y,double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString(){
        String value ;
        value = "{" + String.valueOf(x)+ ", " + String.valueOf(y)+ ", " + String.valueOf(z)+ "}";
        return value;
    }

    public void vectorAdd(double x, double y, double z){
        this.x += x;
        this.y += y;
        this.z += z;


    }

    public static void main(String[] args) {
        Vector3 nV = new Vector3(1,2,3);
        nV.vectorAdd(2,3,4);
        System.out.println();
        System.out.println(nV);
        int x = 5;
        int y = 2;
        System.out.println(x + " " + y);
        x =+ y;
        System.out.println(x  + " " + y);

    }
}
