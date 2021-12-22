package io.project.ships.menu;

import io.project.ships.game.Position;
import io.project.ships.game.Square;


public class ReplaySquare {

    private Position position;
    private SquareStatus squareStatus;
    private boolean checked;

    public ReplaySquare(int x, int y, char status) {
        this.position = new Position(x, y);
        setSquareStatus(status);
        setChecked(false);
    }

    public SquareStatus getSquareStatus() {
        return squareStatus;
    }

    public void setSquareStatus(char status) {
        if (status == 'e') {
            this.squareStatus = SquareStatus.EMPTY;
        } else if (status == 's') {
            this.squareStatus = SquareStatus.SHIP;
        }
    }

    public void updateSquareStatus(char status) {
        if (status == 'm') {
            this.squareStatus = SquareStatus.MISS;
        } else if (status == 'd') {
            this.squareStatus = SquareStatus.DAMAGED;
        } else if (status == 'x') {
            this.squareStatus = SquareStatus.DESTROYED;
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
