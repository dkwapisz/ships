package io.project.ships.controllers;

import io.project.ships.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class GameSettingsController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private RadioButton easy1_RB;

    @FXML
    private ToggleGroup difficulty1Group;

    @FXML
    private RadioButton medium1_RB;

    @FXML
    private RadioButton hard1_RB;

    @FXML
    private RadioButton player_VS_player_RB;

    @FXML
    private ToggleGroup playersGroup;

    @FXML
    private RadioButton player_VS_AI_RB;

    @FXML
    private RadioButton AI_VS_AI_RB;

    @FXML
    private TextField difficulty1TextField;

    @FXML
    private TextField difficulty2TextField;

    @FXML
    private RadioButton easy2_RB;

    @FXML
    private ToggleGroup difficulty2Group;

    @FXML
    private RadioButton medium2_RB;

    @FXML
    private RadioButton hard2_RB;

    @FXML
    void goToBoard() throws IOException {
        Main main = new Main();
        Main.setGameType(whoPlays(), getDifficulty(1), getDifficulty(2));
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        main.start(stage);
    }

    @FXML
    void backToMenu() throws IOException {
        Main.getPlayer1Board().setBoardEmpty();
        Main.getPlayer1Board().generateShips();
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/menu-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private int whoPlays() {
        if (player_VS_player_RB.isSelected()) {
            return 2;
        } else if (player_VS_AI_RB.isSelected()) {
            return 1;
        } else if (AI_VS_AI_RB.isSelected()) {
            return 0;
        } else {
            System.out.println("ERROR: Players not selected");
            return -1;
        }
    }

    private int getDifficulty(int whichAI) {
        if (player_VS_player_RB.isSelected() || (player_VS_AI_RB.isSelected() && whichAI == 2)) {
            return 0;
        }

        if (whichAI == 1) {
            if (easy1_RB.isSelected()) {
                return 1;
            } else if (medium1_RB.isSelected()) {
                return 2;
            } else if (hard1_RB.isSelected()) {
                return 3;
            } else {
                System.out.println("ERROR: Difficulty 1 not set");
                return 0;
            }
        } else if (whichAI == 2) {
            if (easy2_RB.isSelected()) {
                return 1;
            } else if (medium2_RB.isSelected()) {
                return 2;
            } else if (hard2_RB.isSelected()) {
                return 3;
            } else {
                System.out.println("ERROR: Difficulty 2 not set");
                return 0;
            }
        } else {
            System.out.println("ERROR: Wrong function call");
            return 0;
        }
    }

    public void initialize() {
        player_VS_player_RB.setOnAction(actionEvent -> {
            if (player_VS_player_RB.isSelected()) {
                easy1_RB.setDisable(true);
                medium1_RB.setDisable(true);
                hard1_RB.setDisable(true);
                easy2_RB.setDisable(true);
                medium2_RB.setDisable(true);
                hard2_RB.setDisable(true);
            }
        });

        player_VS_AI_RB.setOnAction(actionEvent -> {
            easy1_RB.setDisable(false);
            medium1_RB.setDisable(false);
            hard1_RB.setDisable(false);
            easy2_RB.setDisable(true);
            medium2_RB.setDisable(true);
            hard2_RB.setDisable(true);
        });

        AI_VS_AI_RB.setOnAction(actionEvent -> {
            easy1_RB.setDisable(false);
            medium1_RB.setDisable(false);
            hard1_RB.setDisable(false);
            easy2_RB.setDisable(false);
            medium2_RB.setDisable(false);
            hard2_RB.setDisable(false);
        });
    }
}
