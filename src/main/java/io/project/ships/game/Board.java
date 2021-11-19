package io.project.ships.game;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Board extends Pane {

    private Square[][] squareBoard;

    private final int COLUMNS;
    private final int ROWS;
    private final double WIDTH;
    private final double HEIGHT;
    private boolean isEnemyBoard;
    private Ship[] ships = new Ship[10];

    public Board(int columns, int rows, double width, double height, boolean isEnemyBoard) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.COLUMNS = columns;
        this.ROWS = rows;
        this.isEnemyBoard = isEnemyBoard;

        squareBoard = new Square[columns][rows];

        generateBoard();
        generateShips();
    }

    private void generateBoard() {
        for (int row = 0; row < 10; row++) {
            for (int column = 0; column < 10; column++) {

                Square square = new Square(column, row);
                addSquare(square, column, row);

            }
        }
    }

    public void generateShips() {
        if (!isEnemyBoard) {
            for (int i = 0; i < 4; i++) {
                ships[i] = new Ship(1, i, squareBoard);
            }
            for (int i = 4; i < 7; i++) {
                ships[i] = new Ship(2, i-4, squareBoard);
            }
            for (int i = 7; i < 9; i++) {
                ships[i] = new Ship(3, i-7, squareBoard);
            }
            for (int i = 9; i < 10; i++) {
                ships[i] = new Ship(4, i-9, squareBoard);
            }
        }
    }

    public void generateShipsRandom() {
        //First place the biggest ship
        for (int i = 9; i > -1; i--) {
            ships[i].placeInRandom();
        }
    }

    public void markAsMiss(Ship ship, boolean AI, ArrayList<Position> notTakenPos) {
        int x = ship.getPosition()[0].getX();
        int y = ship.getPosition()[0].getY();

        for (int i = -1; i < ship.getSize() + 1; i++) {
            for (int j = -1; j < 2; j++) {
                if ((ship.getShape().getWidth() >= ship.getShape().getHeight()) && (!(x + i < 0) && !(x + i > 9) && !(y + j < 0) && !(y + j > 9))) {
                    if (squareBoard[x + i][y + j].getSquareStatus() == Square.SquareStatus.EMPTY) {
                        squareBoard[x + i][y + j].setSquareStatus(Square.SquareStatus.MISS);
                        if (AI) {
                            removePosition(x + i, y + j, notTakenPos);
                        }
                    }
                }
                else if ((ship.getShape().getWidth() < ship.getShape().getHeight()) && (!(x + j < 0) && !(x + j > 9) && !(y + i < 0) && !(y + i > 9))) {
                    if (squareBoard[x + j][y + i].getSquareStatus() == Square.SquareStatus.EMPTY) {
                        squareBoard[x + j][y + i].setSquareStatus(Square.SquareStatus.MISS);
                        if (AI) {
                            removePosition(x + i, y + j, notTakenPos);
                        }
                    }
                }
            }
        }
    }

    private void removePosition(int x, int y,  ArrayList<Position> notTakenPos) {
        notTakenPos.removeIf(pos -> pos.getX() == x && pos.getY() == y);
    }

    private void addSquare(Square square, int column, int row) {
        squareBoard[column][row] = square;

        double w = WIDTH / COLUMNS;
        double h = HEIGHT / ROWS;
        double x = w * column;
        double y = h * row;
        if (!isEnemyBoard) {
            square.setLayoutX(x + WIDTH/10);

        } else {
            square.setLayoutX(x + 2*WIDTH - WIDTH/10);
        }

        square.setLayoutY(y + HEIGHT/10);

        square.setPrefWidth(w);
        square.setPrefHeight(h);
        getChildren().add(square);
    }

    public void setBoardEmpty() {
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS; j++) {
                squareBoard[i][j].setSquareStatus(Square.SquareStatus.EMPTY);
            }
        }
    }

    public void setEnemyBoardStatus(Board enemyBoard) {
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS; j++) {
                squareBoard[i][j].setSquareStatus(enemyBoard.getSquareBoard()[i][j].getSquareStatus());
            }
        }
        setShips(enemyBoard.getShips());
    }

    public Ship[] getShips() {
        return ships;
    }

    public void setShips(Ship[] ships) {
        this.ships = ships;
    }

    public Square[][] getSquareBoard() {
        return squareBoard;
    }


}
