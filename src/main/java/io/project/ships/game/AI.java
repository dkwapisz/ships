package io.project.ships.game;

import io.project.ships.Main;

import java.util.ArrayList;
import java.util.Random;

public class AI {

    // Easy AI - shots randomly
    // Medium AI - plays like human
    // Hard AI - plays like human, have 25% chance to double shot, if he doesn't double shot, chance increace by 25%
    // Hard AI - after double shot, another chance to double shot is reset to 25%

    private final Random random = new Random();
    private final ArrayList<Position> notTakenPos = new ArrayList<>();
    private final ArrayList<Position> lastShipPos = new ArrayList<>();
    private int secondShotProb;
    private boolean isShot1Missed;
    private boolean isShot2Missed;
    private boolean doubleShot;

    public AI() {
        addNotTakenPos();
        secondShotProb = 25;
    }

    private void addNotTakenPos() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                notTakenPos.add(new Position(i, j));
            }
        }
    }

    private Position getRandomShot() {
        Position shot = notTakenPos.get(random.nextInt(notTakenPos.size()));
        removePosition(shot.getX(), shot.getY());

        return shot;
    }

    private Position getSmartShot() {

        int x = 0, y = 0;
        Position shot = null;

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
            if (notTakenPos.size() > 0) {
                shot = notTakenPos.get(random.nextInt(notTakenPos.size()));
            }
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

    public void hitAISquare(int difficulty, Board board, Ship[] ships, boolean firstShot) {
        Position position = null;

        if (difficulty == 1) {
            position = getRandomShot();
        } else if (difficulty == 2 || difficulty == 3) {
            position = getSmartShot();
        }

        if (board.getSquareBoard()[position.getX()][position.getY()].getSquareStatus() == Square.SquareStatus.EMPTY) {
            board.getSquareBoard()[position.getX()][position.getY()].setSquareStatus(Square.SquareStatus.MISS);

            if (difficulty != 3) {
                Main.setPlayer1Turn(!Main.isPlayer1Turn());
            }
            if (difficulty == 3) {
                if (doubleShot) {
                    if (firstShot) {
                        isShot1Missed = true;
                    } else {
                        isShot2Missed = true;
                    }

                    if (isShot1Missed && isShot2Missed) {
                        Main.setPlayer1Turn(!Main.isPlayer1Turn());
                    }
                } else {
                    Main.setPlayer1Turn(!Main.isPlayer1Turn());
                }
            }

        } else if (board.getSquareBoard()[position.getX()][position.getY()].getSquareStatus() == Square.SquareStatus.SHIP) {
            board.getSquareBoard()[position.getX()][position.getY()].setSquareStatus(Square.SquareStatus.DAMAGED);
            lastShipPos.add(position);

            if (difficulty == 3) {
                if (doubleShot) {
                    if (firstShot) {
                        isShot1Missed = false;
                    } else {
                        isShot2Missed = false;
                    }
                }
            }

            for (Ship ship : ships) {
                for (int j = 0; j < ship.getSize(); j++) {
                    if (position.getX() == ship.getPosition()[j].getX() && position.getY() == ship.getPosition()[j].getY()) {
                        ship.addDmgCount();
                        break;
                    }
                }
            }
            for (Ship ship : ships) {
                if (ship.getDmgCount() == ship.getSize() && !ship.isDestroyed()) {

                    for (int j = 0; j < ship.getSize(); j++) {
                        board.getSquareBoard()[ship.getPosition()[j].getX()][ship.getPosition()[j].getY()].setSquareStatus(Square.SquareStatus.DESTROYED);
                    }
                    ship.setDestroyed(true);
                    lastShipPos.clear();
                    board.markAsMiss(ship, true, notTakenPos);
                    break;
                }
            }
        }
    }

    public boolean calculateDoubleShot() {
        return random.nextInt(100) < secondShotProb;
    }

    public void removePosition(int x, int y) {
        notTakenPos.removeIf(pos -> pos.getX() == x && pos.getY() == y);
    }

    public int getSecondShotProb() {
        return secondShotProb;
    }

    public void addSecondShotProb() {
        if (secondShotProb < 100) {
            this.secondShotProb += 25;
        }
    }

    public void resetSecondShotProb() {
        this.secondShotProb = 25;
    }

    public boolean isDoubleShot() {
        return doubleShot;
    }

    public void setDoubleShot(boolean doubleShot) {
        this.doubleShot = doubleShot;
    }
}
