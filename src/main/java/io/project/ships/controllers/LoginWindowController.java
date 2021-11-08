package io.project.ships.controllers;


import io.project.ships.LoginWindow;
import io.project.ships.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginWindowController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    void login() throws IOException {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/menu-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
