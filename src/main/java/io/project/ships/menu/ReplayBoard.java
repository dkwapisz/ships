package io.project.ships.menu;

import javafx.scene.layout.Pane;

public class ReplayBoard extends Pane {
    private ReplaySquare[][] squareBoard;
    private String boardInString;
    private final int COLUMNS;
    private final int ROWS;
    private final double WIDTH;
    private final double HEIGHT;
    private boolean isEnemyBoard;

    public ReplayBoard(String boardInString, int columns, int rows, double width, double height) {
        squareBoard = new ReplaySquare[rows][columns];
        this.boardInString = boardInString;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.COLUMNS = columns;
        this.ROWS = rows;
        char status;
        int index;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                index = row + columns * column;
                status = this.boardInString.charAt(index);
                this.squareBoard[row][column] = new ReplaySquare(row, column, status);
                addSquare(this.squareBoard[row][column], row, column);
            }
        }
    }

    private void addSquare(ReplaySquare square, int row, int column) {
        squareBoard[row][column] = square;

        double w = WIDTH / COLUMNS;
        double h = HEIGHT / ROWS;
        double x = w * column;
        double y = h * row;
        square.setLayoutX(x);
        square.setLayoutY(y);

        square.setPrefWidth(w);
        square.setPrefHeight(h);
        getChildren().add(square);
    }

    //just for testing sakes
    public void printBoardStatus() {
        String line = "";
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (squareBoard[i][j].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                    line += "O ";
                } else if (squareBoard[i][j].getSquareStatus() == ReplaySquare.SquareStatus.SHIP) {
                    line += "S ";
                } else if (squareBoard[i][j].getSquareStatus() == ReplaySquare.SquareStatus.DAMAGED) {
                    line += "D ";
                } else if (squareBoard[i][j].getSquareStatus() == ReplaySquare.SquareStatus.DESTROYED) {
                    line += "X ";
                } else if (squareBoard[i][j].getSquareStatus() == ReplaySquare.SquareStatus.MISS) {
                    line += "M ";
                }
            }
            line += "\n";
        }
        System.out.println(line);
    }

    public void updateBoard(int row, int column, int whichBoard) {
        if (this.squareBoard[row][column].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
            this.squareBoard[row][column].updateSquareStatus('m', whichBoard);
        } else if (this.squareBoard[row][column].getSquareStatus() == ReplaySquare.SquareStatus.SHIP){
            this.squareBoard[row][column].updateSquareStatus('d', whichBoard);
            if (isDestroyed(row, column)) {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (this.squareBoard[i][j].getChecked()) {
                            this.squareBoard[i][j].setChecked(false);
                            this.squareBoard[i][j].updateSquareStatus('x', whichBoard);
                            if (i != 9) {
                                if (this.squareBoard[i + 1][j].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                    this.squareBoard[i + 1][j].updateSquareStatus('m', whichBoard);
                                }
                            }
                            if (i != 0) {
                                if (this.squareBoard[i - 1][j].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                    this.squareBoard[i - 1][j].updateSquareStatus('m', whichBoard);
                                }
                            }
                            if (j != 9) {
                                if (this.squareBoard[i][j + 1].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                    this.squareBoard[i][j + 1].updateSquareStatus('m', whichBoard);
                                }
                            }
                            if (j != 0) {
                                if (this.squareBoard[i][j - 1].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                    this.squareBoard[i][j - 1].updateSquareStatus('m', whichBoard);
                                }
                            }
                            if (i != 9 && j != 9) {
                                if (this.squareBoard[i + 1][j + 1].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                    this.squareBoard[i + 1][j + 1].updateSquareStatus('m', whichBoard);
                                }
                            }
                            if (i != 0 && j != 0) {
                                if (this.squareBoard[i - 1][j - 1].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                    this.squareBoard[i - 1][j - 1].updateSquareStatus('m', whichBoard);
                                }
                            }
                            if (i != 0 && j != 9) {
                                if (this.squareBoard[i - 1][j + 1].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                    this.squareBoard[i - 1][j + 1].updateSquareStatus('m', whichBoard);
                                }
                            }
                            if (i != 9 && j != 0) {
                                if (this.squareBoard[i + 1][j - 1].getSquareStatus() == ReplaySquare.SquareStatus.EMPTY) {
                                    this.squareBoard[i + 1][j - 1].updateSquareStatus('m', whichBoard);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isDestroyed(int row, int column) {
        squareBoard[row][column].setChecked(true);
        if (row != 9) {
            if (this.squareBoard[row + 1][column].getSquareStatus() == ReplaySquare.SquareStatus.SHIP) {
                this.squareBoard[row + 1][column].setChecked(false);
                this.squareBoard[row][column].setChecked(false);
                return false;
            }
        }
        if (row != 0) {
            if (this.squareBoard[row - 1][column].getSquareStatus() == ReplaySquare.SquareStatus.SHIP) {
                this.squareBoard[row - 1][column].setChecked(false);
                this.squareBoard[row][column].setChecked(false);
                return false;
            }
        }
        if (column != 9) {
            if (this.squareBoard[row][column + 1].getSquareStatus() == ReplaySquare.SquareStatus.SHIP) {
                this.squareBoard[row][column + 1].setChecked(false);
                this.squareBoard[row][column].setChecked(false);
                return false;
            }
        }
        if (column != 0) {
            if (this.squareBoard[row][column - 1].getSquareStatus() == ReplaySquare.SquareStatus.SHIP) {
                this.squareBoard[row][column - 1].setChecked(false);
                this.squareBoard[row][column].setChecked(false);
                return false;
            }
        }
        if (row != 9) {
            if (this.squareBoard[row + 1][column].getSquareStatus() == ReplaySquare.SquareStatus.DAMAGED && !squareBoard[row + 1][column].getChecked()) {
                if (!isDestroyed(row + 1, column)) {
                    this.squareBoard[row + 1][column].setChecked(false);
                    this.squareBoard[row][column].setChecked(false);
                    return false;
                }
            }
        }
        if (row != 0) {
            if (this.squareBoard[row - 1][column].getSquareStatus() == ReplaySquare.SquareStatus.DAMAGED && !squareBoard[row - 1][column].getChecked()) {
                if (!isDestroyed(row - 1, column)) {
                    this.squareBoard[row - 1][column].setChecked(false);
                    this.squareBoard[row][column].setChecked(false);
                    return false;
                }
            }
        }
        if (column != 9) {
            if (this.squareBoard[row][column + 1].getSquareStatus() == ReplaySquare.SquareStatus.DAMAGED && !squareBoard[row][column + 1].getChecked()) {
                if (!isDestroyed(row, column + 1)) {
                    this.squareBoard[row][column + 1].setChecked(false);
                    this.squareBoard[row][column].setChecked(false);
                    return false;
                }
            }
        }
        if (column != 0) {
            if (this.squareBoard[row][column - 1].getSquareStatus() == ReplaySquare.SquareStatus.DAMAGED && !squareBoard[row][column - 1].getChecked()) {
                if (!isDestroyed(row, column - 1)) {
                    this.squareBoard[row][column - 1].setChecked(false);
                    this.squareBoard[row][column].setChecked(false);
                    return false;
                }
            }
        }
        return true;
    }

//    just for testing sakes
    public static void main(String[] args) {
        ReplayBoard board = new ReplayBoard("eeeeeeeeesesesseseeeeseeeeeeeeeseeeeeeeeeeeesssseesseeeeeeeeeeeeeeesseeeseeeeeeeeeeeseessseeeeeeeeee", 10, 10, 400, 400);
        board.printBoardStatus();
        board.updateBoard(4, 4, 1);
        board.printBoardStatus();
        board.updateBoard(7, 4, 1);
        board.printBoardStatus();
        board.updateBoard(5, 4,1 );
        board.printBoardStatus();
        board.updateBoard(6, 4,1 );
        board.printBoardStatus();

    }
}
