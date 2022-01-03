package io.project.ships;

import com.google.gson.Gson;
import io.project.ships.game.AI;
import io.project.ships.game.Board;
import io.project.ships.game.Square;
import io.project.ships.menu.*;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
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
    private static AnimationTimer timerAI1;
    private static AnimationTimer timerAI2;
    private static final long moveInterval = 1_000_000_000; // in nanoseconds

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
    private static boolean timer1Started;
    private static boolean timer2Started;
    private static boolean pause;
    private static boolean gameEnded;

    private static int lastPlayer;

    private static User user1;
    private static User user2;
    private static GameFlow gameFlow;
    private static int moveNumber;
    private static ArrayList<UserStatistics> statistics;
    private static UserStatistics user1Statistics;
    private static UserStatistics user2Statistics;

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
        gameFlow = new GameFlow(new ArrayList<Move>());
        moveNumber = 0;
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
                    boardSet = true;
                }

                changeStyle();
                long time = System.nanoTime();

                timerAI1 = new AnimationTimer() {
                    @Override
                    public void handle(long l) {
                        if (l - (moveInterval/2) > time) {
                            if (difficulty1 == 3) {
                                AI_1.setDoubleShot(AI_1.calculateDoubleShot());
                            }
                            AI_1.hitAISquare(difficulty1, enemy2Board, enemy2Board.getShips());
                            checkEndGame();
                            timer1Started = false;
                            super.stop();
                        }
                    }
                };

                if (!player1Turn && !timer1Started) {
                    timer1Started = true;
                    timerAI1.start();
                }
            }
        } else if (humanPlayers == 0) {
            if (!pause) {
                if (!gameStarted) {
                    player1Board.generateShipsRandom();
                    player2Board.generateShipsRandom();
                    player1SetShips = true;
                    player2SetShips = true;
                    player1Turn = true;
                    gameStarted = true;
                } else {
                    if (!boardSet) {
                        setEnemyBoards();
                        boardSet = true;
                    }
                    changeStyle();
                    long time = System.nanoTime();

                    timerAI1 = new AnimationTimer() {
                        @Override
                        public void handle(long l) {
                            if (l - moveInterval > time) {
                                if (difficulty1 == 3) {
                                    AI_1.setDoubleShot(AI_1.calculateDoubleShot());
                                }
                                AI_1.hitAISquare(difficulty1, enemy2Board, enemy2Board.getShips());
                                checkEndGame();
                                timer1Started = false;
                                super.stop();
                            }
                        }
                    };

                    timerAI2 = new AnimationTimer() {
                        @Override
                        public void handle(long l) {
                            if (l - moveInterval > time) {
                                if (difficulty2 == 3) {
                                    AI_2.setDoubleShot(AI_2.calculateDoubleShot());
                                }
                                AI_2.hitAISquare(difficulty2, enemy1Board, enemy1Board.getShips());
                                checkEndGame();
                                timer2Started = false;
                                super.stop();
                            }
                        }
                    };


                    if (!player1Turn && !timer1Started) {
                        timer1Started = true;
                        addNodesToRoot(2);
                        timerAI1.start();
                    }

                    if (player1Turn && !timer2Started) {
                        timer2Started = true;
                        addNodesToRoot(1);
                        timerAI2.start();
                    }
                }
            }
        }
        checkEndGame();
    }

    public static void stepByStep(boolean player1Turn) {
        if (!gameEnded) {
            if (!gameStarted) {
                player1Board.generateShipsRandom();
                player2Board.generateShipsRandom();
                player1SetShips = true;
                player2SetShips = true;
                Main.player1Turn = true;
                gameStarted = true;
            } else {
                if (!boardSet) {
                    setEnemyBoards();
                    boardSet = true;
                }

                changeStyle();

                if (!player1Turn) {
                    if (difficulty1 == 3) {
                        AI_1.setDoubleShot(AI_1.calculateDoubleShot());
                    }
                    AI_1.hitAISquare(difficulty1, enemy2Board, enemy2Board.getShips());
                } else {
                    if (difficulty2 == 3) {
                        AI_2.setDoubleShot(AI_2.calculateDoubleShot());
                    }
                    AI_2.hitAISquare(difficulty2, enemy1Board, enemy1Board.getShips());
                }
                checkEndGame();

                if (!player1Turn) {
                    addNodesToRoot(2);
                }

                if (player1Turn) {
                    addNodesToRoot(1);
                }
            }
        }
    }

    private static void changeStyle() {
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

    private static void checkEndGame() {
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
            changeStyle();
            gameEnded = true;
            mainTimeline.stop();
            System.out.println("PLAYER 1 WINS");
            saveGame();
            updateStatistics(1);
            loadStatistics();
        } else if (count2 == 20){
            changeStyle();
            gameEnded = true;
            mainTimeline.stop();
            System.out.println("PLAYER 2 WINS");
            saveGame();
            updateStatistics(2);
            loadStatistics();
        }

    }

    private static void updateStatistics(int whoWon) {
        Database db = new Database();

        if(difficulty1 == 0) {
            if (whoWon == 1) {
                user1Statistics.updatePlayed();
                user1Statistics.updateVictories();
                user1Statistics.updateWinRate();
                user2Statistics.updatePlayed();
                user2Statistics.updateWinRate();
            } else {
                user1Statistics.updatePlayed();
                user1Statistics.updateWinRate();
                user2Statistics.updatePlayed();
                user2Statistics.updateVictories();
                user2Statistics.updateWinRate();
            }
            db.updateStatistics(user1Statistics);
            db.updateStatistics(user2Statistics);
        } else if(difficulty1 !=0 && difficulty2 == 0) {
            if (whoWon == 1) {
                user1Statistics.updatePlayed();
                user1Statistics.updateVictories();
                user1Statistics.updateWinRate();
            } else {
                user1Statistics.updatePlayed();
                user1Statistics.updateWinRate();
            }
            db.updateStatistics(user1Statistics);
        }
        db.closeConnection();
    }

    private static void saveGame() {
        int isAivsai;
        int user2Uid;
        if (user2 == null) {
            user2Uid = 0;
        } else {
            user2Uid = user2.getUid();
        }
        if (difficulty2 > 0) {
            isAivsai = 1;
        } else {
            isAivsai = 0;
        }
        Gson gson = new Gson();
        String gameFlowJSON = gson.toJson(gameFlow);
        Database db = new Database();
        db.insertIntoGameHistory(user1.getUid(),
                user2Uid,
                player1Board.boardToString(),
                player2Board.boardToString(),
                gameFlowJSON,
                isAivsai
        );
        db.closeConnection();
    }

    public static void loadStatistics() {
        Database db = new Database();
        statistics = db.selectStatistics();
        db.closeConnection();
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

        if (timerAI1 != null) {
            timerAI1.stop();
        }
        if (timerAI2 != null) {
            timerAI2.stop();
        }

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

        gameFlow = new GameFlow(new ArrayList<Move>());
        moveNumber = 0;
    }

    public static void addMove(Move move) {
        gameFlow.addMove(move);
    }

    public static void moveNumberIncrement() {
        moveNumber = moveNumber + 1;
    }

    public static int getMoveNumber() {
        return moveNumber;
    }

    public static GameFlow getGameFlow() {
        return gameFlow;
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

    public static int getDifficulty2() {
        return difficulty2;
    }

    public static Scene getScene() {
        return scene;
    }

    public static UserStatistics getUser1Statistics() {
        return user1Statistics;
    }

    public static UserStatistics getUser2Statistics() {
        return user2Statistics;
    }

    public static boolean isPlayer1SetShips() {
        return player1SetShips;
    }

    public static boolean isPlayer2SetShips() {
        return player2SetShips;
    }

    public static boolean isNotPaused() {
        return !pause;
    }

    public static void setPause(boolean pause) {
        Main.pause = pause;
    }

    public static boolean isTimer1Started() {
        return timer1Started;
    }

    public static void main(String[] args) {
        launch();
    }

    public static User getUser1() {
        return user1;
    }

    public static User getUser2() {
        return user2;
    }

    public static ArrayList<UserStatistics> getStatistics() {
        return statistics;
    }

    public static void setUser1(User user) {
        user1 = user;
    }

    public static void setUser2(User user) {
        user2 = user;
    }

    public static void setUser1Statistics() {
        for(UserStatistics stats : statistics) {
            if (stats.getUid() == user1.getUid()) {
                user1Statistics = stats;
            }
        }
        if (user1Statistics == null) {
            Database db = new Database();
            db.insertIntoStatistics(user1.getUid());
            user1Statistics = new UserStatistics(user1.getUid(), 0, 0, 0, 0, 0, 0);
        }
    }
    public static void setUser2Statistics() {
        for(UserStatistics stats : statistics) {
            if (stats.getUid() == user2.getUid()) {
                user2Statistics = stats;
            }
        }
        if (user2Statistics == null) {
            Database db = new Database();
            db.insertIntoStatistics(user2.getUid());
            user2Statistics = new UserStatistics(user2.getUid(), 0, 0, 0, 0, 0, 0);
        }
    }
}