package io.project.ships.game;

import io.project.ships.Main;
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
        if (Main.isGameStarted()) {
            if (squareStatus == SquareStatus.EMPTY) {
                squareStatus = SquareStatus.MISS;
                Main.setPlayer1Turn(!Main.isPlayer1Turn());

                //Jeśli nie gramy z AI to trzeba przysłaniać plansze -> wtedy difficulty1 jest ustawione na 0
                if (Main.getDifficulty1() == 0) {
                    Main.hideBoard();
                }
            }
            if (squareStatus == SquareStatus.SHIP) {
                squareStatus = SquareStatus.DAMAGED;
                if (Main.isPlayer1Turn()) {
                    hitShip(Main.getEnemy1Board().getShips(), true);
                } else {
                    hitShip(Main.getEnemy2Board().getShips(), false);
                }
            }
        }
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
