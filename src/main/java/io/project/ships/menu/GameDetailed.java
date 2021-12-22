package io.project.ships.menu;

public class GameDetailed extends GameBasic{
    private String board1;
    private String board2;
    private String gameFlow;
    private int isAivsai;

    public GameDetailed(int gid, int uid1, int uid2, String board1, String board2, String gameFlow, int isAivsai) {
        super(gid, uid1, uid2);
        this.board1 = board1;
        this.board2 = board2;
        this.gameFlow = gameFlow;
        this.isAivsai = isAivsai;
    }

    public int getIsAivsai() {
        return isAivsai;
    }

    public String getGameFlow() {
        return gameFlow;
    }

    public String getBoard2() {
        return board2;
    }

    public String getBoard1() {
        return board1;
    }

}
