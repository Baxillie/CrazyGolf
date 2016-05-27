package nl.dke12.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * Created by Daniel on 23.05.2016.
 */
public class Button
{
    private Sprite skin;


        public Button(Texture texture, float x, float y, float width, float height) {
            skin = new Sprite(texture);
            skin.setPosition(x, y);
            skin.setSize(width, height);
        }

        public void update (SpriteBatch batch, float input_x, float input_y) {
            checkIfClicked(input_x, input_y);
            skin.draw(batch); // draw the button
        }

        public void checkIfClicked(float ix, float iy) {
//            System.out.printf("button xmin: %f xmax: %f ymin: %f ymax: %f ix: %f iy:%f %n",
//                    skin.getX(), skin.getX() + skin.getWidth(), skin.getY(), skin.getY() + skin.getHeight(),
//                    ix, iy);
            if (ix > skin.getX() && ix < skin.getX() + skin.getWidth()) {
                if (iy > skin.getY() && iy < skin.getY() + skin.getHeight()) {
                    if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                        executeClick();
                    }
                }
            }
        }

    protected void executeClick() {
        System.out.println("Button clicked !");
    }


}
