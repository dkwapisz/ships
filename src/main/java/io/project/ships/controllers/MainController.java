package io.project.ships.controllers;

import io.project.ships.Main;
import io.project.ships.game.Square;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MainController {

    @FXML
    private Pane mainPane;

    @FXML
    private Pane playerBoard;

    @FXML
    private Pane enemyBoard;

    @FXML
    private Button randomButton;

    @FXML
    private Button imReadyButton;

    @FXML
    private Label playerLabel;

    @FXML
    private Label enemyLabel;

    @FXML
    private Button pauseButton;

    @FXML
    private Button nextMoveButton;

    private boolean timelineCreated;
    private Timeline updateTimeline;
    private boolean buttonsBlocked;

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
                randomButton.setVisible(false);
                imReadyButton.setVisible(false);
                randomButton.setDisable(true);
                imReadyButton.setDisable(true);
            } else {
                System.out.println("Put all ships");
            }
        }

        if (Main.isPlayer1SetShips() && Main.getDifficulty1() > 0) {
            randomButton.setVisible(false);
            imReadyButton.setVisible(false);
            randomButton.setDisable(true);
            imReadyButton.setDisable(true);
        }
    }

    @FXML
    void backToMenu() throws IOException {
        updateTimeline.stop();
        Main.restartGame();
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

    @FXML
    void playPause() {
        if (Main.getDifficulty1() > 0 && Main.getDifficulty2() > 0) {
            if (Main.isNotPaused()) {
                Main.setPause(true);
                //updateTimeline.pause();
                pauseButton.setText("Play");
            } else {
                Main.setPause(false);
                //updateTimeline.play();
                pauseButton.setText("Pause");
            }
        }
    }

    @FXML
    void nextMove() {
        if (Main.getDifficulty1() > 0 && Main.getDifficulty2() > 0) {
            boolean player1Turn = Main.isPlayer1Turn();

            if (Main.isNotPaused()) {
                Main.setPause(true);

                //TODO Poprawne wyświetlanie podpisów plansz podczas odtwarzania rozgrywki Step By Step
                //Chwilowo updateTimeline będzie pauzowany podczas przewijania rozgrywki Step By Step
                updateTimeline.pause();
                pauseButton.setText("Play");
            }

            Main.stepByStep(player1Turn);
        }
    }

    // dev function
    @FXML
    void printBoardStatus() {
        String line = "";

        if (Main.isPlayer1Turn()) {
            System.out.println("- - - - - - - - - -");
            System.out.println("PLAYER1 BOARD:");
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 10; i++) {
                    if (Main.getPlayer1Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.EMPTY) {
                        line += "0 ";
                    } else if (Main.getPlayer1Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.SHIP) {
                        line += "S ";
                    } else if (Main.getPlayer1Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.DAMAGED) {
                        line += "X ";
                    } else if (Main.getPlayer1Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.DESTROYED) {
                        line += "D ";
                    } else if (Main.getPlayer1Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.MISS) {
                        line += "M ";
                    }
                }
                line += "\n";
            }
            System.out.println(line);
            line = "";
            System.out.println("- - - - - - - - - -");
            System.out.println("ENEMY1 BOARD:");
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 10; i++) {
                    if (Main.getEnemy1Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.EMPTY) {
                        line += "0 ";
                    } else if (Main.getEnemy1Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.SHIP) {
                        line += "S ";
                    } else if (Main.getEnemy1Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.DAMAGED) {
                        line += "X ";
                    }  else if (Main.getEnemy1Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.DESTROYED) {
                        line += "D ";
                    } else if (Main.getEnemy1Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.MISS) {
                        line += "M ";
                    }
                }
                line += "\n";
            }
            System.out.println(line);
            line = "";
        } else {
            System.out.println("- - - - - - - - - -");
            System.out.println("PLAYER2 BOARD:");
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 10; i++) {
                    if (Main.getPlayer2Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.EMPTY) {
                        line += "0 ";
                    } else if (Main.getPlayer2Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.SHIP) {
                        line += "S ";
                    } else if (Main.getPlayer2Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.DAMAGED) {
                        line += "X ";
                    } else if (Main.getPlayer2Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.DESTROYED) {
                        line += "D ";
                    } else if (Main.getPlayer2Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.MISS) {
                        line += "M ";
                    }
                }
                line += "\n";
            }
            System.out.println(line);
            line = "";
            System.out.println("- - - - - - - - - -");
            System.out.println("ENEMY2 BOARD:");
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 10; i++) {
                    if (Main.getEnemy2Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.EMPTY) {
                        line += "0 ";
                    } else if (Main.getEnemy2Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.SHIP) {
                        line += "S ";
                    } else if (Main.getEnemy2Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.DAMAGED) {
                        line += "X ";
                    } else if (Main.getEnemy2Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.DESTROYED) {
                        line += "D ";
                    } else if (Main.getEnemy2Board().getSquareBoard()[i][j].getSquareStatus() == Square.SquareStatus.MISS) {
                        line += "M ";
                    }
                }
                line += "\n";
            }
            System.out.println(line);
            line = "";
        }
        String siema = new String(Main.getEnemy1Board().boardToString());
        System.out.println(siema);
    }

    public void initialize() {
        if (!timelineCreated) {
            updateTimeline = new Timeline(new KeyFrame(Duration.millis(25), e -> {updateLabels();}));
            updateTimeline.setCycleCount(Timeline.INDEFINITE);
            updateTimeline.play();
            timelineCreated = true;
        }

        //AI vs AI -> blocking buttons "Im ready" and "Random"
        if (Main.getDifficulty2() > 0 && !buttonsBlocked) {
            randomButton.setVisible(false);
            imReadyButton.setVisible(false);
            randomButton.setDisable(true);
            imReadyButton.setDisable(true);
            buttonsBlocked = true;
        }

        if (Main.getDifficulty2() == 0 && !buttonsBlocked) {
            pauseButton.setVisible(false);
            nextMoveButton.setVisible(false);
            pauseButton.setDisable(true);
            nextMoveButton.setDisable(true);
        }
    }

    private void updateLabels() {
        if (Main.getDifficulty1() == 0) {
            if (Main.isPlayer1Turn()) {
                playerLabel.setText("PLAYER 1 BOARD");
            } else {
                playerLabel.setText("PLAYER 2 BOARD");
            }
        }
        else if (Main.getDifficulty1() > 0 && Main.getDifficulty2() == 0) {
            playerLabel.setText("PLAYER 1 BOARD");
            if (Main.getDifficulty1() == 1) {
                enemyLabel.setText("AI BOARD ☆");
            }
            else if (Main.getDifficulty1() == 2) {
                enemyLabel.setText("AI BOARD ☆☆");
            }
            else if (Main.getDifficulty1() == 3) {
                enemyLabel.setText("AI BOARD ☆☆☆");
            }
        }
        else if (Main.getDifficulty2() > 0) {

            if (!Main.isPlayer1Turn()) {
                if (Main.getDifficulty1() == 1) {
                    playerLabel.setText("AI 1 BOARD ☆");
                }
                else if (Main.getDifficulty1() == 2) {
                    playerLabel.setText("AI 1 BOARD ☆☆");
                }
                else if (Main.getDifficulty1() == 3) {
                    playerLabel.setText("AI 1 BOARD ☆☆☆");
                }
                if (Main.getDifficulty2() == 1) {
                    enemyLabel.setText("AI 2 BOARD ☆");
                }
                else if (Main.getDifficulty2() == 2) {
                    enemyLabel.setText("AI 2 BOARD ☆☆");
                }
                else if (Main.getDifficulty2() == 3) {
                    enemyLabel.setText("AI 2 BOARD ☆☆☆");
                }
            } else {
                if (Main.getDifficulty1() == 1) {
                    enemyLabel.setText("AI 1 BOARD ☆");
                }
                else if (Main.getDifficulty1() == 2) {
                    enemyLabel.setText("AI 1 BOARD ☆☆");
                }
                else if (Main.getDifficulty1() == 3) {
                    enemyLabel.setText("AI 1 BOARD ☆☆☆");
                }
                if (Main.getDifficulty2() == 1) {
                    playerLabel.setText("AI 2 BOARD ☆");
                }
                else if (Main.getDifficulty2() == 2) {
                    playerLabel.setText("AI 2 BOARD ☆☆");
                }
                else if (Main.getDifficulty2() == 3) {
                    playerLabel.setText("AI 2 BOARD ☆☆☆");
                }
            }
        }
    }
}
