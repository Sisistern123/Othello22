package KI;

import Game.Othello;
import szte.mi.Move;
import szte.mi.Player;

import java.util.Random;

public class KI implements Player {
    private Othello othello;
    private int player;
    private int opponent;

    //https://courses.cs.washington.edu/courses/cse573/04au/Project/mini1/O-Thell-Us/Othellus.pdf
    private final int[][] weightMatrix = {
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

    public void init(int order) {
        if(order == 0) {
            player = 1; //black
            opponent = 2; //white
        } else if (order == 1) {
            player = 2;
            opponent = 1;
        }
        othello = new Othello();

    }

    public Move guiNextMove(Move prevMove) {
        if(prevMove != null) {
            othello.setOpponent(opponent);
            othello.directionFlip(opponent, new Move(prevMove.x, prevMove.y));
        }

        othello.guiCalcLegalMoves(player);

        if(!othello.getLegalMoves().isEmpty()) {
            int random = othello.getLegalMoves().size();
            Random r = new Random(0); //random move from legalMoves ArrayList
            Move randomMove = othello.getLegalMoves().get(r.nextInt(random));

            if(randomMove != null) {
                othello.directionFlip(player, randomMove);
            }
            return randomMove;
        } else {
            return null;
        }

    }

    @Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {

        if(prevMove != null) {
            othello.setOpponent(opponent);
            othello.directionFlip(opponent, new Move(prevMove.y, prevMove.x));
        }

        othello.guiCalcLegalMoves(player);

        if(!othello.getLegalMoves().isEmpty()) {
            //int random = othello.getLegalMoves().size();
            //Random r = new Random(0); //random move from legalMoves ArrayList
            //Move randomMove = othello.getLegalMoves().get(r.nextInt(random));

            Move bestMove = othello.getLegalMoves().get(0);
            for (Move move: othello.getLegalMoves()) {
                if(weightMatrix[move.y][move.x] > weightMatrix[bestMove.y][bestMove.x]) {
                    bestMove = move;
                }
            }
            if(bestMove != null) {
                othello.directionFlip(player, bestMove);
            }
            return new Move(bestMove.y, bestMove.x);
        } else {
            return null;
        }

    }

}
