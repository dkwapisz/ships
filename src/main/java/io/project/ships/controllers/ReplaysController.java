package io.project.ships.controllers;

import com.google.gson.Gson;
import io.project.ships.Main;
import io.project.ships.menu.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


public class ReplaysController {

    private ArrayList<GameBasic> gamesList;
    private GameDetailed game;
    private GameFlow gameFlow;
    private ReplayBoard board1;
    private ReplayBoard board2;
    private final int COLUMNS = 10;
    private final int ROWS = 10;
    private final double WIDTH = 400;
    private final double HEIGHT = 400;
    private int moveCount = 0;


    @FXML
    private Button backButton;

    @FXML
    private ComboBox<String> choiceBox;

    @FXML
    private Pane enemyBoard;

    @FXML
    private Label enemyLabel;

    @FXML
    private ImageView imageField1;

    @FXML
    private ImageView imageField2;

    @FXML
    private Pane replaysPane;

    @FXML
    private Button nextMoveButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Pane playerBoard;

    @FXML
    private Label playerLabel;

    @FXML
    void backToMenu() throws IOException {
        Stage stage = (Stage) replaysPane.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/menu-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {
        loadGames();
        gamesList.forEach((n) -> choiceBox.getItems().add(n.toString()));
    }

    @FXML
    private void loadGameData() {
        int chosen = Integer.parseInt(choiceBox.getValue().substring(choiceBox.getValue().indexOf(':') + 1, choiceBox.getValue().indexOf(';')));
        Database db = new Database();
        game = db.selectGameDetailed(chosen);
        db.closeConnection();
        playerBoard.getChildren().remove(board1);
        enemyBoard.getChildren().remove(board2);
        board1 = new ReplayBoard(game.getBoard1(), COLUMNS, ROWS, WIDTH, HEIGHT);
        board2 = new ReplayBoard(game.getBoard2(), COLUMNS, ROWS, WIDTH, HEIGHT);
        playerBoard.getChildren().add(board1);
        enemyBoard.getChildren().add(board2);
        loadGameFlow();
        moveCount = 0;
    }

    @FXML
    void nextMove() {
        if (moveCount < gameFlow.getMoves().size()) {
            Move move = gameFlow.getMoves().get(moveCount);
            if (move.getWhichboard() == 2) {
                playerBoard.getChildren().remove(board1);
                board1.updateBoard(move.getRow(), move.getColumn(), 2);
                playerBoard.getChildren().add(board1);
                System.out.println("move: " + moveCount + ", board: 1, row: " + move.getRow() + ", column: " + move.getColumn());
            } else {
                enemyBoard.getChildren().remove(board2);
                board2.updateBoard(move.getRow(), move.getColumn(), 1);
                enemyBoard.getChildren().add(board2);
                System.out.println("move: " + moveCount + ", board: 2, row: " + move.getRow() + ", column: " + move.getColumn());
            }
            moveCount += 1;
        }
    }

    @FXML
    void playPause(ActionEvent event) {

    }

    private void loadGames() {
        Database db = new Database();
        gamesList = db.selectGamesBasic(Main.getUser1().getUid());
        db.closeConnection();
    }

    private void loadGameFlow() {
        Gson gson = new Gson();
        gameFlow = gson.fromJson(game.getGameFlow(), GameFlow.class);
    }
}

