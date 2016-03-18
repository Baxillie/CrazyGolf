package nl.dke13.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Ajki on 17/03/2016.
 */
public class UserInterface {

    private int ARROW_MIN_Y = 2;
    private int ARROW_MAX_Y = 100;

    private SpriteBatch batch;
    private Sprite slider;
    private Sprite arrow;

    private int arrowHeight;
    private boolean moveArrow;

    private Label playerLabel;
    private boolean player1 = true;

    public UserInterface()
    {
        this.batch = new SpriteBatch();

        createSlider();
        createArrow();
        createLabel();
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

    public void createLabel()
    {
        //playerLabel = new Label("Player 1", new Skin(Gdx.files.internal("core/assets/uiskin.json")));
    }

    public void togglePlayer()
    {
        if(player1)
        {
            playerLabel.setText("Player 2");
            playerLabel.setColor(Color.GOLDENROD);
            player1 = false;
        }
        else
        {
            playerLabel.setText("Player 1");
            playerLabel.setColor(Color.PINK);
            player1 = true;
        }
    }

    public void render()
    {
        batch.begin();
        batch.draw(slider, 10, 10);
        //playerLabel.draw(batch, 1);
        if(moveArrow)
        {
            arrowHeight += 5;
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

    public int getArrowStrength()
    {
        int strength = arrowHeight;
        arrowHeight = 0;
        moveArrow = false;
        return strength;
    }
}
