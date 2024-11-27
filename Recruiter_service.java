package logic_layer;
import Model_classes.application_model;
import Model_classes.MessageRecord;
import Model_classes.Recruiter_model;
import database_logic.RecruiterDao;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import Model_classes.Test;
import Model_classes.UserFactory;
import Model_classes.User_model;
import Model_classes.application_model;
import Model_classes.Job_model;
import Model_classes.Recruiter_model;
import Model_classes.Qualification_model;
public class Recruiter_service extends UserService {
   int recruiterId;
   int qualId;
   Recruiter_model recruiter;
   private RecruiterDao recruiterDao;
   public Recruiter_service() {
       recruiterDao = new RecruiterDao(); // Initialize the DAO
   }
   @Override
   public int validateUser(String email, String password) {
       this.email = email;
       this.password = password;
       // Validate email format
       if (!isValidEmail(email)) {
           showAlert("Validation Error", "Invalid email format: " + email);
           return -1; // Indicate failure
       }
       // Validate password strength
       if (!isValidPassword(password)) {
           showAlert("Validation Error", "Password must be at least 8 characters long.");
           return -1; // Indicate failure
       }
       // Check credentials in the database
       int recruiterId = recruiterDao.validateLogin(email, password);
       if (recruiterId != -1) {
           this.userId = recruiterId;
           return recruiterId; // Return the valid recruiter ID
       } else {
           showAlert("Login Failed", "Invalid Email or Password.");
           return -1; // Indicate failure
       }
   }
   @Override
   public boolean register(String recruiterName, String recruiterEmail, String recruiterPassword,
                           int recruiterAge, String recruiterGender, int recruiterExperience) {

       // Create Recruiter_model instance using the Factory Method
       UserFactory userFactory = new UserFactory();
       User_model recruiter = userFactory.createUser("Recruiter", 0, recruiterName, recruiterEmail, recruiterPassword, 
                                                     recruiterExperience, recruiterAge, recruiterGender);

       // Validate that age is greater than 25
       if (recruiterAge < 25) {
           showAlert("Invalid Input", "Age must be greater than 25.");
           return false;
       }

       // Validate that experience is less than age minus 15
       if (recruiterAge - 15 < recruiterExperience) {
           showAlert("Invalid Input", "Experience must be less than age minus 15.");
           return false;
       }

       // Validate the email format
       if (!isValidEmail(recruiterEmail)) {
           showAlert("Invalid Input", "Invalid email format.");
           return false;
       }

       // Validate that password is at least 8 characters
       if (!isValidPassword(recruiterPassword)) {
           showAlert("Validation Error", "Password must be at least 8 characters.");
           return false;
       }

       // Validate that name does not contain numbers
       if (recruiterName.matches(".*\\d.*")) {
           showAlert("Invalid Input", "Name should not contain numbers.");
           return false;
       }

       // Use getter methods to insert recruiter data
       int recruiterId = recruiterDao.insertRecruiter(
               recruiter.getName(),
               recruiter.getEmail(),
               recruiter.getPassword(),
               recruiter.getAge(),
               recruiter.getGender(),
               recruiter.getExperience()
       );

       if (recruiterId == -1) {
           showAlert("Error", "Failed to register Recruiter.");
           return false;
       }

       // Set the recruiterId in the Recruiter_model object
       recruiter.setUserId(recruiterId);

       return true;
   }

