package application;

import javafx.scene.control.TextField;

import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import java.sql.Date;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic_layer.Hr_service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HR_scenecontroller {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private RadioButton admin;
    @FXML
    private RadioButton hrmanager;
    @FXML
    private RadioButton recruiter;
    @FXML
    private RadioButton applicant;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField aemailField;
    @FXML
    private TextField apasswordField;
    @FXML
    private ChoiceBox<String> experienceComboBox;
    @FXML
    private ChoiceBox<String> qualificationChoiceBox;
    @FXML
    private ChoiceBox<String> genderChoiceBox;  // Add gender ChoiceBox
    @FXML
    private TextField schoolField;
    @FXML
    private TextField cgpaField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;
    @FXML
    private DatePicker yearCompletedField;
    @FXML
    private Button submitcontract;
    @FXML
    private Button submit;
    @FXML
    private Button submitmsg;
    @FXML
    private Button loadmsg;
    @FXML
    private Button submitqualificationButton;
    @FXML
    private ComboBox<Integer> jobIDChoicebox;
    @FXML

    private ComboBox<String> salaryChoiceBox;
    @FXML
    private ComboBox<Integer> probPriodChoicebox;
    @FXML
    private DatePicker ContractS_date;
    @FXML
    private DatePicker ContractE_date;
    @FXML
    private ComboBox<String> benefitsChoicebox;
    @FXML
    private ChoiceBox<String>  recommendationChoiceBox;
    @FXML
    private ChoiceBox<Integer>  applicantIdChoiceBox ;
    @FXML
    private Button submitrecommendation;
    @FXML
    private TextArea sendmessage;
    @FXML
    private ListView<String> msglistview;
    @FXML
    private ListView<String> listview;
    @FXML
    private RadioButton hrtype;
    @FXML
    private RadioButton recruitertype;
    @FXML
    private RadioButton applicanttype;
    @FXML
    private ChoiceBox<Integer>  recruiterIdChoiceBox ;
    @FXML
    private ChoiceBox<Integer>  hrIdChoiceBox ;
    @FXML
   private Button peoplerecommended;
    @FXML
    private Button  options;
    @FXML
    private ChoiceBox<String> sorton;
    @FXML
    private ChoiceBox<String> sortby;
    @FXML
    private ChoiceBox<String> FieldComboBox;
    private Hr_service hrService;
    @FXML
    private ComboBox<Integer> applicantComboBox; // Your ComboBox
    @FXML
    private ComboBox<Integer> JobComboBox; // Your ComboBox
    public HR_scenecontroller() {
        this.hrService = new Hr_service(); // Initialize the Hr_service
    }

    public void populatesignupComboBox() {
        // Check if qualificationChoiceBox is initialized before using it
        if (FieldComboBox != null) {
            ObservableList<String> qualificationList = FXCollections.observableArrayList("Human Resource Management", "Sociology", " HR and Organizational Sociology"," Organizational Psychology with Sociology Components",
            		"Public Administration (MPA) with HR and Sociology Focus");
            FieldComboBox.setItems(qualificationList);
        }

        if (qualificationChoiceBox != null) {
            ObservableList<String> qualificationList = FXCollections.observableArrayList("Bachelor's", "Master's", "PhD");
            qualificationChoiceBox.setItems(qualificationList);
        }

        // Check if experienceComboBox is initialized before using it
        if (experienceComboBox != null) {
            ObservableList<String> experienceList = FXCollections.observableArrayList();
            for (int i = 1; i <= 50; i++) {
                experienceList.add(String.valueOf(i));
            }
            experienceComboBox.setItems(experienceList);
        }

        // Check if genderChoiceBox is initialized before using it
        if (genderChoiceBox != null) {
            ObservableList<String> genderList = FXCollections.observableArrayList("Male", "Female", "Other");
            genderChoiceBox.setItems(genderList);
        }
    }
    @FXML
    private void populateRecommendationComboBox() {
        // Populate the recommendation dropdown
        ObservableList<String> recommendationList = FXCollections.observableArrayList(
            "Highly Recommended", "Recommended", "Neutral"
        );
        recommendationChoiceBox.setItems(recommendationList);
        applicantIdChoiceBox.getItems().clear();
        // Populate the applicant ID dropdown with dummy data (e.g., Applicant IDs from 1 to 100)
        List<Integer> applicantIds = hrService.ApplicantIds();  // Fetch applicant IDs from service

        // Populate the applicant ID dropdown
        ObservableList<Integer> applicantIdList = FXCollections.observableArrayList(applicantIds); // Convert to ObservableList
        applicantIdChoiceBox.setItems(applicantIdList); 
        
        

    }
    
  
  
    @FXML
    private void populateContractComboBox() { 
        // Populate the job ID dropdown with data
        List<Integer> jobIds = hrService.JobIds();  // Fetch job IDs from service

        
        // Populate the job ID dropdown
        ObservableList<Integer> jobIdsList = FXCollections.observableArrayList(jobIds); // Convert to ObservableList
        jobIDChoicebox.setItems(jobIdsList); // Ensure you are setting it to the correct ChoiceBox
        jobIDChoicebox.setOnAction(event -> {
            Integer selectedJobId = jobIDChoicebox.getValue(); // Get selected job ID
            if (selectedJobId != null) {
                List<String> details = hrService.getJobDetails(selectedJobId); // Fetch job details
                listview.getItems().clear(); // Clear previous items
                listview.getItems().addAll(details); // Add new job details
            }
        });
        // Populate the salary choice box
        ObservableList<String> salaryList = FXCollections.observableArrayList("Below 30,000", "30,000 - 50,000", "50,000 - 70,000", "Above 70,000");
        salaryChoiceBox.setItems(salaryList);  // Set the items to the salary choice box
        
        // Populate the probation period choice box
        
        ObservableList<Integer> probationPeriodList = FXCollections.observableArrayList(1, 3, 6, 12);
        probPriodChoicebox.setItems(probationPeriodList);  // Set the items to the probation period choice box

        // Populate the benefits choice box
        ObservableList<String> benefitsList = FXCollections.observableArrayList("Medical Insurance", "Paid Time Off", "Retirement Plan", "Employee Discounts", "Stock Options");
        benefitsChoicebox.setItems(benefitsList);  // Set the items to the benefits choice box
    }

    @FXML
    private void populateSendMessageComboBox() {
    	int job_id=1;
        // Populate the send message combo box with applicant IDs from your Applicant table
        List<Integer> applicantIds = hrService.fetchAllApplicantIds(job_id);
        // Convert the applicant IDs to an ObservableList
        ObservableList<Integer> applicantIdList = FXCollections.observableArrayList(applicantIds);

        // Set the list of applicant IDs to the combo box
        applicantIdChoiceBox.setItems(applicantIdList);
    }
    @FXML
    private void populatejonComboBox() {
        // Fetch the job IDs for the combo box from the hrService
        List<Integer> jobIds = hrService.fetchApplicantIdsForJob();

        // Convert the job IDs list to an ObservableList
        ObservableList<Integer> jobIds_1 = FXCollections.observableArrayList(jobIds);

        // Set the list of job IDs to the ComboBox
        JobComboBox.setItems(jobIds_1);

        // Add listener to handle job selection and populate the applicants
        JobComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // When a job is selected, get the job_id and populate applicants
                int job_id = newValue;  // This is the selected job ID
                populateApplicantIDS(job_id);  // Call your function to populate applicants for the selected job
            }
        });
    }

  

   
    @FXML
    private void populatereceiveMessageApplicantHrComboBox() {
        // Fetch applicant IDs from the service
        List<Integer> applicantIds = hrService.ApplicantIds();

        // Convert the applicant IDs to an ObservableList
        ObservableList<Integer> applicantIdList = FXCollections.observableArrayList(applicantIds);

        // Set the list of applicant IDs to the ChoiceBox
        applicantIdChoiceBox.setItems(applicantIdList);

        // Add a listener to detect when an applicant ID is selected
        applicantIdChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) { // Ensure a valid applicant ID is selected
                try {
                    // Fetch HR ID based on the login credentials
                    int hrId = hrService.validateAndFetchHrId(loginemail, loginpassword);
                   System.out.println(hrId);
                    // Fetch messages for the selected applicant ID and HR ID
                     hrService.GetMessagesForApplicant(newValue, hrId);

                    // Convert messages into an ObservableList
                   
               

                } catch (Exception e) {
                    // Log the exception and notify the user
                    e.printStackTrace();
                   
                }
            }
        });
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
    	
         // Populate the job ID dropdown
        // Ensure you are setting it to the correct ChoiceBox
         
        // Populate ChoiceBox with recruiter IDs
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

    private void applySorting() {
        String sortBy = sorton.getValue();   // Get the sorting option (CGPA or Experience)
        String sortOrder = sortby.getValue(); // Get the sorting order (Ascending or Descending)
        if (sortBy != null && sortOrder != null) {
            fetchAndSortApplicants(sortBy, sortOrder); // Fetch sorted applicants based on the selected criteria
        }
    }

    private void fetchAndSortApplicants(String sortBy, String sortOrder) {
        // Fetch sorted applicants from the DAO layer
        List<String> sortedApplicants = hrService.getSortedApplicants(sortBy, sortOrder);

        // Set the items for the ListView
        listview.setItems(FXCollections.observableArrayList(sortedApplicants));
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

    
    public void populate() {
        // Fetch applicant IDs from the service
        List<Integer> applicantIds = hrService.ApplicantIds();

        // Convert the applicant IDs to an ObservableList
        ObservableList<Integer> applicantIdList = FXCollections.observableArrayList(applicantIds);

        // Set the list of applicant IDs to the ChoiceBox
        applicantIdChoiceBox.setItems(applicantIdList);

        // Add a listener to detect when an applicant ID is selected
        applicantIdChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Fetch HR ID from the service
                try {
                    int hrId = hrService.validateAndFetchHrId(loginemail, loginpassword);

                    if (hrId > 0) {
                        // Display messages for the selected applicant and HR ID
                        displaymsg(newValue, hrId);
                    } else {
                        // Handle the case where HR ID is invalid
                        System.err.println("Invalid HR ID. Could not fetch messages.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Optionally, show an error message to the user
                    System.err.println("Error while fetching HR ID or displaying messages.");
                }
            } else {
                // Handle the case where no applicant ID is selected
                System.err.println("No applicant ID selected.");
            }
        });
    }

    private void displaymsg(Integer applicantId, Integer hrId) {
        try {
        	applicantId=applicantIdChoiceBox.getValue();
            // Fetch messages for the applicant ID and HR ID
            List<String> messages = hrService.GetMessagesForApplicant(applicantId, hrId);

            // Convert messages into an ObservableList
            ObservableList<String> msgInfo = FXCollections.observableArrayList(messages);

            // Populate the ListView with the messages
            msglistview.setItems(msgInfo);

            // Log for debugging
            System.out.println("Applicant ID: " + applicantId + ", HR ID: " + hrId);
            System.out.println("Messages: " + messages);
        } catch (Exception e) {
            e.printStackTrace();
         
        }
    }
    


  

  

 // Add an instance variable to store the logged-in email
    private static String loginemail;
    private static String loginpassword;
    
    public static String getLoginEmail() {
        return loginemail;
    }

    // Method to retrieve the static password value, if needed
    public static String getLoginPassword() {
        return loginpassword;
    }
    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
    	loginemail = emailField.getText().trim();
       loginpassword = passwordField.getText().trim();
        if (loginemail.isEmpty() || loginpassword.isEmpty()) {
           
            return;
        }

        int isValidUser = hrService.validateUser(loginemail, loginpassword);

        if (isValidUser>=1) {
        	
            // Store the logged-in email
            
            System.out.println("Logged-in email: " +loginemail);
           
            switchtohrmenu(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Email or Password.");
        }
    }


   


    @FXML
    private void submitQualification() {
        // Get input values
        String qualification = qualificationChoiceBox.getValue();
        String school = schoolField.getText();
        String cgpaStr = cgpaField.getText();
        LocalDate yearCompleted = yearCompletedField.getValue();
       String field =FieldComboBox.getValue();

        // Call the submitHRQualification method on the HRService instance
        hrService.submitHRQualification(qualification, school, yearCompleted, cgpaStr,field);

      
    }

    @FXML
    private void submitHrInfo() {
        // Get input values
        String name = nameField.getText();
        String email = emailField.getText();
        String pass = passwordField.getText();
        String ageStr = ageField.getText();
        int age = 0;
        String gender = genderChoiceBox.getValue();
        String experienceStr = experienceComboBox.getValue();
        int experience = 0;
        
        // Validate age input
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error","invalid values");
            return; // Exit if the age is invalid
        }

        // Validate experience input
        try {
            experience = Integer.parseInt(experienceStr);
        } catch (NumberFormatException e) {
           
            return; // Exit if the experience is invalid
        }

       
     
        // Create an instance of HRService
        boolean success = hrService.register(name, email, pass, age, gender, experience);

        // Display appropriate message based on success
        if (success) {
            // Show confirmation with the HR information
        	 System.out.println("Experience: " + experience);
           
        } else {
        	 System.out.println("Experience: " + experience);
        }
    }


