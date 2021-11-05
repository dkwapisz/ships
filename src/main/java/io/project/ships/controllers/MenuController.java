package io.project.ships.controllers;

import io.project.ships.Main;
import javafx.fxml.FXML;
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

}
