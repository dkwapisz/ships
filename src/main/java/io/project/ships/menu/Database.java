package io.project.ships.menu;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;


public class Database {
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:ships.db";

    private Connection conn;
    private Statement stat;

    public Database() {
        try {
            Class.forName(Database.DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("no jdbc driver found");
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
        } catch (SQLException e) {
            System.err.println("connection failed");
            e.printStackTrace();
        }
    }

    public boolean createUserListTable() {
        String path = getClass().getResource("/image/image.jpg").toString();
        String createUserList = "CREATE TABLE IF NOT EXISTS users_list (\n" +
                "    uid      INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                     UNIQUE\n" +
                "                     NOT NULL,\n" +
                "    username STRING  UNIQUE ON CONFLICT ABORT\n" +
                "                     NOT NULL,\n" +
                "    password STRING  NOT NULL,\n" +
                "    salt     STRING  NOT NULL, \n" +
                "    path     STRING  \n" +
                ");";
        try {
            stat.execute(createUserList);
        } catch (SQLException e) {
            System.err.println("creating table failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean createGameHistoryTable() {
        String createGameHistory = "CREATE TABLE game_history (\n" +
                "    gid       INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                      NOT NULL\n" +
                "                      UNIQUE,\n" +
                "    uid1      INTEGER,\n" +
                "    uid2      INTEGER DEFAULT (0),\n" +
                "    board1    STRING,\n" +
                "    board2    STRING,\n" +
                "    game_flow STRING,\n" +
                "    is_aivsai INTEGER DEFAULT (0) \n" +
                ");";
        try {
            stat.execute(createGameHistory);
        } catch (SQLException e) {
            System.err.println("creating table failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean createStatisticsTable() {
        String createStatistics = "CREATE TABLE statistics (\n" +
                "    uid       INTEGER PRIMARY KEY\n" +
                "                      UNIQUE,\n" +
                "    played    INTEGER DEFAULT (0),\n" +
                "    victories INTEGER DEFAULT (0),\n" +
                "    win_rate  DECIMAL DEFAULT (0),\n" +
                "    shots     INTEGER DEFAULT (0),\n" +
                "    on_target INTEGER DEFAULT (0),\n" +
                "    accuracy  DECIMAL DEFAULT (0) \n" +
                ");\n";
        try {
            stat.execute(createStatistics);
        } catch (SQLException e) {
            System.err.println("creating table failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean dropUsersTable() {
        String drop = "DROP TABLE IF EXISTS users_list;";
        try {
            stat.execute(drop);
        } catch (SQLException e) {
            System.err.println("drop failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean dropStatisticsTable() {
        String drop = "DROP TABLE IF EXISTS statistics;";
        try {
            stat.execute(drop);
        } catch (SQLException e) {
            System.err.println("drop failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean dropGamesTable() {
        String drop = "DROP TABLE IF EXISTS game_history;";
        try {
            stat.execute(drop);
        } catch (SQLException e) {
            System.err.println("drop failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertIntoUserList(String username, String password) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement("INSERT INTO users_list (username, password, salt, path) VALUES (?, ?, ?, ?);");
            prepStmt.setString(1, username);
            String[] credentials = this.generateHash(password);
            prepStmt.setString(2, credentials[0]);
            prepStmt.setString(3, credentials[1]);
            prepStmt.setString(4, getClass().getResource("/image/image.jpg").toString());
            prepStmt.execute();
        } catch (SQLException e) {
            System.err.println("insert failed");
            if (e.getErrorCode() == 19) {
                System.out.println("user already exists!");
            }
            return false;
        }
        return true;
    }

    public boolean insertIntoGameHistory(int uid1, int uid2, String board1, String board2, String gameFlow, int isAiVsAi) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement("INSERT INTO games_history " +
                    "(uid1, uid2, board1, board2, game_flow, is_aivsai) VALUES (?, ?, ?, ?, ?, ?);");
            prepStmt.setInt(1, uid1);
            prepStmt.setInt(2, uid2);
            prepStmt.setString(3, board1);
            prepStmt.setString(4, board2);
            prepStmt.setString(5, gameFlow);
            prepStmt.setInt(6, isAiVsAi);
            prepStmt.execute();
        } catch (SQLException e) {
            System.err.println("insert failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertIntoStatistics(int uid) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement("INSERT INTO statistics (uid) VALUES (?);");
            prepStmt.setInt(1, uid);
            prepStmt.execute();
        } catch (SQLException e) {
            System.out.println("insert failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateImages(int uid, String path) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement("UPDATE users_list SET path=? WHERE uid=?");
            prepStmt.setString(1, path);
            prepStmt.setInt(2, uid);
            prepStmt.execute();
        } catch (SQLException e) {
            System.err.println("update failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateStatistics(UserStatistics statistics) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement("UPDATE statistics SET " +
                    "played=?, " +
                    "victories=?, " +
                    "win_rate=?, " +
                    "shots=?, " +
                    "onTarget=?, " +
                    "accuracy=? " +
                    "WHERE uid=?"
            );
            prepStmt.setInt(1, statistics.getPlayed());
            prepStmt.setInt(2, statistics.getVictories());
            prepStmt.setFloat(3, statistics.getWinRate());
            prepStmt.setInt(4, statistics.getShots());
            prepStmt.setInt(5, statistics.getOnTarget());
            prepStmt.setFloat(6, statistics.getAccuracy());
            prepStmt.setInt(7, statistics.getUid());
            prepStmt.execute();
        } catch (SQLException e) {
            System.err.println("update failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<User> selectUsers() {
        ArrayList<User> users = new ArrayList<User>();
        try {
            ResultSet result = stat.executeQuery("SELECT * from users_list");
            int uid;
            String username, password, salt, path;
            while (result.next()) {
                uid = result.getInt("uid");
                username = result.getString("username");
                password = result.getString("password");
                salt = result.getString("salt");
                path = result.getString("path");
                users.add(new User(uid, username, password, salt, path));
            }
        } catch (SQLException e) {
            System.err.println("select failed");
            e.printStackTrace();
            return null;
        }
        return users;
    }

    public ArrayList<UserStatistics> selectStatistics() {
        ArrayList<UserStatistics> statistiscs = new ArrayList<UserStatistics>();
        try {
            ResultSet result = stat.executeQuery("SELECT * from statistics");
            int uid, played, victories, shots, onTarget;
            float winRate, accuracy;
            while (result.next()) {
                uid = result.getInt("uid");
                played = result.getInt("played");
                victories = result.getInt("victories");
                shots = result.getInt("shots");
                onTarget = result.getInt("on_target");
                winRate = result.getFloat("win_rate");
                accuracy = result.getFloat("accuracy");
                statistiscs.add(new UserStatistics(uid, played, victories, winRate, shots, onTarget, accuracy));
            }
        } catch (SQLException e) {
            System.err.println("select failed");
            e.printStackTrace();
            return null;
        }
        return statistiscs;
    }

    public ArrayList<GameBasic> selectGamesBasic(int uid) {
        ArrayList<GameBasic> games = new ArrayList<GameBasic>();
        try {
            PreparedStatement prepStmt = conn.prepareStatement("SELECT gid, uid1, uid2 from games_history where uid1=? or uid2=?");
            prepStmt.setInt(1, uid);
            prepStmt.setInt(2, uid);
            ResultSet result = prepStmt.executeQuery();
            int gid;
            int uid1;
            int uid2;
            while (result.next()) {
                gid = result.getInt("gid");
                uid1 = result.getInt("uid1");
                uid2 = result.getInt("uid2");
            }
        } catch (SQLException e) {
            System.err.println("select failed");
            e.printStackTrace();
            return null;
        }
        return games;
    }

    public GameDetailed selectGamesDetailed(int gid) {
        GameDetailed game = null;
        try {
            PreparedStatement prepStmt = conn.prepareStatement("SELECT * from games_history where gid=?;");
            prepStmt.setInt(1, gid);
            ResultSet result = prepStmt.executeQuery();
            gid = result.getInt("gid");
            int uid1 = result.getInt("uid1");
            int uid2 = result.getInt("uid2");
            String board1 = result.getString("board1");
            String board2 = result.getString("board2");
            String gameFlow = result.getString("game_flow");
            int isAiVsAi = result.getInt("is_aivsai");
            game = new GameDetailed(gid, uid1, uid2, board1, board2, gameFlow, isAiVsAi);
        } catch (SQLException e) {
            System.err.println("select failed");
            e.printStackTrace();
            return null;
        }
        return game;
    }

    public String[] generateHash(String password, String... salt) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        Base64.Encoder encoder = Base64.getUrlEncoder();
        byte[] salt_bytes = new byte[16];
        String passwordHash = new String();
        String newSalt = new String();
        if (salt.length == 0) {
            newSalt = this.generateSalt();
            salt_bytes = decoder.decode(newSalt);
        } else {
            salt_bytes = decoder.decode(salt[0]);
            newSalt = salt[0];
        }
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt_bytes, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            passwordHash = encoder.encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return new String[]{passwordHash, newSalt};
    }

    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder();
        String salt = encoder.encodeToString(bytes);
        return salt;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Closing connection failed");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
////        just for testing sakes
//        Database db = new Database();
//        db.dropUsersTable();
//        db.createUserListTable();
////        db.dropTables();
//        db.createUserListTable();
//        db.createGameHistoryTable();
//        db.insertIntoUserList("siema","siema");
//        ArrayList<User> users = new ArrayList<User>();
//        users=db.selectUsers();
//        ArrayList<GameHistory> games = new ArrayList<GameHistory>();
//        games=db.selectGames(2);
//        db.closeConnection();
//        Gson gson = new Gson();
    }
}
