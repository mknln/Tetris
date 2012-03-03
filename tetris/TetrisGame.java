/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris;

import java.util.*;
import java.awt.event.*;
import java.applet.*;

/**
 *
 * @author mpn
 */
public class TetrisGame {
    private TetrisPiece currentPiece;
    private TetrisPiece nextPiece;
    private TetrisGrid playGrid;
    private DisplayFrame displayFrame;
    private int score;
    private int r, c;
    
    private final AudioClip donkClip = Applet.newAudioClip(getClass().getResource("/sounds/donk.wav"));
    private final AudioClip zapClip = Applet.newAudioClip(getClass().getResource("/sounds/zap.wav"));
    private final AudioClip rotateClip = Applet.newAudioClip(getClass().getResource("/sounds/rotate.wav"));
    
    public class GameOverException extends RuntimeException {}
    
    public TetrisGame() {
        //this.displayFrame = displayFrame;
        displayFrame = null;
        playGrid = new TetrisGrid();
        currentPiece = this.getRandomPiece();
        nextPiece = this.getRandomPiece();
        r = 0;
        c = 6;
    }
    
    public void setFrame(DisplayFrame displayFrameIn) {
        displayFrame = displayFrameIn;
    }
    
    public void advanceState() throws GameOverException {
        movePieceDown();
    }

    private void movePieceDown() throws GameOverException {
        if (!playGrid.isPieceStopped(currentPiece, r, c)) {
             r++;
        } else {
            // piece is stopped. lock piece into grid, zap out
            // full lines, update score, move on to next piece.
            
            // we can't lock the piece.
            // either something's broken or, more likely,
            // the piece goes over the top of the grid --
            // which is our end game condition.
            if (!playGrid.lockPiece(currentPiece, r, c))
                throw new GameOverException();
            
            donkClip.play();
            resetPiece();
            flashGrid();
        }
        
        //displayFrame.updateDisplay();
    }
    
    private void resetPiece() {
            currentPiece = nextPiece;
            nextPiece = getRandomPiece();
            r = 0;
            c = 6;    
    }
    
    private void flashGrid() {
            int rowsCleared = playGrid.reduce();
            if (rowsCleared > 0)
                zapClip.play();
            
            if (rowsCleared == 0)
                score += 10;
            else if (rowsCleared == 1)
                score += 100;
            else if (rowsCleared == 2)
                score += 400;
            else if (rowsCleared == 3)
                score += 800;
            else if (rowsCleared == 4)
                score += 1200;
            else
                score += 1200;
            //System.out.println("Score: " + score);
    }
    
    // handle keystrokes detected in parent window
    public void keyPressed(int keycode) {
        if (keycode == KeyEvent.VK_UP) {
            if (playGrid.canRotatePiece(currentPiece, r, c))
                currentPiece.rotate();
                rotateClip.play();
        } else if (keycode == KeyEvent.VK_DOWN) {
            if (playGrid.canMovePieceDown(currentPiece, r, c))
                movePieceDown();
        } else if (keycode == KeyEvent.VK_RIGHT) {
            if (playGrid.canMovePieceRight(currentPiece, r, c))
                c++;
        } else if (keycode == KeyEvent.VK_LEFT) {
            if (playGrid.canMovePieceLeft(currentPiece, r, c))
                c--;
        } else if (keycode == KeyEvent.VK_SPACE) {
            if (playGrid.canDropPiece(currentPiece, r, c)) {
                playGrid.dropPiece(currentPiece, r, c);
                donkClip.play();
                resetPiece();
                flashGrid();
            }
        }
        
        displayFrame.updateDisplay();
    }
    
    public int getRow() {
        return r;
    }
    
    public int getCol() {
        return c;
    }
    
    public TetrisPiece getCurrentPiece() {
        return currentPiece;
    }
    
    public TetrisPiece getNextPiece() {
        return nextPiece;
    }
    
    public TetrisGrid getGrid() {
        return playGrid;
    }
    
    public int getScore() {
        return score;
    }
    
    private TetrisPiece getRandomPiece()
    {
        Random rand = new Random();
        TetrisPiece.Piece randomPiece = TetrisPiece.Piece.values()[rand.nextInt(TetrisPiece.Piece.values().length)];
        return new TetrisPiece(randomPiece);
    }
    
    public boolean isGameOver() {
        return playGrid.isFull();
    }
}
