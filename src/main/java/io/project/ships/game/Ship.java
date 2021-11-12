package io.project.ships.game;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Ship {

    private int size;
    private Rectangle shape;
    private Square[][] board;
    private Position[] position;

    public Ship(int size, int whichShip, Square[][] board) {
        this.board = board;
        this.size = size;
        position = new Position[size];
        setStartPositions();
        makeShape(size, whichShip);
        shape.setFill(Color.BLUE);
        shape.setOpacity(0.6);
        setMouseListener();
    }

    private void setStartPositions() {
        for (int i = 0; i < size; i++) {
            position[i] = new Position(-1, -1);
        }
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

    private void setMouseListener() {
        shape.setOnMouseDragged(event -> {
            shape.setX(event.getSceneX() - shape.getWidth()/2);
            shape.setY(event.getSceneY() - shape.getHeight()/2);

            //TODO TO DZIAŁA ŹLE (DODATKOWA FUNKCJA)
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
            shape.toFront();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {

                    Rectangle squareRectangle = new Rectangle(board[i][j].getLayoutX(), board[i][j].getLayoutY(), board[i][j].getWidth(), board[i][j].getHeight());
                    if (shape.intersects(squareRectangle.getLayoutBounds()) && verifyOutOfBoard()) {

                        int xPos = (int) calculateLowestDist().getLayoutX()/40 - 1;
                        int yPos = (int) calculateLowestDist().getLayoutY()/40 - 1;

                        if (position[0].getX() >= 0) {
                            clearLastPos();
                        }

                        if (verifyShipsTouch(xPos, yPos)) {
                            shape.setX(calculateLowestDist().getLayoutX());
                            shape.setY(calculateLowestDist().getLayoutY());

                            setPositionOnBoard(xPos, yPos);
                        }
                    }
                }
            }
        });

        //TODO Zrobić, żeby nie można było obracać statków jeśli wystąpi kolizja z innym statkiem
        shape.setOnMouseClicked(event -> {
            if (event.isPopupTrigger()) {
                double tmp = shape.getHeight();
                shape.setHeight(shape.getWidth());
                shape.setWidth(tmp);
            }
        });
    }

    private boolean verifyOutOfBoard() {
        Rectangle boardRectangle = new Rectangle(board[0][0].getLayoutX(), board[0][0].getLayoutY(), board[0][0].getWidth()*board.length, board[0][0].getHeight()*board.length);
        Point2D vertex1 = new Point2D(shape.getX(), shape.getY());
        Point2D vertex2 = new Point2D(shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight());

        return boardRectangle.contains(vertex1) && boardRectangle.contains(vertex2);
    }

    private boolean verifyShipsTouch(int shipPosX, int shipPosY) {
        boolean correctPlace = true;

        try {
            if (size == 1) {
                if (board[shipPosX][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                        ||  board[shipPosX][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                        ||  board[shipPosX][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                        ||  board[shipPosX + 1][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                        ||  board[shipPosX + 1][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                        ||  board[shipPosX + 1][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                        ||  board[shipPosX - 1][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                        ||  board[shipPosX - 1][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                        ||  board[shipPosX - 1][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP) {
                    correctPlace = false;
                }
            } else if (size == 2) {
                if (shape.getWidth() > shape.getHeight()) {
                    if (board[shipPosX][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 2][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 2][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 2][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP) {
                        correctPlace = false;
                    }
                } else if (shape.getWidth() < shape.getHeight()) {
                    if (board[shipPosX][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY + 2].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY + 2].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY + 2].getSquareStatus() == Square.SquareStatus.SHIP) {
                        correctPlace = false;
                    }
                }
            } else if (size == 3) {
                if (shape.getWidth() > shape.getHeight()) {
                    if (board[shipPosX][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 2][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 2][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 2][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 3][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 3][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 3][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP) {
                        correctPlace = false;
                    }
                } else if (shape.getWidth() < shape.getHeight()) {
                    if (board[shipPosX][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY + 2].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY + 3].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY + 2].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY + 3].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY + 2].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY + 3].getSquareStatus() == Square.SquareStatus.SHIP) {
                        correctPlace = false;
                    }
                }
            } else if (size == 4) {
                if (shape.getWidth() > shape.getHeight()) {
                    if (board[shipPosX][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 2][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 2][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 2][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 3][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 3][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 3][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 4][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 4][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 4][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP) {
                        correctPlace = false;
                    }
                } else if (shape.getWidth() < shape.getHeight()) {
                    if (board[shipPosX][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY - 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY + 1].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY + 2].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY + 2].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY + 2].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY + 3].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY + 3].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY + 3].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX - 1][shipPosY + 4].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX][shipPosY + 4].getSquareStatus() == Square.SquareStatus.SHIP
                            || board[shipPosX + 1][shipPosY + 4].getSquareStatus() == Square.SquareStatus.SHIP) {
                        correctPlace = false;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}

        return correctPlace;
    }

    private Square calculateLowestDist() {
        double lastDist = Integer.MAX_VALUE;
        double dist;
        int closestI = 0;
        int closestJ = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
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

    private void setPositionOnBoard(int i, int j) {
        if (size == 1) {
            board[i][j].setSquareStatus(Square.SquareStatus.SHIP);
            position[0].setX(i);
            position[0].setY(j);
        } else if (size == 2) {
            if (shape.getWidth() > shape.getHeight()) {
                board[i][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i+1][j].setSquareStatus(Square.SquareStatus.SHIP);
                position[0].setX(i);
                position[0].setY(j);
                position[1].setX(i+1);
                position[1].setY(j);
            } else if (shape.getWidth() < shape.getHeight()) {
                board[i][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i][j+1].setSquareStatus(Square.SquareStatus.SHIP);
                position[0].setX(i);
                position[0].setY(j);
                position[1].setX(i);
                position[1].setY(j+1);
            }
        } else if (size == 3) {
            if (shape.getWidth() > shape.getHeight()) {
                board[i][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i+1][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i+2][j].setSquareStatus(Square.SquareStatus.SHIP);
                position[0].setX(i);
                position[0].setY(j);
                position[1].setX(i+1);
                position[1].setY(j);
                position[2].setX(i+2);
                position[2].setY(j);
            } else if (shape.getWidth() < shape.getHeight()) {
                board[i][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i][j+1].setSquareStatus(Square.SquareStatus.SHIP);
                board[i][j+2].setSquareStatus(Square.SquareStatus.SHIP);
                position[0].setX(i);
                position[0].setY(j);
                position[1].setX(i);
                position[1].setY(j+1);
                position[2].setX(i);
                position[2].setY(j+2);
            }
        } else if (size == 4) {
            if (shape.getWidth() > shape.getHeight()) {
                board[i][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i+1][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i+2][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i+3][j].setSquareStatus(Square.SquareStatus.SHIP);
                position[0].setX(i);
                position[0].setY(j);
                position[1].setX(i+1);
                position[1].setY(j);
                position[2].setX(i+2);
                position[2].setY(j);
                position[3].setX(i+3);
                position[3].setY(j);
            } else if (shape.getWidth() < shape.getHeight()) {
                board[i][j].setSquareStatus(Square.SquareStatus.SHIP);
                board[i][j+1].setSquareStatus(Square.SquareStatus.SHIP);
                board[i][j+2].setSquareStatus(Square.SquareStatus.SHIP);
                board[i][j+3].setSquareStatus(Square.SquareStatus.SHIP);
                position[0].setX(i);
                position[0].setY(j);
                position[1].setX(i);
                position[1].setY(j+1);
                position[2].setX(i);
                position[2].setY(j+2);
                position[3].setX(i);
                position[3].setY(j+3);
            }
        }
    }

    private void clearLastPos() {
        for (int i = 0; i < size; i++) {
            board[position[i].getX()][position[i].getY()].setSquareStatus(Square.SquareStatus.EMPTY);
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