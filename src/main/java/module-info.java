module order_managment_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jbcrypt;
    requires org.xerial.sqlitejdbc;


    exports controllers;
    exports controllers.fragments;
    exports database;
    exports models;
    exports views;
    opens views to javafx.fxml;
    opens controllers to javafx.fxml;
    opens controllers.fragments to javafx.fxml;
}