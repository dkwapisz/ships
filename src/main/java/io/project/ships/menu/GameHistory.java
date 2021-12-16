package io.project.ships.menu;

public class GameHistory {
    private int gid;
    private int uid;
    private int moveNumber;
    private String board;

    public GameHistory(int gid, int moveNumber, int uid, String board){
        this.gid = gid;
        this.uid = uid;
        this.moveNumber = moveNumber;
        this.board = board;
    }

    public int getGid() {
        return gid;
    }

    public int getUid() {
        return uid;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public String getBoard() {
        return board;
    }
}
