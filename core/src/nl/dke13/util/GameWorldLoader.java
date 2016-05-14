package nl.dke13.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import nl.dke13.game.GameWorld;
import nl.dke13.screens.EditorScreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Loads games in a text file
 * creates a GameWorld based on the .txt file
 */
public class GameWorldLoader
{

    public GameWorldLoader(EditorScreen screen)
    {
        this.screen = screen;
    }
    private EditorScreen screen;
    public static GameWorld createGameWorld(String filepath)
    {
        return null;
    }
    public void fileReader(String name) {
        File file = ((Gdx.files.internal(name)).file());

        String s;
        try {
            Scanner sc = new Scanner(file);
            int xPos = 0;
            int yPos = 0;
            int zPos = 0;
            int semiColonCount = 0;
            while (sc.hasNext()) {
                s = sc.next();


                for (int i = 0; i < s.length(); i++) {
                    //System.out.println(s.charAt(i));

                    if (s.charAt(i) == '1') {
                        screen.placeTile((xPos-10)*8, (yPos-10)*8, 4, screen.model2);
                        yPos += 1;
                    }
                    if (s.charAt(i) == '2') {
                        screen.placeTile((xPos-10)*8, (yPos-10)*8, 8, screen.modelwall);
                        yPos += 1;
                    }
                    if (s.charAt(i) == '3') {
                        screen.placeTile((xPos-10)*8, (yPos-10)*8, 4, screen.mill);
                        yPos += 1;
                    }
                    if (s.charAt(i) == '0') {
                        yPos += 1;
                    }
                    if (s.charAt(i) == ';') {
                        semiColonCount++;
                        //if (semiColonCount == 1)
                        //{
                        xPos += 1;
                        yPos = 0;
                        //}
                            /*if (semiColonCount == 2)
                            {
                                xPos+=1;
                            }*/
                    }
                }
            }



            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


}
