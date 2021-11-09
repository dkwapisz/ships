package io.project.ships.game;

import javafx.scene.layout.Pane;

public class Board extends Pane {

    private Square[][] playerBoard;

    private final int COLUMNS;
    private final int ROWS;
    private final double WIDTH;
    private final double HEIGHT;
    private boolean isEnemyBoard;

    public Board(int columns, int rows, double width, double height, boolean isEnemyBoard) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.COLUMNS = columns;
        this.ROWS = rows;
        this.isEnemyBoard = isEnemyBoard;

        playerBoard = new Square[columns][rows];

        generateBoard();
    }

    private void generateBoard() {
        for (int row = 0; row < 10; row++) {
            for (int column = 0; column < 10; column++) {

                Square square = new Square(column, row);
                addSquare(square, column, row);

            }
        }
    }

    private void addSquare(Square square, int column, int row) {
        playerBoard[column][row] = square;

        double w = WIDTH / COLUMNS;
        double h = HEIGHT / ROWS;
        double x = w * column;
        double y = h * row;
        if (isEnemyBoard) {
            square.setLayoutX(x + WIDTH/10);

        } else {
            square.setLayoutX(x + 2*WIDTH - WIDTH/10);
        }

        square.setLayoutY(y + HEIGHT/10);

        square.setPrefWidth(w);
        square.setPrefHeight(h);
        getChildren().add(square);
    }

}
