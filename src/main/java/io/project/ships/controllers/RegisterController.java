package io.project.ships.controllers;

import io.project.ships.menu.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private Button cancelButton;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private PasswordField password;

    @FXML
    private Button registerButton;

    @FXML
    private TextField username;

    @FXML
    void goBack() throws IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/login-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void register() throws IOException {
        Database db = new Database();
        if (password.getText().equals(confirmPassword.getText())) {
            if (4 < username.getText().length() && username.getText().length() < 16) {
                if ((4 < password.getText().length() && password.getText().length() < 32)) {
                    db.insertIntoUserList(username.getText(), password.getText());
                    Stage stage = (Stage) registerButton.getScene().getWindow();
                    Pane root = FXMLLoader.load(getClass().getResource("/fxml/login-view.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    System.out.println("Password length incorrect (4-32)");
                }
            } else {
                System.out.println("Username length incorrect (4-16)");
            }
        } else {
            System.out.println("Confirmed password incorrct");
        }
    }
}
