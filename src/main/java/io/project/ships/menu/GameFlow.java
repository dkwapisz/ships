package io.project.ships.menu;

import java.util.ArrayList;

public class GameFlow {
    private ArrayList<Move> moves;

    public GameFlow(ArrayList<Move> moves) {
        this.moves = moves;
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }
}
