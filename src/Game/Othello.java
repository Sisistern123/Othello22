package Game;
import szte.mi.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Othello {

    int[][] board;
    ArrayList<Move> legalMoves = new ArrayList<>();

    public final int EMPTY = 0;
    public final int BLACK = 1;
    public final int WHITE = 2;

    boolean down;
    boolean up;
    boolean left;
    boolean right;
    boolean diagLeftUp;
    boolean diagLeftDown;
    boolean diagRightUp;
    boolean diagRightDown;

    boolean legalMove = false;

    boolean gameEnd = false;

    int player = 1;
    int opponent = 0;
    int passCounter = 0;


    public Othello() {
        this.board = new int[8][8];

        for (int i=0; i<8; i++)
            for (int j=0; j<8; j++)
                this.board[i][j] = EMPTY;

        // start positions
        this.board[3][4] = BLACK; //b
        this.board[4][3] = BLACK;
        this.board[3][3] = WHITE; //w
        this.board[4][4] = WHITE;

        legalMoves.add(new Move(2,3));
        legalMoves.add(new Move(3,2));
        legalMoves.add(new Move(4,5));
        legalMoves.add(new Move(5,4));
    }

    //Methoden für GUI und für Terminal separat gemacht, sorry für den Spaghetti-Code


    public void switchPlayer() {

        Scanner sc = new Scanner(System.in);
        int[] position = new int[2];

        while(!gameEnd) {
            if(player == 1) {
                calcLegalMoves(1);

                if(!legalMoves.isEmpty()) {
                    while(player == 1) { //solange player one dran ist und ein legal move existiert, darf player one einen zug machen
                        System.out.println("Black, enter x value of position.");
                        position[0] = sc.nextInt();
                        System.out.println("Black, enter y value of position.");
                        position[1] = sc.nextInt();

                        play(1,position[0],position[1]);
                        if(legalMove) {
                            player = 2;
                            printBoard();
                        } else {
                            System.out.println("wrong move, try again.");
                        }
                    }
                } else { //es existiert kein legal move, weshalb player one passt und somit player two dran ist
                    passCounter++;
                    player = 2;
                    System.out.println("No legal moves left. Black passed.");
                }

                switchPlayer();

            } else if(player == 2) {
                calcLegalMoves(2);

                if(!legalMoves.isEmpty()) {
                    while(player == 2) {
                        System.out.println("White, enter x value of position.");
                        position[0] = sc.nextInt();
                        System.out.println("White, enter y value of position.");
                        position[1] = sc.nextInt();

                        play(2,position[0],position[1]);
                        if(legalMove) {
                            player = 1;
                            printBoard();
                        } else {
                            System.out.println("wrong move, try again.");
                        }
                    }

                } else {
                    passCounter++;
                    player = 1;
                    System.out.println("No legal moves left. White passed.");
                }

                //checkt am Ende, ob beide gepasst haben; am Ende, damit die loop nicht nochmal ausgeführt wird, falls beide gepasst haben
                if(passCounter == 2) {
                    checkWinner();
                } else {
                    passCounter = 0; //resets counter nach einer "Runde", falls nur einer gepasst hat
                }
                switchPlayer();
            }

        }
    }

    public void guiPlay(int player, int x, int y) {
        guiCalcLegalMoves(player);
        if(checkLegalMoves(player,new Move(x,y))) {
            directionFlip(player,new Move(x,y));
            legalMove = true;
        } else {
            legalMove = false;
        }
    }

    public void play(int player, int x, int y) {
        calcLegalMoves(player);
        if(checkLegalMoves(player,new Move(x,y))) {
            directionFlip(player,new Move(x,y));
            legalMove = true;
        } else {
            legalMove = false;
        }
    }

    public void guiSwitchPlayer(int x, int y) {
        guiPlay(player, x, y);

        if(player == 1) {
            player = 2;
            setOpponent(player);
        } else {
            player = 1;
            setOpponent(player);
        }
    }

    public boolean checkFullBoard() {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++) {
                if(board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }


    public void checkWinner() {
        int blackCounter = 0;
        int whiteCounter = 0;

        if(passCounter == 2 || checkFullBoard()) {
            System.out.println("Game has ended. No legal moves for both left.");
            gameEnd = true;

            for(int i = 0; i < board.length; i++) {
                for(int j = 0; j < board.length; j++) {
                    if(board[i][j] == BLACK) {
                        blackCounter++;
                    } else if(board[i][j] == WHITE) {
                        whiteCounter++;
                    }
                }
            }
            if(blackCounter>whiteCounter) {
                System.out.println("Black has won.");
            } else if(whiteCounter>blackCounter) {
                System.out.println("White has won.");
            } else {
                System.out.println("Draw.");
            }
        }
    }

    public boolean checkLegalMoves(int player, Move move) {
        calcLegalMoves(player);

        boolean legal = false;

        for(Move temp:legalMoves) {
            legal = (temp.x == move.x && temp.y == move.y);
            if(legal) {
                return legal;
            }
        }
        return legal;
    }

    public void calcLegalMoves(int player) {
        setOpponent(player);
        legalMoves.clear();

        for(int x = 0; x < board.length; x++) {
            for(int y = 0; y < board.length; y++) {

                //fängt an von jedem player stein zu checken
                if(board[x][y] == player) {
                    directionCheck(player, x, y);
                }
            }
        }

    }

    public void guiCalcLegalMoves(int player) {
        setOpponent(player);
        legalMoves.clear();

        for(int x = 0; x < board.length; x++) {
            for(int y = 0; y < board.length; y++) {

                //fängt an von jedem player stein zu checken
                if(board[x][y] == player) {
                    directionCheck(player, x, y);
                }
            }
        }
        if(legalMoves.isEmpty()) {
            passCounter++;
        } else {
            passCounter = 0;
        }
    }

    public void directionCheck(int player, int x, int y) {
        //check down
        down = directionIncrements(player,x,y,1,0);

        //check up
        up = directionIncrements(player,x,y,-1,0);

        //check left
        left = directionIncrements(player,x,y,0,-1);

        //check right
        right = directionIncrements(player,x,y,0,1);

        //check diag left up
        diagLeftUp = directionIncrements(player,x,y,-1,-1);

        //check diag right up
        diagRightUp = directionIncrements(player,x,y,-1,1);

        //check diag right down
        diagRightDown = directionIncrements(player,x,y,1,1);

        //check diag left down
        diagLeftDown = directionIncrements(player,x,y,1,-1);

    }

    public void flipCheck(int player, int x, int y) {
        //check down
        down = flipIncrements(player,x,y,1,0);

        //check up
        up = flipIncrements(player,x,y,-1,0);

        //check left
        left = flipIncrements(player,x,y,0,-1);

        //check right
        right = flipIncrements(player,x,y,0,1);

        //check diag left up
        diagLeftUp = flipIncrements(player,x,y,-1,-1);

        //check diag right up
        diagRightUp = flipIncrements(player,x,y,-1,1);

        //check diag right down
        diagRightDown = flipIncrements(player,x,y,1,1);

        //check diag left down
        diagLeftDown = flipIncrements(player,x,y,1,-1);

    }

    public void directionFlip(int player, Move move) {
        //setzt booleans nochmal auf die richtigen werte vom move aus
        flipCheck(player, move.x, move.y);

        //check down
        if(down) {
            flip(player, move, 1, 0);
        }

        //check up
        if(up) {
            flip(player, move,-1, 0);
        };

        //check left
        if(left){
            flip(player, move, 0, -1);
        }

        //check right
        if(right){
            flip(player, move, 0, 1);
        }

        //check diag left up
        if(diagLeftUp){
            flip(player, move,-1,-1);
        }

        //check diag right up
        if(diagRightUp){
            flip(player, move,-1,1);
        }

        //check diag right down
        if(diagRightDown){
            flip(player, move,1,1);
        }

        //check diag left down
        if(diagLeftDown) {
            flip(player, move,1,-1);
        }

    }

    public void flip(int player, Move move, int xIncrement, int yIncrement) {
        board[move.x][move.y] = player;

        int x_temp = move.x;
        int y_temp = move.y;

        if(x_temp + xIncrement >= 0 && x_temp + xIncrement <= 7 && y_temp + yIncrement >= 0 && y_temp + yIncrement <= 7) {
            if(board[x_temp+xIncrement][move.y+yIncrement] == opponent) {

                while (x_temp + xIncrement >= 0 && x_temp + xIncrement <= 7 && y_temp + yIncrement >= 0 && y_temp + yIncrement <= 7) {
                    x_temp += xIncrement;
                    y_temp += yIncrement;

                    if(board[x_temp][y_temp] == opponent) {
                        board[x_temp][y_temp] = player;
                    } else if(board[x_temp][y_temp] == player) {
                        break;
                    }

                }
            }
        }
    }

    public boolean flipIncrements(int player, int x, int y, int xIncrement, int yIncrement) {

        //checkt ob es im Board ist
        if(x + xIncrement >= 0 && x + xIncrement <= 7 && y + yIncrement >= 0 && y + yIncrement <= 7) {
            if(board[x+xIncrement][y+yIncrement] == opponent) {

                while (x + xIncrement >= 0 && x + xIncrement <= 7 && y + yIncrement >= 0 && y + yIncrement <= 7) {
                    x += xIncrement;
                    y += yIncrement;

                    if(board[x][y] == EMPTY) {
                        return false; //leeres feld existiert mit opponent dazwischen
                    } else if (board[x][y] == opponent) {
                        continue; //wird hochgezählt, wieviele gesandwiched werden
                    } else if (board[x][y] == player) {
                        return true; //es gibt kein leeres feld zum setzen
                    }
                }
            } else {
                return false; //neighbour von player ist direkt player oder empty, es ist kein opponent dazwischen
            }
        }
        return false; //außerhalb des Boards

    }

    public boolean directionIncrements(int player, int x, int y, int xIncrement, int yIncrement) {

        //checkt ob es im Board ist
        if(x + xIncrement >= 0 && x + xIncrement <= 7 && y + yIncrement >= 0 && y + yIncrement <= 7) {
            if(board[x+xIncrement][y+yIncrement] == opponent) {

                while (x + xIncrement >= 0 && x + xIncrement <= 7 && y + yIncrement >= 0 && y + yIncrement <= 7) {
                    x += xIncrement;
                    y += yIncrement;

                    if(board[x][y] == EMPTY) {

                        legalMoves.add(new Move(x,y));
                        return true; //leeres feld existiert mit opponent dazwischen
                    } else if (board[x][y] == opponent) {
                        continue; //wird hochgezählt, wieviele gesandwiched werden
                    } else if (board[x][y] == player) {
                        return false; //es gibt kein leeres feld zum setzen
                    }
                }
            } else {
                return false; //neighbour von player ist direkt player oder empty, es ist kein opponent dazwischen
            }
        }
        return false; //außerhalb des Boards

    }

    public void setOpponent(int player) {
        if(player == BLACK) {
            opponent = WHITE;
        } else if(player == WHITE) {
            opponent = BLACK;
        }
    }


    public void printBoard()
    {
        System.out.println
                ("\n" +
                        Arrays.deepToString(this.board)
                                .replace("], ", "\n")
                                .replace("[", "")
                                .replace("]", "")
                                .replace(",", "\t")
                                .replace("0", "-")
                                .replace("1", "B")
                                .replace("2", "W")
                        + "\n");

    }

    public int[][] getBoard() {
        return board;
    }

    public ArrayList<Move> getLegalMoves() {
        return legalMoves;
    }

    public int getEMPTY() {
        return EMPTY;
    }

    public int getBLACK() {
        return BLACK;
    }

    public int getWHITE() {
        return WHITE;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isDiagLeftUp() {
        return diagLeftUp;
    }

    public boolean isDiagLeftDown() {
        return diagLeftDown;
    }

    public boolean isDiagRightUp() {
        return diagRightUp;
    }

    public boolean isDiagRightDown() {
        return diagRightDown;
    }

    public boolean isGameEnd() {
        return gameEnd;
    }

    public int getOpponent() {
        return opponent;
    }

    public int getPlayer() {
        return player;
    }

    public int getPassCounter() {
        return passCounter;
    }

    public int getWhiteScore() {
        int white = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] == WHITE) {
                    white++;
                }
            }
        }
        return white;
    }

    public int getBlackScore() {
        int black = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] == BLACK) {
                    black++;
                }
            }
        }
        return black;
    }


}