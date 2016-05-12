package nl.dke13.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
@Deprecated
public class UserInterface {

    private final static float arrowSpeedPerSecond = 120;

    private float ARROW_MIN_Y = 2;
    private float ARROW_MAX_Y = 100;
    private SpriteBatch batch;
    private Sprite slider;
    private Sprite arrow;

    private float arrowHeight;
    private boolean moveArrow;

    //todo: change controls

    public UserInterface()
    {
        this.batch = new SpriteBatch();

        createSlider();
        createArrow();
    }

    private void createSlider()
    {
        Texture texture = new Texture(Gdx.files.internal("core/assets/slider.png"));
        slider = new Sprite(texture);
    }

    private void createArrow()
    {
        Texture texture = new Texture(Gdx.files.internal("core/assets/arrow.png"));
        arrow = new Sprite(texture);
    }


    public void render(float delta)
    {
        batch.begin();
        batch.draw(slider, 10, 10);
        if(moveArrow)
        {
            arrowHeight += (delta * arrowSpeedPerSecond);
            if(arrowHeight > ARROW_MAX_Y)
            {
                arrowHeight = ARROW_MIN_Y;
            }
            batch.draw(arrow, 22 ,ARROW_MIN_Y + arrowHeight );
        }

        batch.end();
    }

    public void startArrowMovement()
    {
        moveArrow = true;
    }

    public float getArrowStrength()
    {
        float strength = arrowHeight;
        arrowHeight = 0;
        moveArrow = false;
        return strength;
    }
}
