package tictactoe;

import java.util.List;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Arrays;

public class MiniMaxAlphaBeta {
    private static final int WIN_VAL = 1_000_000;
    private static final ConcurrentHashMap<BoardKey, Integer> cache = new ConcurrentHashMap<>(5_000_000);

    public static MoveScore getBestMoveParallel(GameBoard board, boolean isX) {
        cache.clear();
        List<Integer> moves = board.getAllMoves();
        if (moves.isEmpty())
            return new MoveScore(0, 0, 0);

        return moves.parallelStream()
                .map(idx -> {
                    GameBoard nextBoard = board.place(idx / board.size(), idx % board.size(),
                            isX ? GameBoard.X : GameBoard.O);
                    // Start search. Note: we pass depth 1
                    int score = getScoreRecursive(nextBoard, !isX, -2_000_000, 2_000_000, 1);
                    return new MoveScore(score, idx / board.size(), idx % board.size());
                })
                .max(isX ? Comparator.comparingInt(MoveScore::score)
                        : Comparator.comparingInt(MoveScore::score).reversed())
                .orElseThrow();
    }

    private static int getScoreRecursive(GameBoard board, boolean isX, int alpha, int beta, int depth) {
        // 1. ALWAYS check for win before cache lookup
        if (board.checkWinAtLastMove()) {
            // If X just moved (isX is now false) and won: positive score
            // If O just moved (isX is now true) and won: negative score
            return isX ? -WIN_VAL + depth : WIN_VAL - depth;
        }

        List<Integer> moves = board.getAllMoves();
        if (moves.isEmpty())
            return 0;

        // 2. Cache Lookup (using canonical symmetry)
        BoardKey key = new BoardKey(getCanonical(board.grid(), board.size()));
        Integer cached = cache.get(key);
        if (cached != null) {
            // Adjust cached score for current depth
            if (cached >= WIN_VAL - 100)
                return cached - depth;
            if (cached <= -WIN_VAL + 100)
                return cached + depth;
            return cached;
        }

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

        // 3. Store in cache (store the raw score, adjustment happens on retrieval)
        cache.put(key, bestScore);
        return bestScore;
    }

    public static byte[] getCanonical(byte[] grid, int size) {
        byte[] min = grid;
        for (int t = 0; t < 8; t++) {
            byte[] current = new byte[grid.length];
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    int nr = r, nc = c;
                    switch (t) {
                        case 1 -> {
                            nr = c;
                            nc = size - 1 - r;
                        }
                        case 2 -> {
                            nr = size - 1 - r;
                            nc = size - 1 - c;
                        }
                        case 3 -> {
                            nr = size - 1 - c;
                            nc = r;
                        }
                        case 4 -> {
                            nr = r;
                            nc = size - 1 - c;
                        }
                        case 5 -> {
                            nr = size - 1 - r;
                            nc = c;
                        }
                        case 6 -> {
                            nr = c;
                            nc = r;
                        }
                        case 7 -> {
                            nr = size - 1 - c;
                            nc = size - 1 - r;
                        }
                    }
                    current[nr * size + nc] = grid[r * size + c];
                }
            }
            if (Arrays.compare(current, min) < 0)
                min = current;
        }
        return min;
    }

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