   public boolean submitQualification(String qualType, String university, Date yearCompleted, float cgpa, String recruiterField) {
	    // Create Qualification_model instance
	    Qualification_model qualification = new Qualification_model();
	    qualification.setQualType(qualType);
	    qualification.setUniversitySchoolName(university);
	    qualification.setYearCompleted(yearCompleted);
	    qualification.setCgpa(cgpa);
	    qualification.setField(recruiterField);

	    // Validate CGPA range
	    if (cgpa < 0 || cgpa > 4) {
	        showAlert("Invalid Input", "CGPA should be between 0 and 4.");
	        return false;
	    }

	    // Use getters to insert qualification data
	    int qualId = recruiterDao.insertQualification(
	            qualification.getQualType(),
	            qualification.getUniversitySchoolName(),
	            qualification.getYearCompleted(),
	            qualification.getCgpa(),
	            qualification.getField()
	    );

	    if (qualId == -1) {
	        showAlert("Error", "Failed to submit qualification. Please try again.");
	        return false;
	    }

	    // Link the qualification to the recruiter using their IDs
	    boolean linked = recruiterDao.insertRecruiterQualification(recruiter.getUserId(), qualId);

	    if (!linked) {
	        showAlert("Error", "Failed to link qualification to recruiter.");
	        return false;
	    }

	    return true;
	}
	   public boolean postJob(String jobTitle, String jobDescription, String companyName, String qualifications,
	           float cgpaRequired, String jobType, int experienceRequired, Date applicationDeadline, 
	           int recruiterId) {
	// Validate recruiter ID
	if (recruiterId <= 0) {
	showAlert("Invalid Recruiter", "Recruiter is not logged in or has an invalid recruiter ID.");
	return false;
	}
	
	// Validate job title
	if (!isValidString(jobTitle)) {
	showAlert("Invalid Job Title", "Job title must be meaningful and at least 3 characters long.");
	return false;
	}
	
	// Validate job description
	if (!isValidString(jobDescription)) {
	showAlert("Invalid Job Description", "Job description must be meaningful and at least 3 characters long.");
	return false;
	}
	
	// Validate company name
	if (!isValidString(companyName)) {
	showAlert("Invalid Company Name", "Company name must be meaningful and at least 3 characters long.");
	return false;
	}
	
	// Validate qualifications
	if (!isValidString(qualifications)) {
	showAlert("Invalid Qualifications", "Qualifications must be meaningful and at least 3 characters long.");
	return false;
	}
	
	// Validate CGPA
	if (cgpaRequired <= 0 || cgpaRequired > 4) {
	showAlert("Invalid CGPA", "CGPA must be greater than 0 and less than or equal to 4.");
	return false;
	}
	
	// Validate application deadline
	if (applicationDeadline == null) {
	showAlert("Invalid Deadline", "Application deadline invalid");
	return false;
	}
	
	// Create a Job_model object
	Job_model job = new Job_model();
	job.setJobTitle(jobTitle);
	job.setJobDescription(jobDescription);
	job.setCompanyName(companyName);
	job.setQualificationSkillsRequired(qualifications);
	job.setCgpaRequired(cgpaRequired);
	job.setJobType(jobType);
	job.setJobExperience(experienceRequired);
	job.setApplicationDeadline(applicationDeadline);
	job.setRecruiterId(recruiterId);
	
	// Insert the job into the database
	boolean isInserted = recruiterDao.insertJob(job.getJobTitle(), job.getJobDescription(), job.getCompanyName(),
	job.getQualificationSkillsRequired(), job.getCgpaRequired(),
	job.getJobType(), job.getJobExperience(),
	job.getApplicationDeadline(), job.getRecruiterId());
	
	if (!isInserted) {
	showAlert("Error", "Failed to post the job. Please try again.");
	return false;
	}
	
	return true;
	}

