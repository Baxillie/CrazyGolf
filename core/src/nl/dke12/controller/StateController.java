package nl.dke12.controller;

import com.badlogic.gdx.Game;
import nl.dke12.desktop.ModelTest;
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
        setScreen(new GameDisplay(multiplayer));
    }

    @Override
    public void create() {
        super.setScreen(new MenuScreen(this));
    }
}
