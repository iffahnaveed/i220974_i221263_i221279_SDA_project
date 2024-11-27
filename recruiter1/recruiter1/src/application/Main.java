package application;
import java.sql.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application {
	private Connection connect() {
        // Update this URL with your correct database configuration
        String url = "jdbc:sqlserver://DESKTOP-9LTVH1T\\SQLEXPRESS;databaseName=EHMS;integratedSecurity=true;trustServerCertificate=true";
        Connection conn = null;
        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to the database successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("SQL Server JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }
        return conn;
    }
    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Main_present.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
            String css= this.getClass().getResource("application.css").toExternalForm();
            scene.getStylesheets().add(css);
            
            stage.setScene(scene);
           // stage.setTitle("JavaFX Application");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}