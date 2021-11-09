package io.project.ships.game;

import javafx.scene.layout.StackPane;

public class Square extends StackPane {

    private Position position;
    private SquareStatus squareStatus;

    public Square(int x, int y) {
        position = new Position(x, y);
        squareStatus = SquareStatus.EMPTY;
        getStyleClass().add("cell");
        setOpacity(1);
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

    private enum SquareStatus {
        EMPTY,
        MISS,
        SHIP,
        DAMAGED,
        DESTROYED
    }
}
