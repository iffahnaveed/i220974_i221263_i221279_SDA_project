package logic_layer;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import Model_classes.Applicant_model;
import Model_classes.Qualification_model;
import Model_classes.UserFactory;
import Model_classes.User_model;
import Model_classes.applicant_application;
import Model_classes.application_model;
import javafx.scene.control.Alert;
import database_logic.ApplicantDao;

public class Applicant_service extends UserService{
	int loggedInApplicantId;
	int recruiter_id;
	int qualification_id;
    private ApplicantDao applicantDao;
    Applicant_model applicant;
    public Applicant_service() {
        applicantDao = new ApplicantDao(); // Initialize the DAO
    }
    public void setLoggedInApplicantId(int applicantId) {
        this.loggedInApplicantId = applicantId;
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

        // Check credentials in the database
        int applicantId = applicantDao.validateLogin(email, password);
        if (applicantId != -1) {
            this.userId = applicantId;
            return applicantId; // Return the valid applicant ID
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Email or Password.");
            return -1; // Indicate failure
        }
    }
    // Method to get the logged-in applicant's ID
    public int getLoggedInApplicantId() {
        return loggedInApplicantId;
    }
    public int getQualificationId(int applicantId) {
        return applicantDao.getQualificationIdForApplicant(applicantId);
    }
    public List<String> getJobDetails() {
        List<String[]> jobs = getJobs(); // Fetch all job data
        List<String> jobDetailsList = new ArrayList<>();

        for (String[] job : jobs) {
            String details = """
                Job ID: %s | Title: %s | Description: %s | Company: %s 
                Required Skills: %s | Minimum CGPA: %s | Type: %s | Experience: %s years
                Deadline: %s | Recruiter ID: %s | Status: %s
            """.formatted(
                job[0], job[1], job[2], job[3], 
                job[4], job[5], job[6], job[7], 
                job[8], job[9], job[10]
            );
            jobDetailsList.add(details);
        }

        return jobDetailsList;
    }


    public List<Integer> getAgeRange() {
        return IntStream.rangeClosed(1, 100).boxed().toList();
    }

    public List<Integer> getExperienceRange() {
        return IntStream.rangeClosed(1, 50).boxed().toList();
    }

    public List<String> getSubjects() {
        return Arrays.asList(
            "Computer Science", "AI", "Data Science", "Cyber Security", "Software Engineering", 
            "Machine Learning", "Information Technology", "Cloud Computing", 
            "Blockchain", "Robotics"
        );
    }

    public List<String> getQualificationTypes() {
        return Arrays.asList("BS", "Masters", "PhD");
    }

    public List<String> getGenders() {
        return Arrays.asList("Male", "Female", "Other");
    }
    public String getFormattedMessagesForRecruiter(int recruiterId, int applicantId) {
        List<String> messages = getMessagesForRecruiter(recruiterId, applicantId);
        
        // Check if messages exist and format them
        if (messages.isEmpty()) {
            return "No messages found for the selected Recruiter and Applicant.";
        } else {
            StringBuilder messageBuilder = new StringBuilder();
            for (String message : messages) {
                messageBuilder.append(message).append("\n");
            }
            return messageBuilder.toString();
        }
    }
    public String getFormattedMessagesForHr(int hrId, int applicantId) {
        List<String> messages = getMessages(hrId, applicantId);
        
        if (messages.isEmpty()) {
            return "No messages found for the selected HR and Applicant.";
        } else {
            StringBuilder messageBuilder = new StringBuilder();
            for (String message : messages) {
                messageBuilder.append(message).append("\n");
            }
            return messageBuilder.toString();
        }
    }
    // Assume getJobs() is implemented here to fetch job data from the database
    public List<String[]> getJobs1() {
        // Implementation to fetch jobs (e.g., from the database)
        return new ArrayList<>(); // Placeholder
    }
    public int getExperience(int applicantId) {
        // Call the DAO method to fetch the experience value from the applicant table
        return applicantDao.getExperienceForApplicant(applicantId);
    }
    public boolean validateMessageInputs(String message, int selectedRecruiterId) {
        if (message == null || message.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Message cannot be empty.");
            return false; // Validation failed
        }

        if (selectedRecruiterId == 0) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a Recruiter ID.");
            return false; // Validation failed
        }

