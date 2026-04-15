package tictactoe;

import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import java.util.ArrayList;
import java.util.List;

public record GameBoard(byte[] grid, int size, int target, int lastMoveIdx) {
    public static final byte X = 1;
    public static final byte O = -1;
    public static final byte EMPTY = 0;
    private static final VectorSpecies<Byte> SPECIES = ByteVector.SPECIES_PREFERRED;

    public GameBoard(int size, int target) {
        this(new byte[size * size], size, target, -1);
    }

    public GameBoard place(int r, int c, byte val) {
        byte[] nextGrid = grid.clone();
        int idx = r * size + c;
        nextGrid[idx] = val;
        return new GameBoard(nextGrid, size, target, idx);
    }

    public boolean checkWinAtLastMove() {
        if (lastMoveIdx == -1)
            return false;
        byte val = grid[lastMoveIdx];
        int r = lastMoveIdx / size;
        int c = lastMoveIdx % size;
        int[][] directions = { { 0, 1 }, { 1, 0 }, { 1, 1 }, { 1, -1 } };
        for (int[] d : directions) {
            if (1 + countInDir(r, c, d[0], d[1], val) + countInDir(r, c, -d[0], -d[1], val) >= target)
                return true;
        }
        return false;
    }

    private int countInDir(int r, int c, int dr, int dc, byte val) {
        int count = 0;
        for (int i = 1; i < target; i++) {
            int nr = r + dr * i, nc = c + dc * i;
            if (nr >= 0 && nr < size && nc >= 0 && nc < size && grid[nr * size + nc] == val)
                count++;
            else
                break;
        }
        return count;
    }

    public List<Integer> getAllMoves() {
        List<Integer> moves = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == EMPTY)
                moves.add(i);
        }
        return moves;
    }
}