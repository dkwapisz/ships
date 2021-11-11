package io.project.ships;

import io.project.ships.game.Board;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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

    private static Board playerBoard = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, false);
    private static Board enemyBoard = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, true);

    @Override
    public void start(Stage stage) throws IOException {
        Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/main-view.fxml")));

        addNodesToRoot(root);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Ships!");
        stage.setScene(scene);
        stage.show();

    }

    private void addNodesToRoot(Pane root) {
        root.getChildren().add(playerBoard);
        root.getChildren().add(enemyBoard);
        for (int i = 0; i < playerBoard.getShips().length; i++) {
            root.getChildren().add(playerBoard.getShips()[i].getShape());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}