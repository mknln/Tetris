/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris;

import java.util.*;
import java.awt.Point;

/**
 * 12x20 grid representation
 * @author mpn
 */
public class TetrisGrid {
    private ArrayList<StringBuffer> grid;
    
    public class PieceOverflowException extends RuntimeException {}
    
    public TetrisGrid() {
        grid = new ArrayList<StringBuffer>();
        // add 20 empty strings of length 12
        // i.e. make a 20x12 grid
        for (int r = 0; r < 20; r++)
            grid.add(new StringBuffer("            "));
            
    }
    
    public int reduce() {
        int count = 0;
        ArrayList<StringBuffer> newGrid = new ArrayList<StringBuffer>();
        for (StringBuffer line : grid) {
            if (line.toString().equals("************"))
                count++;
            else
                newGrid.add(line);
        }
        // for each deleted full-line, we have to add a new
        // blank-line to the top of the grid.
        for (int i = 0; i < count; i++)
            newGrid.add(0, new StringBuffer("            "));
        grid = newGrid;
        
        return count;
    }
    
    public void setBlockAt(int r, int c, boolean on) {
        //System.out.println("setBlockAt " + r + ", " + c);
        grid.get(r).setCharAt(c, (on ? '*' : ' '));
    }
    
    public boolean hasBlockAt(int r, int c) {
        return (grid.get(r).charAt(c) == '*');
    }
    
    public boolean inBounds(int r, int c) {
        return (r >= 0 && r < 20 && c >= 0 && c < 12);
    }
    
    // can we move a piece along direction vector (dr,dc)?
    //     * is it in bounds?
    //     * does it avoid collisions with other blocks?
    //
    // (dr,dc) should only be one of down=(1,0), right=(0,1), left=(0,-1)
    public boolean canMovePiece(TetrisPiece P, int r, int c, int dr, int dc) {
        Point[] blocks = P.getBlocks();
        for (Point block : blocks)
            if (!inBounds(r + dr + block.y, c + dc + block.x)
                || hasBlockAt(r + dr + block.y, c + dc + block.x))
                return false;
        
        return true;
    }
    
    public boolean canLockPiece(TetrisPiece P, int r, int c) {
        return canMovePiece(P, r, c, 0, 0);
    }
    
    public boolean canMovePieceDown(TetrisPiece P, int r, int c) {
        return canMovePiece(P, r, c, 1, 0);
    }
    
    public boolean canMovePieceLeft(TetrisPiece P, int r, int c) {
        return canMovePiece(P, r, c, 0, -1);
    }
    
    public boolean canMovePieceRight(TetrisPiece P, int r, int c) {
        return canMovePiece(P, r, c, 0, 1);
    }
    
    public boolean canDropPiece(TetrisPiece P, int r, int c) {
        return canMovePieceDown(P, r, c);
    }
    
    // check if a piece with pivot point at (r,c) can be rotated without collision
    public boolean canRotatePiece(TetrisPiece P, int r, int c) {
        // rotate piece
        P.rotateRight();
        // get rotated piece's point-set
        Point[] blocks = P.getBlocks();
        // make sure to rotate back to original state
        P.rotateLeft();
        // check if any blocks will overlap with those already there
        for (Point block : blocks)
            if (!inBounds(r + block.y, c + block.x)
                || hasBlockAt(r + block.y, c + block.x))
                return false;
        return true;
    }
    
    public boolean isPieceStopped(TetrisPiece P, int r, int c) {
        return (canMovePieceDown(P, r, c) == false);
    }
    
    public boolean lockPiece(TetrisPiece P, int r, int c) {
        if (!canLockPiece(P, r, c))
            return false;
        
        Point[] blocks = P.getBlocks();
        for (Point block : blocks)
        {
            if (!inBounds(r + block.y, c + block.x))
                return false;
            setBlockAt(r + block.y, c + block.x, true);
        }
        return true;
    }
    
    public void dropPiece(TetrisPiece P, int r, int c) {
        for (;;) {
            if (!canLockPiece(P, r, c))
                break;
            r++;
        }
        r = r - 1;
        lockPiece(P, r, c);
    }
    
    public boolean isFull() {
        return false;
    }
}
