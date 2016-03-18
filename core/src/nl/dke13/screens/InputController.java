package nl.dke13.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import nl.dke13.physics.Ball;

public class InputController implements InputProcessor {

    private UserInterface ui;
    private Ball ball;
    private Ball multiplayerBall;
    private boolean firstClickDone, secondClickDown, secondClickUp;
    private int xDown, yDown;
    private int xUp, yUp;
    private int sliderStrength;
    private boolean multiplayer;

    private boolean player1Turn;
    private int player1Turns;
    private int player2Turns;


    public InputController(Ball ball, UserInterface ui)
    {
        this.ball = ball;
        this.ui = ui;;
        multiplayer = false;
        player1Turn = false;
        player2Turns = 0;
    }

    public InputController(Ball ball1, Ball ball2, UserInterface ui)
    {
        multiplayer = true;
        ball = ball1;
        multiplayerBall = ball2;
        this.ui = ui;
        player1Turn = true;
        player2Turns = 0;
        player1Turns = 0;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.printf("screenX: %d screenY:%d pointer:%d button:%d clickdown: %b\n", screenX, screenY, pointer, button, firstClickDone);
        if(firstClickDone && !secondClickDown)
        {
            secondClickDown = true;
            ui.startArrowMovement();
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.printf("screenX: %d screenY:%d pointer:%d button:%d clickup: %b\n", screenX, screenY, pointer, button, secondClickUp);
        if(!firstClickDone && ball.getVelocity().isZero() && button == 0)
        {
            System.out.println("user clicked down");
            firstClickDone = true;
            xDown = screenX;
            yDown = screenY;
            return false;
        }
        else if(secondClickDown && button == 0)
        {
            System.out.println("user clicked up");
            secondClickUp = true;
            xUp = screenX;
            yUp = screenY;
            sliderStrength = ui.getArrowStrength();
            return true;
        }
        return false;
    }

    private void resetClick()
    {
        firstClickDone = false;
        secondClickUp = false;
        secondClickDown = false;
    }

    public Vector2 distanceClicked() throws IllegalStateException{
        if (hasClicked()) {
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
            float scalar = (sliderStrength / 100.0f) * 5;
            Vector3 vel = new Vector3(v2.x * scalar, v2.y * scalar, 0);
            if(!multiplayer) {
                ball.setVelocity(vel);
                player1Turns++;
            }
            else if(ball.getBallDone() || multiplayerBall.getBallDone())
            {
                if(!ball.getBallDone())
                {
                    ball.setVelocity(vel);
                    player1Turns++;
                }
                else
                {
                    multiplayerBall.setVelocity(vel);
                    player2Turns++;
                }
            }
            else if(player1Turn)
            {
                ball.setVelocity(vel);
                player1Turns++;
                player1Turn = false;
            }
            else
            {
                multiplayerBall.setVelocity(vel);
                player2Turns++;
                player1Turn = true;
            }
            resetClick();
        }
    }

    public boolean isGameOver()
    {
        if (multiplayer)
            return ball.getBallDone() && multiplayerBall.getBallDone();
        else
            return ball.getBallDone();
    }

    public int getPlayer1Turn()
    {
        return player1Turns;
    }

    public int getPlayer2Turns()
    {
        return player2Turns;
    }

    public boolean isMultiplayer()
    {
        return multiplayer;
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

    private boolean hasClicked()
    {
        return firstClickDone && secondClickDown && secondClickUp;
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
}
