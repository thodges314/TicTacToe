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
        //loop for autoplay
        byte boardsize = 5;
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
            
            reportTime(netTime);
        }

    }
    
    public static void autoplay(byte boardSize, byte turns){
    
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
