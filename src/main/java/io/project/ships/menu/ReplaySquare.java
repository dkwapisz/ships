package io.project.ships.menu;

import io.project.ships.game.Position;
import javafx.scene.layout.StackPane;


public class ReplaySquare extends StackPane {

    private Position position;
    private SquareStatus squareStatus;
    private boolean checked;

    public ReplaySquare(int x, int y, char status) {
        this.position = new Position(x, y);
        setSquareStatus(status);
        setChecked(false);
        setOpacity(1);
    }

    public SquareStatus getSquareStatus() {
        return squareStatus;
    }

    public void setSquareStatus(char status) {
        if (status == 'e') {
            this.squareStatus = SquareStatus.EMPTY;
            getStyleClass().add("cell");
        } else if (status == 's') {
            this.squareStatus = SquareStatus.SHIP;
            getStyleClass().add("ship");
        }
    }

    public void updateSquareStatus(char status) {
        if (status == 'm') {
            this.squareStatus = SquareStatus.MISS;
            getStyleClass().add("miss");
        } else if (status == 'd') {
            this.squareStatus = SquareStatus.DAMAGED;
            getStyleClass().add("damaged");
        } else if (status == 'x') {
            this.squareStatus = SquareStatus.DESTROYED;
            getStyleClass().add("destroyed");
        }
    }

    public enum SquareStatus {
        EMPTY,
        MISS,
        SHIP,
        DAMAGED,
        DESTROYED
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean getChecked() {
        return this.checked;
    }
}
