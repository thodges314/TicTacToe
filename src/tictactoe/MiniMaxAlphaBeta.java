package tictactoe;

import java.util.List;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Arrays;

public class MiniMaxAlphaBeta {
    private static final int WIN_VAL = 1_000_000;

    // Transposition Table: Stores canonical board hashes and their scores
    private static final ConcurrentHashMap<BoardKey, Integer> cache = new ConcurrentHashMap<>(2_000_000);

    public static MoveScore getBestMoveParallel(GameBoard board, boolean isX) {
        cache.clear(); // Fresh start for each move
        List<Integer> moves = board.getAllMoves();
        if (moves.isEmpty())
            return new MoveScore(0, 0, 0);

        return moves.parallelStream()
                .map(idx -> {
                    GameBoard nextBoard = board.place(idx / board.size(), idx % board.size(),
                            isX ? GameBoard.X : GameBoard.O);
                    int score = getScoreRecursive(nextBoard, !isX, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
                    return new MoveScore(score, idx / board.size(), idx % board.size());
                })
                .max(isX ? Comparator.comparingInt(MoveScore::score)
                        : Comparator.comparingInt(MoveScore::score).reversed())
                .orElseThrow();
    }

    private static int getScoreRecursive(GameBoard board, boolean isX, int alpha, int beta, int depth) {
        // Linear Algebra Hook: Get the canonical representation accounting for all 8
        // symmetries
        byte[] canonicalGrid = getCanonical(board.grid(), board.size());
        BoardKey key = new BoardKey(canonicalGrid);

        Integer cached = cache.get(key);
        if (cached != null)
            return cached;

        if (board.checkWinAtLastMove()) {
            return isX ? -WIN_VAL + depth : WIN_VAL - depth;
        }

        List<Integer> moves = board.getAllMoves();
        if (moves.isEmpty())
            return 0;

        int bestScore = isX ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int idx : moves) {
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
                break;
        }

        cache.put(key, bestScore);
        return bestScore;
    }

    /**
     * Applies the 8 orthogonal matrix transformations (4 rotations, 4 reflections)
     * and returns the lexicographically smallest byte array.
     */
    public static byte[] getCanonical(byte[] grid, int size) {
        byte[] min = grid;

        for (int transform = 0; transform < 8; transform++) {
            byte[] current = new byte[grid.length];
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    int nr = r, nc = c;
                    switch (transform) {
                        case 0 -> {
                            nr = r;
                            nc = c;
                        } // Identity
                        case 1 -> {
                            nr = c;
                            nc = size - 1 - r;
                        } // Rotate 90 deg
                        case 2 -> {
                            nr = size - 1 - r;
                            nc = size - 1 - c;
                        } // Rotate 180 deg
                        case 3 -> {
                            nr = size - 1 - c;
                            nc = r;
                        } // Rotate 270 deg
                        case 4 -> {
                            nr = r;
                            nc = size - 1 - c;
                        } // Flip Horizontal
                        case 5 -> {
                            nr = size - 1 - r;
                            nc = c;
                        } // Flip Vertical
                        case 6 -> {
                            nr = c;
                            nc = r;
                        } // Flip Diagonal 1
                        case 7 -> {
                            nr = size - 1 - c;
                            nc = size - 1 - r;
                        } // Flip Diagonal 2
                    }
                    current[nr * size + nc] = grid[r * size + c];
                }
            }
            // Arrays.compare finds which array is "smaller" lexicographically
            if (Arrays.compare(current, min) < 0) {
                min = current;
            }
        }
        return min;
    }

    // This allows the byte[] grid to be used as a key in the memory map
    private record BoardKey(byte[] grid) {
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof BoardKey that))
                return false;
            return Arrays.equals(grid, that.grid);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(grid);
        }
    }
}