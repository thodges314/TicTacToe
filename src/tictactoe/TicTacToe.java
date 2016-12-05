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
public class TicTacToe {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final byte SIZE = 8;
        GameBoard board = new GameBoard(SIZE);
        //fill random
        for (int c = 0; c<(SIZE*2); c++){
            byte i = (byte)(Math.random()*SIZE);
            byte j = (byte)(Math.random()*SIZE);
            if (c%2 == 0)
                System.out.println("X: " + i + ", " + j + ": " + board.placeX(i, j));
            else
                System.out.println("O: " + i + ", " + j + ": " + board.placeO(i, j));
        }
        //print marks
        for (byte i = 0; i < SIZE; i++) {
            for (byte j = 0; j < SIZE; j++) {
                System.out.print(board.markAt(i, j));
            }
            System.out.println();
        }
        //print vals
        for (byte i = 0; i < SIZE; i++) {
            for (byte j = 0; j < SIZE; j++) {
                System.out.print(board.valAt(i, j));
            }
            System.out.println();
        }
        
        //mark diagonal
        System.out.println("win: " + board.checkWin());
        for (byte i = 0; i<SIZE; i++){
            System.out.println(board.placeX(i, i));
        }
        
        //show marks
        for (byte i = 0; i < SIZE; i++) {
            for (byte j = 0; j < SIZE; j++) {
                System.out.print(board.markAt(i, j));
            }
            System.out.println();
        }
        //show vals
        for (byte i = 0; i < SIZE; i++) {
            for (byte j = 0; j < SIZE; j++) {
                System.out.print(board.valAt(i, j));
            }
            System.out.println();
        }
        System.out.println("win: " + board.checkWin());
        byte[][] possMoves = board.possibleMoves();
        byte possSize = (byte)possMoves.length;
        for(byte i = 0; i<possSize; i++){
            System.out.print("("+possMoves[i][0]+", " + possMoves[i][1]+") ");
        }

    }

}
