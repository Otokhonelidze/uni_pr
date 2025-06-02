package oto.project.frontend;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboard implements Structure {
    private Scene scene;
    private Stage stage;

    public AdminDashboard() {
        this.stage = new Stage();
        this.setupStructure();
    }

    @Override
    public void setupStructure() {
        BorderPane root = new BorderPane();
        // header
        HBox header = new HBox();
        Label text = new Label("Admin management system");
        Label adminName = new Label();
        header.getChildren().addAll(text, adminName);
        root.setTop(header);
        // main
        Label overViewLabel = new Label("Overview");
        VBox status = new VBox(5);
        Label totalUsers = new Label("Total users: ");
        Label totalProducts = new Label("Total products: ");
        status.getChildren().addAll(overViewLabel, totalUsers, totalProducts);
        root.setCenter(status);
        // footer
        VBox userRoot = new VBox(10);
        Button createUserBtn = new Button("Create user");
        HBox userList = new HBox(10);
        userRoot.getChildren().addAll(createUserBtn, userList);
        root.setBottom(userRoot);
        // TODO: user table view

        this.scene = new Scene(root, 800, 600);
        this.stage.setScene(scene);
    }

    public void show() {
        this.stage.show();
    }
}
