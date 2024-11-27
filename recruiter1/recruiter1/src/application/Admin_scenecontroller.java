package application;
import javafx.collections.FXCollections;
import Model_classes.application_model;
import Model_classes.Recruiter_model;
import Model_classes.MessageRecord;
import javafx.collections.ObservableList;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import logic_layer.Recruiter_service;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import logic_layer.Hr_service;
public class Admin_scenecontroller {
	private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField aemailField;
    @FXML
    private TextField apasswordField;
    @FXML
    private ChoiceBox<String> sorton;
    @FXML
    private ChoiceBox<String> sortby;
    @FXML
    private ChoiceBox<Integer>  recruiterIdChoiceBox;
    @FXML
    private ChoiceBox<Integer> hrIdChoiceBox;
    @FXML
    private ListView<String> listview;
    Hr_service hrService=new Hr_service(); 
    public Admin_scenecontroller() {
        this.hrService = new Hr_service(); // Initialize the Hr_service
    }
    private void fetchAndSortApplicants(String sortBy, String sortOrder) {
        // Fetch sorted applicants from the DAO layer
        List<String> sortedApplicants = hrService.getSortedApplicants(sortBy, sortOrder);

        // Set the items for the ListView
        listview.setItems(FXCollections.observableArrayList(sortedApplicants));
    }
    private void applySorting() {
        String sortBy = sorton.getValue();   // Get the sorting option (CGPA or Experience)
        String sortOrder = sortby.getValue(); // Get the sorting order (Ascending or Descending)
        if (sortBy != null && sortOrder != null) {
            fetchAndSortApplicants(sortBy, sortOrder); // Fetch sorted applicants based on the selected criteria
        }
    }
    private void displayRecruiterInfo(Integer recruiterId) {
        // Fetch detailed information for the selected recruiter
        ObservableList<String> recruiterInfo = FXCollections.observableArrayList(hrService.fetchRecruiterInformation(recruiterId));

        // Set the recruiter info in the ListView
        listview.setItems(recruiterInfo);
    }

    private void displayHrInfo(Integer hrId) {
        // Fetch detailed information for the selected recruiter
        ObservableList<String> hrInfo = FXCollections.observableArrayList(hrService.fetchHrInformation(hrId));

        // Set the recruiter info in the ListView
        listview.setItems(hrInfo);
    }
    @FXML
    public void populaterecruiterreport() {
        // Define options for the report selection
       
        // Fetch recruiter IDs from the service
        List<Integer> recruiterIds = hrService.recruiterIds();  // Fetch recruiter IDs

        // Convert List<Integer> to ObservableList<Integer> for ChoiceBox
        ObservableList<Integer> recruiterIdsList = FXCollections.observableArrayList(recruiterIds);
        recruiterIdChoiceBox.setItems(recruiterIdsList);  // Set the recruiter IDs in the ChoiceBox

        // Fetch and display recruiter information when an ID is selected
        recruiterIdChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayRecruiterInfo(newValue); // Display recruiter info when a new ID is selected
            }
        });
    }

    
    @FXML
    public void populateHrreport() {
    	hrIdChoiceBox.getItems().addAll(hrService.HrIds());

        // Fetch and display recruiter information when an ID is selected
    	hrIdChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayHrInfo(newValue);
                
                
            }
        });
    }
    
    @FXML
    public void populateapplicantreport() {
        // Define options for sorting (CGPA or Experience)
        ObservableList<String> sorting = FXCollections.observableArrayList(
                "CGPA", "Experience"
        );
        sorton.setItems(sorting);  // Set sorting options for "sorton" ChoiceBox

        // Define options for sorting order (Ascending or Descending)
        ObservableList<String> order = FXCollections.observableArrayList(
                "Ascending", "Descending"
        );
        sortby.setItems(order);  // Set order options for "sortby" ChoiceBox

        // Add event listeners to detect changes in sorting and order options
        sorton.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                applySorting(); // Apply sorting when sorting option changes
            }
        });

        sortby.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                applySorting(); // Apply sorting when sorting order changes
            }
        });
    }
    @FXML
    public void handleadminLogin(ActionEvent event) throws IOException {
    	String adminemail = aemailField.getText().trim();
    String  adminpassword = apasswordField.getText().trim();
        if (adminemail.isEmpty() || adminpassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Email or Password cannot be empty.");
            return;
        }

        boolean isValidUser = hrService.validateAdminUser(adminemail, adminpassword);
        if (isValidUser) {
        	
            // Store the logged-in email
        	showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome Admin");
            switchtotype(event);
           
            
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Email or Password.");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
    @FXML
    public void switchtoadminlogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("adminlogin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void switchtotype(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("admin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void recruiterReport(ActionEvent event) throws IOException {
   	  FXMLLoader loader = new FXMLLoader(getClass().getResource("recruiterreport.fxml"));
         root = loader.load();
         Admin_scenecontroller controller = loader.getController();
         controller.populaterecruiterreport();
        

         stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
    }  
    @FXML
    public void HrReport(ActionEvent event) throws IOException {
   	  FXMLLoader loader = new FXMLLoader(getClass().getResource("hrreport.fxml"));
         root = loader.load();
         Admin_scenecontroller controller = loader.getController();
         controller.populateHrreport();
        

         stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
    }
    @FXML
    public void ApplicantReport(ActionEvent event) throws IOException {
   	  FXMLLoader loader = new FXMLLoader(getClass().getResource("applicantreports.fxml"));
         root = loader.load();
         Admin_scenecontroller controller = loader.getController();
         controller.populateapplicantreport();
        

         stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
    }
    @FXML
    private void displayRecommendationCounts() {
        // Fetch the selected HR ID (or Applicant ID, depending on your design)
        Integer selectedHrId =  hrIdChoiceBox.getSelectionModel().getSelectedItem();  // Assuming hrChoiceBox is where you select the HR ID
        if (selectedHrId == null) {
            // Handle case when no HR ID is selected
            return;
        }

        // Get the recommendation counts from the service layer for the selected HR ID
        List<String> counts = hrService.getRecommendationCounts(selectedHrId);

        // Assuming you have a ListView to display the counts
        ObservableList<String> countsList = FXCollections.observableArrayList(counts);  // Convert List<String> to ObservableList
        listview.setItems(countsList);  // Display the counts in the ListView
    }
    @FXML
    private void displayTotalJobsPosted() {
        // Get the selected recruiter ID from the ChoiceBox or another UI component
        Integer selectedRecruiterId = recruiterIdChoiceBox.getSelectionModel().getSelectedItem(); // Get the selected recruiter ID

        if (selectedRecruiterId != null) {
            // Call the HrService to get total jobs posted for the selected recruiter
            int totalJobs = hrService.gettotaljobposted(selectedRecruiterId);

           
            // If you want to display the total jobs in a ListView instead, do it like this:
            ObservableList<String> jobCountList = FXCollections.observableArrayList("Total Jobs Posted: " + totalJobs);
            listview.setItems(jobCountList); // Update the ListView with the total job count
        }
    }
}
