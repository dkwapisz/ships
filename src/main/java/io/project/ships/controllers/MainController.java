package io.project.ships.controllers;


import io.project.ships.Main;
import io.project.ships.game.Square;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Pane mainPane;

    @FXML
    private Pane playerBoard;

    @FXML
    private Pane enemyBoard;

    @FXML
    void setShip() {
        int countShips = 0;
        if (Main.isPlayer1Turn()) {
            for (int i = 0; i < Main.getPlayer1Board().getSquareBoard().length; i++) {
                for (int j = 0; j < Main.getPlayer1Board().getSquareBoard()[i].length; j++) {
                    if (Main.getPlayer1Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.SHIP) {
                        countShips++;
                    }
                }
            }
            //All ships -> 20 squares
            if (countShips == 20) {
                Main.setPlayer1SetShips(true);
            } else {
                System.out.println("Put all ships");
            }
        } else {
            for (int i = 0; i < Main.getPlayer2Board().getSquareBoard().length; i++) {
                for (int j = 0; j < Main.getPlayer2Board().getSquareBoard()[i].length; j++) {
                    if (Main.getPlayer2Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.SHIP) {
                        countShips++;
                    }
                }
            }
            //All ships -> 20 squares
            if (countShips == 20) {
                Main.setPlayer2SetShips(true);
            } else {
                System.out.println("Put all ships");
            }
        }
    }

    @FXML
    void backToMenu() throws IOException {
        Main.getPlayer1Board().setBoardEmpty();
        Main.getPlayer1Board().generateShips();
        Stage stage = (Stage) mainPane.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/menu-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void placeShipsRandom() {
        if (Main.isPlayer1Turn()) {
            Main.getPlayer1Board().setBoardEmpty();
            Main.getPlayer1Board().generateShipsRandom();
        } else {
            Main.getPlayer2Board().setBoardEmpty();
            Main.getPlayer2Board().generateShipsRandom();
        }
    }

    // dev function
    @FXML
    void printBoardStatus() {
        String line = "";

        if (Main.isPlayer1Turn()) {
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 10; i++) {
                    if (Main.getPlayer1Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.EMPTY) {
                        line += "0 ";
                    } else if (Main.getPlayer1Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.SHIP) {
                        line += "S ";
                    }
                }
                line += "\n";
            }
        } else {
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 10; i++) {
                    if (Main.getPlayer2Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.EMPTY) {
                        line += "0 ";
                    } else if (Main.getPlayer2Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.SHIP) {
                        line += "S ";
                    }
                }
                line += "\n";
            }
        }


        System.out.println(line);
    }

}
