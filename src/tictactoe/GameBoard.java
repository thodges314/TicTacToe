/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.util.*;

/**
 *
 * @author thodges
 */
public class GameBoard {

    private byte[][] board;
    private byte size;
    final static byte X = 1;
    final static byte O = -1;
    final static byte EMPTY = 0;

    public GameBoard(byte size) {
        this.size = size;
        board = new byte[size][size];
        for (byte[] row : board) {
            for (byte square : row) {
                square = EMPTY;
            }
        }
    }

    private boolean place(byte i, byte j, byte val) {
        if (board[i][j] == EMPTY) {
            board[i][j] = val;
            return true;
        }
        return false;
    }

    public boolean placeX(byte i, byte j) {
        return place(i, j, X);
    }

    public boolean placeO(byte i, byte j) {
        return place(i, j, O);
    }

    public byte valAt(byte i, byte j) {
        return board[i][j];
    }

    public char markAt(int i, int j) {
        switch (board[i][j]) {
            case X:
                return 'X';
            case O:
                return 'O';
            default:
                return ' ';
        }
    }

    public byte checkWin() {
        byte winner = 0;
        byte sum;

        //check rows
        for (byte i = 0; i < size; i++) {
            sum = 0;
            for (byte j = 0; j < size; j++) {
                sum += board[i][j];
            }
            if (Math.abs(sum) == size) {
                return (byte) (sum / size);
            }
        }
        for (byte j = 0; j < size; j++) {
            sum = 0;
            for (byte i = 0; i < size; i++) {
                sum += board[i][j];
            }
            if (Math.abs(sum) == size) {
                return (byte) (sum / size);
            }
        }
        //check columns
        sum = 0;
        for (byte i = 0; i < size; i++) {
            sum += board[i][i];
        }
        if (Math.abs(sum) == size) {
            return (byte) (sum / size);
        }
        //check backwards diagonal
        sum = 0;
        for (byte i = 0; i < size; i++) {
            sum += board[i][size - 1 - i];
        }
        //check forwards diagonal
        if (Math.abs(sum) == size) {
            return (byte) (sum / size);
        }
        return 0;
    }

    private byte countPossibleMoves() {
        byte count = 0;
        for (byte i = 0; i < size; i++) {
            for (byte j = 0; j < size; j++) {
                if (board[i][j] == EMPTY) {
                    count++;
                }
            }
        }
        return count;
    }

    public byte[][] possibleMoves() {
        byte[][] movesArray = new byte[countPossibleMoves()][2];
        byte count = 0;
        for (byte i = 0; i < size; i++) {
            for (byte j = 0; j < size; j++) {
                if (board[i][j] == EMPTY) {
                    movesArray[count][0] = i;
                    movesArray[count++][1] = j;
                }
            }
        }
        return movesArray;
    }
}
