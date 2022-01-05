package io.project.ships.menu;

public class User {

    private int uid;
    private String username;
    private String passwordHash;
    private String salt;
    private  String path;

    public User(int uid, String username, String passwordHash, String salt, String path) {
        this.uid=uid;
        this.username=username;
        this.passwordHash=passwordHash;
        this.salt=salt;
        this.path = path;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path =path;
    }
}