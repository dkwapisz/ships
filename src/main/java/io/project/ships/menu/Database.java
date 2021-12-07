package io.project.ships.menu;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.transform.Source;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.ResourceBundle;
import java.nio.charset.StandardCharsets;

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
        String createUserList = "CREATE TABLE IF NOT EXISTS users_list (\n" +
                "    uid      INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                     UNIQUE\n" +
                "                     NOT NULL,\n" +
                "    username STRING  UNIQUE ON CONFLICT ABORT\n" +
                "                     NOT NULL,\n" +
                "    password STRING  NOT NULL,\n" +
                "    salt     STRING \n" +
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
        String createGameHistory = "CREATE TABLE IF NOT EXISTS game_history (\n" +
                "    gid         INTEGER DEFAULT (0),\n" +
                "    uid1        INTEGER,\n" +
                "    uid2        INTEGER DEFAULT (0),\n" +
                "    move_number INTEGER,\n" +
                "    board1      STRING,\n" +
                "    board2      STRING,\n" +
                "    is_aivsai   INTEGER DEFAULT (0) \n" +
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

    public boolean dropTables(){
        String drop = "DROP TABLE IF EXISTS users_list;";
        try {
            stat.execute(drop);
        }catch (SQLException e) {
            System.err.println("drop failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertIntoUserList(String username, String password){
        try {
            PreparedStatement prepStmt = conn.prepareStatement("INSERT INTO users_list (username, password, salt) VALUES (?, ?, ?);");
            prepStmt.setString(1, username);
            String[] credentials = this.generateHash(password);
            prepStmt.setString(2, credentials[0]);
            prepStmt.setString(3, credentials[1]);
            prepStmt.execute();
        }catch (SQLException e) {
            System.err.println("insert failed");
            e.printStackTrace();
            if (e.getErrorCode() == 19){
                System.out.println("user already exists!");
            }
            return false;
        }
        return true;
    }

    public boolean insertIntoGameHistory(int gid, int uid1, int uid2, int moveNumber, String board1, String board2, int isAiVsAi){
        try {
            PreparedStatement prepStmt = conn.prepareStatement("INSERT INTO game_history " +
                    "(gid, uid1, uid2, move_number, board1, board2, is_aivsai) VALUES (?, ?, ?, ?, ?, ?, ?);");
            prepStmt.setInt(1, gid);
            prepStmt.setInt(2, uid1);
            prepStmt.setInt(3, uid2);
            prepStmt.setInt(4, moveNumber);
            prepStmt.setString(5, board1);
            prepStmt.setString(6, board2);
            prepStmt.setInt(7, isAiVsAi);
            prepStmt.execute();
        }catch (SQLException e) {
            System.err.println("insert failed");
            e.printStackTrace();
            if (e.getErrorCode() == 19){
                System.out.println("user already exists!");
            }
            return false;
        }
        return true;
    }

    public ArrayList<User> selectUsers(){
        ArrayList<User> users= new ArrayList<User>();
        try {
            ResultSet result = stat.executeQuery("SELECT * from users_list");
            int uid;
            String username, password, salt;
            while (result.next()) {
                uid = result.getInt("uid");
                username = result.getString("username");
                password = result.getString("password");
                salt = result.getString("salt");
                users.add(new User(uid, username, password, salt));
            }
        }catch (SQLException e) {
            System.err.println("select failed");
            e.printStackTrace();
            return null;
        }
        return users;
    }

    public ArrayList<GameHistory> selectGames(int uid){
        ArrayList<GameHistory> games= new ArrayList<GameHistory>();
        try {
            PreparedStatement prepStmt = conn.prepareStatement("SELECT * from game_history where uid1=? or uid2=?;");
            prepStmt.setInt(1, uid);
            prepStmt.setInt(2, uid);
            ResultSet result = prepStmt.executeQuery();
            int gid, uid1, uid2, moveNumber, isAiVsAi;
            String board1, board2;
            while (result.next()) {
                gid = result.getInt("gid");
                uid1 = result.getInt("uid1");
                uid2 = result.getInt("uid2");
                moveNumber = result.getInt("move_number");
                board1 = result.getString("board1");
                board2 = result.getString("board2");
                isAiVsAi = result.getInt("is_aivsai");
                games.add(new GameHistory(gid, uid1, uid2, moveNumber, board1, board2, isAiVsAi));
            }
        }catch (SQLException e) {
            System.err.println("select failed");
            e.printStackTrace();
            return null;
        }
        return games;
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

    public static void main(String[] args){
////        just for testing sakes
//        Database db = new Database();
////        db.dropTables();
//        db.createUserListTable();
//        db.createGameHistoryTable();
//        db.insertIntoUserList("siema","siema");
//        ArrayList<User> users = new ArrayList<User>();
//        users=db.selectUsers();
//        ArrayList<GameHistory> games = new ArrayList<GameHistory>();
//        games=db.selectGames(2);
//        db.closeConnection();
    }
}
