package io.project.ships.menu;

import io.project.ships.Main;
import io.project.ships.game.Position;
import io.project.ships.game.Square;

public class ReplayBoard {
    private ReplaySquare[][] square;
    private String boardInString;

    public ReplayBoard(String boardInString) {
        square = new ReplaySquare[10][10];
        this.boardInString = boardInString;
        char status;
        int index;
        for (int row = 0; row < 10; row++) {
            for (int column = 0; column < 10; column++) {
                index = row + 10 * column;
                status = this.boardInString.charAt(index);
                this.square[row][column] = new ReplaySquare(row, column, status);
            }
        }
    }

    //just for testing sakes
    public void printBoardStatus() {
        String line = "";
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (square[i][j].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                    line += "O ";
                } else if (square[i][j].getSquareStatus() == ReplaySquare.SquareStatus.SHIP) {
                    line += "S ";
                } else if (square[i][j].getSquareStatus() == ReplaySquare.SquareStatus.DAMAGED) {
                    line += "D ";
                } else if (square[i][j].getSquareStatus() == ReplaySquare.SquareStatus.DESTROYED) {
                    line += "X ";
                } else if (square[i][j].getSquareStatus() == ReplaySquare.SquareStatus.MISS) {
                    line += "M ";
                }
            }
            line += "\n";
        }
        System.out.println(line);
    }

    public void updateBoard(int row, int column) {
        if (this.square[row][column].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
            this.square[row][column].updateSquareStatus('m');
        } else {
            this.square[row][column].updateSquareStatus('d');
            if (isDestroyed(row, column)) {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (this.square[i][j].getChecked()) {
                            this.square[i][j].setChecked(false);
                            this.square[i][j].updateSquareStatus('x');
                            if (this.square[i + 1][j].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                this.square[i + 1][j].updateSquareStatus('m');
                            }
                            if (this.square[i - 1][j].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                this.square[i - 1][j].updateSquareStatus('m');
                            }
                            if (this.square[i][j + 1].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                this.square[i][j + 1].updateSquareStatus('m');
                            }
                            if (this.square[i][j - 1].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                this.square[i][j - 1].updateSquareStatus('m');
                            }
                            if (this.square[i + 1][j + 1].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                this.square[i + 1][j + 1].updateSquareStatus('m');
                            }
                            if (this.square[i - 1][j - 1].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                this.square[i - 1][j - 1].updateSquareStatus('m');
                            }
                            if (this.square[i - 1][j + 1].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                this.square[i - 1][j + 1].updateSquareStatus('m');
                            }
                            if (this.square[i + 1][j - 1].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                this.square[i + 1][j - 1].updateSquareStatus('m');
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isDestroyed(int row, int column) {
        square[row][column].setChecked(true);
        if (this.square[row + 1][column].getSquareStatus() == ReplaySquare.SquareStatus.SHIP) {
            this.square[row + 1][column].setChecked(false);
            return false;
        } else if (this.square[row - 1][column].getSquareStatus() == ReplaySquare.SquareStatus.SHIP) {
            this.square[row - 1][column].setChecked(false);
            return false;
        } else if (this.square[row][column + 1].getSquareStatus() == ReplaySquare.SquareStatus.SHIP) {
            this.square[row][column + 1].setChecked(false);
            return false;
        } else if (this.square[row][column - 1].getSquareStatus() == ReplaySquare.SquareStatus.SHIP) {
            this.square[row][column - 1].setChecked(false);
            return false;
        }
        if (this.square[row + 1][column].getSquareStatus() == ReplaySquare.SquareStatus.DAMAGED && !square[row + 1][column].getChecked()) {
            if (!isDestroyed(row + 1, column)) {
                this.square[row + 1][column].setChecked(false);
                return false;
            }
        }
        if (this.square[row - 1][column].getSquareStatus() == ReplaySquare.SquareStatus.DAMAGED && !square[row - 1][column].getChecked()) {
            if (!isDestroyed(row - 1, column)) {
                this.square[row - 1][column].setChecked(false);
                return false;
            }
        }
        if (this.square[row][column + 1].getSquareStatus() == ReplaySquare.SquareStatus.DAMAGED && !square[row][column + 1].getChecked()) {
            if (!isDestroyed(row, column + 1)) {
                this.square[row][column + 1].setChecked(false);
                return false;
            }
        }
        if (this.square[row][column - 1].getSquareStatus() == ReplaySquare.SquareStatus.DAMAGED && !square[row][column - 1].getChecked()) {
            if (!isDestroyed(row, column - 1)) {
                this.square[row][column - 1].setChecked(false);
                return false;
            }
        }
        return true;
    }

//    just for testing sakes
    public static void main(String[] args) {
        ReplayBoard board = new ReplayBoard("eeeeeeeeesesesseseeeeseeeeeeeeeseeeeeeeeeeeesssseesseeeeeeeeeeeeeeesseeeseeeeeeeeeeeseessseeeeeeeeee");
        board.printBoardStatus();
        board.updateBoard(4, 4);
        board.printBoardStatus();
        board.updateBoard(7, 4);
        board.printBoardStatus();
        board.updateBoard(5, 4);
        board.printBoardStatus();
        board.updateBoard(6, 4);
        board.printBoardStatus();

    }
}
