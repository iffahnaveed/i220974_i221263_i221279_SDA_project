package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Parent_controller {
    private Stage stage;
    private Scene scene;
    private Parent root;

    // Reusable method to load and switch scenes
    private void loadScene(ActionEvent event, String fxmlFile) throws IOException {
        // Load the specified FXML file
        root = FXMLLoader.load(getClass().getResource(fxmlFile));
        // Get the current stage from the event source
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Set the new scene
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Method for Recruiter Login
    @FXML
    public void switchtologinRecruiter(ActionEvent event) throws IOException {
        loadScene(event, "recruiterlogin.fxml");
    }

    // Method for Admin Login
    @FXML
    public void switchtologinAdmin(ActionEvent event) throws IOException {
        loadScene(event, "adminlogin.fxml");
    }
    @FXML
    public void switchtologinApplicant(ActionEvent event) throws IOException {
        loadScene(event, "login.fxml");
    }
    @FXML
    public void switchtologinHR(ActionEvent event) throws IOException {
        loadScene(event, "hrlogin.fxml");
    }
}
