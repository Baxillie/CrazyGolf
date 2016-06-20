package nl.dke12.bot;

import nl.dke12.bot.SimpleAI;
import nl.dke12.controller.GameController;
import nl.dke12.controller.InputProcessor;
import nl.dke12.game.GameWorld;

import java.util.Random;

/**
 * Created by Ajki on 20/06/2016.
 */
public class PathFindingBot extends SimpleAI
{

    private GameController gameController;
    private Random rng;

    public PathFindingBot(GameWorld gameWorld, InputProcessor processor)
    {
        super(gameWorld, processor);
        rng = new Random(System.currentTimeMillis());
        this.gameController = gameWorld.getGameController();
    }

    @Override
    protected void calculateBestMove()
    {
        super.calculateBestMove();
    }
}
