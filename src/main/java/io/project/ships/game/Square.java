package io.project.ships.game;

import io.project.ships.Main;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class Square extends StackPane {

    private Position position;
    private SquareStatus squareStatus;

    public Square(int x, int y) {
        position = new Position(x, y);
        squareStatus = SquareStatus.EMPTY;
        getStyleClass().add("cell");
        setOpacity(1);
        addEventFilter(MouseEvent.MOUSE_CLICKED, e -> mouseClick());
    }

    private void mouseClick() {
        if (Main.isGameStarted()) {
            if (squareStatus == SquareStatus.EMPTY) {
                squareStatus = SquareStatus.MISS;
                Main.setPlayer1Turn(!Main.isPlayer1Turn());
            }
            if (squareStatus == SquareStatus.SHIP) {
                squareStatus = SquareStatus.DAMAGED;
                if (Main.isPlayer1Turn()) {
                    for (int i = 0; i < Main.getEnemy1Board().getShips().length; i++) {
                        for (int j = 0; j < Main.getEnemy1Board().getShips()[i].getSize(); j++) {
                            if (Main.getEnemy1Board().getShips()[i].getPosition()[j].getX() == position.getX() && Main.getEnemy1Board().getShips()[i].getPosition()[j].getY() == position.getY()) {
                                Main.getEnemy1Board().getShips()[i].addDmgCount();
                                if (Main.getEnemy1Board().getShips()[i].getDmgCount() == Main.getEnemy1Board().getShips()[i].getSize()) {
                                    for (int k = 0; k < Main.getEnemy1Board().getShips()[i].getSize(); k++) {
                                        Main.getEnemy1Board().getSquareBoard()[Main.getEnemy1Board().getShips()[i].getPosition()[k].getX()][Main.getEnemy1Board().getShips()[i].getPosition()[k].getY()].setSquareStatus(SquareStatus.DESTROYED);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < Main.getEnemy2Board().getShips().length; i++) {
                        for (int j = 0; j < Main.getEnemy2Board().getShips()[i].getSize(); j++) {
                            if (Main.getEnemy2Board().getShips()[i].getPosition()[j].getX() == position.getX() && Main.getEnemy2Board().getShips()[i].getPosition()[j].getY() == position.getY()) {
                                Main.getEnemy2Board().getShips()[i].addDmgCount();
                                if (Main.getEnemy2Board().getShips()[i].getDmgCount() == Main.getEnemy2Board().getShips()[i].getSize()) {
                                    for (int k = 0; k < Main.getEnemy2Board().getShips()[i].getSize(); k++) {
                                        Main.getEnemy2Board().getSquareBoard()[Main.getEnemy2Board().getShips()[i].getPosition()[k].getX()][Main.getEnemy2Board().getShips()[i].getPosition()[k].getY()].setSquareStatus(SquareStatus.DESTROYED);
                                    }
                                }
                            }
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
