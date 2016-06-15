package nl.dke12.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import nl.dke12.controller.EditorController;

/**
 * Created by Daniel on 26.05.2016.
 */
public class ModelSelectButton extends Button
{
    private EditorController controller;
    private String whatToPlaceString;

    public ModelSelectButton(Texture texture, float x, float y, float width, float height, EditorController controller,
                             String model)
    {
        super(texture, x, y, width, height);
        this.controller = controller;
        whatToPlaceString = model;
    }

    @Override
    protected void executeClick()
    {
        controller.changeWhatToPlace(whatToPlaceString);
        System.out.println("What is clicked : " + whatToPlaceString);
    }



}
