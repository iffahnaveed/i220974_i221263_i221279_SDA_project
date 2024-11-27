package logic_layer;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import Model_classes.Contract_model;
import Model_classes.HR_model;
import Model_classes.Qualification_model;
import Model_classes.Recommendation_model;
import Model_classes.UserFactory;
import Model_classes.User_model;
import database_logic.HrDao;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Hr_service extends UserService{

	
    private HrDao hrDao;
    int hrid;
    int qualificationid;
    HR_model hr;
    public Hr_service() {
        hrDao = new HrDao();
    }

    @Override
    public int validateUser(String email, String password) {
        this.email = email;
        this.password = password;

        // Validate email format
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format: " + email);
            return -1; // Indicate failure
        }

        // Validate password strength
        if (!isValidPassword(password)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password must be at least 8 characters long.");
            return -1; // Indicate failure
        }

        // Delegate validation to DAO layer for actual credentials check
        int hrId = hrDao.validateLogin(email, password);
        if (hrId != -1) {
            this.userId = hrId;
            return hrId; // Return the valid HR ID
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Email or Password.");
            return -1; // Indicate failure
        }
    }
    @Override
    public boolean register(String hrName, String hrEmail, String hrPassword, int hrAge, String hrGender, int hrExperience) {
        // Validate inputs
        if (hrName.isEmpty() || hrEmail.isEmpty() || hrPassword.isEmpty() || hrGender == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false;
        }

        if (hrAge < 25) {
            showAlert(Alert.AlertType.ERROR, "Error", "Age must be greater than 25.");
            return false;
        }

        if ((hrAge - 10) < hrExperience) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid experience.");
            return false;
        }

        if (!isValidEmail(hrEmail)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid email format.");
            return false;
        }

        if (!isValidPassword(hrPassword)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password must be at least 8 characters.");
            return false;
        }

        // Use the User_factory to create an HR_model instance
        UserFactory userFactory = new UserFactory();
        User_model hrModel = userFactory.createUser("HR", 0, hrName, hrEmail, hrPassword, hrExperience, hrAge, hrGender);

        // Insert HR info into the database using the HR_model object
        int hrId = hrDao.inserthrinfo(hrModel.getName(), hrModel.getEmail(), hrModel.getPassword(),
                                      hrModel.getAge(), hrModel.getGender(), hrModel.getExperience());

        if (hrId == -1) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to register HR.");
            return false;
        }

        Platform.runLater(() -> showAlert(Alert.AlertType.INFORMATION, "Success", "HR registered successfully!"));
        return true;
    }

    public boolean submitHRQualification(String qualType, String university, LocalDate yearCompleted, String cgpaStr, String field) {
        // Validate that no field is empty or null
        if (qualType == null || university == null || university.isEmpty() || yearCompleted == null || cgpaStr == null || cgpaStr.isEmpty() || field.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled.");
            return false;
        }

        // Parse and validate CGPA
        float cgpa;
        try {
            cgpa = Float.parseFloat(cgpaStr);
        } catch (NumberFormatException e) {
            System.out.println("CGPA parsing failed: " + cgpaStr); // Debugging log
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Validation Error", "CGPA must be a valid number."));
            return false;
        }

        // Ensure CGPA is between 0 and 4
        if (cgpa < 0 || cgpa > 4) {
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Validation Error", "CGPA must be between 0 and 4."));
            return false;
        }

        // Create a Qualification_model object
        Qualification_model qualification = new Qualification_model();
        qualification.setQualType(qualType);
        qualification.setUniversitySchoolName(university);
        qualification.setCgpa(cgpa);
        qualification.setField(field);

        // Insert qualification and store the result (qualification ID)
        int qualificationid = hrDao.insertQualification(qualType, university, yearCompleted, cgpa, field);

        if (qualificationid == -1) {
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save qualification details. Please try again."));
            return false; // Insertion failed
        } else {
            // Save the qualification ID and return success
            this.qualificationid = qualificationid;
            hrDao.insertHRQualification(hrid, qualificationid);
            Platform.runLater(() -> showAlert(Alert.AlertType.INFORMATION, "Success", "Qualification submitted successfully!"));
            return true;
        }
    }

    public Integer validateAndFetchHrId(String email, String password) {
        if (hrDao.validateLogin(email, password)>=0) {
            return hrDao.fetchHrIdByEmail(email);
        }
        return null;  // Return null if validation fails
    }
    public List<Integer> ApplicantIds() {
        return hrDao.fetchApplicantIds();  // Call the DAO method to fetch applicant IDs
    }
    public List<Integer> fetchAllApplicantIds(int Job_id) {
        return hrDao.fetchAllApplicantIds(Job_id);  // Call the DAO method to fetch applicant IDs
    }
    public List<Integer> fetchApplicantIdsForJob() {
        return hrDao.fetchApplicantIdsForJob();  // Call the DAO method to fetch applicant IDs
    }
    public List<Integer> recruiterIds() {
        return hrDao.fetchRecruiterIds();  // Call the DAO method to fetch applicant IDs
    }

    
    public List<Integer> HrIds() {
        return hrDao.fetchHrIds();  // Call the DAO method to fetch applicant IDs
    }
    public boolean submitHrRecommendation(int hrId, int applicantId, String description) {
        // Validate inputs
        if (hrId <= 0 || applicantId <= 0 || description == null || description.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return false; // Return false if validation fails
        }

        // Create a new Recommendation object
        Recommendation_model recommendation = new Recommendation_model();
        recommendation.setRecommendationApplicantId(applicantId);
        recommendation.setRecommendationDescription(description);
        recommendation.setRecommendationHrId(hrId);
        // Call the DAO method to insert the recommendation using the object
        boolean isSuccessful = hrDao.insertRecommendation(hrId, applicantId, description);

        if (!isSuccessful) {
        	hr.addRecommendation(recommendation);
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit the recommendation.");
        } else {
        	
            showAlert(Alert.AlertType.INFORMATION, "Done", "Recommendation given.");
        }

        return isSuccessful; // Return the success status of the insertion
    }

    
    public List<Integer> JobIds() {
        return hrDao.fetchJobIds();  // Call the DAO method to fetch job IDs
    }
    public boolean submitMessageToApplicant(int hrId, int applicantId, String message,int jobid) {
        // Validate HR ID
        if (hrId <= 0) {
        	showAlert(Alert.AlertType.ERROR, "Error", "Invalid hr id.");
            return false; // Invalid HR ID
        }

        // Validate Applicant ID
        if (applicantId <= 0) {
        	showAlert(Alert.AlertType.ERROR, "Error", "invalid applicant id.");
            return false; // Invalid Applicant ID
        }

        // Validate Message
        if (message == null || message.trim().isEmpty()) {
        	showAlert(Alert.AlertType.ERROR, "Error", "Message cannot be empty.");
            return false; // Message is empty or null
        }       
        // Call DAO to insert the message into the database
        int generatedId = hrDao.insertMessageToApplicant(hrId, applicantId, message,jobid);

        // Return true if message was successfully inserted (ID > 0)
        if (generatedId > 0) {
        	showAlert(Alert.AlertType.INFORMATION, "Message sent successfully to the applicant.","Successful");
            return true;
        } else {
        	showAlert(Alert.AlertType.ERROR, "Error", "Try again");
            return false; // Failed to insert message
        }
    }
    public List<String> ReceiveMsgHRApplicant(int hrId,String message) {
        // Call the DAO method to insert the message and check if it was successful
        return hrDao.receivemsghrapplicant(hrId,  message);
    }
    
    public List<String> GetMessagesForApplicant(int applicantId, int hrId) {
        // Call the DAO method to insert the message and check if it was successful
        return hrDao.getMessagesForApplicant(applicantId,  hrId);
    }
    public boolean registerHrContractInfo(int jobId, int hrId, String salary, int probationPeriod, 
	            LocalDate startDate, LocalDate endDate, String benefits) {
	// Validate inputs
	if (jobId <= 0) {
	showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a valid Job ID.");
	return false;
	}
	
	if (hrId <= 0) {
	showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a valid HR ID.");
	return false;
	}
	
	if (salary == null || salary.trim().isEmpty()) {
	showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid Salary.");
	return false;
	}
	
	if (probationPeriod <= 0) {
	showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid Probation Period.");
	return false;
	}
	
	if (startDate == null || endDate == null) {
	showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select both Start and End Dates.");
	return false;
	}
	
	// Ensure end date is after start date
	if (endDate.isBefore(startDate)) {
	showAlert(Alert.AlertType.ERROR, "Validation Error", "End Date must be after Start Date.");
	return false;
	}
	
	if (benefits == null || benefits.trim().isEmpty()) {
	showAlert(Alert.AlertType.ERROR, "Validation Error", "Please specify the benefits.");
	return false;
	}
	
	// Check if the probation period is valid in relation to start and end dates
	long duration = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
	if (duration < probationPeriod * 30) { // Assuming probation period is in months, and 30 days per month
	showAlert(Alert.AlertType.ERROR, "Validation Error", 
	"Probation Period cannot be longer than the contract duration.");
	return false;
	}
	
	// Check if the job ID already has a contract
	if (hrDao.isContractExistsForJob(jobId)) {
	showAlert(Alert.AlertType.ERROR, "Duplicate Contract", 
	"A contract already exists for the selected Job ID. Cannot create another contract.");
	return false;
	}
	
	// Create a new Contract object
	Contract_model contract = new Contract_model();
	contract.setBenefits(benefits);
	contract.setContractEndDate(null);
	contract.setContractId(0);
	contract.setHrId(0);
	contract.setJobId(jobId);
	contract.setProbationPeriod(probationPeriod);
	contract.setSalary(salary);
	// Call the DAO to insert HR contract info using the Contract object
	int result = hrDao.insertHrContractInfo(jobId, hrId, salary, probationPeriod, startDate, endDate, benefits);
	
	// Return true if insertion was successful
	if (result != -1) {
	showAlert(Alert.AlertType.INFORMATION, "Success", 
	"HR contract information submitted successfully.");

	return true;
	} else {
		hr.addContract(contract);
	showAlert(Alert.AlertType.ERROR, "Error", 
	"Failed to submit HR contract information.");
	return false;  // Return false if insertion failed
	}
	}
    
    public boolean validateAdminUser(String email, String password) {
        // Check if email and password are not empty
    	
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Email or Password cannot be empty.");
            return false;
        }

        // Basic email format validation
        if (!email.contains("@")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid email format.");
            return false;
        }

        // Delegate validation to DAO layer
        if (hrDao.validateadminLogin(email, password)) {
            return true; // Credentials are valid
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Email or Password.");
            return false; // Invalid credentials
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
    // This method is responsible for showing the alert dialog
    public List<String> getJobDetails(int jobId) {
        // Interact with the database layer to fetch job details
        return hrDao.getJobDetailsFromId(jobId);
    }
    
    public List<String> fetchRecruiterInformation(Integer recruiterId) {
        return hrDao.fetchRecruiterInfo(recruiterId);
    }
    
    public List<String> fetchHrInformation(Integer recruiterId) {
        return hrDao.fetchHrInfo(recruiterId);
    }
    
    public int gettotaljobposted(Integer recruiterId) {
        return hrDao.getTotalJobsPosted(recruiterId);  // Calls the DAO layer to fetch job count
    }
    
    public List<String> getRecommendationCounts(Integer selectedHrId) {
        return hrDao.getRecommendationCounts(selectedHrId);  // Call DAO to get the recommendation counts for selected applicant IDs
    }
    public List<String> getSortedApplicants(String sortBy, String sortOrder) {
        return hrDao.getSortedApplicants(sortBy, sortOrder);
    }
    public boolean submitApplicants(int applicantId, int hrId, int jobId) {
        try {
            boolean isShortlisted = hrDao.insertShortlistedApplicant(applicantId, hrId, jobId); 
            if(isShortlisted)
            {
            	hrDao.updateJobStatus(jobId);
            }// Attempt to insert the applicant
            return isShortlisted;  // Return true if successfully shortlisted, false otherwise
        } catch (SQLException e) {
            System.err.println("Error shortlisting applicant: " + e.getMessage());
            return false;  // Return false if an error occurs
        }
    }



}
    
    

