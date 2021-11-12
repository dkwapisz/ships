package io.project.ships.game;

import javafx.scene.layout.Pane;

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

    private void generateShips() {
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

    public Ship[] getShips() {
        return ships;
    }

    public Square[][] getSquareBoard() {
        return squareBoard;
    }
}
