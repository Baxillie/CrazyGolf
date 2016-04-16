package nl.dke13.physics;

/**
 * Created by baxie on 15-4-16.
 */
public class Vector3
{
    public static void main(String[] args)
    {
        Vector3 vector = new Vector3(1,2,3);
        System.out.println(vector);
    }

    private double x,y,z;

    public Vector3(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    @Override
    public String toString()
    {
        return String.format("[%f, %f, %f]", x, y, z);
    }

    public Vector3 copy()
    {
        return new Vector3(x,y,z);
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }

    public void add(Vector3 v)
    {

    }

    public void add(double x, double y, double z)
    {

    }

    //just call add() with minus x, y, z
    public void substract(Vector3 v)
    {

    }

    public void substract(double x, double y, double z)
    {

    }

    public void scale(int scaler)
    {

    }

    public void scale(double scaler)
    {

    }

    public int dotProduct(Vector3 v)
    {
        return 0;
    }


    public Vector3 crossProduct(Vector3 v)
    {
        return new Vector3();
    }


    public void rotate(int degrees)
    {

    }

    public void normalize()
    {

    }

}
