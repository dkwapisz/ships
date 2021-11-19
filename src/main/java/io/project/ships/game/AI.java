package io.project.ships.game;

import java.util.ArrayList;
import java.util.Random;

public class AI {

    private static Random random = new Random();
    private static ArrayList<Position> alreadyTakenPositions = new ArrayList<>();

    public static Position getShotEasy() {
        int x = random.nextInt(10);
        int y = random.nextInt(10);

        while (checkPositionWasUsed(x, y)) {
            x = random.nextInt(10);
            y = random.nextInt(10);
        }

        Position shot = new Position(x, y);

        alreadyTakenPositions.add(shot);

        return shot;
    }

    private static boolean checkPositionWasUsed(int x, int y) {
        if (alreadyTakenPositions.size() > 0) {
            for (Position position : alreadyTakenPositions) {
                if (position.getX() == x && position.getY() == y) {
                    return true;
                }
            }
        }
        return false;
    }
}
