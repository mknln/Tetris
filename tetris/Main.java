/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

// Testing merge features
package tetris;
import java.net.URL;
import java.applet.*;

/**
 *
 * @author mpn
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TetrisGame game = new TetrisGame();
        DisplayFrame frame = new DisplayFrame(game);
    }

}
