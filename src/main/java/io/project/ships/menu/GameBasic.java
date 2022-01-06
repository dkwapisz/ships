package io.project.ships.menu;

import io.project.ships.Main;

import java.util.ArrayList;

public class GameBasic {
    private int gid;
    private int uid1;
    private int uid2;

    public GameBasic(int gid, int uid1, int uid2){
        this.gid = gid;
        this.uid1 = uid1;
        this.uid2 = uid2;
    }

    public String toString() {
        String username1 = "";
        String username2 = "";
        ArrayList<User> users = Main.getUsers();
        if (uid2 == 0) {
            for (User user : users) {
                if (user.getUid() == uid1) {
                    username1 = user.getUsername();
                }
            }
            username2 = "AI";
        } else {
                for (User user : users) {
                    if(user.getUid() == uid1) {
                        username1 = user.getUsername();
                    }
                    if(user.getUid() == uid2) {
                        username2 = user.getUsername();
                    }
                }
            }

        return new String("game ID:" + gid + "; " + username1 + " vs " + username2);
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
