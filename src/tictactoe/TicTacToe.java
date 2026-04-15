package tictactoe;

import java.util.Scanner;

public class TicTacToe {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Java 21 Multithreaded Full-Depth AI ---");
        System.out.print("Enter Board Size: ");
        int size = scanner.nextInt();
        System.out.print("Enter Target to Win: ");
        int target = scanner.nextInt();

        GameBoard board = new GameBoard(size, target);
        boolean xTurn = true;

        display(board);

        while (true) {
            String player = xTurn ? "X" : "O";
            var moves = board.getAllMoves();

            if (moves.isEmpty()) {
                System.out.println("It's a Draw!");
                break;
            }

            System.out.println("Player " + player + " is calculating (Multithreaded)...");
            long startTime = System.nanoTime();

            // CALL THE PARALLEL VERSION
            MoveScore best = MiniMaxAlphaBeta.getBestMoveParallel(board, xTurn);

            long endTime = System.nanoTime();
            double ms = (endTime - startTime) / 1_000_000.0;

            board = board.place(best.row(), best.col(), xTurn ? GameBoard.X : GameBoard.O);

            display(board);
            System.out.printf("Move: [%d, %d] | Score: %d | Time: %.2f ms%n",
                    best.row(), best.col(), best.score(), ms);
            System.out.println("-------------------------------");

            if (board.checkWinAtLastMove()) {
                System.out.println("*** PLAYER " + player + " WINS! ***");
                break;
            }

            xTurn = !xTurn;
        }
        scanner.close();
    }

    private static void display(GameBoard b) {
        int size = b.size();
        byte[] grid = b.grid();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                byte val = grid[r * size + c];
                char symbol = (val == GameBoard.X) ? 'X' : (val == GameBoard.O) ? 'O' : '.';
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
    }
}