package io.project.ships;

import io.project.ships.game.Board;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    private static Pane root;

    private static final int BOARD_COLUMNS = 10;
    private static final int BOARD_ROWS = 10;
    private static final int BOARD_WIDTH = 400;
    private static final int BOARD_HEIGHT = 400;

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    private static Timeline mainTimeline;

    private static Board player1Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, false);
    private static Board enemy1Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, true);
    private static Board player2Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, false);
    private static Board enemy2Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, true);

    private static int humanPlayers;
    private static int difficulty1;
    private static int difficulty2;

    private static boolean player1Turn = true;
    private static boolean player1SetShips;
    private static boolean player2SetShips;
    private static boolean gameStarted;

    private static int lastPlayer;

    @Override
    public void start(Stage stage) throws IOException {
        Main.root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/main-view.fxml")));

        addNodesToRoot(1);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Ships!");
        stage.setScene(scene);
        stage.show();

        mainTimeline = new Timeline(new KeyFrame(Duration.millis(25), e -> gameLoop()));
        mainTimeline.setCycleCount(Timeline.INDEFINITE);
        mainTimeline.play();
    }

    // humanPlayers: 2 - Player vs Player, 1 - Player vs AI, 0 - AI vs AI
    // difficulty: 0 - Player vs Player (difficulty is useless), 1 - Easy, 2 - Medium, 3 - Hard
    public static void setGameType(int humanPlayers, int difficulty1, int difficulty2) {
        Main.humanPlayers = humanPlayers;
        Main.difficulty1 = difficulty1;
        Main.difficulty2 = difficulty2;
    }

    private void gameLoop() {
        if (humanPlayers == 2) {
            if (!gameStarted) {
                if (player1SetShips && !player2SetShips) {
                    player1Turn = false;
                    addNodesToRoot(2);
                }
                else if (player1SetShips && player2SetShips) {
                    player1Turn = true;
                    gameStarted = true;
                }
            } else {
                System.out.println("GAME BEGINS");
                // TODO Mouselistener i cała rozgrywka player vs player
            }
        }
    }

    private void addNodesToRoot(int whichPlayer) {

        if (lastPlayer != whichPlayer) {
            if (whichPlayer == 1) {
                root.getChildren().remove(player2Board);
                root.getChildren().remove(enemy2Board);
                for (int i = 0; i < player2Board.getShips().length; i++) {
                    root.getChildren().remove(player2Board.getShips()[i].getShape());
                }
                root.getChildren().add(player1Board);
                root.getChildren().add(enemy1Board);
                for (int i = 0; i < player1Board.getShips().length; i++) {
                    root.getChildren().add(player1Board.getShips()[i].getShape());
                }
            } else if (whichPlayer == 2) {
                root.getChildren().remove(player1Board);
                root.getChildren().remove(enemy1Board);
                for (int i = 0; i < player1Board.getShips().length; i++) {
                    root.getChildren().remove(player1Board.getShips()[i].getShape());
                }
                root.getChildren().add(player2Board);
                root.getChildren().add(enemy2Board);
                for (int i = 0; i < player2Board.getShips().length; i++) {
                    root.getChildren().add(player2Board.getShips()[i].getShape());
                }
            }
            lastPlayer = whichPlayer;
        }

    }

    public static void setPlayer1Turn(boolean player1Turn) {
        Main.player1Turn = player1Turn;
    }

    public static void setPlayer1SetShips(boolean player1SetShips) {
        Main.player1SetShips = player1SetShips;
    }

    public static void setPlayer2SetShips(boolean player2SetShips) {
        Main.player2SetShips = player2SetShips;
    }

    public static boolean isPlayer1Turn() {
        return player1Turn;
    }

    public static Board getPlayer1Board() {
        return player1Board;
    }

    public static Board getEnemy1Board() {
        return enemy1Board;
    }

    public static Board getPlayer2Board() {
        return player2Board;
    }

    public static Board getEnemy2Board() {
        return enemy2Board;
    }

    public static void main(String[] args) {
        launch();
    }
}