package nl.dke13.screens;

import com.badlogic.gdx.Game;

public class MainMenu extends Game {

    @Override
    public void create()
    {
        super.setScreen(new MenuScreen(this));
    }
}
