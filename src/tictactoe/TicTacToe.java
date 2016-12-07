/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.lang.*;

/**
 *
 * @author thodges
 */
public class TicTacToe {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //loop for autoplay
        byte boardsize = 3;
        System.out.println("Automatic play for board size " + boardsize);
        byte maxTurns = (byte) (boardsize * boardsize);
        byte turns = 0;
        boolean xTurn = true;
        GameBoard autoBoard = new GameBoard(boardsize);
        autoBoard.displayBoard();
        Score bestScore;
        byte imove;
        byte jmove;
        long startTime;
        long netTime;

        while (autoBoard.checkWin() == 0 && turns++ < maxTurns) {

            startTime = System.nanoTime();

            bestScore = (new MiniMaxAlphaBeta(autoBoard, xTurn).getBestScore());
            imove = bestScore.getMove()[0];
            jmove = bestScore.getMove()[1];
            System.out.println("Next move: " + imove + ", " + jmove);
            if (xTurn) {
                autoBoard = autoBoard.placeX(imove, jmove);
            } else {
                autoBoard = autoBoard.placeO(imove, jmove);
            }
            autoBoard.displayBoard();
            System.out.println("win? " + autoBoard.checkWin());
            xTurn = !xTurn;

            netTime = (System.nanoTime() - startTime);

            System.out.println("in nanoSeconds: " + netTime);

            System.out.print("This is ");
            if (netTime > (60 * 60 * 1000 * 1000 * 1000)) {
                long hours = (netTime / (60 * 60 * 1000 * 1000 * 1000));
                netTime = netTime % (60 * 60 * 1000 * 1000 * 1000);
                System.out.print(hours + " hours ");
            }
            if (netTime > (60 * 1000 * 1000 * 1000)) {
                long minutes = (netTime / (60 * 1000 * 1000 * 1000));
                netTime = netTime % (60 * 1000 * 1000 * 1000);
                System.out.print(minutes + " minutes ");
            }
            if (netTime > (1000 * 1000 * 1000)) {
                long seconds = (netTime / (1000 * 1000 * 1000));
                netTime = netTime % (1000 * 1000 * 1000);
                System.out.print(seconds + " seconds ");
            }
            if (netTime > (1000 * 1000)) {
                long milliseconds = (netTime / (1000 * 1000));
                netTime = netTime % (1000 * 1000);
                System.out.print(milliseconds + " milliseconds ");
            }
            if (netTime > (1000)) {
                long microseconds = (netTime / (1000));
                netTime = netTime % (1000);
                System.out.print(microseconds + " microseconds ");
            }
            if (netTime > (1)) {
                long nanoseconds =  (netTime);
                System.out.print(nanoseconds + " nanoseconds ");
            }
            System.out.println();
        }

    }
}
