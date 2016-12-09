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
public class MiniMaxAlphaBeta {
    private static byte MIN_SCORE = -128;
    private static byte MAX_SCORE = 127;
    private static byte ALPHA_START = -128;
    private static byte BETA_START = 127;
    
    private Score bestScore = new Score();
    private boolean keepSearching = true;
    private byte alpha;
    private byte beta;
    private GameBoard board;
    private boolean playerX;
    
    protected MiniMaxAlphaBeta(GameBoard board, boolean playerX, byte alpha, byte beta){
        this.board = board;
        this.playerX = playerX;
        this.alpha = alpha;
        this.beta = beta;
    }
    
    public MiniMaxAlphaBeta(GameBoard board, boolean playerX){
        this(board, playerX, ALPHA_START, BETA_START);
    }
    
    public Score getBestScore(){
        if(board.possibleMoves().length == 0 || board.checkWin() != 0)
            bestScore.setScore(board.checkWin());
        else if(playerX){
            bestScore.setScore(MIN_SCORE);
            byte[][] possibleMoves = board.possibleMoves();
            for(byte[] move: possibleMoves){
                GameBoard expBoard = board.placeX(move[0], move[1]);
                byte expScore = (new MiniMaxAlphaBeta(expBoard, false, alpha, beta)).getBestScore().getScore();
                if(expScore > bestScore.getScore()) {
                    bestScore.setScore(expScore);
                    bestScore.setMove(move);
                    if(expScore > alpha)
                        alpha = expScore;
                    if(expScore >= beta){
                        keepSearching = false;
                        break;
                    }
                }
            }
        } else {
            bestScore.setScore(MAX_SCORE);
            byte[][] possibleMoves = board.possibleMoves();
            for(byte[] move: possibleMoves){
                GameBoard expBoard = board.placeO(move[0], move[1]);
                byte expScore = (new MiniMaxAlphaBeta(expBoard, true, alpha, beta)).getBestScore().getScore();
                if(expScore < bestScore.getScore()) {
                    bestScore.setScore(expScore);
                    bestScore.setMove(move);
                    if(expScore < beta)
                        beta = expScore;
                    if(expScore <= alpha){
                        break;
                    }
                }
            }
        }
        return bestScore;
    }
}
