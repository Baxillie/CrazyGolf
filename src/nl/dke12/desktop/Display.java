package nl.dke12.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
@Deprecated
public class Display extends Game
{

    @Override
    public void create()
    {
        super.setScreen(new Screen() {
            @Override
            public void show() {

            }

            @Override
            public void render(float delta) {

            }

            @Override
            public void resize(int width, int height) {

            }

            @Override
            public void pause() {

            }

            @Override
            public void resume() {

            }

            @Override
            public void hide() {

            }

            @Override
            public void dispose() {

            }
        });
    }
}
