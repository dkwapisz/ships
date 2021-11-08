package io.project.ships.controllers;

import io.project.ships.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private VBox menuScene;

    @FXML
    void startGame() throws IOException {
        Main main = new Main();
        Stage stage = (Stage) menuScene.getScene().getWindow();
        main.start(stage);
    }

    @FXML
    void goToStatistics() throws IOException {
        Stage stage = (Stage) menuScene.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/statistics-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goToReplays() throws IOException {
        Stage stage = (Stage) menuScene.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/replays-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
