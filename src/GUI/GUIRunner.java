package GUI;

import Game.Othello;
import KI.KI;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.w3c.dom.css.Rect;
import szte.mi.Move;

import java.util.ArrayList;
import java.util.Optional;

public class GUIRunner extends Application implements EventHandler<ActionEvent> {

    private Othello othello;
    int size = 100;
    OthelloButton[][] othelloButtons = new OthelloButton[8][8];
    SplitPane split;
    AnchorPane pane;
    GridPane grid;
    
    Label blackScore = new Label("");
    Label whiteScore = new Label("");


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

        if (alert.getResult() == modeFriend) {

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

            othelloButtons[2][3].drawRectangle();
            othelloButtons[3][2].drawRectangle();
            othelloButtons[4][5].drawRectangle();
            othelloButtons[5][4].drawRectangle();



        } else if(alert.getResult() == modeAI) {
            KI ki = new KI(othello);

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

            othelloButtons[2][3].drawRectangle();
            othelloButtons[3][2].drawRectangle();
            othelloButtons[4][5].drawRectangle();
            othelloButtons[5][4].drawRectangle();
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

        blackScore.setLayoutX(170);
        blackScore.setLayoutY(68.0);
        blackScore.setText(""+othello.getBlackScore());
        blackScore.setFont(new Font("Arial", 35));

        whiteScore.setLayoutX(685);
        whiteScore.setLayoutY(68.0);
        whiteScore.setText(""+othello.getWhiteScore());
        whiteScore.setFont(new Font("Arial", 35));

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
        pane.getChildren().addAll(gameName, black, white, blackScore, whiteScore);

        split.getItems().addAll(pane, grid);

    }


    @Override
    public void handle(ActionEvent e) {

        if(e.getSource() instanceof OthelloButton) {
            if(!othello.isGameEnd()) {
                ((OthelloButton)e.getSource()).clicked(othello.getPlayer());
                 blackScore.setText(""+othello.getBlackScore());
                 whiteScore.setText(""+othello.getWhiteScore());

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

    class OthelloButton extends Button {
        private int row;
        private int column;

        public OthelloButton(int r, int c){
            super();
            this.row = r;
            this.column = c;
            setPrefSize(size,size);
        }

        public void drawRectangle() {
            Rectangle rectangle = new Rectangle(98,98);
            rectangle.setStroke(Color.BLACK);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setStrokeWidth(3);

            getChildren().add(rectangle);
        }


        public void drawLegalMoves(int player) {
            getChildren().clear();
            othello.calcLegalMoves(player);
            ArrayList<Move> alist = othello.getLegalMoves();

            for (int i = 0; i < alist.size()-1; i++) {
                othelloButtons[alist.get(i).x][alist.get(i).y].drawRectangle();
            }
        }

        public void clearBoard() {
            for (int row = 0; row < othello.getBoard().length; row++) {
                for (int column = 0; column < othello.getBoard().length; column++) {
                    othelloButtons[row][column].getChildren().clear();
                }
            }

        }
        public void clicked(int player){
            othello.calcLegalMoves(player);
            boolean legalMoveExist = !othello.getLegalMoves().isEmpty();

            getChildren().clear();
            if(legalMoveExist) {
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
                //TODO:geht nur rein, wenn gepasst wird
                Alert pass = new Alert(Alert.AlertType.INFORMATION);
                pass.setContentText("player "+ othello.getPlayer()+" passed.");
                pass.show();

                othello.guiSwitchPlayer(row,column);
                System.out.println("we are here");


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
