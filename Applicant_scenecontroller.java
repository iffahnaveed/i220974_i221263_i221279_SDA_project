package application;

import javafx.fxml.FXML;

import java.sql.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import java.io.IOException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import logic_layer.Applicant_service;
import database_logic.ApplicantDao;
class ShareDataApp {
    private static ShareDataApp instance; // Singleton instance
    private int applicantId;           // Shared data: applicant_id

    // Private constructor to prevent instantiation
    private ShareDataApp() {}

    // Get the singleton instance
    public static ShareDataApp getInstance() {
        if (instance == null) {
            instance = new ShareDataApp();
        }
        return instance;
    }

    // Getter for applicantId
    public int getApplicantId() {
        return applicantId;
    }

    // Setter for applicantId
    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }
}
public class Applicant_scenecontroller implements Initializable{

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Injecting the Applicant_service
    private Applicant_service applicantService = new Applicant_service();

    // Fields for login form
    @FXML
    private ListView<String> jobsListView;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<Integer> ageComboBox;
    @FXML
    private ComboBox<Integer>jobIdComboBox;
   
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private ComboBox<Integer> experienceComboBox;
    @FXML
    private ComboBox<String> subjectComboBox;

    @FXML
    private TextField universityField;
    @FXML
    private TextField cgpaField;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private DatePicker yearCompletedPicker;
    @FXML
    private ComboBox<Integer> recruiterIdComboBox;
    @FXML
    private ComboBox<Integer> hrIdComboBox;
    @FXML
    private ComboBox<Integer> applicantIdComboBox;
    @FXML
    private ComboBox<Integer> applicantComboBox;
    @FXML
    private ComboBox<Integer> applicationIdComboBox;
    
    @FXML
    private TextArea messageTextArea;
    @FXML
    private ListView<String> portfolioListView;
    @FXML
    private ComboBox<Integer> jobid; // Job ID ComboBox
    @FXML
    private ListView<String> statusListView; // ListView to display the job status
    @FXML
    private ComboBox<Integer> applicantIdComboBox2;
    @FXML
    private TextArea messageArea;
    @FXML
    private ListView<String> recommendationListView;
    @FXML
    private ListView<String> appointmentListView;
    @FXML
    private void handleViewRecommendations() {
        // Clear the ListView before fetching new data
        recommendationListView.getItems().clear();

        // Get the selected applicant ID from the ComboBox
        Integer selectedApplicantId = applicantIdComboBox2.getValue();

        if (selectedApplicantId == null) {
            recommendationListView.getItems().add("Please select an applicant ID.");
            return;
        }

        try {
            // Fetch the recommendations from the service
            List<String> recommendations = applicantService.fetchRecommendationsForApplicant(selectedApplicantId);

            if (recommendations.isEmpty()) {
                recommendationListView.getItems().add("No recommendations found for this applicant.");
            } else {
                // Add each recommendation to the ListView
                recommendationListView.getItems().addAll(recommendations);
            }
        } catch (SQLException e) {
            recommendationListView.getItems().add("Failed to fetch recommendations. Please try again.");
            e.printStackTrace();
        }
    }
    @FXML
    public void handleCheckStatus() {
        // Clear the ListView before adding new items
        statusListView.getItems().clear();

        // Get the selected applicant ID from the ComboBox
        Integer selectedApplicantId = applicationIdComboBox.getValue();
        Integer selectedJobId = jobIdComboBox.getValue();
        if (selectedApplicantId == null) {
            statusListView.getItems().add("Please select an application ID.");
            return;
        }

        try {
            // Fetch the application status message
            String statusMessage = applicantService.getApplicationStatusMessage(selectedApplicantId,selectedJobId);

            // Add the message to the ListView
            statusListView.getItems().add(statusMessage);
        } catch (SQLException e) {
            statusListView.getItems().add("Failed to fetch application status. Please try again.");
            e.printStackTrace();
        }
    }
    @FXML
    private void handleGetStatus() {
        if (jobid.getValue() == null) {
            System.out.println("Please select a Job ID first.");
            return;
        }

        int selectedJobId = jobid.getValue(); // Get the selected Job ID
        int applicantId = ShareDataApp.getInstance().getApplicantId(); // Get the logged-in applicant's ID
        statusListView.getItems().clear(); // Clear previous status messages

        try {
            // Fetch the status message using the service
            String statusMessage = applicantService.getJobStatusMessage(selectedJobId, applicantId);

            // Populate the ListView with the status message
            statusListView.getItems().add("Job ID: " + selectedJobId);
            statusListView.getItems().add("Status: " + statusMessage);

            System.out.println("Status retrieved and displayed: " + statusMessage);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving status for Job ID: " + selectedJobId);
            statusListView.getItems().add("Error retrieving status for Job ID: " + selectedJobId);
        }
    }
    @FXML
    private void handleReceiveMessage() {
        // Clear the message area before displaying new messages
        messageArea.clear();

        // Ensure HR ID and Applicant ID are selected
        Integer selectedHrId = hrIdComboBox.getValue();
        Integer selectedApplicantId = ShareDataApp.getInstance().getApplicantId();

        if (selectedHrId == null || selectedApplicantId == null) {
            messageArea.setText("Please select both HR ID and Applicant ID.");
            return;
        }

        // Fetch formatted messages from the service
        Applicant_service applicantService = new Applicant_service();
        String formattedMessages = applicantService.getFormattedMessagesForHr(selectedHrId, selectedApplicantId);

        // Display the messages in the message area
        messageArea.setText(formattedMessages);
    }
    @FXML
    private void handleReceiveRecruiterMessage() {
        // Clear the message area before displaying new messages
        messageArea.clear();

        // Ensure Recruiter ID and Applicant ID are selected
        Integer selectedRecruiterId = recruiterIdComboBox.getValue(); // Assume recruiterIdComboBox exists
        Integer selectedApplicantId = ShareDataApp.getInstance().getApplicantId();
        
        if (selectedRecruiterId == null || selectedApplicantId == null) {
            messageArea.setText("Please select both Recruiter ID and Applicant ID.");
            return;
        }

        // Fetch formatted messages from the service
        Applicant_service applicantService = new Applicant_service();
        String formattedMessages = applicantService.getFormattedMessagesForRecruiter(selectedRecruiterId, selectedApplicantId);

        messageArea.setText(formattedMessages);
    }

