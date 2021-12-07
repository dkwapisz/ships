package io.project.ships.menu;

public class GameHistory {
    private int gid;
    private int uid1;
    private int uid2;
    private int moveNumber;
    private String board1;
    private String board2;
    private int isAiVsAi;

    public GameHistory(int gid, int uid1, int uid2, int moveNumber, String board1, String board2, int isAiVsAi){
        this.gid=gid;
        this.uid1=uid1;
        this.uid2=uid2;
        this.moveNumber=moveNumber;
        this.board1=board1;
        this.board2=board2;
        this.isAiVsAi=isAiVsAi;
    }

    public int getGid() {
        return gid;
    }

    public int getUid1() {
        return uid1;
    }

    public int getUid2() {
        return uid2;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public String getBoard1() {
        return board1;
    }

    public String getBoard2() {
        return board2;
    }

    public int getIsAiVsAi() {
        return isAiVsAi;
    }
}
