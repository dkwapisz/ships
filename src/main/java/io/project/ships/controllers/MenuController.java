package io.project.ships.controllers;

import io.project.ships.menu.Database;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import io.project.ships.Main;

import java.io.File;
import java.io.IOException;

public class MenuController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button gameSettingsButton;

    @FXML
    private Button quitButton;

    @FXML
    private Button replaysButton;

    @FXML
    private Button statisticsButton;

    @FXML
    private Label userLabel;

    @FXML
    private ImageView imageField;

    @FXML
    public void initialize() throws IOException {
        Image img = new Image(Main.getUser1().getPath());
        File tempFile = new File(Main.getUser1().getPath().substring(6).replace("%20", " "));
        boolean exists = tempFile.exists();
        if (exists) {
            imageField.setImage(img);
        } else {
            String path = getClass().getResource("/image/image.jpg").toString();
            Image image = new Image(path);
            imageField.setImage(image);
        }
    }

    @FXML
    void goToGameSettings() throws IOException {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/gameSettings-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goToStatistics() throws IOException {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/statistics-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goToReplays() throws IOException {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/replays-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void changeImage(MouseEvent event) throws IOException {
        System.out.println("siema");
        String path = new String();
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter images = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fc.getExtensionFilters().add(images);
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            Image img = new Image(file.toURI().toString());
            imageField.setImage(img);
            Database db = new Database();
            db.updateImages(Main.getUser1().getUid(), file.toURI().toString());
            db.closeConnection();
            Main.getUser1().setPath(file.toURI().toString());
        }
    }

    @FXML
    void quit() {
        Platform.exit();
    }
}
