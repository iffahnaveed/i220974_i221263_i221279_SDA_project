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
import java.time.format.DateTimeFormatter;
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
import logic_layer.Recruiter_service;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
class SharedData {
    private static SharedData instance;
    private int recruiter_i;

    private SharedData() {}  // Private constructor to prevent instantiation

    public static SharedData getInstance() {
        if (instance == null) {
            instance = new SharedData();
        }
        return instance;
    }

    public int getRecruiter_i() {
        return recruiter_i;
    }

    public void setRecruiter_i(int recruiter_i) {
        this.recruiter_i = recruiter_i;
    }
}

public class scenecontroller {
    public int recruiter_Id;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> universityComboBox;
    @FXML
    private ComboBox<Integer> yearComboBox;
    @FXML
    private TextField cgpaField;
    @FXML
    private RadioButton postJobRadioButton;
    @FXML
    private RadioButton viewApplicationsRadioButton;
    @FXML
    private RadioButton sendMessageRadioButton;
    @FXML
    private RadioButton receiveMessageRadioButton;
    @FXML
    private RadioButton viewShortlistApplicantRadioButton;
    @FXML
    private RadioButton generateReportRadioButton;
    @FXML
    private RadioButton createTestRadioButton;
    @FXML
    private RadioButton viewPortfolioRadioButton;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField universityField;
    @FXML
    private ComboBox<Integer> ageComboBox;
    @FXML
    private ComboBox<Integer> ageComboBox1;
    @FXML
    private ComboBox<Integer> experienceComboBox;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private DatePicker yearPicker;
    @FXML
    private TextField jobTitleField;
    @FXML
    private TextField jobDescriptionField;
    @FXML
    private TextField AppointmentDescription;
    @FXML
    private TextField companyNameField;
    @FXML
    private TextField qualificationsField;
    @FXML
    private TextField qualificationsField1;
    @FXML
    private ComboBox<String> jobTypeComboBox;
    @FXML
    private ComboBox<String> FieldTypeComboBox;
    @FXML
    private DatePicker applicationDeadlinePicker;
    @FXML
    private DatePicker appointment_date;
    @FXML
    private ComboBox<Integer> jobComboBox; // ComboBox for job IDs
    @FXML
    private ComboBox<Integer> MarksComboBox; // ComboBox for job IDs
    @FXML
    private ComboBox<Integer> Applicant_ids_ComboBox; // ComboBox for job IDs
    @FXML
    private TableView<application_model> applicantsTable;

    @FXML
    private TableColumn<application_model, Integer> idColumn;

    @FXML
    private TableColumn<application_model, String> dateColumn;

    @FXML
    private TableColumn<application_model, Double> gpaColumn;

    @FXML
    private TableColumn<application_model, Integer> experienceColumn;
    @FXML
    private Button loadButton;

    private Recruiter_service recruiterService;
    @FXML
    private TableView<Recruiter_model> recruiterTable;  // Your TableView in FXML
    @FXML
    private TableColumn<Recruiter_model, Integer> recruiterIdColumn;
    @FXML
    private TableColumn<Recruiter_model, String> nameColumn;
    @FXML
    private TableColumn<Recruiter_model, String> emailColumn;
    @FXML
    private TableColumn<Recruiter_model, Integer> ageColumn;
    @FXML
    private TableColumn<Recruiter_model, String> genderColumn;
    @FXML
    private TableColumn<Recruiter_model, Integer> experienceColumn_1;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private TableView<MessageRecord> messageTableView;
    @FXML
    private TableColumn<MessageRecord, Integer> applicantIdColumn;
    @FXML
    private TableColumn<MessageRecord, String> messageColumn;
    @FXML
    private ListView<String> Applicant_information;
    @FXML
    private ListView<String> Recruiter_Qualification_information;
    @FXML
    private ComboBox<Integer> hourComboBox;

