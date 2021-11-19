package io.project.ships.game;

import io.project.ships.Main;

import java.util.ArrayList;
import java.util.Random;

public class AI {

    private Random random = new Random();
    private ArrayList<Position> notTakenPos = new ArrayList<>();
    private ArrayList<Position> lastShipPos = new ArrayList<>();

    public AI() {
        addNotTakenPos();
    }

    private void addNotTakenPos() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                notTakenPos.add(new Position(i, j));
            }
        }
    }

    private Position getShotEasy() {
        Position shot = notTakenPos.get(random.nextInt(notTakenPos.size()));
        removePosition(shot.getX(), shot.getY());

        return shot;
    }

    private Position getShotMedium() {

        int x = 0, y = 0;
        Position shot;

        if (lastShipPos.size() > 0) {
            if (lastShipPos.size() == 1) {
                x = lastShipPos.get(0).getX();
                y = lastShipPos.get(0).getY() + 1;
                if (positionIsTaken(x, y) || y == 10) {
                    x = lastShipPos.get(0).getX();
                    y = lastShipPos.get(0).getY() - 1;
                    if (positionIsTaken(x, y) || y == -1) {
                        x = lastShipPos.get(0).getX() + 1;
                        y = lastShipPos.get(0).getY();
                        if (positionIsTaken(x, y) || x == 10) {
                            x = lastShipPos.get(0).getX() - 1;
                            y = lastShipPos.get(0).getY();
                        }
                    }
                }
            } else {
                int i = lastShipPos.size() - 2;
                boolean isPositive;
                if (lastShipPos.get(i).getX() == lastShipPos.get(i + 1).getX()) {
                    //Vertical ship
                    x = lastShipPos.get(i).getX();
                    if (lastShipPos.get(i + 1).getY() - lastShipPos.get(i).getY() > 0) {
                        y = lastShipPos.get(i + 1).getY() + 1;
                        isPositive = true;
                    } else {
                        y = lastShipPos.get(i + 1).getY() - 1;
                        isPositive = false;
                    }
                    if (positionIsTaken(x, y) || y == -1 || y == 10) {
                        if (isPositive) {
                            y = lastShipPos.get(i + 1).getY() - 2;
                        } else {
                            y = lastShipPos.get(i + 1).getY() + 2;
                        }
                        if (positionIsTaken(x, y)) {
                            if (isPositive) {
                                y = lastShipPos.get(i + 1).getY() - 3;
                            } else {
                                y = lastShipPos.get(i + 1).getY() + 3;
                            }
                        }
                    }
                }
                else if (lastShipPos.get(i).getY() == lastShipPos.get(i + 1).getY()) {
                    //Horizontal ship
                    y = lastShipPos.get(i).getY();
                    if (lastShipPos.get(i + 1).getX() - lastShipPos.get(i).getX() > 0) {
                        x = lastShipPos.get(i + 1).getX() + 1;
                        isPositive = true;
                    } else {
                        x = lastShipPos.get(i + 1).getX() - 1;
                        isPositive = false;
                    }
                    if (positionIsTaken(x, y) || x == -1 || x == 10) {
                        if (isPositive) {
                            x = lastShipPos.get(i + 1).getX() - 2;
                        } else {
                            x = lastShipPos.get(i + 1).getX() + 2;
                        }
                        if (positionIsTaken(x, y)) {
                            if (isPositive) {
                                x = lastShipPos.get(i + 1).getX() - 3;
                            } else {
                                x = lastShipPos.get(i + 1).getX() + 3;
                            }
                        }
                    }
                }
            }
            shot = new Position(x, y);
        } else {
            shot = notTakenPos.get(random.nextInt(notTakenPos.size()));
        }

        removePosition(shot.getX(), shot.getY());

        return shot;
    }

    private boolean positionIsTaken(int x, int y) {
        for (Position pos : notTakenPos) {
            if (pos.getX() == x && pos.getY() == y) {
                return false;
            }
        }
        return true;
    }

    public void hitAISquare(int difficulty, Board board, Ship[] ships) {
        Position position = null;

        if (difficulty == 1) {
            position = getShotEasy();
        } else if (difficulty == 2) {

            position = getShotMedium();
        }

        if (board.getSquareBoard()[position.getX()][position.getY()].getSquareStatus() == Square.SquareStatus.EMPTY) {
            board.getSquareBoard()[position.getX()][position.getY()].setSquareStatus(Square.SquareStatus.MISS);
            Main.setPlayer1Turn(!Main.isPlayer1Turn());
        } else if (board.getSquareBoard()[position.getX()][position.getY()].getSquareStatus() == Square.SquareStatus.SHIP) {
            board.getSquareBoard()[position.getX()][position.getY()].setSquareStatus(Square.SquareStatus.DAMAGED);
            lastShipPos.add(position);
            for (int i = 0; i < ships.length; i++) {
                for (int j = 0; j < ships[i].getSize(); j++) {
                    if (position.getX() == ships[i].getPosition()[j].getX() && position.getY() == ships[i].getPosition()[j].getY()) {
                        ships[i].addDmgCount();
                        break;
                    }
                }
            }
            for (int i = 0; i < ships.length; i++) {
                if (ships[i].getDmgCount() == ships[i].getSize() && !ships[i].isDestroyed()) {

                    for (int j = 0; j < ships[i].getSize(); j++) {
                        board.getSquareBoard()[ships[i].getPosition()[j].getX()][ships[i].getPosition()[j].getY()].setSquareStatus(Square.SquareStatus.DESTROYED);
                    }
                    ships[i].setDestroyed(true);
                    lastShipPos.clear();
                    board.markAsMiss(ships[i], true, notTakenPos);
                    break;
                }
            }
        }
    }

    public void removePosition(int x, int y) {
        notTakenPos.removeIf(pos -> pos.getX() == x && pos.getY() == y);
    }


}
