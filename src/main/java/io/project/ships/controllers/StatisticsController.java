package io.project.ships.controllers;

import io.project.ships.Main;
import io.project.ships.menu.User;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class StatisticsController {
    private TableView<Statistics> table = new TableView<Statistics>();
    private final ObservableList<Statistics> data = FXCollections.observableArrayList();

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
        Main.getStatistics().forEach((n) -> data.add(new Statistics(
                n.getUid(),
                n.getPlayed(),
                n.getVictories(),
                n.getShots(),
                n.getOnTarget(),
                n.getWinRate(),
                n.getAccuracy()
        )));

        TableColumn usernameColumn = new TableColumn("Username");
        TableColumn playedColumn = new TableColumn("Played");
        TableColumn victoriesColumn = new TableColumn("Victories");
        TableColumn winRateColumn = new TableColumn("Win Rate[%]");
        TableColumn shotsColumn = new TableColumn("Shots");
        TableColumn onTargetColumn = new TableColumn("On Target");
        TableColumn accuracyColumn = new TableColumn("Accuracy[%]");

        usernameColumn.setCellValueFactory(
                new PropertyValueFactory<Statistics, SimpleStringProperty>("username")
        );
        playedColumn.setCellValueFactory(
                new PropertyValueFactory<Statistics, Integer>("played")
        );
        victoriesColumn.setCellValueFactory(
                new PropertyValueFactory<Statistics, Integer>("victories")
        );
        shotsColumn.setCellValueFactory(
                new PropertyValueFactory<Statistics, Integer>("shots")
        );
        onTargetColumn.setCellValueFactory(
                new PropertyValueFactory<Statistics, Integer>("onTarget")
        );
        winRateColumn.setCellValueFactory(
                new PropertyValueFactory<Statistics, Float>("winRate")
        );
        accuracyColumn.setCellValueFactory(
                new PropertyValueFactory<Statistics, Float>("accuracy")
        );

        table.setItems(data);
        table.getColumns().addAll(
                usernameColumn, playedColumn, victoriesColumn, winRateColumn, shotsColumn, onTargetColumn, accuracyColumn
        );
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(325);
        table.setPrefWidth(570);


        final VBox vbox = new VBox();
        vbox.setSpacing(0);
        vbox.setPadding(new Insets(0, 0, 0, 0));
        vbox.getChildren().addAll(table);
        scrollPane.setContent(vbox);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
    public static class Statistics {

        private final SimpleStringProperty username;
        private final SimpleIntegerProperty played;
        private final SimpleIntegerProperty victories;
        private final SimpleIntegerProperty shots;
        private final SimpleIntegerProperty onTarget;
        private final SimpleFloatProperty winRate;
        private final SimpleFloatProperty accuracy;

        private Statistics(int uid, int played, int victories, int shots, int onTarget, float winRate, float accuracy) {
            String username = new String();
            for (User user : Main.getUsers()) {
                if (user.getUid() == uid) {
                    username = user.getUsername();
                    break;
                }
            }
            this.username = new SimpleStringProperty(username);
            this.played = new SimpleIntegerProperty(played);
            this.victories = new SimpleIntegerProperty(victories);
            this.shots = new SimpleIntegerProperty(shots);
            this.onTarget = new SimpleIntegerProperty(onTarget);
            this.winRate = new SimpleFloatProperty(winRate);
            this.accuracy = new SimpleFloatProperty(accuracy);
        }

        public String getUsername() {
            return username.get();
        }

        public SimpleStringProperty usernameProperty() {
            return username;
        }

        public int getPlayed() {
            return played.get();
        }

        public SimpleIntegerProperty playedProperty() {
            return played;
        }

        public int getVictories() {
            return victories.get();
        }

        public SimpleIntegerProperty victoriesProperty() {
            return victories;
        }

        public int getShots() {
            return shots.get();
        }

        public SimpleIntegerProperty shotsProperty() {
            return shots;
        }

        public int getOnTarget() {
            return onTarget.get();
        }

        public SimpleIntegerProperty onTargetProperty() {
            return onTarget;
        }

        public float getWinRate() {
            return winRate.get();
        }

        public SimpleFloatProperty winRateProperty() {
            return winRate;
        }

        public float getAccuracy() {
            return accuracy.get();
        }

        public SimpleFloatProperty accuracyProperty() {
            return accuracy;
        }
    }
}