    @FXML
    private void handleLoadJobId1() {
        if (applicationIdComboBox.getValue() == null) {
            System.out.println("Please select an Application ID first.");
            return;
        }

        int selectedApplicationId = applicationIdComboBox.getValue();
        int jobId = applicantService.getJobIdByApplicationId(selectedApplicationId); // Call service method

        if (jobId != -1) {
            System.out.println("Job ID for Application ID " + selectedApplicationId + ": " + jobId);
            populateRecruiterIdComboBox(jobId); // Populate the ComboBox
            populateJobIdComboBox(jobId);
        } else {
            System.out.println("No job found for Application ID: " + selectedApplicationId);
        }
    }

    @FXML
    private void handleLoadJobId() {
        if (applicationIdComboBox.getValue() == null) {
            System.out.println("Please select an Application ID first.");
            return;
        }

        int selectedApplicationId = applicationIdComboBox.getValue();
        int jobId = applicantService.getJobIdByApplicationId(selectedApplicationId); // Call service method

        if (jobId != -1) {
            System.out.println("Job ID for Application ID " + selectedApplicationId + ": " + jobId);
            populateRecruiterIdComboBox(jobId); // Populate the ComboBox
           // populateJobIdComboBox(jobId);
        } else {
            System.out.println("No job found for Application ID: " + selectedApplicationId);
        }
    }

