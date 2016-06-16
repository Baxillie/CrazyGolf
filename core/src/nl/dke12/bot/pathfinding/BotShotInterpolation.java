package nl.dke12.bot.pathfinding;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by Ajki on 14/06/2016.
 */
public class BotShotInterpolation
{
    //get path
    //make curve out of path's x and y
    //cubic polynomial approx
    //represent in 3D Vector
    //use secant / newton's method to find the actual vector / midpoint rule
    //simulate shots along the path
    private ArrayList<MapNode> path;
    private int[][] XYValues;
    private double[] coeffa;
    private double[] coeffb;

    public BotShotInterpolation(ArrayList<MapNode> path)
    {
        this.path = path;
        setPath(path);
    }

    public void discreteFourierTransform() throws PathNotFoundException
    {
        //fills in the coefficients a and b
        int n = XYValues.length;
        int m = (XYValues.length + 1)/2;
        coeffa = new double[n + 1];
        coeffb = new double[n + 1];

        for (int k = 0; k < n; k++)
        {
            double suma = 0;
            double sumb = 0;
            for (int i = 0; i < n; i++)
            {
                //problem with x; x = pi * i /m
                double angle = k * XYValues[i][0];
                suma += XYValues[i][1] * Math.cos(angle);
                sumb += XYValues[i][1] * Math.sin(angle);
            }
            coeffa[k] = 1/m * suma;
            coeffb[k] = 1/m * sumb;
        }
    }

    public void computeDerivative()
    {

    }

    public static void computeDft(double[] inreal, double[] inimag, double[] outreal, double[] outimag) {
        int n = inreal.length;
        for (int k = 0; k < n; k++) {  // For each output element
            double sumreal = 0;
            double sumimag = 0;
            for (int t = 0; t < n; t++) {  // For each input element
                double angle = 2 * Math.PI * t * k / n;
                sumreal +=  inreal[t] * Math.cos(angle) + inimag[t] * Math.sin(angle);
                sumimag += -inreal[t] * Math.sin(angle) + inimag[t] * Math.cos(angle);
            }
            outreal[k] = sumreal;
            outimag[k] = sumimag;
        }
    }

    public int midpointRuleArea()
    {
        try
        {
            int c = (XYValues[0][0] - XYValues[XYValues.length - 1][0]) / XYValues.length;
            int area = 0;
            for (int i = 0; i < XYValues.length; i++)
            {
                //area should be = f((a+b)/2)
                area += f(XYValues[i][1]);
            }
            return c * area;
        }
        catch(Exception e)
        {
            System.out.println("Smth went wrong with the xy values");
            return 0;
        }
    }

    private int f(int a)
    {
        int func = 0;

        //func = evaluate func at a;

        return func;
    }

    private void setXY(ArrayList<MapNode> path)
    {
        this.XYValues = new int[path.size()][2];
        for(int i = 0; i < XYValues.length; i++)
        {
            String identifier = path.get(i).getIdentifier();
            XYValues[i][0] = Character.getNumericValue(identifier.charAt(1));
            XYValues[i][1] = Character.getNumericValue(identifier.charAt(4));
        }
    }

    public void setPath(ArrayList<MapNode> path)
    {
        this.path = path;
        setXY(path);
    }

    public ArrayList<MapNode> getPath()
    {
        return this.path;
    }

}
