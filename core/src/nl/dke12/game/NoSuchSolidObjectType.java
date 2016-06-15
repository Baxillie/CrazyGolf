package nl.dke12.game;

/**
 * Created by nik on 6/15/16.
 */
public class NoSuchSolidObjectType extends Exception{

    public NoSuchSolidObjectType()
    {
        super();
    }

    public NoSuchSolidObjectType(String s)
    {
        super(s);
    }
}