@FXML
private void submitRecommendation() {
    // Get input values from the UI
    Integer applicantId = applicantIdChoiceBox.getValue();
    String description = recommendationChoiceBox.getValue();
    
    // Get logged-in email (simulated, replace with actual logic)
    System.out.println("Logged-in email: " +loginemail);
    // Fetch the HR ID based on the logged-in email
    Integer hrId = hrService.validateAndFetchHrId(loginemail,loginpassword);  // Fetch HR ID from the service using the email
    System.out.println("Logged-in email: " + loginemail);
    // Validate inputs
    

    // Insert the recommendation into the database
   hrService.submitHrRecommendation(hrId, applicantId, description);

    // Show success or error message based on the result
   
}


@FXML
private void submitMessage() {
    // Get selected applicant ID and message from UI
    Integer applicantId = applicantComboBox.getValue();
    String message = sendmessage.getText().trim(); // Trim spaces
    Integer hrId = hrService.validateAndFetchHrId(loginemail, loginpassword);
    int JobId=JobComboBox.getValue();
    // Call the service layer to send the message to the database and validate
    hrService.submitMessageToApplicant(hrId, applicantId, message,JobId);

    // Show success or failure alert and update ListView
 
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





@FXML
private void submitHrContractInfo() {
    // Get input values from UI components
    Integer jobId = jobIDChoicebox.getValue();
    String salary = salaryChoiceBox.getValue();
    Integer probationPeriod = probPriodChoicebox.getValue();
    LocalDate startDate = ContractS_date.getValue();
    LocalDate endDate = ContractE_date.getValue();
    String benefits = benefitsChoicebox.getValue();
    
   

   // Replace with actual logic to get the logged-in email

    // Fetch the HR ID based on the logged-in email
    Integer hrId = hrService.validateAndFetchHrId(loginemail,loginpassword);   // Fetch HR ID from the service using the email
    System.out.println("Logged-in email: " + loginemail);
    // Call the service method to register the HR contract info
   hrService.registerHrContractInfo(jobId, hrId, salary, probationPeriod, startDate, endDate, benefits);

  
}




private void showAlert(Alert.AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType, message, ButtonType.OK);
    alert.setTitle(title);
    alert.showAndWait();
}