    @FXML
    private ComboBox<Integer> minuteComboBox;
    public scenecontroller() {
        this.recruiterService = new Recruiter_service(); // Initialize the Recruiter_service
    }
    public void populate_time()
    {
    	for (int i = 0; i < 24; i++) {
            hourComboBox.getItems().add(i);
        }

        // Populate minutes (0-59)
        for (int i = 0; i < 60; i++) {
            minuteComboBox.getItems().add(i);
        }

        // Set default values
        hourComboBox.setValue(12); // Default hour
        minuteComboBox.setValue(0); // Default minute
    }
    public void populateListView() {
    	int applicant_id=Applicant_ids_ComboBox.getValue();
        ObservableList<String> applicantQualifications = recruiterService.getApplicantQualifications(applicant_id);
        Applicant_information.setItems(applicantQualifications); // Bind data to ListView
    }
    public void populateRecruiterQualificationsListView() {      
        ObservableList<String> recruiterQualifications = recruiterService.getRecruiterQualifications(SharedData.getInstance().getRecruiter_i());
        Recruiter_Qualification_information.setItems(recruiterQualifications); // Bind data to ListView
    }

    public void populateMarksComboBoxes()
    {
    	 ObservableList<Integer> MarksList = FXCollections.observableArrayList();
    	 int jobId=jobComboBox.getValue();
 	    for (int i = 1; i <= recruiterService.get_no_of_questions(jobId); i++) {
 	        MarksList.add(i);
 	    }
 	    MarksComboBox.setItems(MarksList);
 	  
    	
    }
    public void populateTestComboBoxes()
    {
    	 ObservableList<Integer> MarksList = FXCollections.observableArrayList();
 	    for (int i = 1; i <= 50; i++) {
 	        MarksList.add(i);
 	    }
 	   ageComboBox.setItems(MarksList);
 	   ageComboBox1.setItems(MarksList);
    	
    }
    public void populateComboBoxes1()
    {
    	ObservableList<Integer> experienceList = FXCollections.observableArrayList();
	    for (int i = 1; i <= 50; i++) {
	        experienceList.add(i);
	    }
	    experienceComboBox.setItems(experienceList);
	    ObservableList<String> jobTypeList = FXCollections.observableArrayList(
	    		"Full Time",          // New job type
	    	    "Contract",           // New job type
	    	    "Internship",         // New job type
	    	    "Freelance",
	    	    "Remote"
            );
            jobTypeComboBox.setItems(jobTypeList);
            
    	
    }
    public void populateComboBoxes() {
        // Populate ageComboBox
    	 ObservableList<Integer> ageList = FXCollections.observableArrayList();
    	    for (int i = 1; i <= 100; i++) {
    	        ageList.add(i);
    	    }
    	    ageComboBox.setItems(ageList);

    	    // Populate experienceComboBox with values from 1 to 50
    	    ObservableList<Integer> experienceList = FXCollections.observableArrayList();
    	    for (int i = 1; i <= 50; i++) {
    	        experienceList.add(i);
    	    }
    	    experienceComboBox.setItems(experienceList);

        // Populate genderComboBox
        ObservableList<String> genderList = FXCollections.observableArrayList("Male", "Female", "Other");
        genderComboBox.setItems(genderList);

        // Populate typeComboBox
        ObservableList<String> typeList = FXCollections.observableArrayList("Bachelors", "Masters", "PHD");
        typeComboBox.setItems(typeList);
        ObservableList<String> FieldTypeList = FXCollections.observableArrayList(
	    		"Computer Science",          // New job type
	    	    "Software Engineering",           // New job type
	    	    "Datascience",         // New job type
	    	    "Robotics",
	    	    "AI"
            );
            FieldTypeComboBox.setItems(FieldTypeList);
    }
    @FXML
    public void handleSignUp(ActionEvent event) throws IOException {
        // Collect recruiter details from the form
        String recruiterName = nameField.getText().trim();
        String recruiterEmail = emailField.getText().trim();
        String recruiterPassword = passwordField.getText().trim();
        int recruiterAge = ageComboBox.getValue();
        String recruiterGender = genderComboBox.getValue();
        int recruiterExperience = experienceComboBox.getValue();
       
        // Validate fields
        if (recruiterName.isEmpty() || recruiterEmail.isEmpty() || recruiterPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled.");
            return;
        }
        // Call service method to insert recruiter and qualifications
        boolean success = recruiterService.register(recruiterName, recruiterEmail, recruiterPassword, recruiterAge, recruiterGender, recruiterExperience);
                                                                     
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Recruiter registered successfully.");
           
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Registration failed. Please try again.");
        }
    }

    @FXML
    public void handleSubmitQualification(ActionEvent event) {
        // Collect qualification details from the form
        String qualType = typeComboBox.getValue();
		String university = universityField.getText().trim(); // University is a TextField
        LocalDate yearCompletedDate = yearPicker.getValue(); // Year is selected via DatePicker
        float cgpa = Float.parseFloat(cgpaField.getText().trim());
        String recruiterField = FieldTypeComboBox.getValue();
        // Validate qualification fields
        if (qualType == null || qualType.isEmpty() || university.isEmpty() || yearCompletedDate == null || cgpa <= 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled, and CGPA should be positive.");
            return;
        }

        // Convert yearCompletedDate to SQL Date (if necessary)
        Date sqlYearCompletedDate = Date.valueOf(yearCompletedDate); // Convert LocalDate to Date

        // Call DAO to insert qualification
        boolean success = recruiterService.submitQualification(qualType, university, sqlYearCompletedDate, cgpa,recruiterField);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Qualification submitted successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Qualification submission failed. Please try again.");
        }
    }

    @FXML
    public void handlePostJob_1(ActionEvent event) {
        // Collect job post details from the form
        String jobTitle = jobTitleField.getText().trim();
        String jobDescription = jobDescriptionField.getText().trim();
        String companyName = companyNameField.getText().trim();
        String qualifications = qualificationsField.getText().trim();
        String jobType = jobTypeComboBox.getValue();
        Integer experienceRequired = experienceComboBox.getValue();
        java.time.LocalDate applicationDeadline = applicationDeadlinePicker.getValue();
        String cgpaInput = qualificationsField1.getText().trim(); // CGPA input field

        // Validate fields
        if (jobTitle.isEmpty() || jobDescription.isEmpty() || companyName.isEmpty() || qualifications.isEmpty() || jobType == null || experienceRequired == null || applicationDeadline == null || cgpaInput.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled.");
            return;
        }

        // Parse and validate CGPA
        float cgpaRequired;
        try {
            cgpaRequired = Float.parseFloat(cgpaInput);
            if (cgpaRequired < 0 || cgpaRequired > 4) {
                showAlert(Alert.AlertType.ERROR, "Error", "CGPA must be between 0 and 4.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "CGPA must be a valid number.");
            return;
        }

        // Convert LocalDate to SQL Date
        Date sqlApplicationDeadline = Date.valueOf(applicationDeadline);

        // Call service method to post job
        boolean success = recruiterService.postJob(jobTitle, jobDescription, companyName, qualifications, cgpaRequired, jobType, experienceRequired, sqlApplicationDeadline, SharedData.getInstance().getRecruiter_i());

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Job posted successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Job posting failed. Please try again.");
        }
    }
    @FXML
    public void handleSubmissonTest(ActionEvent event) throws IOException {
        try {
            // Retrieve selected values from the ComboBoxes
            Integer selectedJobId = jobComboBox.getValue();
            Integer selectedApplicantId = Applicant_ids_ComboBox.getValue();
            Integer selectedMarks = MarksComboBox.getValue();

            if (selectedJobId == null || selectedApplicantId == null || selectedMarks == null) {
                // Show alert if any required field is missing
                showAlert(Alert.AlertType.WARNING, "Input Missing", "Please select a job, applicant, and marks.");
                return;
            }

            // Get the recruiter ID from shared data
            int recruiterId = SharedData.getInstance().getRecruiter_i();

            // Insert into test_taken and conditionally into shortlist_after_test
            boolean success = recruiterService.insertTestAndShortlist(selectedJobId, recruiterId, selectedApplicantId, selectedMarks);

            if (success) {
                // Show success alert
                showAlert(Alert.AlertType.INFORMATION, "Success", "Test score submitted successfully.");
            } else {
                // Show failure alert
                showAlert(Alert.AlertType.ERROR, "Submission Failed", "Failed to submit the test score.");
            }
        } catch (Exception e) {
            // Show error alert in case of exception
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    public void handleShortlistAfterInterview(ActionEvent event) {
        try {
            // Retrieve selected values from the ComboBoxes
            Integer selectedApplicantId = Applicant_ids_ComboBox.getValue();
            Integer selectedJobId = jobComboBox.getValue();

            // Check if all required fields are selected
            if (selectedApplicantId == null || selectedJobId == null) {
                // Show alert if fields are missing
                showAlert(Alert.AlertType.WARNING, "Input Missing", "Please select an applicant and a job.");
                return;
            }

            // Insert into the shortlist_after_interview table
            boolean success = recruiterService.insertIntoShortlistAfterInterview(selectedApplicantId, selectedJobId);

            if (success) {
                // Show success alert
                showAlert(Alert.AlertType.INFORMATION, "Success", "Applicant successfully shortlisted for the job.");
            } else {
                // Show failure alert
                showAlert(Alert.AlertType.ERROR, "Shortlisting Failed", "Failed to shortlist the applicant.");
            }
        } catch (Exception e) {
            // Handle any exceptions and display an error alert
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Email or Password cannot be empty.");
            return;
        }
        int recriuter_id=recruiterService.validateUser(email, password);
        // Call the Recruiter_service to validate credentials
        if (recruiterService.validateUser(email, password)==-1) {
            // If login is successful, switch to the menu
        	// If login fails, show an error alert
        	
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Email or Password.");
           
        } else {
        	SharedData.getInstance().setRecruiter_i(recruiterService.validateUser(email, password));
            switchtomenu(event);
        }
    }
    // Show alert method
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
    @FXML
    public void switchtologin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("recruiterlogin.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void switchtomenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Recruiter_menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void switchToSignUp(ActionEvent event) throws IOException {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Sign_up_recruiter.fxml"));
        root = loader.load();  // This loads the FXML file and initializes the controller
        
        // Get the controller of the FXML
        scenecontroller controller = loader.getController();
        
        // Call populateComboBoxes() after loading the FXML and initializing the controller
        controller.populateComboBoxes();  // Populate the combo boxes with data
        
        // Switch the scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void handlePostJob(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Job_post.fxml"));
        root = loader.load();  // This loads the FXML file and initializes the controller
        // Get the controller of the FXML
        scenecontroller controller = loader.getController();
        // Call populateComboBoxes() after loading the FXML and initializing the controller
        controller.populateComboBoxes1();  // Populate the combo boxes with data
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println(SharedData.getInstance().getRecruiter_i());
    }
    @FXML
    public void populateMessageTable() {
        try {
            // Bind the table columns to the MessageRecord properties
            applicantIdColumn.setCellValueFactory(new PropertyValueFactory<>("applicantId"));
            messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
            List<MessageRecord> messages=recruiterService.getMessagesByApplicantId(Applicant_ids_ComboBox.getValue());
            // Clear previous items and populate the table with new data
            messageTableView.getItems().clear();
            if (messages == null || messages.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Messages Found", "No messages are available for the selected job.");
            } else {
            	messageTableView.getItems().addAll(messages);
            }
        } catch (Exception e) {
            System.err.println("Error populating the table: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while populating the table. Please try again.");
        }
    }

    @FXML
    public void populate_table() {
        try {
            // Bind columns to properties in application_model
            idColumn.setCellValueFactory(new PropertyValueFactory<>("applicantId"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            gpaColumn.setCellValueFactory(new PropertyValueFactory<>("gpa"));
            experienceColumn.setCellValueFactory(new PropertyValueFactory<>("experience"));

            // Get selected Job ID
            Integer selectedJobId = jobComboBox.getValue();
            if (selectedJobId == null) {
                showAlert(Alert.AlertType.WARNING, "No Job Selected", "Please select a job ID to view applications.");
                return;
            }

            // Fetch applications for the selected Job ID
            List<application_model> applications = recruiterService.getApplicationsByJobId(selectedJobId);

            // Clear the table and populate it with new data
            applicantsTable.getItems().clear();
            if (applications == null || applications.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Applications Found", 
                          "There are no applications for the selected job.");
            } else {
                applicantsTable.getItems().addAll(applications);
            }
        } catch (Exception e) {
            System.err.println("Error fetching applications: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching applications. Please try again.");
        }
    }

    public void populateJobComboBox() {
        // Get the list of job IDs for the current recruiter
        List<Integer> jobIds = recruiterService.getJobIdsByRecruiter(SharedData.getInstance().getRecruiter_i());

        // Clear previous items and add the new job IDs to ComboBox
        jobComboBox.getItems().clear();
        jobComboBox.getItems().addAll(jobIds);

        if (!jobIds.isEmpty()) {
            jobComboBox.getSelectionModel().selectFirst();
        }
       
    }
    public void loadApplicantsForSelectedJob() {
        Integer selectedJobId = jobComboBox.getValue();

        if (selectedJobId == null) {
            // Handle the case when no job is selected, maybe show an error message
            System.out.println("No job selected");
            return;
        }

        // Populate the Applicant ComboBox based on the selected Job ID
        populateApplicantComboBox(selectedJobId);
    } 
    public void loadApplicantsForHr_Interview() {
        Integer selectedJobId = jobComboBox.getValue();

        if (selectedJobId == null) {
            // Handle the case when no job is selected, maybe show an error message
            System.out.println("No job selected");
            return;
        }

        // Populate the Applicant ComboBox based on the selected Job ID
        populateApplicantforhrComboBox(selectedJobId);
    }
    public void populateApplicantComboBox(int Job_id) {
        // Get the list of job IDs for the current recruiter
        List<Integer> Applicant_Ids = recruiterService.getApplicantIdsByRecruiter(SharedData.getInstance().getRecruiter_i(),Job_id);

        // Clear previous items and add the new job IDs to ComboBox
        Applicant_ids_ComboBox.getItems().clear();
        Applicant_ids_ComboBox.getItems().addAll(Applicant_Ids);
    }
    public void populateApplicantforhrComboBox(int Job_id) {
        // Get the list of job IDs for the current recruiter
        List<Integer> Applicant_Ids_forhr = recruiterService.getShortlistedApplicantIds(Job_id);

        // Clear previous items and add the new job IDs to ComboBox
        Applicant_ids_ComboBox.getItems().clear();
        Applicant_ids_ComboBox.getItems().addAll(Applicant_Ids_forhr);
    }
    @FXML
    public void handleViewApplications(ActionEvent event) throws IOException {
        // Load the FXML file for the new scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Current_Applications.fxml"));
        root = loader.load();

        // Get the controller of the newly loaded FXML
        scenecontroller controller = loader.getController();
        controller.populateJobComboBox();
        // Get the stage and scene from the current event to switch to the new scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void send_message_to_applicant() {
        // Get the message from the TextArea
        String message = messageTextArea.getText();
        
        // Get the selected Applicant ID from the ComboBox
        int Applicant_id = Applicant_ids_ComboBox.getValue();
        
        // Call the method to insert the message into the database
        boolean success = recruiterService.insert_into_send_message_recruiter_and_applicant(Applicant_id, message, SharedData.getInstance().getRecruiter_i());

        // Check if the insert was successful
        if (success) {
            // Show a success message (e.g., using an Alert)
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Message Sent");
            alert.setContentText("The message was successfully sent to the applicant.");
            alert.showAndWait();
        } else {
            // Show an error message if something went wrong
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Message Failed");
            alert.setContentText("There was an error sending the message.");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleSendMessage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Send_rmessage.fxml"));
        root = loader.load();
        // Get the controller of the newly loaded FXML
        scenecontroller controller = loader.getController();
        // Populate job ComboBox and ensure a job is selected
        controller.populateJobComboBox();
       
        // Switch to the new scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    @FXML
    public void handleReceiveMessage(ActionEvent event) throws IOException {
    	 FXMLLoader loader = new FXMLLoader(getClass().getResource("recieve_rmessage.fxml"));
         root = loader.load();
         // Get the controller of the newly loaded FXML
         scenecontroller controller = loader.getController();
         // Populate job ComboBox and ensure a job is selected
         controller.populateJobComboBox();
        
         // Switch to the new scene
         stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
    }

    @FXML
    public void handleViewShortlistApplicant(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Shortlist_applicants.fxml"));
        root = loader.load();
        // Get the controller of the newly loaded FXML
        scenecontroller controller = loader.getController();
        // Populate job ComboBox and ensure a job is selected
        controller.populateJobComboBox();
       
        // Switch to the new scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleGenerateReport(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ShortList_for_hr.fxml"));
        root = loader.load();
        // Get the controller of the newly loaded FXML
        scenecontroller controller = loader.getController();
        // Populate job ComboBox and ensure a job is selected
        controller.populateJobComboBox();
        // Switch to the new scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleCreateTest(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("create_test.fxml"));
        root = loader.load();
        // Get the controller of the newly loaded FXML
        scenecontroller controller = loader.getController();
        // Populate job ComboBox and ensure a job is selected
        controller.populateJobComboBox();
        controller.populateMarksComboBoxes();
        // Switch to the new scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }
    @FXML
    public void populateRecruiterTable() {
        try {
            // Bind columns to properties in Recruiter_model
            recruiterIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
            genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
            experienceColumn_1.setCellValueFactory(new PropertyValueFactory<>("experience"));

            // Fetch the recruiter (single recruiter expected)
            Recruiter_model recruiter = recruiterService.getRecruiterByRecruiterId(SharedData.getInstance().getRecruiter_i());

            // Clear the table and populate it with new data
            recruiterTable.getItems().clear();
            if (recruiter == null) {
                showAlert(Alert.AlertType.INFORMATION, "No Recruiter Found", 
                          "There is no recruiter to display.");
            } else {
                recruiterTable.getItems().add(recruiter); // Add the single recruiter to the table
            }
        } catch (Exception e) {
            System.err.println("Error fetching recruiters: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching recruiters. Please try again.");
        }
    }
    @FXML
    public void handleViewPortfolio(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view_recruiter_portfolio.fxml"));
        root = loader.load();

        // Get the controller of the newly loaded FXML
        scenecontroller controller = loader.getController();
        controller.populateRecruiterTable();
        controller.populateRecruiterQualificationsListView();
        // Get the stage and scene from the current event to switch to the new scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
       
       
    }
    @FXML
    public void handleTestInsertion(ActionEvent event) {
        try {
            // Retrieve selected values from ComboBoxes
            Integer selectedJobId = jobComboBox.getValue(); // Job ID from jobComboBox
            Integer numberOfQuestions = ageComboBox.getValue(); // Number of questions from ageComboBox
            Integer minimumScoreRequired = ageComboBox1.getValue(); // Minimum score from ageComboBox1

            if (selectedJobId == null || numberOfQuestions == null || minimumScoreRequired == null) {
                // Show alert if any required field is missing
                showAlert(Alert.AlertType.WARNING, "Input Missing", "Please select a job, number of questions, and minimum score.");
                return;
            }

            // Get recruiter ID from shared data (assuming it's stored globally)
            int recruiterId = SharedData.getInstance().getRecruiter_i();

            // Insert test information into the database
            int testId = recruiterService.insertTestinfo(selectedJobId, recruiterId, numberOfQuestions, minimumScoreRequired);

            if (testId != -1) {
                // Show success alert
                showAlert(Alert.AlertType.INFORMATION, "Success", "Test created successfully with ID: " + testId);
            } else {
                // Show failure alert
                showAlert(Alert.AlertType.ERROR, "Insertion Failed", "Failed to create the test.");
            }
        } catch (Exception e) {
            // Show error alert in case of exception
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleConductingTest(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Test_making.fxml"));
        root = loader.load();

        // Get the controller of the newly loaded FXML
        scenecontroller controller = loader.getController();
        controller.populateJobComboBox();
        controller.populateTestComboBoxes();
        
        // Get the stage and scene from the current event to switch to the new scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
       
       
    }
    @FXML
    public void handleSubmitButtonAction_Appointment() {
        try {
            // Get selected applicant ID
            Integer applicantId = Applicant_ids_ComboBox.getValue();
            if (applicantId == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select an Applicant ID.");
                return;
            }

            // Get selected job ID
            Integer jobId = jobComboBox.getValue();
            if (jobId == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a Job ID.");
                return;
            }

            // Get selected hour and minute
            Integer hour = hourComboBox.getValue();
            Integer minute = minuteComboBox.getValue();
            if (hour == null || minute == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a valid time.");
                return;
            }

            ComboBoxBase<LocalDate> datePicker;
			// Get selected date
            LocalDate date = appointment_date.getValue();
            if (date == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a date.");
                return;
            }

            // Combine date and time into a single string
            String appointmentTime = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                                     " " + String.format("%02d:%02d:00", hour, minute);

            Labeled descriptionField;
			// Get description
            String description = AppointmentDescription.getText();

            // Call the insert function
            boolean success = recruiterService.insertAppointment(jobId, applicantId, appointmentTime, description);

            // Show success or error message
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment successfully scheduled.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to schedule the appointment.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.");
        }
    }
    @FXML
    public void handleBookingAppointment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Book_an_appointment.fxml"));
        root = loader.load();
        // Get the controller of the newly loaded FXML
        scenecontroller controller = loader.getController();
        controller.populateJobComboBox();
        controller.populate_time();
        // Get the stage and scene from the current event to switch to the new scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
       
       
    }
}
