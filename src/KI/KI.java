package KI;

import Game.Othello;
import szte.mi.Move;
import szte.mi.Player;

import java.util.Random;

public class KI implements Player {

    private Othello othello;
    private int player;
    private int opponent;
    private int currentPlayer = 1;

    @Override
    public void init(int order, long t, Random rnd) {
        if(order == 0) {
            player = 1; //black
            opponent = 2; //white
        } else if (order == 1) {
            player = 2;
            opponent = 1;
        }
        if(this.othello == null) {
            othello = new Othello();
        }

    }

    @Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {
        if(othello.checkLegalMoves(currentPlayer, prevMove)) {
            othello.directionFlip(currentPlayer, prevMove);
            currentPlayer = opponent;
        }
        return null;
    }
}
