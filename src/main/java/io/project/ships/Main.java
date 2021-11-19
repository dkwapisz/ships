package io.project.ships;

import io.project.ships.game.AI;
import io.project.ships.game.Board;
import io.project.ships.game.Position;
import io.project.ships.game.Square;
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
    private static Scene scene;

    private static final int BOARD_COLUMNS = 10;
    private static final int BOARD_ROWS = 10;
    private static final int BOARD_WIDTH = 400;
    private static final int BOARD_HEIGHT = 400;

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    private static Timeline mainTimeline;

    // playerBoard przechowuje tylko informacje, gdzie znajdują się statki danego gracza
    // enemyBoard przechowuje informacje o wszystkich uszkodzonych, nieuszkodzonych, zatopionych statkach oraz o pudłach
    // enemy1Board przedstawia plansze gracza nr 2, enemy2Board przedstawia plansze gracza nr 1
    private static Board player1Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, false);
    private static Board enemy1Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, true);
    private static Board player2Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, false);
    private static Board enemy2Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, true);

    private static AI AI_1;
    private static AI AI_2;

    private static int humanPlayers;
    private static int difficulty1;
    private static int difficulty2;

    private static boolean player1Turn = true;
    private static boolean player1SetShips;
    private static boolean player2SetShips;
    private static boolean gameStarted;
    private static boolean boardSet;

    private static int lastPlayer;

    @Override
    public void start(Stage stage) throws IOException {
        Main.root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/main-view.fxml")));

        addNodesToRoot(1);

        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Ships!");
        stage.setScene(scene);
        stage.show();

        mainTimeline = new Timeline(new KeyFrame(Duration.millis(25), e -> {
            try {
                gameLoop();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));
        mainTimeline.setCycleCount(Timeline.INDEFINITE);
        mainTimeline.play();
    }

    // humanPlayers: 2 - Player vs Player, 1 - Player vs AI, 0 - AI vs AI
    // difficulty: 0 - Player vs Player (difficulty is useless), 1 - Easy, 2 - Medium, 3 - Hard
    public static void setGameType(int humanPlayers, int difficulty1, int difficulty2) {
        Main.humanPlayers = humanPlayers;
        Main.difficulty1 = difficulty1;
        Main.difficulty2 = difficulty2;

        if (difficulty1 > 0) {
            AI_1 = new AI();
        }
        if (difficulty2 > 0) {
            AI_2 = new AI();
        }
    }

    private static void setEnemyBoards() {
        boardSet = true;
        enemy1Board.setEnemyBoardStatus(player2Board);
        enemy2Board.setEnemyBoardStatus(player1Board);
    }

    private void gameLoop() throws IOException {
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
                if (!boardSet) {
                    setEnemyBoards();
                }

                if (player1Turn) {
                    addNodesToRoot(1);
                } else {
                    addNodesToRoot(2);
                }
                changeStyle();
            }
        } else if (humanPlayers == 1) {
            if (!gameStarted) {
                if (player1SetShips) {
                    player2Board.generateShipsRandom();
                    player2SetShips = true;
                    player1Turn = true;
                    gameStarted = true;
                }
            } else {
                if (!boardSet) {
                    setEnemyBoards();
                }

                changeStyle();

                while (!player1Turn) {
                    AI_1.hitAISquare(difficulty1, enemy2Board, enemy2Board.getShips());
                }
            }
        } else if (humanPlayers == 0) {
            //TODO Rozgrywka AI vs AI.
        }

        checkEndGame();
    }


    private void changeStyle() {
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLUMNS; j++) {
                if (enemy1Board.getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.DAMAGED) {
                    enemy1Board.getSquareBoard()[i][j].getStyleClass().add("damaged");
                    player2Board.getSquareBoard()[i][j].getStyleClass().add("damagedMyBoard");
                } else if (enemy1Board.getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.DESTROYED) {
                    enemy1Board.getSquareBoard()[i][j].getStyleClass().add("destroyed");
                    player2Board.getSquareBoard()[i][j].getStyleClass().add("destroyedMyBoard");
                } else if (enemy1Board.getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.MISS) {
                    enemy1Board.getSquareBoard()[i][j].getStyleClass().add("miss");
                    player2Board.getSquareBoard()[i][j].getStyleClass().add("missMyBoard");
                }
                if (enemy2Board.getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.DAMAGED) {
                    enemy2Board.getSquareBoard()[i][j].getStyleClass().add("damaged");
                    player1Board.getSquareBoard()[i][j].getStyleClass().add("damagedMyBoard");
                } else if (enemy2Board.getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.DESTROYED) {
                    enemy2Board.getSquareBoard()[i][j].getStyleClass().add("destroyed");
                    player1Board.getSquareBoard()[i][j].getStyleClass().add("destroyedMyBoard");
                } else if (enemy2Board.getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.MISS) {
                    enemy2Board.getSquareBoard()[i][j].getStyleClass().add("miss");
                    player1Board.getSquareBoard()[i][j].getStyleClass().add("missMyBoard");
                }
            }
        }
    }

    private void checkEndGame() {
        int count1 = 0;
        int count2 = 0;
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLUMNS; j++) {
                if (enemy1Board.getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.DESTROYED) {
                    count1++;
                }
                if (enemy2Board.getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.DESTROYED) {
                    count2++;
                }
            }
        }
        if (count1 == 20) {
            mainTimeline.stop();
            System.out.println("PLAYER 1 WINS");
        } else if (count2 == 20){
            mainTimeline.stop();
            System.out.println("PLAYER 2 WINS");
        }
    }

    public static void addNodesToRoot(int whichPlayer) {

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

    public static void hideBoard() throws IOException {
        if (isGameStarted()) {
            Stage stage = (Stage) root.getScene().getWindow();
            Pane root = FXMLLoader.load(Main.class.getResource("/fxml/hidingBoard-view.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public static void restartGame() {
        mainTimeline.stop();

        player1Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, false);
        enemy1Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, true);
        player2Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, false);
        enemy2Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, true);

        humanPlayers = 0;
        difficulty1 = 0;
        difficulty2 = 0;

        player1Turn = true;
        player1SetShips = false;
        player2SetShips = false;
        gameStarted = false;
        boardSet = false;

        lastPlayer = 0;
    }

    public static Timeline getMainTimeline() {
        return mainTimeline;
    }

    public static boolean isGameStarted() {
        return gameStarted;
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

    public static int getDifficulty1() {
        return difficulty1;
    }

    public static Scene getScene() {
        return scene;
    }

    public static boolean isPlayer1SetShips() {
        return player1SetShips;
    }

    public static boolean isPlayer2SetShips() {
        return player2SetShips;
    }

    public static void main(String[] args) {
        launch();
    }
}