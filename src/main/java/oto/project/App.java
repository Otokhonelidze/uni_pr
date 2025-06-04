package oto.project;

import javafx.application.Application;
import javafx.stage.Stage;
import oto.project.backend.Database;
import oto.project.frontend.Admin.AdminDashboard;
import oto.project.frontend.LoginDialog;
import oto.project.frontend.SalaryMan.SalaryManDashboard;
import oto.project.frontend.SalaryMan.User;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        Database db = new Database("OTARI\\SQLEXPRESS", "Sales", "sa", "database890");

        LoginDialog loginDialog = new LoginDialog(primaryStage, db);
        loginDialog.showAndWait();

        User authUser = loginDialog.getAuthUser();

        if (authUser != null) {
            if (authUser.isAdmin()) {
                AdminDashboard adminDashboard = new AdminDashboard(db, authUser);
                adminDashboard.show();
            } else if (authUser.isEmployee()) {
                SalaryManDashboard salaryManDashboard = new SalaryManDashboard(db, authUser);
                salaryManDashboard.show();
            }
            primaryStage.close();
        } else {
            primaryStage.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}