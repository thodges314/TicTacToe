package tictactoe;

public record MoveScore(int score, int row, int col) implements Comparable<MoveScore> {
    @Override
    public int compareTo(MoveScore other) {
        return Integer.compare(this.score, other.score);
    }
}