package nl.dke12.controller;

import com.badlogic.gdx.Game;
import nl.dke12.bot.PathFindingBot;
import nl.dke12.bot.RandomAI;
import nl.dke12.bot.SimpleAI;
import nl.dke12.game.GameWorld;
import nl.dke12.screens.*;

/**
 * Switches between different screens
 * Has logic for screen switching and loading
 */
public class StateController extends Game
{
    //GameController
    //Display
    //needs to know every screen
    //methods that create the screens

    public void displayMenuScreen()
    {
        setScreen(new MenuScreen(this));
    }

    public void displayEditorScreen()
    {
        setScreen(new EditorScreen(this));
    }

    public void displayHoleSelectionScreen()
    {
        setScreen(new HoleSelectionScreen(this));
    }

    public void displayGameOverScreen(int player1Turn, int player2Turn, boolean multiplayer)
    {
        if(multiplayer)
            setScreen(new GameOverScreen(player1Turn, player2Turn, this));
        else
            setScreen(new GameOverScreen(player1Turn, this));
    }

    public void displayInstructions()
    {
        setScreen(new Instructions(this));
    }

    public void displayGameDisplay(boolean multiplayer)
    {
        GameWorld gameWorld = new GameWorld(multiplayer, true);
        GameDisplay gameDisplay = new GameDisplay(multiplayer, gameWorld,this);
        gameWorld.setDisplay(gameDisplay);

        setScreen(gameDisplay);
    }

    public void displayAI()
    {
        GameWorld gameWorld = new GameWorld(false, false);
        GameDisplay gameDisplay = new GameDisplay(false, gameWorld,this);
        gameWorld.setDisplay(gameDisplay);

        //SimpleAI ai = new SimpleAI(gameWorld, gameWorld.getGameController().getInputProcessor());
        //RandomAI ai = new RandomAI(gameWorld, gameWorld.getGameController().getInputProcessor());
        PathFindingBot ai = new PathFindingBot(gameWorld, gameWorld.getGameController().getInputProcessor());

        new Thread(ai).start();

        setScreen(gameDisplay);
    }

    @Override
    public void create() {
        super.setScreen(new MenuScreen(this));
    }
}
