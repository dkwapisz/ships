package io.project.ships.game;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ship {

    private int size;
    private Rectangle shape;
    private Position position;

    public Ship(int size, int whichShip, Square[][] board) {
        makeShape(size, whichShip);
        this.size = size;
        position = new Position(0, 0);
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

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {

                    Rectangle boardRectangle = new Rectangle(board[i][j].getLayoutX(), board[i][j].getLayoutY(), board[i][j].getWidth(), board[i][j].getHeight());

                    if (shape.intersects(boardRectangle.getBoundsInParent())) {
                        board[i][j].getStyleClass().add("cell-highlight");
                    } else {
                        board[i][j].getStyleClass().remove("cell-highlight");
                        board[i][j].getStyleClass().add("cell");
                    }
                }
            }
        });

        shape.setOnMouseReleased(event -> {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {

                    Rectangle boardRectangle = new Rectangle(board[i][j].getLayoutX(), board[i][j].getLayoutY(), board[i][j].getWidth(), board[i][j].getHeight());
                    if (shape.intersects(boardRectangle.getLayoutBounds())) {
                        shape.setX(calculateLowestDist(board).getLayoutX());
                        shape.setY(calculateLowestDist(board).getLayoutY());
                    }
                }
            }
        });

        shape.setOnMouseClicked(event -> {
            if (event.isPopupTrigger()) {
                double tmp = shape.getHeight();
                shape.setHeight(shape.getWidth());
                shape.setWidth(tmp);
                System.out.println(shape.getX());
                System.out.println(shape.getY());
            }
        });
    }

    private Square calculateLowestDist(Square[][] board) {
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

    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position) {
        this.position = position;
    }

}