package io.project.ships.controllers;

import io.project.ships.menu.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import io.project.ships.menu.Database;
import io.project.ships.Main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LoginWindowController {

    @FXML
    private User user;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button registerButton;

    @FXML
    private Label titleLabel;

    @FXML
    void login() throws IOException {
        if (checkCredentials()){
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Pane root = FXMLLoader.load(getClass().getResource("/fxml/menu-view.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void goToRegisterWindow() throws IOException {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/register-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    boolean checkCredentials() {
        Database db = new Database();
        ArrayList<User> users;
        users=db.selectUsers();
        for (User user : users) {
            if (user.getUsername().equals(loginTextField.getText())) {
                if (user.getPasswordHash().equals(db.generateHash(passwordTextField.getText(), user.getSalt())[0])) {
                    System.out.println("signed in successfully!");
                    Main.setUser1(user);
                    db.closeConnection();
//                    String bip = "bitwa.mp3";
//                    Media hit = new Media(new File(bip).toURI().toString());
//                    MediaPlayer mediaPlayer = new MediaPlayer(hit);
//                    mediaPlayer.play(); //no copyright infringement has been detected
                    return true;
                } else {
                    System.out.println("password incorrect!");
                }
            }
        }
        return false;
    }

    @FXML
    void register() {

    }

    public User getUser() {
        return user;
    }
}
