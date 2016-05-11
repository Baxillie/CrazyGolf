package nl.dke13.physics;

public class ObjectBox
{
    private float x, y, z, width, height, depth;
    private float xMin, xMax, yMin, yMax, zMin, zMax;
    private float oldxMax, oldyMax, oldzMax, oldxMin, oldyMin, oldzMin;
    private boolean bumpX, bumpY, bumpZ;

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

    public boolean overlapsOld(ObjectBox box)
    {
        //todo: fix Z next period
        if ((xMin > box.getxMin() && xMin < box.getxMax()) || (xMax > box.getxMin() && xMax < box.getxMax()))
        {
            if ((yMin > box.getyMin() && yMin < box.getyMax()) || (yMax > box.getyMin() && yMax < box.getyMax()))
            {
               // if ((zMin > box.getzMin() && zMin < box.getzMax()) || (zMax > box.getzMin() && zMax < box.getzMax()))
                {
                    if((oldxMin > box.getxMin() && oldxMin < box.getxMax()) || (oldxMax > box.getxMin() && oldxMax < box.getxMax()))
                    {
                        bumpY = true;
                        bumpX = false;
                    }
                    if((oldyMin > box.getyMin() && oldyMin < box.getyMax()) || (oldyMax > box.getyMin() && oldyMax < box.getyMax()))
                    {
                        bumpX = true;
                        bumpY = false;
                    }
                    return true;
                }
            }
        }
       return false;
    }

    public boolean overlaps(ObjectBox box)
    {
        if((xMin <= box.getxMax() && xMax >= box.getxMin()) && (yMin <= box.getyMax() && yMax >= box.getyMin()) &&
                (zMin <= box.getzMax() && zMax >= box.getzMin()))
        {
            System.out.println("collided");
            bumpX = false;
            bumpY = false;
            bumpZ = false;
            // if movement is in the x axis, to get a 45 degree bump the y axis has to change
            if((oldxMin > box.getxMin() && oldxMin < box.getxMax()) || (oldxMax > box.getxMin() && oldxMax < box.getxMax()))
            {
                bumpY = true;
            }
            if((oldyMin > box.getyMin() && oldyMin < box.getyMax()) || (oldyMax > box.getyMin() && oldyMax < box.getyMax()))
            {
                bumpX = true;
            }
            if((oldzMin > box.getzMin() && oldzMin < box.getzMax()) || (oldzMax > box.getzMin() && oldzMax < box.getzMax()))
            {
                bumpZ = true;
                System.out.println("SET BUMP Z TO TRUE!!!");
            }
            return true;
        }
        return false;
    }

    public void incrementXYZ(float x, float y, float z)
    {
        oldxMin = this.x - width/2;
        oldxMax = this.x + width/2;

        oldyMin = this.y - height/2;
        oldyMax = this.y + height/2;

        oldzMin = this.z - depth/2;
        oldzMax = this.z + depth/2;

        this.x = x + this.x;
        this.y = y + this.y;
        this.z = z + this.z;

        xMin = this.x - width/2;
        xMax = this.x + width/2;

        yMin = this.y - height/2;
        yMax = this.y + height/2;

        zMin = this.z - depth/2;
        zMax = this.z + depth/2;
    }

    public String toString()
    {
        return String.format("x= %f  y=%f  z=%f\nw=%f  h=%f  d=%f\n" +
                "xmin: %f xmax: %f\n" +
                "ymin: %f ymax: %f\n" +
                "zmin: %f zmax: %f\n",
                x,y,z,width,height,depth, xMin, xMax, yMin, yMax, zMin, zMax);
    }

    public float getxMin()
    {
        return xMin;
    }

    public float getxMax()
    {
        return xMax;
    }

    public float getyMin()
    {
        return yMin;
    }

    public float getyMax()
    {
        return yMax;
    }

    public float getzMin()
    {
        return zMin;
    }

    public float getzMax()
    {
        return zMax;
    }

    public boolean isBumpX()
    {
        return bumpX;
    }

    public boolean isBumpY()
    {
        return bumpY;
    }

    public boolean isBumpZ()
    {
        return bumpZ;
    }

    public String debugString()
    {
        return String.format(
                "bumpx: %b%nbumpx: %b%nbumpz: %b%n", bumpX, bumpY, bumpZ);
    }
}