@FXML
public void handleadminLogin(ActionEvent event) throws IOException {
    String adminemail = aemailField.getText().trim();
    String adminpassword = apasswordField.getText().trim();

    // Call the service to validate the user and show alerts within the service
    boolean isValidUser = hrService.validateAdminUser(adminemail, adminpassword);

    if (isValidUser) {
        System.out.println("Logged-in email: " + adminemail);
        switchtotype(event); // Proceed to the next screen
    }
}
@FXML
public void SubmitApplicants(ActionEvent event) throws IOException {
    int applicantId = applicantComboBox.getValue();  // Get selected applicant ID
    int hrId = hrService.validateAndFetchHrId(loginemail, loginpassword);  // Get HR ID
    int jobId = JobComboBox.getValue();  // Get selected Job ID

    // Call the service method to submit the applicant
    boolean isSubmitted = hrService.submitApplicants(applicantId, hrId, jobId);

    // Show alert based on the result
    if (isSubmitted) {
        // Success alert
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText("Applicant Shortlisted");
        successAlert.setContentText("The applicant has been successfully shortlisted for the job.");
        successAlert.showAndWait();
    } else {
        // Failure alert (e.g., already shortlisted)
        Alert failureAlert = new Alert(Alert.AlertType.ERROR);
        failureAlert.setTitle("Error");
        failureAlert.setHeaderText("Shortlisting Failed");
        failureAlert.setContentText("This applicant has already been shortlisted for the job.");
        failureAlert.showAndWait();
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
    public void switchtologin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("hrlogin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchtosignup(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hrsignup.fxml"));
        root = loader.load();
        HR_scenecontroller controller = loader.getController();
        controller.populatesignupComboBox();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void populateApplicantIDS(int Job_id)
    {
    List<Integer> applicantIds = hrService.fetchAllApplicantIds(Job_id);

    // Clear any existing items in the ComboBox
    applicantComboBox.getItems().clear();

    // Add each applicant ID to the ComboBox
    applicantComboBox.getItems().addAll(applicantIds);
    }
    @FXML
    public void switchtosubmithr(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("shortlistafter_hr.fxml"));
    root = loader.load();
    HR_scenecontroller controller = loader.getController();
    controller.populatejonComboBox();
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    }
    @FXML
    public void switchtohrmenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("hrmenu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void handleSubmitAndSwitch() throws IOException {
        submitHrInfo();  // Call the method to submit HR info
        // Pass the event to switch to HR menu
    }
   
    public void switchtocontract(ActionEvent event) throws IOException {
    	   FXMLLoader loader = new FXMLLoader(getClass().getResource("contract.fxml"));
           root = loader.load();
           HR_scenecontroller controller = loader.getController();
           controller. populateContractComboBox();

           stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
           scene = new Scene(root);
           stage.setScene(scene);
           stage.show();
    }
    
    public void Backfromcontract(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("hrmenu.fxml")); // Load the next menu FXML
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the current window (stage)
        scene = new Scene(root); // Create a new scene with the next menu root
        stage.setScene(scene); // Set the new scene
        stage.show(); // Show the new stage
    }
    
    public void gotorecommendations(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("giverecommendation.fxml"));
        root = loader.load();
        HR_scenecontroller controller = loader.getController();
        controller. populateRecommendationComboBox();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void Backfromrecommendation(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("hrmenu.fxml")); // Load the next menu FXML
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the current window (stage)
        scene = new Scene(root); // Create a new scene with the next menu root
        stage.setScene(scene); // Set the new scene
        stage.show(); // Show the new stage
    }
    public void gotomsgwithapplicant(ActionEvent event) throws IOException {
    	 FXMLLoader loader = new FXMLLoader(getClass().getResource("sendmessagetoapplicant.fxml"));
         root = loader.load();
         HR_scenecontroller controller = loader.getController();
         controller.populatejonComboBox();
         stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
     }
   
   
   
   public void Backfromapplicant(ActionEvent event) throws IOException {
       root = FXMLLoader.load(getClass().getResource("hrmenu.fxml")); // Load the next menu FXML
       stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the current window (stage)
       scene = new Scene(root); // Create a new scene with the next menu root
       stage.setScene(scene); // Set the new scene
       stage.show(); // Show the new stage
   }
  @FXML
   public void gotoreceivemsg(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("appreceivemsgfromhr.fxml"));
	    root = loader.load();
        HR_scenecontroller controller = loader.getController();
        controller.populate();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}


 
 
   
 public void Backfromreceivemsg(ActionEvent event) throws IOException {
     root = FXMLLoader.load(getClass().getResource("hrmenu.fxml")); // Load the next menu FXML
     stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the current window (stage)
     scene = new Scene(root); // Create a new scene with the next menu root
     stage.setScene(scene); // Set the new scene
     stage.show(); // Show the new stage
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
      HR_scenecontroller controller = loader.getController();
      controller. populaterecruiterreport();
     

      stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
 }
 public void Backfromrecruiterreport(ActionEvent event) throws IOException {
     root = FXMLLoader.load(getClass().getResource("admintype.fxml")); // Load the next menu FXML
     stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the current window (stage)
     scene = new Scene(root); // Create a new scene with the next menu root
     stage.setScene(scene); // Set the new scene
     stage.show(); // Show the new stage
 }
 
 @FXML
 public void HrReport(ActionEvent event) throws IOException {
	  FXMLLoader loader = new FXMLLoader(getClass().getResource("hrreport.fxml"));
      root = loader.load();
      HR_scenecontroller controller = loader.getController();
      controller. populateHrreport();
     

      stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
 }
 public void Backfromhrreport(ActionEvent event) throws IOException {
     root = FXMLLoader.load(getClass().getResource("admintype.fxml")); // Load the next menu FXML
     stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the current window (stage)
     scene = new Scene(root); // Create a new scene with the next menu root
     stage.setScene(scene); // Set the new scene
     stage.show(); // Show the new stage
 }
 
 @FXML
 public void ApplicantReport(ActionEvent event) throws IOException {
	  FXMLLoader loader = new FXMLLoader(getClass().getResource("applicantreports.fxml"));
      root = loader.load();
      HR_scenecontroller controller = loader.getController();
      controller. populateapplicantreport();
     

      stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
 }
 public void Backfromapplicantreport(ActionEvent event) throws IOException {
     root = FXMLLoader.load(getClass().getResource("admintype.fxml")); // Load the next menu FXML
     stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the current window (stage)
     scene = new Scene(root); // Create a new scene with the next menu root
     stage.setScene(scene); // Set the new scene
     stage.show(); // Show the new stage
 }
 
}