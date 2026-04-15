package tictactoe;

import java.util.List;
import java.util.Comparator;

public class MiniMaxAlphaBeta {
    private static final int WIN_VAL = 1_000_000;

    /**
     * Entry point: Uses Parallel Streams to use all M2 Max cores for the root
     * moves.
     */
    public static MoveScore getBestMoveParallel(GameBoard board, boolean isX) {
        List<Integer> moves = board.getAllMoves();
        if (moves.isEmpty())
            return new MoveScore(0, 0, 0);

        return moves.parallelStream()
                .map(idx -> {
                    // FIXED: GameBoard.O (Letter)
                    GameBoard nextBoard = board.place(idx / board.size(), idx % board.size(),
                            isX ? GameBoard.X : GameBoard.O);
                    int score = getScoreRecursive(nextBoard, !isX, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
                    return new MoveScore(score, idx / board.size(), idx % board.size());
                })
                .max(isX ? Comparator.comparingInt(MoveScore::score)
                        : Comparator.comparingInt(MoveScore::score).reversed())
                .orElseThrow();
    }

    /**
     * Recursive Step: Standard Minimax with Alpha-Beta Pruning.
     * Full Depth (No Limit).
     */
    private static int getScoreRecursive(GameBoard board, boolean isX, int alpha, int beta, int depth) {
        // Standard Win Check
        if (board.checkWinAtLastMove()) {
            return isX ? -WIN_VAL + depth : WIN_VAL - depth;
        }

        List<Integer> moves = board.getAllMoves();
        if (moves.isEmpty())
            return 0; // Draw

        int bestScore = isX ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int idx : moves) {
            // FIXED: GameBoard.O (Letter)
            GameBoard nextBoard = board.place(idx / board.size(), idx % board.size(), isX ? GameBoard.X : GameBoard.O);
            int eval = getScoreRecursive(nextBoard, !isX, alpha, beta, depth + 1);

            if (isX) {
                bestScore = Math.max(bestScore, eval);
                alpha = Math.max(alpha, eval);
            } else {
                bestScore = Math.min(bestScore, eval);
                beta = Math.min(beta, eval);
            }
            if (beta <= alpha)
                break; // Alpha-Beta Pruning
        }
        return bestScore;
    }
}