    private void populateJobIdComboBox(int jobId) {
        jobid.getItems().clear(); // Clear any existing items
        jobid.getItems().add(jobId); // Add the retrieved job ID
        jobid.setValue(jobId); // Optionally set the default selection to the first job ID
        System.out.println("Job ID ComboBox populated with: " + jobId);
    }
    @FXML
    private void handleSubmit(ActionEvent event) throws IOException {
        // Retrieve values from the form
        String qualType = typeComboBox.getValue();
        String university = universityField.getText();
        String yearCompleted = yearCompletedPicker.getValue().toString();
        float cgpa = Float.parseFloat(cgpaField.getText());
        String qualSubject = subjectComboBox.getValue(); // Get the subject from the ComboBox

        // Validate if all required fields are filled
        if (qualType == null || university.isEmpty() || yearCompleted.isEmpty() || qualSubject == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please fill all fields before submitting.");
            return;
        }
        boolean isInserted = applicantService.registerQualification(qualType, university, yearCompleted, cgpa, qualSubject);
        if (isInserted) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Qualification inserted successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to insert qualification.");
        }
    }

    @FXML
    private void applyForJob(ActionEvent event) {
        try {
            // Get selected job ID
            Integer jobId = jobIdComboBox.getValue();
            if (jobId == null) {
                showAlert(Alert.AlertType.ERROR, "Application Error", "Please select a job.");
                return;
            }
            int applicantId =ShareDataApp.getInstance().getApplicantId();
            int qualificationId = applicantService.getQualificationId(applicantId);
            int experience = applicantService.getExperience(applicantId);
            boolean isApplicationSuccessful = applicantService.applyForJob(ShareDataApp.getInstance().getApplicantId(), jobId, qualificationId, experience);
            if (isApplicationSuccessful)
                showAlert(Alert.AlertType.INFORMATION, "Success", "Job application submitted!");
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + e.getMessage());
        }
    }
    @Override
   
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initializing controller...");
        System.out.println("jobsListView: " + jobsListView);
        int applicantId1 = ShareDataApp.getInstance().getApplicantId();
        populateApplicantIdComboBox2(applicantId1);
        populateJobsListView();
        populateAgeComboBox();
        populateExperienceComboBox();
        populateTypeComboBox();
        populateGenderComboBox();
        populateSubjectComboBox();
        populateJobIdComboBox();
        populateApplicantIdComboBox();
        populateApplicantIdComboBox1();
        int applicantId = ShareDataApp.getInstance().getApplicantId();
        populateApplicationIdComboBox(applicantId);
    }
    private void populateApplicantIdComboBox2(int applicantId) {
        if (applicantIdComboBox2 != null) {
            applicantIdComboBox2.getItems().clear(); // Clear existing items if any
            applicantIdComboBox2.getItems().add(applicantId); // Add the passed applicantId
            applicantIdComboBox2.setValue(applicantId); // Set the default value to the passed applicantId
        } else {
            System.out.println("applicantIdComboBox is not properly initialized.");
        }
    }

    private void populateRecruiterIdComboBox(int jobId) {
        if (recruiterIdComboBox != null) { // Ensure the ComboBox is initialized
            recruiterIdComboBox.getItems().clear(); // Clear existing items to avoid duplicates

            // Fetch recruiter IDs via Applicant_service
            List<Integer> recruiterIds = applicantService.getRecruiterIds(jobId);

            
            if (!recruiterIds.isEmpty()) {
                recruiterIdComboBox.getItems().addAll(recruiterIds);
                System.out.println("Recruiter IDs for Job ID " + jobId + ": " + recruiterIds);
            } else {
                System.out.println("No recruiter found for Job ID: " + jobId);
            }
        } else {
            System.out.println("recruiterIdComboBox is not properly initialized.");
        }
    }
    private void populateApplicantIdComboBox() {
        if (applicantIdComboBox != null) { // Ensure the ComboBox is initialized
            applicantIdComboBox.getItems().clear(); // Clear existing items

            // Fetch applicant IDs using Applicant_service
            List<Integer> applicantIds = applicantService.getApplicantIds();

            if (!applicantIds.isEmpty()) {
                applicantIdComboBox.getItems().addAll(applicantIds);
                System.out.println("Applicant IDs loaded: " + applicantIds);
            } else {
                System.out.println("No applicant IDs found in the database.");
            }
        } else {
            System.out.println("applicantId ComboBox is not properly initialized.");
        }
    }
    
    private void populateApplicationIdComboBox(int applicantId) {
        if (applicationIdComboBox != null) { // Ensure the ComboBox is initialized
            applicationIdComboBox.getItems().clear(); // Clear existing items

            // Fetch application IDs using Applicant_service
            List<Integer> applicationIds = applicantService.getApplicationIdsForApplicant(applicantId);

            if (!applicationIds.isEmpty()) {
                applicationIdComboBox.getItems().addAll(applicationIds);
                System.out.println("Application IDs successfully populated for applicant ID: " + applicantId);
            } else {
                System.out.println("No application IDs found for applicant ID: " + applicantId);
            }
        } else {
            System.out.println("applicationIdComboBox is not properly initialized.");
        }
    }
    private void populateApplicantIdComboBox1() {
        if (applicantComboBox != null) { // Ensure the ComboBox is initialized
            applicantComboBox.getItems().clear(); // Clear existing items

            List<Integer> applicantIds = applicantService.getApplicantIds();

            if (!applicantIds.isEmpty()) {
                applicantComboBox.getItems().addAll(applicantIds);
                System.out.println("Applicant IDs successfully populated.");
            } else {
                System.out.println("No applicant IDs found in the database.");
            }
        } else {
            System.out.println("applicantComboBox is not properly initialized.");
        }
    }
    @FXML
    public void handleViewPortfolio() {
        // Clear the ListView before adding new data
        portfolioListView.getItems().clear();

        // Get the selected applicant ID
        Integer selectedApplicantId = applicantIdComboBox2.getValue();
        if (selectedApplicantId == null) {
            portfolioListView.getItems().add("Please select an applicant ID.");
            return;
        }

        // Fetch the portfolio details using the Service layer
        List<String> portfolioDetails = applicantService.getApplicantPortfolio(selectedApplicantId);

        if (portfolioDetails.isEmpty()) {
            portfolioListView.getItems().add("No portfolio details found for the selected applicant.");
        } else {
            portfolioListView.getItems().addAll(portfolioDetails);
        }
    }

    @FXML
    private void handleLoadHrIds() {
        // Ensure an applicant is selected
        Integer selectedApplicantId = applicantIdComboBox2.getValue();

        if (selectedApplicantId == null) {
            System.out.println("Please select an Applicant ID first.");
            return;
        }

        // Clear the HR ID combo box before populating
        hrIdComboBox.getItems().clear();

        // Fetch HR IDs associated with the selected applicant
        List<Integer> hrIds = applicantService.fetchHrIdsForApplicant(selectedApplicantId);

        if (hrIds.isEmpty()) {
            System.out.println("No HR IDs found for Applicant ID: " + selectedApplicantId);
            return;
        }

        // Populate the HR ID combo box
        hrIdComboBox.getItems().addAll(hrIds);
        System.out.println("HR IDs loaded for Applicant ID: " + selectedApplicantId);
    }
    @FXML
    private void handleSendMessage() {
        int selectedRecruiterId = recruiterIdComboBox.getSelectionModel().getSelectedItem();
        String message = messageTextArea.getText();

        int applicantId = ShareDataApp.getInstance().getApplicantId();

        Applicant_service applicantService = new Applicant_service();
        if (!applicantService.validateMessageInputs(message, selectedRecruiterId)) {
            return; 
        }

        boolean isMessageSent = applicantService.sendMessage(selectedRecruiterId, applicantId, message);

        if (isMessageSent) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Message sent successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to send the message. Please try again.");
        }
    }
    
    @FXML
    private void handleSendMessagehr() {
        int selectedHRId = hrIdComboBox.getSelectionModel().getSelectedItem();
        String message = messageTextArea.getText();

        // Retrieve the logged-in applicant's ID
        int applicantId = ShareDataApp.getInstance().getApplicantId();
        Applicant_service applicantService = new Applicant_service();
        if (!applicantService.validateMessageInputs(message, selectedHRId )) {
            return; 
        }
    
        boolean success = applicantService.sendMessageToHR(selectedHRId, applicantId, message);

        if (success) {
        	 showAlert(Alert.AlertType.INFORMATION, "Success", "Message sent successfully.");
        } else {
        	 showAlert(Alert.AlertType.ERROR, "Error", "Failed to send the message. Please try again.");
        }
    }

    
    
    private void populateJobIdComboBox() {
        if (jobIdComboBox != null) {
            List<Integer> jobIds = applicantService.getJobIds();
            jobIdComboBox.setItems(FXCollections.observableArrayList(jobIds));
        } else {
            System.out.println("...");
        }
    }

    private void populateJobsListView() {
        if (jobsListView == null) {
            System.out.println("...");
            return;
        }

        Applicant_service applicantService = new Applicant_service();
        List<String> jobs = applicantService.getJobDetails();
        ObservableList<String> jobList = FXCollections.observableArrayList(jobs);

        jobsListView.setItems(jobList);
    }

    private void populateAgeComboBox() {
        if (ageComboBox != null) {
            Applicant_service applicantService = new Applicant_service();
            List<Integer> ages = applicantService.getAgeRange();
            ageComboBox.getItems().setAll(ages);
        } else {
            System.out.println("...");
        }
    }

    private void populateExperienceComboBox() {
        if (experienceComboBox != null) {
            Applicant_service applicantService = new Applicant_service();
            List<Integer> experiences = applicantService.getExperienceRange();
            experienceComboBox.getItems().setAll(experiences);
        } else {
            System.out.println("...");
        }
    }

    private void populateSubjectComboBox() {
        if (subjectComboBox != null) {
            Applicant_service applicantService = new Applicant_service();
            List<String> subjects = applicantService.getSubjects();
            subjectComboBox.getItems().setAll(subjects);
        } else {
            System.out.println("...");
        }
    }

    private void populateTypeComboBox() {
        if (typeComboBox != null) {
            Applicant_service applicantService = new Applicant_service();
            List<String> types = applicantService.getQualificationTypes();
            typeComboBox.getItems().setAll(types);
        } else {
            System.out.println("...");
        }
    }

    private void populateGenderComboBox() {
        if (genderComboBox != null) {
            Applicant_service applicantService = new Applicant_service();
            List<String> genders = applicantService.getGenders();
            genderComboBox.getItems().setAll(genders);
        } else {
            System.out.println("...");
        }
    }

    @FXML
    
    public void handleLogin(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        int applicantId = applicantService.validateUser(email, password);

        if (applicantId != -1) {
            ShareDataApp.getInstance().setApplicantId(applicantId);
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, " + email + "!");
            root = FXMLLoader.load(getClass().getResource("menu.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            // Show an error alert if login fails
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }


    @FXML
    public void switchtologin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void switchtosignup(ActionEvent event) throws IOException {
        // Load the signup.fxml and get the controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
        root = loader.load();
        Applicant_scenecontroller signupController = loader.getController();
        signupController.populateAgeComboBox();
        signupController.populateExperienceComboBox();
        signupController.populateTypeComboBox();
        signupController.populateGenderComboBox();
        signupController.populateSubjectComboBox();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void register(ActionEvent event) throws IOException {
        // Collect data from the form
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        Integer age = ageComboBox.getValue();
        String gender = genderComboBox.getValue();
        Integer experience = experienceComboBox.getValue();

        // Validate input values
        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            age == null || gender == null || experience == null) {
        	  showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

   
        boolean isRegistered = applicantService.register(name, email, password, age, gender, experience);
        if (isRegistered) {
        	 showAlert(Alert.AlertType.INFORMATION, "Success", "Applicant registered successfully!");
          
        } else {
        	 showAlert(Alert.AlertType.ERROR, "Registration Error", "Failed to register applicant. Please try again.");
        }
    }

   
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void viewavailablejobs(ActionEvent event) throws IOException {
    	 FXMLLoader loader = new FXMLLoader(getClass().getResource("viewavailablejobs.fxml"));
         root = loader.load();
         Applicant_scenecontroller jobsController = loader.getController();

         // Call the populateComboBox methods from the signup controller
         jobsController.populateJobsListView();
         jobsController.populateJobIdComboBox();

         // Switch to the signup scene
         stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
    }
    public void viewportfolio(ActionEvent event) throws IOException  {
    	 FXMLLoader loader = new FXMLLoader(getClass().getResource("viewportfolio.fxml"));
         root = loader.load();
         stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
    }
    public void recievemessagehr(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("recievemessagehr.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchtomenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void sendmessagehr(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("sendmessagehr.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void recievemessagerecruiter(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("recievemessagerecruiter.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void sendmessagerecruiter(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("sendmessagerecruiter.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void viewstatus(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("viewstatus.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void loadAppointments() {
        try {
        	int applicantId = ShareDataApp.getInstance().getApplicantId();
        	int recruiterId= recruiterIdComboBox.getValue();
            List<String> appointments = applicantService.getAllAppointments(applicantId,recruiterId);
            appointmentListView.getItems().setAll(appointments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void loadContracts() {
        try {
        	int applicantId = ShareDataApp.getInstance().getApplicantId();
        	int jobId=jobid.getValue();
            List<String> appointments = applicantService.getAllContracts(applicantId,jobId);
            statusListView.getItems().setAll(appointments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void View_Contracts(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("View_contract.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void View_appointments(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("View_appointment.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void viewnotifications(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("viewnotifications.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}