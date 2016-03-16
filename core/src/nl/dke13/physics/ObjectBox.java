package nl.dke13.physics;

import java.io.PrintWriter;

public class ObjectBox
{
    private float x, y, z, width, height, depth;
    private float xMin, xMax, yMin, yMax, zMin, zMax;

    public ObjectBox (float x, float y, float z, float width, float height, float depth)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.depth = depth;

        xMin = x - width/2;
        xMax = x + width/2;

        yMin = y - height/2;
        yMax = y + height/2;

        zMin = z - depth/2;
        zMax = z + depth/2;
    }

    public boolean overlaps(ObjectBox box, PrintWriter pw)
    {
        if ((xMin > box.getxMin() && xMin < box.getxMax()) || (xMax > box.getxMin() && xMax < box.getxMax()))
        {
            pw.printf("Xmin= %f > boxxmin = %f %b %n",xMin , box.getxMin () , (xMin > box.getxMin()));
            pw.printf("Xmin= %f < boxxmax = %f %b %n",xMin , box.getxMax () , (xMin < box.getxMax()));
            pw.printf("Xmax= %f > boxxmin = %f %b %n",xMax , box.getxMin () , (xMax > box.getxMin()));
            pw.printf("Xmax= %f < boxxmax = %f %b %n",xMax , box.getxMax () , (xMax < box.getxMax()));
            if ((yMin > box.getyMin() && yMin < box.getyMax()) || (yMax > box.getyMin() && yMax < box.getyMax()))
            {
                pw.printf("ymin= %f > boxymin = %f %b %n",yMin , box.getyMin () , (yMin > box.getyMin()));
                pw.printf("ymin= %f < boxymax = %f %b %n",yMin , box.getyMax () , (yMin < box.getyMax()));
                pw.printf("ymax= %f > boxymin = %f %b %n",yMax , box.getyMin () , (yMax > box.getyMin()));
                pw.printf("ymax= %f < boxymax = %f %b %n",yMax , box.getyMax () , (yMax < box.getyMax()));
                if ((zMin > box.getzMin() && zMin < box.getzMax()) || (zMax > box.getzMin() && zMax < box.getzMax()))
                {
                    pw.printf("zmin= %f > boxzmin = %f %b %n",zMin , box.getzMin () , (zMin > box.getzMin()));
                    pw.printf("zmin= %f < boxzmax = %f %b %n",zMin , box.getzMax () , (zMin < box.getzMax()));
                    pw.printf("zmax= %f > boxzmin = %f %b %n",zMax , box.getzMin () , (zMax > box.getzMin()));
                    pw.printf("zmax= %f < boxzmax = %f %b %n",zMax , box.getzMax () , (zMax < box.getzMax()));
                    return true;
                }
            }
        }
        pw.println("returning false");
       return false;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public float getDepth()
    {
        return depth;
    }

    public void setXYZ(float x, float y, float z)
    {
        this.x = x + this.x;
        this.y = y + this.y;
        this.z = z + this.z;

        xMin = this.x - width/2;
        xMax = this.x + width/2;

        yMin = this.y - height/2;
        yMax = this.y + height/2;

        zMin = this.z - depth/2;
        zMax = this.z + depth/2;

        System.out.printf("The location of the box : %f %f %f %n", this.x, this.y, this.z);
    }
    public String toString()
    {
        return String.format("x= %f  y=%f  z=%f\nw=%f  h=%f  d=%f\n" +
                "xmin: %f xmax: %f\n" +
                "ymin: %f ymax: %f\n" +
                "zmin: %f zmax: %f\n",
                x,y,z,width,height,depth, xMin, xMax, yMin, yMax, zMin, zMax);
    }

    public float getxMin() {
        return xMin;
    }

    public float getxMax() {
        return xMax;
    }

    public float getyMin() {
        return yMin;
    }

    public float getyMax() {
        return yMax;
    }

    public float getzMin() {
        return zMin;
    }

    public float getzMax() {
        return zMax;
    }
}
