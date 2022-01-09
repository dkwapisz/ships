package io.project.ships.controllers;

import com.google.gson.Gson;
import io.project.ships.Main;
import io.project.ships.menu.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


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
    private Timeline timeline;
    private boolean timelineRunning;


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
    private Button previousMoveButton;

    @FXML
    void backToMenu() throws IOException {
        Stage stage = (Stage) replaysPane.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/menu-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stopTimeline();
    }

    @FXML
    void initialize() {
        loadGames();
        gamesList.forEach((n) -> choiceBox.getItems().add(n.toString()));
        pauseButton.setText("▶");
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
        updateLabels();
        loadGameFlow();
        moveCount = 0;
        stopTimeline();
    }

    @FXML
    void nextMove() {
        stopTimeline();
        calculteNextMove();
    }

    @FXML
    void previousMove() {
        int currentMove = moveCount;
        moveCount = 0;
        stopTimeline();
        playerBoard.getChildren().remove(board1);
        enemyBoard.getChildren().remove(board2);
        board1 = new ReplayBoard(game.getBoard1(), COLUMNS, ROWS, WIDTH, HEIGHT);
        board2 = new ReplayBoard(game.getBoard2(), COLUMNS, ROWS, WIDTH, HEIGHT);
        playerBoard.getChildren().add(board1);
        enemyBoard.getChildren().add(board2);
        for (int i = 1; i < currentMove; i++) {
            calculteNextMove();
        }
    }

    @FXML
    void playPause() {
        if (!timelineRunning) {
            timeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> {calculteNextMove();}));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
            timelineRunning = true;
            pauseButton.setText("⏸");
        } else {
            stopTimeline();
        }
    }

    private void stopTimeline() {
        if (timelineRunning) {
            timeline.stop();
            timelineRunning = false;
            pauseButton.setText("▶");
        }
    }

    private void loadGames() {
        Database db = new Database();
        gamesList = db.selectGamesBasic(Main.getUser1().getUid());
        db.closeConnection();
    }

    private void updateLabels() {
        Image image1;
        Image image2;
        if (game.getIsAiVsAi() == 1) {
            playerLabel.setText("AI BOARD");
            enemyLabel.setText("AI BOARD");
            image1 = validateImage(imageField1, null);
            image2 = validateImage(imageField2, null);
            updateImages(image1, image2);
        } else if (game.getUid2() == 0) {
            String username1 = game.getUserById(game.getUid1()).getUsername();
            playerLabel.setText(username1);
            enemyLabel.setText("AI BOARD");
            image1 = validateImage(imageField1, game.getUserById(game.getUid1()));
            image2 = validateImage(imageField2, null);
            updateImages(image1, image2);
        } else {
            String username1 = game.getUserById(game.getUid1()).getUsername();
            String username2 = game.getUserById(game.getUid2()).getUsername();
            playerLabel.setText(username1);
            enemyLabel.setText(username2);
            image1 = validateImage(imageField1, game.getUserById(game.getUid1()));
            image2 = validateImage(imageField2, game.getUserById(game.getUid2()));
            updateImages(image1, image2);
        }
    }

    private void loadGameFlow() {
        Gson gson = new Gson();
        gameFlow = gson.fromJson(game.getGameFlow(), GameFlow.class);
    }

    private void calculteNextMove() {
        if (moveCount < gameFlow.getMoves().size()) {
            Move move = gameFlow.getMoves().get(moveCount);
            if (move.getWhichboard() == 2) {
                playerBoard.getChildren().remove(board1);
                board1.updateBoard(move.getRow(), move.getColumn(), 2);
                playerBoard.getChildren().add(board1);
//                System.out.println("move: " + moveCount + ", board: 1, row: " + move.getRow() + ", column: " + move.getColumn());
            } else {
                enemyBoard.getChildren().remove(board2);
                board2.updateBoard(move.getRow(), move.getColumn(), 1);
                enemyBoard.getChildren().add(board2);
//                System.out.println("move: " + moveCount + ", board: 2, row: " + move.getRow() + ", column: " + move.getColumn());
            }
            moveCount += 1;
        }
    }

    private void updateImages(Image image1, Image image2) {
        imageField1.setImage(image1);
        imageField2.setImage(image2);
    }

    private Image validateImage(ImageView imageView, User user) {
        if (user == null) {
            Random rand = new Random();
            int randInt = rand.nextInt(10) +1;
            String path = getClass().getResource("/image/image"+randInt+".jpg").toString();
            Image defaultImg = new Image(path);
            imageView.setImage(defaultImg);
            return defaultImg;
        } else {
            Image img = new Image(user.getPath());
            File tempFile = new File(user.getPath().substring(6).replace("%20", " "));
            boolean exists = tempFile.exists();
            if (exists) {
                imageView.setImage(img);
                return img;
            } else {
                String path = getClass().getResource("/image/image.jpg").toString();
                Image defaultImg = new Image(path);
                imageView.setImage(defaultImg);
                return defaultImg;
            }
        }
    }
}

