package io.project.ships.game;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ship {

    private int size;
    private Rectangle shape;

    public Ship(int size, int whichShip, Square[][] board) {
        makeShape(size, whichShip);
        this.size = size;
        shape.setFill(Color.BLUE);
        setMouseListener(board);
    }

    private void makeShape(int size, int whichShip) {
        if (size == 1) {
            shape = new Rectangle(40 + whichShip*80, 460, 40, 40);
        } else if (size == 2) {
            shape = new Rectangle(40 + whichShip*120, 540, 80, 40);
        } else if (size == 3) {
            shape = new Rectangle(40 + whichShip*160, 620, 120, 40);
        } else if (size == 4) {
            shape = new Rectangle(40 + whichShip*200, 700, 160, 40);
        }
    }

    private void setMouseListener(Square[][] board) {
        shape.setOnMouseDragged(event -> {
            shape.setX(event.getSceneX() - shape.getWidth()/2);
            shape.setY(event.getSceneY() - shape.getHeight()/2);


            //TODO TO DZIAŁA ŹLE

//            for (int i = 0; i < board.length; i++) {
//                for (int j = 0; j < board[i].length; j++) {
//
//                    Rectangle boardRectangle = new Rectangle(board[i][j].getLayoutX(), board[i][j].getLayoutY(), board[i][j].getWidth(), board[i][j].getHeight());
//
//                    if (shape.intersects(boardRectangle.getBoundsInParent())) {
//                        board[i][j].getStyleClass().add("cell-highlight");
//                    } else {
//                        board[i][j].getStyleClass().remove("cell-highlight");
//                        board[i][j].getStyleClass().add("cell");
//                    }
//                }
//            }
        });

        shape.setOnMouseReleased(event -> {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {

                    Rectangle squareRectangle = new Rectangle(board[i][j].getLayoutX(), board[i][j].getLayoutY(), board[i][j].getWidth(), board[i][j].getHeight());
                    if (shape.intersects(squareRectangle.getLayoutBounds()) && verifyOutOfBoard(board)) {

                        shape.setX(calculateLowestDist(board).getLayoutX());
                        shape.setY(calculateLowestDist(board).getLayoutY());

                        int xPos = (int) calculateLowestDist(board).getLayoutX()/40 - 1;
                        int yPos = (int) calculateLowestDist(board).getLayoutY()/40 - 1;

                        setPositionOnBoard(board, xPos, yPos);

                    }
                }
            }
        });

        shape.setOnMouseClicked(event -> {
            if (event.isPopupTrigger()) {
                double tmp = shape.getHeight();
                shape.setHeight(shape.getWidth());
                shape.setWidth(tmp);
            }
        });
    }

    private boolean verifyOutOfBoard(Square[][] board) {
        Rectangle boardRectangle = new Rectangle(board[0][0].getLayoutX(), board[0][0].getLayoutY(), board[0][0].getWidth()*board.length, board[0][0].getHeight()*board.length);
        Point2D vertex1 = new Point2D(shape.getX(), shape.getY());
        Point2D vertex2 = new Point2D(shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight());

        return boardRectangle.contains(vertex1) && boardRectangle.contains(vertex2);
    }


    //TODO Funkcja do sprawdzania, czy statki się "stykają"
    private boolean verifyShipsTouch(Square[][] board) {

        return false;
    }

    private Square calculateLowestDist(Square[][] board) {
        double lastDist = Integer.MAX_VALUE;
        double dist;
        int closestI = 0;
        int closestJ = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                //TODO poprawić działanie tej funkcji
                Rectangle boardRectangle = new Rectangle(board[i][j].getLayoutX(), board[i][j].getLayoutY(), board[i][j].getWidth(), board[i][j].getHeight());
                dist = Math.hypot(Math.abs(shape.getX() - boardRectangle.getX()), Math.abs(shape.getY() - boardRectangle.getY()));
                if (lastDist > dist) {
                    closestI = i;
                    closestJ = j;
                    lastDist = dist;
                }
            }
        }
        return board[closestI][closestJ];
    }

    private void setPositionOnBoard(Square[][] board, int i, int j) {
        if (size == 1) {
            board[i][j].setSquareStatus(Square.SquareStatus.SHIP);
        } else if (size == 2) {
            if (shape.getWidth() > shape.getHeight()) {
                board[i][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i+1][j].setSquareStatus(Square.SquareStatus.SHIP);
            } else if (shape.getWidth() < shape.getHeight()) {
                board[i][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i][j+1].setSquareStatus(Square.SquareStatus.SHIP);
            }
        } else if (size == 3) {
            if (shape.getWidth() > shape.getHeight()) {
                board[i][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i+1][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i+2][j].setSquareStatus(Square.SquareStatus.SHIP);
            } else if (shape.getWidth() < shape.getHeight()) {
                board[i][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i][j+1].setSquareStatus(Square.SquareStatus.SHIP);
                board[i][j+2].setSquareStatus(Square.SquareStatus.SHIP);
            }
        } else if (size == 4) {
            if (shape.getWidth() > shape.getHeight()) {
                board[i][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i+1][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i+2][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i+3][j].setSquareStatus(Square.SquareStatus.SHIP);
            } else if (shape.getWidth() < shape.getHeight()) {
                board[i][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i][j+1].setSquareStatus(Square.SquareStatus.SHIP);
                board[i][j+2].setSquareStatus(Square.SquareStatus.SHIP);
                board[i][j+3].setSquareStatus(Square.SquareStatus.SHIP);
            }
        }
    }

    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

    public Rectangle getShape() {
        return shape;
    }
    public void setShape(Rectangle shape) {
        this.shape = shape;
    }

}