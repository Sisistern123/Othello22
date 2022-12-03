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


    public KI(Othello othello, int order, Random rando, long t) {
        this.othello = othello;
        init(order, t, rando);

    }

    //GUI
    public KI(Othello othello) {
        this.othello = othello;

    }


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
            switchCurrentPlayer();
        }
        othello.calcLegalMoves(currentPlayer);
        Random r = new Random(othello.getLegalMoves().size()-1); //random move from legalMoves ArrayList

        return othello.getLegalMoves().get(r.nextInt());
    }

    public void switchCurrentPlayer() { //falls der derzeitige spieler black war, wurde er danach zu white gesetzt
        if(currentPlayer == this.player) {
            this.currentPlayer = this.opponent;
        } else if(currentPlayer == this.opponent) {
            this.currentPlayer = this.player;
        }
    }
}