        return true; // Validation passed
    }
    @Override
    public boolean register(String name, String email, String password, int age, String gender, int experience) {
        // Validate inputs
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format.");
            return false;
        }

        if (!isValidPassword(password)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password must be at least 8 characters.");
            return false;
        }

        if (age < 18) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Age must be greater than 18.");
            return false;
        }

        if (age < experience) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Experience must be less than age.");
            return false;
        }

        // Create an object of Applicant_model using the User_factory
        UserFactory userFactory = new UserFactory();
        User_model applicant = userFactory.createUser("Applicant", 0, name, email, password, experience, age, gender);

        // Insert Applicant info into the database using applicantDao
        int applicantId = applicantDao.insertApplicant(
            applicant.getName(),
            applicant.getEmail(),
            applicant.getPassword(),
            applicant.getAge(),
            applicant.getGender(),
            applicant.getExperience()
        );

        if (applicantId == -1) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to register Applicant.");
            return false;
        }

        // Optionally set the applicant ID if required
        applicant.setUserId(applicantId);

        return true;
    }



    public boolean registerQualification(String qualType, String university, String yearCompleted, float cgpa, String qualSubject) {    	    
        int qualificationId = applicantDao.insertQualification(qualType, university, yearCompleted, cgpa, qualSubject);

        if (qualificationId == -1) {
            return false; // Return false if insertion fails
        }
        Qualification_model qualification = new Qualification_model();
        qualification.setQualType(qualType);
        qualification.setUniversitySchoolName(university);
        //qualification.setYearCompleted(Date.valueOf(yearCompleted)); // Convert String to Date
        qualification.setCgpa(cgpa);
        qualification.setField(qualSubject);
        // Link the applicant to the qualification
        applicantDao.linkApplicantToQualification(recruiter_id, qualificationId);
        return true; // Return true if the process succeeds
    }
    public boolean applyForJob(int applicantId, int jobId, int qualificationId, int experience) {
        // Check if the applicant has already applied for the job
        if (applicantDao.hasAlreadyApplied(applicantId, jobId)) {
            showAlert(Alert.AlertType.ERROR, "Application Error", "You have already applied for this job.");
            return false;
        }

        // Check if the application deadline has passed
        Date applicationDeadline = applicantDao.getApplicationDeadline(jobId);
        Date currentDate = new Date(); // Current date
        if (applicationDeadline != null && currentDate.after(applicationDeadline)) {
            showAlert(Alert.AlertType.ERROR, "Application Error", "The application deadline for this job has passed.");
            return false;
        }

        // Fetch the applicant's qualification details
        qualificationId = getQualificationId(applicantId);
        float applicantCgpa = applicantDao.getApplicantCgpa(qualificationId); // Fetch CGPA for the qualification
        experience = applicantDao.getExperienceForApplicant(applicantId); // Fetch experience for the applicant

        // Fetch the job's CGPA and experience requirements
        float requiredCgpa = applicantDao.getJobCgpaRequirement(jobId);
        int requiredExperience = applicantDao.getJobExperienceRequirement(jobId);

        // Check if the applicant meets the CGPA and experience requirements
        if (applicantCgpa < requiredCgpa) {
            showAlert(Alert.AlertType.ERROR, "Application Error", "Your CGPA does not meet the job's requirements.");
            return false;
        }

        if (experience < requiredExperience) {
            showAlert(Alert.AlertType.ERROR, "Application Error", "You do not have sufficient experience for this job.");
            return false;
        }
        
        // Insert the application into the database
        int applicationId = applicantDao.insertApplication(applicantId, jobId, qualificationId, experience);
        applicant_application application = new applicant_application();
        application.setApplicantIdTemp(applicantId);
        application.setJobId(jobId);
        application.setQualificationId(qualificationId);
        application.setExperience(experience);
        application.setGPA(applicantCgpa);
        application.setApplicationDate(currentDate);
        // If application insertion is successful, update the tracker and return true
        if (applicationId != -1) {
            // For example, if you want to add it to a list in the applicant model, use:
            // applicantModel.addApplication(application);
        	
            return true;
        } else {
            showAlert(Alert.AlertType.ERROR, "Application Error", "Failed to apply for the job. Please try again.");
            return false;
        }
    }
    public List<String[]> getJobs() {
        return applicantDao.getJobs(); // Fetch jobs from DAO
    }
    public List<Integer> getJobIds() {
        ApplicantDao dao = new ApplicantDao();
        return dao.getJobIds();
    }
    public List<Integer> getRecruiterIds(int jobId) {
        return applicantDao.getRecruiterIdsByJobId(jobId); // Delegate to DAO method
    }
    public List<Integer> getApplicantIds() {
        return applicantDao.getApplicantIds();
    }
    public List<Integer> getApplicationIdsForApplicant(int applicantId) {
        return applicantDao.getApplicationIdsForApplicant(applicantId);
    }
    public List<Integer> fetchHrIdsForApplicant(int applicantId) {
        return applicantDao.getHrIdsForApplicant(applicantId);
    }
    public boolean sendMessage(int recruiterId, int applicantId, String message) {
        return applicantDao.insertMessage(recruiterId, applicantId, message);
    }
    public boolean sendMessageToHR(int hrId, int applicantId, String message) {
        return applicantDao.insertMessageToHR(hrId, applicantId, message);
    }
    public int getJobIdByApplicationId(int applicationId) {
        return applicantDao.getJobIdByApplicationId(applicationId);
    }
    public List<String> getMessages(int hrId, int applicantId) {
        return applicantDao.getMessagesByHrAndApplicant(hrId, applicantId);
    }
    public List<String> getMessagesForRecruiter(int recruiterId, int applicantId) {
        return applicantDao.getMessagesByRecruiterAndApplicant(recruiterId, applicantId);
    }
    public String getJobStatusMessage(int jobId) throws SQLException  {
        int status = applicantDao.getJobStatusByJobId(jobId);

        // Determine the status message based on the status value
        if (status == -1) {
            return "No status found for the job.";
        } else if (status == 0) {
            return "Pending";
        } else if (status == 1) {
            return "Congratulations, you got the job!";
        } else {
            return "Unknown status for the job.";
        }
    }
    public List<String> fetchRecommendationsForApplicant(int applicantId) throws SQLException {
        return applicantDao.getRecommendationsByApplicantId(applicantId);
    }
    public String getApplicationStatusMessage(int applicantId,int JobId) throws SQLException {
        if (applicantDao.isHired(applicantId,JobId)) {
            return "Congratulations! You got the job.";
        } else if (applicantDao.isWaitingForHRResponse(applicantId,JobId)) {
            return "You got selected. Waiting for HR response.";
        } else if (applicantDao.isQualifiedForInterview(applicantId,JobId)) {
            return "You are qualified for the interview.";
        } else {
            return "No application status found.";
        }
    }
    public String getJobStatusMessage(int JobId,int applicantId) throws SQLException {
    	if (applicantDao.isHired(applicantId,JobId)) {
            return "Congratulations! You got the job.";
        } else if (applicantDao.isWaitingForHRResponse(applicantId,JobId)) {
            return "You got selected. Waiting for HR response.";
        } else if (applicantDao.isQualifiedForInterview(applicantId,JobId)) {
            return "You are qualified for the interview.";
        } else {
            return "No application status found.";
        }
    }
    public List<String> getApplicantPortfolio(int applicantId) {
        List<String> portfolio = new ArrayList<>();
        portfolio.addAll(applicantDao.getApplicantDetails(applicantId)); // Add applicant details
        portfolio.addAll(applicantDao.getApplicantQualifications(applicantId)); // Add qualifications
        return portfolio;
    }
    public List<String> getAllAppointments(int applicantID,int RecriuterID)
    {
    	return applicantDao.getAllAppointments(applicantID,RecriuterID);
    }
    public List<String> getAllContracts(int applicantId, int jobID) throws SQLException {
        // Check if the applicant is hired for the job
        if (applicantDao.isHired(applicantId, jobID)) {
            // Fetch and return the contracts for the given job ID
            return applicantDao.getAllContracts(jobID);
        } else {
            // Return a list with a custom message if the applicant is not hired
            List<String> notHiredMessage = new ArrayList<>();
            notHiredMessage.add("You are not hired for this job ID: " + jobID);
            return notHiredMessage;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}