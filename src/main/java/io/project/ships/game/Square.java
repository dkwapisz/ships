package io.project.ships.game;

import io.project.ships.Main;
import io.project.ships.menu.Move;
import io.project.ships.menu.UserStatistics;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class Square extends StackPane {

    private Position position;
    private SquareStatus squareStatus;

    public Square(int x, int y) {
        position = new Position(x, y);
        squareStatus = SquareStatus.EMPTY;
        getStyleClass().add("cell");
        setOpacity(1);
        addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                mouseClick();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void mouseClick() throws IOException {
        if (Main.isGameStarted() && ((Main.isPlayer1Turn() && Main.getDifficulty1() > 0 && Main.getDifficulty2() == 0 && !Main.isTimer1Started()) || (!(Main.getDifficulty1() > 0) && !(Main.getDifficulty2() > 0)))) {
            if (squareStatus == SquareStatus.EMPTY) {
                addGameFloatMove();
                squareStatus = SquareStatus.MISS;
                if (Main.isPlayer1Turn()) {
                    updateStatistics(false, Main.getUser1Statistics());
                } else {
                    updateStatistics(false, Main.getUser2Statistics());
                }
                if (Main.getDifficulty1() == 0 || (Main.getDifficulty1() > 0)) {
                    Main.setPlayer1Turn(!Main.isPlayer1Turn());
                }

                //Jeśli nie gramy z AI to trzeba przysłaniać plansze -> wtedy difficulty1 jest ustawione na 0
                if (Main.getDifficulty1() == 0) {
                    Main.hideBoard();
                }
            }
            if (squareStatus == SquareStatus.SHIP) {
                addGameFloatMove();
                squareStatus = SquareStatus.DAMAGED;
                if (Main.isPlayer1Turn()) {
                    updateStatistics(true, Main.getUser1Statistics());
                } else {
                    updateStatistics(true, Main.getUser2Statistics());
                }
                if (Main.isPlayer1Turn()) {
                    hitShip(Main.getEnemy1Board().getShips(), true);
                } else {
                    hitShip(Main.getEnemy2Board().getShips(), false);
                }
            }

        }
    }

    private void updateStatistics(boolean onTarget, UserStatistics stats) {
        if (onTarget) {
            stats.updateShots();
            stats.updateOnTarget();
            stats.updateAccuracy();
        } else {
            stats.updateShots();
            stats.updateAccuracy();
        }
    }

    private void addGameFloatMove() {
        int whichBoard;
        if (Main.isPlayer1Turn()) {
            whichBoard = 1;
        } else {
            whichBoard = 2;
        }
        Move move = new Move(Main.getMoveNumber(), whichBoard, position.getY(), position.getX());
        Main.getGameFlow().addMove(move);
        Main.moveNumberIncrement();
    }

    private void hitShip(Ship[] ships, boolean enemy1Board) {
        for (int i = 0; i < ships.length; i++) {
            for (int j = 0; j < ships[i].getSize(); j++) {
                if (ships[i].getPosition()[j].getX() == position.getX() && ships[i].getPosition()[j].getY() == position.getY()) {
                    ships[i].addDmgCount();
                    if (ships[i].getDmgCount() == ships[i].getSize()) {
                        for (int k = 0; k < ships[i].getSize(); k++) {
                            if (enemy1Board) {
                                Main.getEnemy1Board().getSquareBoard()[ships[i].getPosition()[k].getX()][ships[i].getPosition()[k].getY()].setSquareStatus(SquareStatus.DESTROYED);
                            } else {
                                Main.getEnemy2Board().getSquareBoard()[ships[i].getPosition()[k].getX()][ships[i].getPosition()[k].getY()].setSquareStatus(SquareStatus.DESTROYED);
                            }
                        }
                        if (enemy1Board) {
                            Main.getEnemy1Board().markAsMiss(ships[i], false, null);
                        } else {
                            Main.getEnemy2Board().markAsMiss(ships[i], false, null);
                        }
                    }
                }
            }
        }
    }

    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position) {
        this.position = position;
    }

    public SquareStatus getSquareStatus() {
        return squareStatus;
    }
    public void setSquareStatus(SquareStatus squareStatus) {
        this.squareStatus = squareStatus;
    }

    public enum SquareStatus {
        EMPTY,
        MISS,
        SHIP,
        DAMAGED,
        DESTROYED
    }
}
