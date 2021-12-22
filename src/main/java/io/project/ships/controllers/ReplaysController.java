package io.project.ships.controllers;

import io.project.ships.game.Board;
import io.project.ships.menu.Database;
import io.project.ships.menu.ReplayBoard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class ReplaysController {

    @FXML
    private AnchorPane anchorPane;
    private Board board1;
    private Board board2;

    @FXML
    void backToMenu() throws IOException {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/menu-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void generateBoards() {
//        this.board1 = new ReplayBoard();
//        this.board2 = new ReplayBoard();
    }

    public void initialize() {
        generateBoards();
    }
}

