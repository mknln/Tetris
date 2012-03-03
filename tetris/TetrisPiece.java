/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.awt.Point;
import java.awt.Color;
import java.util.*;

/**
 *
 * @author mpn
 */
public class TetrisPiece {
    // enum type for our 7 piece types.
    // used to index our pieces[][] array (by calling the ordinal() method)
    public enum Piece { O_PIECE, I_PIECE, S_PIECE, Z_PIECE,
                        L_PIECE, J_PIECE, T_PIECE }
    
    // list of pieces from:
    // http://www.colinfahey.com/tetris/tetris_diagram_pieces_orientations_new.jpg
    // these are the initial states of our 7 pieces, which can then be rotated
    // using the rotateRight and rotateLeft methods in this class.
    // each piece has a "pivot point" at (0,0), and three points relative to that.
    // 
    // coordinate system:
    // x gets bigger as you move right, and y gets bigger as you move down.
    private Point[][] pieces = {
        // O PIECE
        {new Point(0, 0), new Point(0, 1), new Point(-1, 0), new Point(-1, 1)},
        // I PIECE
        {new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(0, 2)},
        // S PIECE
        {new Point(0, 0), new Point(0, 1), new Point(-1, 1), new Point(1, 0)},
        // Z PIECE
        {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 0)},
        // L PIECE
        {new Point(0, 0), new Point(1, 0), new Point(-1, 0), new Point(-1, 1)},
        // J PIECE
        {new Point(0, 0), new Point(1, 0), new Point(-1, 0), new Point(1, 1)},
        // T PIECE
        {new Point(0, 0), new Point(1, 0), new Point(-1, 0), new Point(0, 1)}};
    
    private Point[] curBlockSet = new Point[4];
    private Piece pieceType;
    // private Color color;
    
    public TetrisPiece() {
        pieceType = Piece.O_PIECE;
        curBlockSet = pieces[pieceType.ordinal()];
    }

    public TetrisPiece(Piece pieceTypeIn) {
        this.pieceType = pieceTypeIn;
        curBlockSet = pieces[pieceType.ordinal()];
    }

    public Point[] getBlocks() {
        Point[] blocks = new Point[curBlockSet.length];
        for (int i = 0; i < curBlockSet.length; i++)
            blocks[i] = new Point(curBlockSet[i]);
        return blocks;
        //return curBlockSet;
    }

    public void rotate() {
        rotateRight();
    }
    
    // clockwise rotation
    public void rotateRight() {
        for (int i = 0; i < curBlockSet.length; i++) {
            curBlockSet[i] = new Point(curBlockSet[i].y, -curBlockSet[i].x);
        }
    }

    // counterclockwise rotation
    public void rotateLeft() {
        for (int i = 0; i < curBlockSet.length; i++) {
            curBlockSet[i] = new Point(-curBlockSet[i].y, curBlockSet[i].x);
        }
    }

    public String toString() {
        return "TetrisPiece";
    }
}
