/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris;

import java.io.*;
import java.util.*;
import java.net.*;

/**
 *
 * @author mpn
 */
public class HighScoreTable {
    private ArrayList<HighScoreEntry> scoreTable;
    private URL scoresFile;
    
    public HighScoreTable() {
        scoreTable = null;
        readScoresFromFile();
    }
    
    private void readScoresFromFile() {
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream("highscores.ser");
            in = new ObjectInputStream(fis);
            scoreTable = (ArrayList<HighScoreEntry>)in.readObject();
            in.close();
        } catch (IOException e) {
            // file does not exist yet, so create a new empty array
            scoreTable = new ArrayList<HighScoreEntry>();
            // e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private void writeScoresToFile() {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream("highscores.ser");
            out = new ObjectOutputStream(fos);
            out.writeObject(scoreTable);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean inTop10(int score) {
        if (scoreTable.size() < 10)
            return true;
        return (score > scoreTable.get(9).getScore());
    }
    
    public void insertEntry(HighScoreEntry newEntry) {
        if (scoreTable.size() == 0) {
            // first entry, insert at front
            System.out.println("Inserted at front");
            scoreTable.add(newEntry);
        } else {
            // insert at correct place in table
            boolean added = false;
            for (int i = 0; i < scoreTable.size() && i < 10; i++) {
                HighScoreEntry entry = scoreTable.get(i);
                if (newEntry.compareTo(entry) > 0) {
                    System.out.println("Inserted at position " + i);
                    scoreTable.add(i, newEntry);
                    added = true;
                    break;
                }
            }
            // if we made it to the end w/o adding, it means it gets added to
            // the very end.
            if (!added)
                scoreTable.add(newEntry);
        }
        
        // trim array to size 10 or less
        while (scoreTable.size() > 10)
            scoreTable.remove(scoreTable.size() - 1);
        
        // now we have our correct score table
        // let's pickle it to a file
        writeScoresToFile();
    }
    
    public ArrayList<HighScoreEntry> getHighScores() {
        return scoreTable;
    }
}
