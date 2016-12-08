/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.util.Scanner;

/**
 *
 * @author thodges
 */
public class TicTacToe {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        byte boardSize;
        byte computerTurns;
        do{
            System.out.print("Board size? ");
            boardSize = input.nextByte();
            System.out.print("Computer Turns? ");
            computerTurns = input.nextByte();
        } while (computerTurns > boardSize * boardSize);
        autoplay(boardSize, computerTurns);


    }

    public static void autoplay(byte boardSize, byte computerTurns) {
        System.out.println("Automatic play for board size " + boardSize);
        byte autoFillTurns = (byte) (boardSize * boardSize - computerTurns);
        byte turns = 0;
        boolean xTurn = true;
        GameBoard autoBoard = new GameBoard(boardSize);
        autoBoard = fillTurns(autoBoard, xTurn, autoFillTurns);
        xTurn = (autoFillTurns % 2 == 0);
        autoBoard.displayBoard();
        Score bestScore;
        byte i;
        byte j;
        long startTime;
        long netTime;

        while (autoBoard.checkWin() == 0 && turns++ < computerTurns) {

            startTime = System.nanoTime();

            bestScore = (new MiniMaxAlphaBeta(autoBoard, xTurn).getBestScore());
            i = bestScore.getMove()[0];
            j = bestScore.getMove()[1];
            System.out.println("Next move: " + i + ", " + j);
            if (xTurn) {
                autoBoard = autoBoard.placeX(i, j);
            } else {
                autoBoard = autoBoard.placeO(i, j);
            }
            autoBoard.displayBoard();
            System.out.println("win? " + autoBoard.checkWin());
            xTurn = !xTurn;

            netTime = (System.nanoTime() - startTime);

            reportTime(netTime);
        }

    }

    public static GameBoard fillTurns(GameBoard board, boolean xTurn, byte numberFill) {
        GameBoard returnBoard = new GameBoard(board.getSize());
        returnBoard.copyBoard(board);
        for (byte i = 0; i < numberFill; i++) {
            returnBoard = fillTurn(returnBoard, xTurn);
            xTurn = !xTurn;
        }
        return returnBoard;
    }

    public static GameBoard fillTurn(GameBoard board, boolean xTurn) {
        GameBoard returnBoard = new GameBoard(board.getSize());
        returnBoard.copyBoard(board);
        boolean success = false;
        byte size = returnBoard.getSize();
        while (success == false) {
            byte i = (byte) (Math.random() * size);
            byte j = (byte) (Math.random() * size);
            if (returnBoard.valAt(i, j) == 0) {
                if (xTurn) {
                    returnBoard = returnBoard.placeX(i, j);
                } else {
                    returnBoard = returnBoard.placeO(i, j);
                }
                success = true;
            }
        }
        return returnBoard;
    }

    public static void reportTime(long time) {
        System.out.println("in nanoSeconds: " + time);

        System.out.print("This is ");
        if (time > (60L * 60 * 1000 * 1000 * 1000)) {
            long hours = (time / (60L * 60 * 1000 * 1000 * 1000));
            time = time % (60L * 60 * 1000 * 1000 * 1000);
            System.out.print(hours + " hours ");
        }
        if (time > (60L * 1000 * 1000 * 1000)) {
            long minutes = (time / (60L * 1000 * 1000 * 1000));
            time = time % (60L * 1000 * 1000 * 1000);
            System.out.print(minutes + " minutes ");
        }
        if (time > (1000 * 1000 * 1000)) {
            long seconds = (time / (1000 * 1000 * 1000));
            time = time % (1000 * 1000 * 1000);
            System.out.print(seconds + " seconds ");
        }
        if (time > (1000 * 1000)) {
            long milliseconds = (time / (1000 * 1000));
            time = time % (1000 * 1000);
            System.out.print(milliseconds + " milliseconds ");
        }
        if (time > (1000)) {
            long microseconds = (time / (1000));
            time = time % (1000);
            System.out.print(microseconds + " microseconds ");
        }
        if (time > (1)) {
            long nanoseconds = (time);
            System.out.print(nanoseconds + " nanoseconds ");
        }
        System.out.println();
    }
}