	//Helper method to validate a string
	private boolean isValidString(String input) {
	if (input == null || input.trim().isEmpty()) {
	return false; // Null or empty strings are invalid
	}
	// Check length and ensure it's not just numbers
	String trimmedInput = input.trim();
	if (trimmedInput.length() < 3 || trimmedInput.matches("\\d+")) {
	return false; // Too short or only numbers
	}
	return true; // Valid string
	}
   public List<Integer> getJobIdsByRecruiter(int recruiterId) {
       return recruiterDao.getJobIdsByRecruiter(recruiterId);
   }
   public List<Integer> getApplicantIdsByRecruiter(int recruiterId,int Job_id) {
       return recruiterDao.getApplicantIdsByRecruiter(recruiterId, Job_id);
   }
   public List<Integer> getShortlistedApplicantIds(int Job_id) {
       return recruiterDao.getShortlistedApplicantIds(Job_id);
   }
   // Method to fetch applications for a given job ID
   public List<application_model> getApplicationsByJobId(int jobId) {
       return recruiterDao.getApplicationsByJobId(jobId);
   }
   public List<MessageRecord> getMessagesByApplicantId(int applicantId)
   {
   	return recruiterDao.getMessagesByApplicantId(applicantId);
   }
   public Recruiter_model getRecruiterByRecruiterId(int recruiterId) {
       return recruiterDao.getRecruiterByRecruiterId(recruiterId);
   }
   public boolean insert_into_send_message_recruiter_and_applicant(int applicant_id,String message,int recruiterId) {
       return recruiterDao.insert_into_send_message_recruiter_and_applicant(applicant_id,message,recruiterId);
   }
   public boolean insertTestAndShortlist(int jobId, int recruiterId, int applicantId, float testScore) {
   	// Create a new Test object and set its values
       Test test = new Test();
       test.setJobId(jobId);
       test.setRecruiterId(recruiterId);
       test.setApplicantId(applicantId);
       test.setTestScore(testScore);
       test.setTestDate(new Date(System.currentTimeMillis())); // Set the current date as test date
       float minimumRequiredScore = get_minimum_req(test.getJobId());
       // Check if the applicant already has a test score for the job
       if (recruiterDao.applicantHasTestScore(test.getApplicantId(),test.getJobId())) {
           showAlert("Duplicate Test Submission",
                     "The applicant with ID " + applicantId + " already has a test score recorded for Job " + jobId + ".");
           return false; // Do not proceed further
       }
       // Validate if the test score is below the minimum required score
       if (test.getTestScore() < minimumRequiredScore) {
           showAlert("Test Submission Failed",
                     "The test score (" + testScore + ") is below the minimum required score (" + minimumRequiredScore + ").");
           // Insert the test (even though it failed validation, to record the failed attempt)
           recruiterDao.insertTest(jobId, recruiterId, applicantId, testScore);
           return false; // Do not proceed further
       }
       // If the test score is valid, insert the test into the database
       boolean testInserted = recruiterDao.insertTest(jobId, recruiterId, applicantId, testScore);
       // If the test is successfully inserted, proceed with shortlisting the applicant
       if (testInserted) {
           boolean shortlistResult = recruiterDao.insertShortlist(jobId, applicantId, testScore);
           return shortlistResult; // Return the result of the shortlisting operation
       }
      //recruiter.addTest(test);
       return false; // Return false if test insertion failed
   }
   public boolean insertIntoShortlistAfterInterview(int applicantId, int jobId) {
       // Check in the database if this applicantId and jobId pair already exists
       if (recruiterDao.isApplicantAlreadyShortlisted(applicantId, jobId)) {
           showAlert("Duplicate Shortlist Entry",
                     "The applicant with ID " + applicantId + " is already shortlisted for Job " + jobId + ".");
           return false;
       }
       boolean result = recruiterDao.insertIntoShortlistAfterInterview(applicantId, jobId);
       if (result) {
           System.out.println("Applicant ID " + applicantId + " successfully shortlisted for Job ID " + jobId + ".");
       } else {
           System.err.println("Failed to shortlist Applicant ID " + applicantId + " for Job ID " + jobId + ".");
       }
       return result;
   }
   public int insertTestinfo(int jobId, int recruiterId, int no_of_questions, int minimum_requirement) {
       // Check if a test already exists for the given jobId
       if (recruiterDao.doesTestExist(jobId)) {
           showAlert("Test Creation Failed",
                     "A test already exists for Job ID: " + jobId + ". You cannot create multiple tests for the same job.");
           return -1; // Indicate failure due to duplicate test
       }
       // Check if the number of questions is valid
       if (no_of_questions < minimum_requirement) {
           showAlert("Test Creation Failed",
                     "The number of questions (" + no_of_questions + ") must be greater than or equal to the minimum requirement (" + minimum_requirement + ").");
           return -1; // Indicate failure due to invalid input
       }
       // If all conditions are satisfied, proceed to insert the new test
       return recruiterDao.insertTestInfo(jobId, recruiterId, no_of_questions, minimum_requirement);
   }
   public ObservableList<String> getApplicantQualifications(int applicantId)
   {
   	return recruiterDao.getApplicantQualificationsAndRecommendations(applicantId);
   }
   public ObservableList<String> getRecruiterQualifications(int recruiterId)
   {
   	return recruiterDao.getRecruiterQualifications(recruiterId);
   }
   public int get_no_of_questions(int JobId)
   {
   	return recruiterDao.getNumberOfQuestionsByJobId(JobId);
   }
   public Float get_minimum_req(int JobId)
   {
   	return recruiterDao.getMinimumScoreRequiredByJobId(JobId);
   }
   public boolean insertAppointment(int recruiterId, int applicantId, String appointmentTime, String description) {
       if (recruiterId <= 0 || applicantId <= 0) {
           System.out.println("Error: Invalid recruiter or applicant ID.");
           return false;
       }

       if (appointmentTime == null || appointmentTime.isEmpty()) {
           System.out.println("Error: Appointment time cannot be null or empty.");
           return false;
       }

       if (description == null || description.length() < 5) {
           System.out.println("Error: Description must be at least 5 characters long.");
           return false;
       }

       // Pass data to DAO for database insertion
       return recruiterDao.insertAppointment(recruiterId, applicantId, appointmentTime, description);
   }
 //Helper method to show alerts
 	private void showAlert(String title, String message) {
 	Alert alert = new Alert(Alert.AlertType.WARNING);
 	alert.setTitle(title);
 	alert.setHeaderText(null);
 	alert.setContentText(message);
 	alert.showAndWait();
 	}
   }

