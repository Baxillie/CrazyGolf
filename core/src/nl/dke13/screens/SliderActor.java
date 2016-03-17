package nl.dke13.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * Created by Ajki on 17/03/2016.
 */
public class SliderActor extends Actor {

    Sprite sprite;

    public SliderActor() {
        sprite = new Sprite();
        sprite.setTexture(new Texture(Gdx.files.internal("core/assets/slider.png")));

        setWidth(sprite.getWidth());
        setHeight(sprite.getHeight());
        setBounds(0, 0, getWidth(), getHeight());
        setTouchable(Touchable.enabled);
        setX(100);
        setY(0);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        //Color color = getColor();
        //batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(sprite, 100, 100);

    }

}
