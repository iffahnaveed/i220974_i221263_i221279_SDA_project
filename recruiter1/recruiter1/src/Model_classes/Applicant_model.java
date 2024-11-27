package Model_classes;

import java.util.ArrayList;
import java.util.List;

public class Applicant_model extends User_model {
    private List<applicant_application> applicationsList = new ArrayList<>();
    private String portfolioLink;
    // Messages between the applicant and HR/recruiter
    private List<String> hrMessages = new ArrayList<>();
    private List<String> recruiterMessages = new ArrayList<>();
    // Parameterized Constructor
    public Applicant_model(int userId, String name, String email, String password,int applicantExperience, int age, String gender) {
    	 super(userId, name, email, password,applicantExperience, age, gender);
    }

    // Getters and Setters for specific Applicant fields
    public int getApplicantExperience() {
        return experience;
    }

    public void setApplicantExperience(int applicantExperience) {
        this.experience = applicantExperience;
    }

    public List<applicant_application> getApplicationsList() {
        return applicationsList;
    }

    // Add an application to the list
    public void addApplication(applicant_application application) {
        if (application != null) {
            applicationsList.add(application);
            System.out.println("Application added for Applicant ID: " + this.getUserId());
        } else {
            System.out.println("Invalid application.");
        }
    }

    public String getPortfolioLink() {
        return portfolioLink;
    }

    public void setPortfolioLink(String portfolioLink) {
        this.portfolioLink = portfolioLink;
    }

    // Messaging methods
    public List<String> getHrMessages() {
        return hrMessages;
    }

    public void addHrMessage(String message) {
        if (message != null && !message.isEmpty()) {
            this.hrMessages.add(message);
            System.out.println("HR message added: " + message);
        } else {
            System.out.println("Invalid HR message.");
        }
    }

    public List<String> getRecruiterMessages() {
        return recruiterMessages;
    }

    public void addRecruiterMessage(String message) {
        if (message != null && !message.isEmpty()) {
            this.recruiterMessages.add(message);
            System.out.println("Recruiter message added: " + message);
        } else {
            System.out.println("Invalid recruiter message.");
        }
    }

    // View Available Jobs (Assumes a List of Job_model exists somewhere)
    public void viewAvailableJobs(List<Job_model> availableJobs) {
        if (availableJobs == null || availableJobs.isEmpty()) {
            System.out.println("No available jobs at the moment.");
        } else {
            System.out.println("Available Jobs:");
            for (Job_model job : availableJobs) {
                System.out.println(job.toString());  // Assuming Job_model has a toString method
            }
        }
    }

    // Apply for a Job (Creates an application)
    public void applyForJob(Job_model job) {
        if (job != null) {
        	applicant_application application = new applicant_application(job.getJobId(),this.getUserId());
            applicationsList.add(application);  // Add to applications list
            System.out.println("You have applied for the job: " + job.getJobTitle());
        } else {
            System.out.println("Invalid job application.");
        }
    }

    // Check Application Status
    public void checkApplicationStatus(int jobId) {
        boolean found = false;
        for (applicant_application app : applicationsList) {
            // Check if the applicant has applied for this job by comparing the jobId
            if (app.getJobId() == jobId) {
                System.out.println("Status for Job ID " + jobId + ": " + app.getJobId());
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("You have not applied for Job ID " + jobId);
        }
    }

    // View Portfolio Link
    public void viewPortfolio() {
        if (portfolioLink != null && !portfolioLink.isEmpty()) {
            System.out.println("Portfolio Link: " + portfolioLink);
        } else {
            System.out.println("No portfolio available.");
        }
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "Applicant {" +
                "userId=" + getUserId() +  // Access from User_model
                ", name='" + getName() + '\'' +  // Access from User_model
                ", email='" + getEmail() + '\'' +  // Access from User_model
                ", age=" + getAge() +  // Access from User_model
                ", gender='" + getGender() + '\'' +  // Access from User_model
                ", applicantExperience=" + experience +
                ", portfolioLink='" + portfolioLink + '\'' +
                ", applicationsList=" + applicationsList +
                '}';
    }
}
