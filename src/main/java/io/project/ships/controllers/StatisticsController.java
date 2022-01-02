package io.project.ships.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class StatisticsController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    void backToMenu() throws IOException {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/menu-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void initialize() {
        VBox content = new VBox();
        content.setAlignment(Pos.TOP_RIGHT);
        scrollPane.setContent(content);
        for (int i = 0; i < 100; i++)
        {
            Label label = new Label("Label " + i);
            content.setPrefHeight(content.getPrefHeight() + label.getPrefHeight());
            content.getChildren().add(label);
        }

    }
}
