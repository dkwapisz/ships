package io.project.ships.controllers;

import io.project.ships.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HideBoardController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button nextPlayerButton;

    @FXML
    void changePlayer() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setScene(Main.getScene());
        stage.show();
    }

    public void initialize() {
        if (Main.isPlayer1Turn()) {
            nextPlayerButton.setText("Player 1 turn");
        } else {
            nextPlayerButton.setText("Player 2 turn");
        }
    }

}
