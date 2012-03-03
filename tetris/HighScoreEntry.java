/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris;

import java.io.Serializable;

/**
 *
 * @author mpn
 */
public class HighScoreEntry implements Comparable, Serializable {
    private String name;
    private int score;
    
    public HighScoreEntry(String nameIn, int scoreIn) {
        name = nameIn;
        score = scoreIn;
    }
    
    public void setName(String nameIn) { name = nameIn; }
    public void setScore(int scoreIn) { score = scoreIn; }
    public String getName() { return name; }
    public int getScore() { return score; }
    
    public int compareTo(Object other) {
        HighScoreEntry otherScore = (HighScoreEntry)other;
        return (getScore() - otherScore.getScore());
    }
}
