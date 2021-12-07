package io.project.ships.menu;

import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;

public class User {

    private int uid;
    private String username;
    private String passwordHash;
    private String salt;

    public User(int uid, String username, String passwordHash, String salt) {
        this.uid=uid;
        this.username=username;
        this.passwordHash=passwordHash;
        this.salt=salt;
    }

    public int getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getSalt() {
        return salt;
    }
}