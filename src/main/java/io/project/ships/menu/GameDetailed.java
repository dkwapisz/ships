package io.project.ships.menu;

import io.project.ships.Main;

public class GameDetailed extends GameBasic{
    private String board1;
    private String board2;
    private String gameFlow;

    public GameDetailed(int gid, int uid1, int uid2, String board1, String board2, String gameFlow, int isAivsai) {
        super(gid, uid1, uid2, isAivsai);
        this.board1 = board1;
        this.board2 = board2;
        this.gameFlow = gameFlow;
    }

    public User getUserById(int uid) {
        for (User user : Main.getUsers()) {
            if (user.getUid() == uid) {
                return user;
            }
        }
        return null;
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
