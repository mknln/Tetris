/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author mpn
 */
public class HighScoreFrame extends JFrame {
    private HighScoreTable hsTable;
    
    public HighScoreFrame(HighScoreTable hsTableIn) {
        super("Winners");
        
        hsTable = hsTableIn;
        
        ArrayList<HighScoreEntry> highScores = hsTable.getHighScores();
        
        JPanel scoresPanel = new JPanel();
        scoresPanel.setLayout(new GridLayout(highScores.size(), 2, 3, 5));
        int count = 1;
        for (HighScoreEntry entry : highScores) {
            System.out.println(entry.getName() + ": " + entry.getScore());
            scoresPanel.add(new JLabel(count+". " + entry.getName()));
            scoresPanel.add(new JLabel(""+entry.getScore()));
            count++;
        }
        scoresPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        JButton closeButton = new JButton("close");
        closeButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
        });
        
        this.setLayout(new BorderLayout());
        JLabel headerLabel = new JLabel("High Scores", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 35));
        this.getContentPane().add(headerLabel, BorderLayout.NORTH);
        this.getContentPane().add(scoresPanel, BorderLayout.CENTER);
        this.getContentPane().add(closeButton, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
        //this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
