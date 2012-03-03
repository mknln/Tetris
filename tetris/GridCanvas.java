/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;

/**
 *
 * @author mpn
 */
public class GridCanvas extends Canvas {
    private TetrisGrid grid;
    private int row, col;
    private TetrisPiece curPiece;
    
    public GridCanvas() {
        super();
        this.setSize(12 * 25, 20 * 25);
        this.setBackground(Color.white);
        row = 0;
        col = 0;
        grid = null;
        curPiece = null;
    }
    
    // we can't call createBufferStrategy to set up double-buffering until
    // the component is added, so we put it here.
    public void addNotify() {
        super.addNotify();
        // set up double-buffering
        this.createBufferStrategy(2);
    }
    
    public void setGrid(TetrisGrid gridIn) {
        this.grid = gridIn;
        this.repaint();
    }
    
    public void setPiece(int rIn, int cIn, TetrisPiece pieceIn) {
        row = rIn;
        col = cIn;
        curPiece = pieceIn;
    }
    
    // we use rather than paint since we're using double-buffering.
    // double-buffering uses BufferStrategy.getDrawGraphics() to get the
    // graphics context, rather than g in paint(Graphics g).
    public void paintWorld() {
        BufferStrategy strategy = this.getBufferStrategy();
        Graphics bufferGraphics = strategy.getDrawGraphics();
        // clear graphics
        bufferGraphics.clearRect(0, 0, 12 * 25, 20 * 25);
        
        // first we paint the current, active piece
        if (curPiece != null) {
            Point[] blocks = curPiece.getBlocks();
            // before drawing the piece itself, we draw a gray
            // "highlighted" area, to make it easier for the user to see
            // what columns the current piece spans.
            bufferGraphics.setColor(Color.LIGHT_GRAY);
            int min = blocks[0].x;
            int max = blocks[0].x;
            for (Point block : blocks) {
                if (block.x < min)
                    min = block.x;
                if (block.x > max)
                    max = block.x;
            }
            bufferGraphics.fillRect(25*(col + min), 0, 25*(max - min + 1), 20 * 25);
            
            // the active piece will be red
            bufferGraphics.setColor(Color.RED);
            // draw each block of the piece
            for (Point block : blocks) {
                // our piece might go over the top of the grid.
                // this is okay -- we just don't paint those blocks.
                if (grid.inBounds(row + block.y, col + block.x)) {
                    bufferGraphics.fillRoundRect(25*(col + block.x), 25*(row + block.y), 23, 23, 2, 2);
                }
            }
        }
        
        if (grid != null) {
            // set pieces will be blue
            bufferGraphics.setColor(Color.BLUE);

            // 0,0 is the top left corner
            int x = 0, y = 0;
            // now we paint all the blocks already on the grid
            for (int r = 0; r < 20; r++) {
                for (int c = 0; c < 12; c++) {
                    x = 25*c;
                    y = 25*r;
                    if (grid.hasBlockAt(r, c)) {
                        bufferGraphics.fillRoundRect(x, y, 23, 23, 2, 2);
                    }
                }
            }
        }
        
        strategy.show();
        //g.drawImage(offscreen, 0, 0, this);
    }

    public void paintPaused() {
        BufferStrategy strategy = this.getBufferStrategy();
        Graphics bufferGraphics = strategy.getDrawGraphics();
        // clear graphics
        bufferGraphics.clearRect(0, 0, 12 * 25, 20 * 25);
        
        // write [PAUSED] in center of canvas
        bufferGraphics.setColor(Color.BLUE);
        bufferGraphics.setFont(new Font("SansSerif", Font.BOLD, 20));
        bufferGraphics.drawString("press p to unpause", 60, 240);
        
        strategy.show();
    }
    
    public static void main(String[] args)
    {
        GridCanvas canvas = new GridCanvas();
        JFrame frame = new JFrame();
        frame.setSize(12*25+100, 20*25+100 );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(canvas);
        frame.setVisible(true);
    }
}
