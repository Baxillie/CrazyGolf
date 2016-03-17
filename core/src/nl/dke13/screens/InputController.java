package nl.dke13.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import nl.dke13.physics.Ball;

/**
 * Created by Ajki on 17/03/2016.
 */
public class InputController implements InputProcessor {

    Ball ball;
    boolean clickDown, clickUp;
    int xDown, yDown;
    int xUp, yUp;

    public InputController(Ball ball)
    {
        this.ball = ball;
        clickDown = false;
        //create multipleyer
//        InputMultiplexer multiplexer = new InputMultiplexer();
//        multiplexer.addProcessor(camera);
//        camera.
//        //multiplexer.addProcessor(new InputController());
//        Gdx.input.setInputProcessor(multiplexer);
    }
    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.printf("screenX: %d screenY:%d pointer:%d button:%d clickdown: %b\n", screenX, screenY, pointer, button, clickDown);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.printf("screenX: %d screenY:%d pointer:%d button:%d clickup: %b\n", screenX, screenY, pointer, button, clickUp);
        if(!clickDown && ball.getVelocity().isZero() && button == 0)
        {
            System.out.println("user clicked down");
            clickDown = true;
            xDown = screenX;
            yDown = screenY;
            return false;
        }
        else if(clickDown && !clickUp && button == 0)
        {
            System.out.println("user clicked up");
            clickUp = true;
            xUp = screenX;
            yUp = screenY;
            return true;
        }
        return false;
    }

    private boolean hasClicked()
    {
        return clickDown && clickUp;
    }

    private void resetClick()
    {
        clickDown = false;
        clickUp = false;
    }

    public Vector2 distanceClicked() throws IllegalStateException{
        if (clickDown && clickUp) {
            System.out.printf("creating v2: xdown:%d xup:%d ydown:%d yup:%d \n", xDown, xUp, yDown, yUp);
            return new Vector2((xUp - xDown), (yDown - yUp));

        }
        throw new IllegalStateException("user hasnt dragged the mouse");
    }

    public void update()
    {
        if(hasClicked())
        {
            Vector2 v2 = distanceClicked();
            System.out.println("user clicked twice: Vector2: " + v2.toString());
            v2 = v2.nor();
            System.out.println("after normaliziton: " + v2.toString());
            ball.setVelocity(new Vector3(v2.x *1.5f, v2.y*1.5f, 0));
            clickDown = false;
            clickUp = false;
        }
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
