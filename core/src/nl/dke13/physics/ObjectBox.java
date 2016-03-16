package nl.dke13.physics;


public class ObjectBox
{
    private float x, y, z, width, height, depth;
    private float xMin, xMax, yMin, yMax, zMin, zMax;
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

    public boolean overlaps(ObjectBox box)
    {
        if ((xMin > box.getxMin() && xMin < box.getxMax()) || (xMax > box.getxMin() && xMax < box.getxMax()))
        {
            if ((yMin > box.getyMin() && yMin < box.getyMax()) || (yMax > box.getyMin() && yMax < box.getyMax()))
            {
                if ((zMin > box.getzMin() && zMin < box.getzMax()) || (zMax > box.getzMin() && zMax < box.getzMax()))
                {
                    return true;
                }
            }
        }
       return false;
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

    public boolean isBumpX() {
        return bumpX;
    }

    public boolean isBumpY() {
        return bumpY;
    }

    public boolean isBumpZ() {
        return bumpZ;
    }
}
