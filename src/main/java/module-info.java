module io.project.ships {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens io.project.ships to javafx.fxml;
    exports io.project.ships;
    exports io.project.ships.controllers;
    opens io.project.ships.controllers to javafx.fxml;
}