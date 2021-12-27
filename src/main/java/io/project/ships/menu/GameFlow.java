package io.project.ships.menu;

import java.util.ArrayList;

public class GameFlow {
    private int gid;
    private ArrayList<Move> moves;

    public GameFlow(int gid, ArrayList<Move> moves) {
        this.gid = gid;
        this.moves = moves;
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public int getGid() {
        return gid;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }
}
