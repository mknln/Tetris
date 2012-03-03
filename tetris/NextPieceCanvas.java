/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris;
import java.awt.*;

/**
 *
 * @author mpn
 */
public class NextPieceCanvas extends Canvas {
    private TetrisPiece nextPiece;
    
    public NextPieceCanvas() {
        this.setSize(4 * 25, 4 * 25);
        //this.setBackground(Color.white);
        nextPiece = null;
    }
    
    public void setNextPiece(TetrisPiece P) {
        nextPiece = P;
    }
    
    public void paint(Graphics g) {
        g.setColor(Color.BLUE);
        int centerX = 2;
        int centerY = 1;
        
        Point[] blocks = nextPiece.getBlocks();
        for (Point block : blocks)
            g.fillRoundRect(25 * (centerX + block.x), 25 * (centerY + block.y), 23, 23, 2, 2);
    }
}
