package tictactoe;

import java.util.Scanner;

public class TicTacToe {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- J21 M2-MAX $5x5$ FULL DEPTH ---");
        System.out.print("Size: ");
        int size = scanner.nextInt();
        System.out.print("Target: ");
        int target = scanner.nextInt();

        GameBoard board = new GameBoard(size, target);
        boolean xTurn = true;

        while (true) {
            var moves = board.getAllMoves();
            if (moves.isEmpty()) {
                System.out.println("Draw!");
                break;
            }

            long start = System.nanoTime();
            MoveScore best = MiniMaxAlphaBeta.getBestMoveParallel(board, xTurn);
            long end = System.nanoTime();

            board = board.place(best.row(), best.col(), xTurn ? GameBoard.X : GameBoard.O);
            display(board);
            System.out.printf("Move [%d,%d] | Score: %d | Time: %.2f ms%n%n",
                    best.row(), best.col(), best.score(), (end - start) / 1_000_000.0);

            if (board.checkWinAtLastMove()) {
                System.out.println("PLAYER " + (xTurn ? "X" : "O") + " WINS!");
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
                byte v = grid[r * size + c];
                System.out.print(v == GameBoard.X ? "X " : v == GameBoard.O ? "O " : ". ");
            }
            System.out.println();
        }
    }
}