package oto.project;

import javafx.application.Application;
import javafx.stage.Stage;
import oto.project.backend.Database;
import oto.project.frontend.AdminDashboard;

public class App extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Database db = new Database("OTARI\\SQLEXPRESS", "myDB", "sa", "database890");
        db.startConnection();

        AdminDashboard admin = new AdminDashboard();
        admin.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}