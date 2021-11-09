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

    private StackPane root = new StackPane();
    private Stage stage;

    private final int BOARD_COLUMNS = 10;
    private final int BOARD_ROWS = 10;
    private final int BOARD_WIDTH = 400;
    private final int BOARD_HEIGHT = 400;

    private final int WINDOW_WIDTH = 1200;
    private final int WINDOW_HEIGHT = 800;

    private Board playerBoard = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, false);
    private Board enemyBoard = new Board(BOARD_COLUMNS, BOARD_ROWS, BOARD_WIDTH, BOARD_HEIGHT, true);

    private final Button backToMenuButton = new Button("Back to menu");

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        setControls();
        root.getChildren().add(playerBoard);
        root.getChildren().add(enemyBoard);
        root.getChildren().add(backToMenuButton);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Ships!");
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/board-style.css")).toExternalForm());
    }

    private void setControls() {
        backToMenuButton.setOnAction(event -> {
            try {
                Pane newRoot = FXMLLoader.load(getClass().getResource("/fxml/menu-view.fxml"));
                Scene scene = new Scene(newRoot);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}