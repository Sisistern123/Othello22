package Game;


public class Runner {

    public static void main(String[] args) {
        //for playing on terminal
        Othello othello = new Othello();

        othello.printBoard();
        othello.switchPlayer();
    }

}
