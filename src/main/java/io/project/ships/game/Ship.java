package io.project.ships.game;

import io.project.ships.Main;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Ship {

    private int size;
    private Rectangle shape;
    private Square[][] board;
    private Position[] position;
    private Random random = new Random();
    private int dmgCount;
    private boolean destroyed;

    public Ship(int size, int whichShip, Square[][] board) {
        this.board = board;
        this.size = size;
        position = new Position[size];
        setStartPositions();
        makeShape(size, whichShip);
        shape.setFill(Color.BLUE);
        shape.setOpacity(0.4);
        setMouseListener();
    }

    private void setStartPositions() {
        for (int i = 0; i < size; i++) {
            position[i] = new Position(-1, -1);
        }
    }

    private void makeShape(int size, int whichShip) {
        if (size == 1) {
            shape = new Rectangle(100 + whichShip*60, 520, 40, 40);
        } else if (size == 2) {
            shape = new Rectangle(100 + whichShip*100, 580, 80, 40);
        } else if (size == 3) {
            shape = new Rectangle(100 + whichShip*140, 640, 120, 40);
        } else if (size == 4) {
            shape = new Rectangle(100 + whichShip*180, 700, 160, 40);
        }
    }

    private void setMouseListener() {

        shape.setOnMouseDragged(event -> {
            if (!Main.isGameStarted()) {
                shape.setX(event.getSceneX() - shape.getWidth() / 2);
                shape.setY(event.getSceneY() - shape.getHeight() / 2);
            }
        });

        shape.setOnMouseReleased(event -> {
            if (!Main.isGameStarted() && event.getButton() == MouseButton.PRIMARY) {
                placeShip();
            }
        });

        shape.setOnMouseClicked(event -> {
            if (!Main.isGameStarted()) {
                if (event.isPopupTrigger()) {
                    rotateShip(false);
                }
            }
        });
    }

    public void placeInRandom() {
        int maxBoundX = 10;
        int maxBoundY = 10;

        if (random.nextBoolean()) {
            rotateShip(true);
        }

        if (shape.getWidth() > shape.getHeight()) {
            maxBoundX = 10 - size + 1;
        } else if (shape.getWidth() < shape.getHeight()) {
            maxBoundY = 10 - size + 1;
        }

        int xPos = random.nextInt(maxBoundX);
        int yPos = random.nextInt(maxBoundY);

        while(!verifyShipsTouch(xPos, yPos)) {
            xPos = random.nextInt(maxBoundX);
            yPos = random.nextInt(maxBoundY);
        }

        shape.setX(40*(xPos + 1));
        shape.setY(40*(yPos + 1));

        setPositionOnBoard(xPos, yPos);
    }

    private void placeShip() {
        shape.toFront();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                Rectangle squareRectangle = new Rectangle(board[i][j].getLayoutX(), board[i][j].getLayoutY(), board[i][j].getWidth(), board[i][j].getHeight());
                if (shape.intersects(squareRectangle.getLayoutBounds()) && verifyOutOfBoard()) {

                    int xPos = (int) calculateLowestDist().getLayoutX()/40 - 1;
                    int yPos = (int) calculateLowestDist().getLayoutY()/40 - 1;

                    // Clear position only if ship was already placed on board (position is not default -1)
                    if (position[0].getX() >= 0) {
                        clearLastPos();
                    }

                    if (verifyShipsTouch(xPos, yPos)) {
                        shape.setX(calculateLowestDist().getLayoutX());
                        shape.setY(calculateLowestDist().getLayoutY());
                        setPositionOnBoard(xPos, yPos);
                        return;
                    }
                }
                if (!verifyOutOfBoard()) {
                    if (position[0].getX() >= 0) {
                        clearLastPos();
                        for (int k = 0; k < size; k++) {
                            position[k].setX(-1);
                            position[k].setY(-1);
                        }
                    }
                }
            }
        }
    }

    private void rotateShip(boolean isRandom) {

        if (isRandom) {
            double tmp = shape.getHeight();
            shape.setHeight(shape.getWidth());
            shape.setWidth(tmp);
        } else {
            if (size == 1) {
                return;
            }

            double tmp = shape.getHeight();
            shape.setHeight(shape.getWidth());
            shape.setWidth(tmp);

            if (position[0].getX() >= 0) {
                clearLastPos();
            }

            if (!verifyShipsTouch(position[0].getX(), position[0].getY()) || !(((shape.getWidth() > shape.getHeight()) && position[0].getX() <= 10 - size) || ((shape.getWidth() < shape.getHeight()) && position[0].getY() <= 10 - size))) {
                tmp = shape.getHeight();
                shape.setHeight(shape.getWidth());
                shape.setWidth(tmp);
            }

            if (position[0].getX() > 0) {
                setPositionOnBoard(position[0].getX(), position[0].getY());
            }
        }

    }

    private boolean verifyOutOfBoard() {
        Rectangle boardRectangle = new Rectangle(board[0][0].getLayoutX(), board[0][0].getLayoutY(), board[0][0].getWidth()*board.length, board[0][0].getHeight()*board.length);
        Point2D vertex1 = new Point2D(shape.getX(), shape.getY());
        Point2D vertex2 = new Point2D(shape.getX() + shape.getWidth(), shape.getY() + shape.getHeight());

        //TODO Ewentualna poprawa działania tej funkcji, aby przy ustawianiu statków nie trzeba było ich "upuszczać" idealnie wewnątrz planszy

        return boardRectangle.contains(vertex1) && boardRectangle.contains(vertex2);
    }

    private boolean verifyShipsTouch(int shipPosX, int shipPosY) {
        boolean correctPlace = true;

        for (int i = -1; i < size + 1; i++) {
            for (int j = -1; j < 2; j++) {
                if ((shape.getWidth() >= shape.getHeight()) && (!(shipPosX + i < 0) && !(shipPosX + i > 9) && !(shipPosY + j < 0) && !(shipPosY + j > 9))) {
                    if (board[shipPosX + i][shipPosY + j].getSquareStatus() == Square.SquareStatus.SHIP) {
                        correctPlace = false;
                    }
                }
                else if ((shape.getWidth() < shape.getHeight()) && (!(shipPosX + j < 0) && !(shipPosX + j > 9) && !(shipPosY + i < 0) && !(shipPosY + i > 9))) {
                    if (board[shipPosX + j][shipPosY + i].getSquareStatus() == Square.SquareStatus.SHIP) {
                        correctPlace = false;
                    }
                }
            }
        }

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

    public int getDmgCount() {
        return dmgCount;
    }
    public void addDmgCount() {
        this.dmgCount += 1;
    }

    public Square[][] getBoard() {
        return board;
    }

    public Position[] getPosition() {
        return position;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}