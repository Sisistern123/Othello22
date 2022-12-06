package KI;

import Game.Othello;
import szte.mi.Move;
import szte.mi.Player;

import java.util.Random;

public class KI implements Player {
    private Othello othello;
    private int player;
    private int opponent;

    private int[][] name = {
            {100, -10, 11, 6, 6, 11, -10, 100},
            {-10, -20, 1, 2, 2, 1, -20, -10},
            {10, 1, 5, 4, 4, 5, 1, 10},
            {6, 2, 4, 2, 2, 4, 2, 6},
            {6, 2, 4, 2, 2, 4, 2, 6},
            {10, 1, 5, 4, 4, 5, 1, 10},
            {-10, -20, 1, 2, 2, 1, -20, -10},
            {100, -10, 11, 6, 6, 11, -10, 100}
    };

    @Override
    public void init(int order, long t, Random rnd) {
        if(order == 0) {
            player = 1; //black
            opponent = 2; //white
        } else if (order == 1) {
            player = 2;
            opponent = 1;
        }
        othello = new Othello();

    }

    //TODO
    public Move guiNextMove(Move prevMove) {
        return null;
    }

    @Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {

        if(prevMove != null) {
            othello.setOpponent(opponent);
            othello.directionFlip(opponent, new Move(prevMove.y, prevMove.x));
        }

        othello.guiCalcLegalMoves(player);

        if(!othello.getLegalMoves().isEmpty()) {
            int random = othello.getLegalMoves().size();
            Random r = new Random(0); //random move from legalMoves ArrayList
            Move randomMove = othello.getLegalMoves().get(r.nextInt(random));

            if(randomMove != null) {
                othello.directionFlip(player, randomMove);
            }
            return new Move(randomMove.y, randomMove.x);
        } else {
            return null;
        }

    }

}
