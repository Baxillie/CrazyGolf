package nl.dke12.screens;

/**
 * Created by Tom Conneely on 12/04/2016.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.DelaunayTriangulator;

public class HeightmapConverter {

    public int mapWidth, mapHeight;
    private float[] heightMap;
    public float[][] map;
    public float[] vertices;
    public short[] indices;
    private int strength;
    private String heightmapFile;
    private float textureWidth;

    public HeightmapConverter(int mapWidth, int mapHeight, int strength, String heightmapFile)
    {
        this.heightMap = new float[(mapWidth+1) * (mapHeight+1)];
        this.map=new float[mapWidth+1][mapHeight+1];
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.vertices = new float[heightMap.length*5];
        this.indices = new short[mapWidth * mapHeight *8];
        this.strength = strength;
        this.heightmapFile = "core/assets/map.jpg";

        loadHeightmap();
        createIndices();
        createVertices();

    }

    public void loadHeightmap()
        {
            try{
                FileHandle handle = Gdx.files.internal(heightmapFile);
                Pixmap heightmapImage = new Pixmap(handle);
                textureWidth = (float)heightmapImage.getWidth();
                Color color = new Color();
                int indexToIterate = 0;
            for(int y = 0;  y < mapHeight + 1; y++)
            {
                for(int x = 0; x < mapWidth + 1; x++)
                {
                    Color.rgba8888ToColor(color, heightmapImage.getPixel(x, y));
                    heightMap[indexToIterate++] = color.r;
                    map[x][y]=color.r;
                    //System.out.println(color.r);

                }
            }
            handle = null;
            heightmapImage.dispose();
            heightmapImage = null;
            color = null;
            indexToIterate = 0;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void createVertices()
    {
        int heightPitch = mapHeight + 1;
        int widthPitch = mapWidth + 1;

        int idx = 0;
        int hIdx = -1;

        for(int z = 0; z < heightPitch; z++)
        {
            for(int x = 0; x < widthPitch; x++)
            {
                vertices[idx+0] = x;
                vertices[idx+1] = z;
                hIdx++;
                vertices[idx+2] = heightMap[hIdx] * strength;
                vertices[idx+3] = x/textureWidth;
                vertices[idx+4] = z/textureWidth;
                idx += 5;
            }
        }
    }

    public void createIndices()
    {
        int idx = 0;
        short pitch = (short)(mapWidth + 1);
        short i1 = 0;
        short i2 = 1;
        short i3 = (short)(1 + pitch);
        short i4 = pitch;

        short row = 0;

        for(int z = 0; z < mapHeight; z++)
        {
            for(int x = 0; x < mapWidth; x++)
            {
                indices[idx+0] = (short)(i1);
//                indices[idx+1] = (short)(i2);
//                indices[idx+2] = (short)(i3);
//
//                indices[idx+3] = (short)(i3);
//                indices[idx+4] = (short)(i4);
//                indices[idx+5] = (short)(i1);
//
//                idx += 6;

                indices[idx+0] = (short)(i1);
                indices[idx+1] = (short)(i2);

                indices[idx+2] = (short)(i2);
                indices[idx+3] = (short)(i3);

                indices[idx+4] = (short)(i3);
                indices[idx+5] = (short)(i1);

                indices[idx+6] = (short)(i1);
                indices[idx+7] = (short)(i4);


                idx+=8;

                i1++;
                i2++;
                i3++;
                i4++;
            }

            row += pitch;
            i1 = row;
            i2 = (short)(row + 1);
            i3 = (short)(i2 + pitch);
            i4 = pitch;
        }

    }

    public void makeVertices()
    {
        int x=0;
        int y=0;
        int index=0;
        for(int i=0;i<map[0].length;i++)
        {
            for(int j=0;j<map.length;j++)
            {
                vertices[index]=j;
                index++;
                vertices[index]=i;
                index++;
                vertices[index]=map[j][i];
                index++;
            }
        }
    }

    public void makeIndices()
    {
        int x=0;
        int y=0;
        int vertIndex=0;
        int index=0;
        while(index<(map[0].length-1)*(map.length-1))
        {
//            if((y%2)!=0)
//            {
                //link first vertex to the one next to it
                if(x+1<=map.length-1)
                {
                    indices[index]=(short)vertIndex;
                    vertIndex++;
                    index++;
                    indices[index]=(short)(vertIndex);
                    //link second vertex to the one beneath it
                    if(y+1<=map[0].length-1)
                    {
                        index++;
                        indices[index]=(short)(vertIndex);
                        index++;
                        indices[index]=(short)(vertIndex+map.length);
                        index++;
                        //link third vertex to first
                        indices[index]=(short)(vertIndex+map.length);
                        index++;
                        indices[index]=(short)(vertIndex-1);
                    }
                    x++;
                    index++;
                }
                else
                {
                    y++;
                    x=0;
                    System.out.println("next row");
                }
                /*
                //link second vertex to the one beneath it
                if(y+1<=map[0].length-1)
                {
                    indices[index++]=(short)(vertIndex);
                    indices[index++]=(short)(vertIndex+map.length);
                }
                //link third vertex to first
                if(x+1<=map.length-1&&y+1<=map[0].length-1)
                {
                    indices[index++]=(short)(vertIndex+map.length);
                    indices[index++]=(short)(vertIndex-1);
                }*/
            /*}
            else
            {
                //link first vertex to the one next to it
                if(x+1<=map.length-1)
                {
                    indices[index]=(short)vertIndex;
                    vertIndex++;
                    index++;
                    indices[index]=(short)(vertIndex);
                    //link second vertex to the one above it
                    if(y+1<=map[0].length-1)
                    {
                        index++;
                        indices[index]=(short)(vertIndex);
                        index++;
                        indices[index]=(short)(vertIndex-map.length);
                    }
                    x++;
                    index++;
                }
                else
                {
                    y++;
                    x=0;
                }
            }*/
        }
    }

    public void generateIndices()
    {
        /*DelaunayTriangulator triangulator = new DelaunayTriangulator();
        indices= new short[triangulator.computeTriangles(vertices,true).toArray().length];
        indices=triangulator.computeTriangles(vertices,true).toArray();*/


    }



    public String getHeightmapFile()
    {
        return heightmapFile;
    }

}

