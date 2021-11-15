package io.project.ships;

import io.project.ships.game.Board;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    private static final int BOARD_COLUMNS = 10;
    private static final int BOARD_ROWS = 10;
    private static final int BOARD_WIDTH = 400;
    private static final int BOARD_HEIGHT = 400;

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    private static Board player1Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, false);
    private static Board enemy1Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, true);
    private static Board player2Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, false);
    private static Board enemy2Board = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, true);


    @Override
    public void start(Stage stage) throws IOException {
        Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/main-view.fxml")));

        addNodesToRoot(root);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Ships!");
        stage.setScene(scene);
        stage.show();

    }

    // humanPlayers: 2 - Player vs Player, 1 - Player vs AI, 0 - AI vs AI
    // difficulty: 0 - Player vs Player (difficulty is useless), 1 - Easy, 2 - Medium, 3 - Hard
    public static void setGameType(int humanPlayers, int difficulty1, int difficulty2) {
        if (humanPlayers == 2) {
            System.out.println("p2p");
        } else if (humanPlayers == 1) {
            System.out.println("p2a");
            if (difficulty1 == 1) {
                System.out.println("easy1");
            } else if (difficulty1 == 2) {
                System.out.println("medium1");
            } else if (difficulty1 == 3) {
                System.out.println("hard1");
            }
        } else if (humanPlayers == 0) {
            System.out.println("a2a");
            if (difficulty1 == 1) {
                System.out.println("easy1");
            } else if (difficulty1 == 2) {
                System.out.println("medium1");
            } else if (difficulty1 == 3) {
                System.out.println("hard1");
            }
            if (difficulty2 == 1) {
                System.out.println("easy2");
            } else if (difficulty2 == 2) {
                System.out.println("medium2");
            } else if (difficulty2 == 3) {
                System.out.println("hard2");
            }
        }
    }

    private void addNodesToRoot(Pane root) {
        root.getChildren().add(player1Board);
        root.getChildren().add(enemy1Board);
        for (int i = 0; i < player1Board.getShips().length; i++) {
            root.getChildren().add(player1Board.getShips()[i].getShape());
        }
    }

    public static Board getPlayer1Board() {
        return player1Board;
    }

    public static void main(String[] args) {
        launch();
    }
}