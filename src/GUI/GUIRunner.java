package GUI;

import Game.Othello;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import szte.mi.Move;

import java.util.Optional;

public class GUIRunner extends Application implements EventHandler<ActionEvent> {

    private Othello othello;
    int size = 100;
    OthelloButton[][] othelloButtons = new OthelloButton[8][8];
    SplitPane split;
    AnchorPane pane;
    GridPane grid;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage window) {
        grid = new GridPane();
        othello = new Othello();

        window.setTitle("Othello");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Select mode");
        alert.setHeaderText("Do you want to play against a friend or against an Artificial Intelligence?");
        alert.setContentText("Choose your option.");

        ButtonType modeFriend = new ButtonType("friend");
        ButtonType modeAI = new ButtonType("Artificial Intelligence");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(modeFriend, modeAI, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();

        if (alert.getResult() == modeAI || alert.getResult() == modeFriend) {

            setPanes(grid);

            for (int row = 0; row < othello.getBoard().length; row++) {
                for (int column = 0; column < othello.getBoard().length; column++) {
                    OthelloButton button = new OthelloButton(row,column);
                    othelloButtons[row][column] = button;
                    button.setOnAction(this);
                    grid.add(button,column,row);
                }
            }


            window.setScene(new Scene(split,1000,1000));
            window.show();

            othelloButtons[3][3].draw(othello.WHITE);
            othelloButtons[3][3].setOnAction(null);
            othelloButtons[3][4].draw(othello.BLACK);
            othelloButtons[3][4].setOnAction(null);
            othelloButtons[4][3].draw(othello.BLACK);
            othelloButtons[4][3].setOnAction(null);
            othelloButtons[4][4].draw(othello.WHITE);
            othelloButtons[4][4].setOnAction(null);
        }


    }

    public void setPanes(GridPane grid) {
        split = new SplitPane();
        split.setOrientation(Orientation.VERTICAL);
        split.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));

        pane = new AnchorPane();
        pane.setMaxHeight(150);
        pane.setMinHeight(150);

        grid.setMinHeight(800);
        grid.setMaxHeight(800);
        grid.setMinWidth(800);
        grid.setMaxWidth(800);

        Label gameName = new Label("Othello");
        gameName.setLayoutX(369.0);
        gameName.setLayoutY(14.0);
        gameName.setFont(new Font("Arial", 53.0));

        Label scoreBlack = new Label(""+othello.getBlackScore());
        scoreBlack.setLayoutX(170);
        scoreBlack.setLayoutY(68.0);
        scoreBlack.setFont(new Font("Arial", 35));

        Label scoreWhite = new Label(""+othello.getWhiteScore());
        scoreWhite.setLayoutX(685);
        scoreWhite.setLayoutY(68.0);
        scoreWhite.setFont(new Font("Arial", 35));

        //Circle black and white
        Circle black = new Circle();
        black.setLayoutX(99.0);
        black.setLayoutY(85.0);
        black.setRadius(37.0);
        black.setFill(Color.BLACK);

        Circle white = new Circle();
        white.setLayoutX(803.0);
        white.setLayoutY(85.0);
        white.setRadius(37.0);
        white.setFill(Color.WHITE);

        pane.setBackground(new Background(new BackgroundFill(Color.FORESTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.getChildren().addAll(gameName, black, white, scoreBlack, scoreWhite);

        split.getItems().addAll(pane, grid);

    }


    @Override
    public void handle(ActionEvent e) {
        if(!othello.isGameEnd())
            if(e.getSource() instanceof OthelloButton)
                ((OthelloButton)e.getSource()).clicked(othello.getPlayer());
    }

    class OthelloButton extends Button {
        private int row;
        private int column;

        public OthelloButton(int r, int c){
            super();
            this.row = r;
            this.column = c;
            setPrefSize(size,size);
        }

        public void clicked(int player){
            othello.calcLegalMoves(player);
            boolean legalMoveExist = !othello.getLegalMoves().isEmpty();

            if (legalMoveExist) {
                if (othello.checkLegalMoves(player, new Move(row, column))) {
                    draw(player);
                    othello.guiSwitchPlayer(row,column);

                    for (int row = 0; row < othello.getBoard().length; row++) {
                        for (int column = 0; column < othello.getBoard().length; column++) {
                            if (othello.getBoard()[row][column] == othello.BLACK) {
                                othelloButtons[row][column].draw(othello.BLACK);
                                othelloButtons[row][column].setOnAction(null);
                            } else if (othello.getBoard()[row][column] == othello.WHITE) {
                                othelloButtons[row][column].draw(othello.WHITE);
                                othelloButtons[row][column].setOnAction(null);
                            }
                        }
                    }
                }
            } else {
                othello.guiSwitchPlayer(row,column);

                System.out.println("we are here");
                if(othello.getPassCounter() == 2 || othello.checkFullBoard()) {
                    othello.checkWinner();
                    if(othello.isGameEnd()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Game has ended.");
                        alert.setHeaderText("Who won?");
                        if(othello.getBlackScore()>othello.getWhiteScore()) {
                            alert.setContentText("Black has won with "+othello.getBlackScore()+" stones.");
                        } else if(othello.getBlackScore()<othello.getWhiteScore()) {
                            alert.setContentText("White has won with "+othello.getWhiteScore()+" stones.");
                        } else {
                            alert.setContentText("Draw.");
                        }
                        alert.show();
                    }
                }
            }
        }



        public void draw(int player) {
            Circle circle = new Circle(size / 2, size / 2, size / 2);
            if (player == othello.getBLACK()) {
                circle.setStroke(Color.BLACK);
                circle.setFill(Color.BLACK);

            } else if (player == othello.getWHITE()) {
                circle.setStroke(Color.WHITE);
                circle.setFill(Color.WHITE);
            }
            getChildren().add(circle);
        }
    }
}
