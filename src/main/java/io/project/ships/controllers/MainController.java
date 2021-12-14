package io.project.ships.controllers;

import io.project.ships.Main;
import io.project.ships.game.Square;
import io.project.ships.menu.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
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

    @FXML
    private ImageView imageField1;

    @FXML
    private ImageView imageField2;

    private boolean timelineCreated;
    private Timeline updateTimeline;
    private boolean buttonsBlocked;
    private Image image1;
    private Image image2;

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
                pauseButton.setText("▶");
            } else {
                Main.setPause(false);
                //updateTimeline.play();
                pauseButton.setText("⏸");
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
                pauseButton.setText("▶");
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
            updateTimeline = new Timeline(new KeyFrame(Duration.millis(250), e -> {updateLabels();}));
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
            image1 = validateImage(imageField1, Main.getUser1());
        }

        if (Main.getDifficulty2() == 0 && !buttonsBlocked) {
            pauseButton.setVisible(false);
            nextMoveButton.setVisible(false);
            pauseButton.setDisable(true);
            nextMoveButton.setDisable(true);
            image1 = validateImage(imageField1, Main.getUser1());
            image2 = validateImage(imageField2, Main.getUser2());
        }
    }

    private void updateImages(Image image1, Image image2) {
        imageField1.setImage(image1);
        imageField2.setImage(image2);
    }

    private Image validateImage(ImageView imageView, User user) {
        if (user == null) {
            String path = getClass().getResource("/image/image.jpg").toString();
            Image defaultImg = new Image(path);
            imageView.setImage(defaultImg);
            return defaultImg;
        } else {
            Image img = new Image(user.getPath());
            File tempFile = new File(user.getPath().substring(6).replace("%20", " "));
            boolean exists = tempFile.exists();
            if (exists) {
                imageView.setImage(img);
                return img;
            } else {
                String path = getClass().getResource("/image/image.jpg").toString();
                Image defaultImg = new Image(path);
                imageView.setImage(defaultImg);
                return defaultImg;
            }
        }
    }

    private void updateLabels() {
        if (Main.getDifficulty1() == 0) {
            if (Main.isPlayer1Turn()) {
                playerLabel.setText(Main.getUser1().getUsername());
                enemyLabel.setText(Main.getUser2().getUsername());
                updateImages(image1, image2);
            } else {
                playerLabel.setText(Main.getUser2().getUsername());
                enemyLabel.setText(Main.getUser1().getUsername());
                updateImages(image2, image1);
            }
        }
        else if (Main.getDifficulty1() > 0 && Main.getDifficulty2() == 0) {
            updateImages(image1, image2);
            playerLabel.setText(Main.getUser1().getUsername());
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
