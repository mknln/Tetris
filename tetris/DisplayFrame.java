/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.applet.*;
/**
 *
 * @author mpn
 */
public class DisplayFrame extends JFrame {

    private TetrisGame game;
    private GridCanvas gridCanvas;
    private javax.swing.Timer timer;
    private JPanel gridPanel;
    private JPanel scorePanel;
    private JLabel scoreLabel;
    private JPanel nextPiecePanel;
    private NextPieceCanvas nextPieceCanvas;
    private JPanel rightPanel;
    
    private boolean paused;
    
    private final AudioClip tetrisClip = Applet.newAudioClip(getClass().getResource("/sounds/tetris.mid"));
    
    public DisplayFrame(TetrisGame gameIn)
    {
        super("Tetris!");
        
        game = gameIn;
        game.setFrame(this);
        // loop theme song
        tetrisClip.loop();
        paused = false;
        
        // we detect arrow keys and send them to the game object
        this.addKeyListener (
            new KeyAdapter() {
                public void keyPressed(KeyEvent e)
                {
                    keystrokeDetect(e);
                }
        });

        this.getContentPane().setLayout(new BorderLayout());
        
        // GridCanvas subclasses java.awt.Canvas and provides
        // a surface for drawing a TetrisGrid object.
        // we repaint this canvas every 200ms or so in the game loop
        gridCanvas = new GridCanvas();
        gridCanvas.setGrid(game.getGrid());
        gridCanvas.repaint();
        gridPanel = new JPanel();
        gridPanel.add(gridCanvas);
        gridPanel.setSize(12 * 25, 20 * 25);
        gridPanel.setBackground(Color.white);
        gridPanel.setBorder(new LineBorder(Color.BLUE, 1));
        
        // The score panel
        scorePanel = new JPanel();
        scorePanel.setMaximumSize(new Dimension(150, 150));
        scorePanel.setBorder(BorderFactory.createTitledBorder("Score"));
        scoreLabel = new JLabel("0");
        scoreLabel.setForeground(Color.BLUE);
        scoreLabel.setFont(new Font("Verdana", Font.BOLD, 30));
        scorePanel.add(scoreLabel);
        
        // The panel which displays the next piece
        nextPiecePanel = new JPanel();
        nextPiecePanel.setMaximumSize(new Dimension(150, 150));
        nextPiecePanel.setBorder(BorderFactory.createTitledBorder("Next Piece"));
        nextPieceCanvas = new NextPieceCanvas();
        nextPieceCanvas.setNextPiece(game.getNextPiece());
        nextPiecePanel.add(nextPieceCanvas);
        
        // The right panel holds the score panel and next-piece panel, stacked
        // on top of each other.
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
        rightPanel.add(nextPiecePanel);
        rightPanel.add(scorePanel);
        rightPanel.add(Box.createVerticalGlue());
        
        // let's make sure none of these can steal focus from the main window
        gridPanel.setFocusable(false);
        gridCanvas.setFocusable(false);
        nextPieceCanvas.setFocusable(false);
        nextPiecePanel.setFocusable(false);
        rightPanel.setFocusable(false);
        scoreLabel.setFocusable(false);
        scorePanel.setFocusable(false);
        
        /**JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.PAGE_AXIS));
        JLabel foo = new JLabel("controls");
        foo.setFont(new Font("Serif", Font.BOLD, 12));
        controlsPanel.add(foo);
        controlsPanel.add(new JLabel("L/R/down - move piece"));
        controlsPanel.add(new JLabel("up - rotate"));
        controlsPanel.add(new JLabel("p - pause"));
        controlsPanel.add(new JLabel("n - new game"));
        controlsPanel.setMaximumSize(new Dimension(150, 200));
        rightPanel.add(controlsPanel);**/
        
        JPanel mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.LINE_AXIS));
        mainPane.add(gridPanel);
        mainPane.add(rightPanel);
        
        this.getContentPane().add(mainPane);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        // game timer. every 200ms, advance state and update display
        // (note: we type javax.swing.Timer since there's a name clash with
        //  java.util.Timer, for some damn reason.)
        timer = new javax.swing.Timer(300, new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                try {
                    game.advanceState();
                } catch (TetrisGame.GameOverException GOE) {
                    timer.stop();
                    tetrisClip.stop();
                    checkHighScore();
                    return;
                } finally {
                    updateDisplay();
                }
            }
        });
        
        timer.start();
    }
    
    public void newGame() {
        game = new TetrisGame();
        game.setFrame(this);
        gridCanvas.setGrid(game.getGrid());
        nextPieceCanvas.setNextPiece(game.getNextPiece());
        // loop theme song
        tetrisClip.loop();
        paused = false;
        timer.start();
    }
    
    // repaint all the changing components
    public void updateDisplay() {
        gridCanvas.setPiece(game.getRow(), game.getCol(), game.getCurrentPiece());
        gridCanvas.paintWorld();
        nextPieceCanvas.setNextPiece(game.getNextPiece());
        nextPieceCanvas.repaint();
        this.scoreLabel.setText("" + game.getScore());
        // this.nextPieceLabel.setText(this.game.getNextPiece());    
    }
    
    // forward keystrokes to game object
    public void keystrokeDetect(KeyEvent e) {
        int keycode = e.getKeyCode();
        //System.out.println("Keystroke detected: " + keycode);
        // arrow keys
        if (keycode == KeyEvent.VK_LEFT ||
            keycode == KeyEvent.VK_RIGHT ||
            keycode == KeyEvent.VK_UP ||
            keycode == KeyEvent.VK_DOWN ||
            keycode == KeyEvent.VK_SPACE) {
                if (!paused)
                    game.keyPressed(keycode);
        // pause (p)
        } else if (keycode == KeyEvent.VK_P) {
            if (paused) {
                timer.start();
                paused = false;
                gridCanvas.paintWorld();
            } else {
                timer.stop();
                paused = true;
                gridCanvas.paintPaused();
            }
        } else if (keycode == KeyEvent.VK_N) {
            newGame();
        }
    }
    
    public void checkHighScore() {
        HighScoreTable table = new HighScoreTable();
        int score = game.getScore();
        // score = (new Random()).nextInt(1000);
        if (!table.inTop10(score))
            return;
        String name = (String)JOptionPane.showInputDialog(this,
                                                          "New high score! Enter your name:",
                                                          "High Score",
                                                          JOptionPane.PLAIN_MESSAGE);
        HighScoreEntry newEntry = new HighScoreEntry(name, score);
        table.insertEntry(newEntry);
        new HighScoreFrame(table);
    }
    
    public void setGame(TetrisGame gameIn) {
        game = gameIn;
    }
}

