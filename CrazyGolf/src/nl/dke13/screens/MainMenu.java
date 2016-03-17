package nl.dke13.screens;

import com.badlogic.gdx.Game;

/**
 * Created by nik on 3/15/16.
 */
public class MainMenu extends Game {

    @Override
    public void create() {
        super.setScreen(new MenuScreen(this));
    }
}
