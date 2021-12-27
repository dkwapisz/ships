package io.project.ships.menu;

public class GameBasic {
    private int gid;
    private int uid1;
    private int uid2;

    public GameBasic(int gid, int uid1, int uid2){
        this.gid = gid;
        this.uid1 = uid1;
        this.uid2 = uid2;
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
}
