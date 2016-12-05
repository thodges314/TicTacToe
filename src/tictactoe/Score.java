/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

/**
 *
 * @author thodges
 */
public class Score implements Comparable<Score> {

    private byte score;
    private byte[] move = new byte[2];

    public void setScore(byte score) {
        this.score = score;
    }

    public byte getScore() {
        return score;
    }

    public void setMove(byte[] move) {
        this.move = move;
    }

    public byte[] getMove() {
        return move;
    }

    public int compareTo(Score otherScore) {
        return (this.score - otherScore.getScore());
    }

}
