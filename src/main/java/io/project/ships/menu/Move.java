package io.project.ships.menu;

public class Move {
    private int moveNumber;
    private int whichboard;
    private int row;
    private int column;

    public Move(int moveNumber, int whichBoard, int row, int column) {
        this.moveNumber = moveNumber;
        this.whichboard = whichBoard;
        this.row = row;
        this.column = column;
    }
}
