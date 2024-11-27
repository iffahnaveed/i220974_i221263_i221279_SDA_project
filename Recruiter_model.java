package Model_classes;

import java.util.ArrayList;
import java.util.List;

public class Recruiter_model extends User_model {
    public List<Job_model> jobList = new ArrayList<>();
    public List<Test> testList = new ArrayList<>();
    public List<Applicant_model> applicantsList = new ArrayList<>();  // List to hold applicants

    // Constructor
    public Recruiter_model(int userId, String name, String email, String password, int experience, int age, String gender) {
        // Call the constructor of the User_model class to initialize inherited properties
        super(userId, name, email, password, experience, age, gender);  // Assuming User_model has a constructor that takes all these parameters
    }

    // Getters and Setters
    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    // Method to add a Job to the jobList
    public void addJob(Job_model job) {
        this.jobList.add(job);
        System.out.println("Job added: " + job.getJobTitle());
    }

    // Method to add a Test to the testList
    public void addTest(Test test) {
        this.testList.add(test);
        System.out.println("Test added for Applicant ID: " + test.getApplicantId());
    }

    // Method to view applications for a specific job
    public void viewApplications(int jobId) {
        System.out.println("Applications for Job ID: " + jobId);
        for (Applicant_model applicant : applicantsList) {
            if (applicant.getUserId() == jobId) {
                System.out.println("Applicant ID: " + applicant.getUserId() + " - " + applicant.getName());
            }
        }
    }

    // Method to create and conduct test for a specific applicant
    public void conductTest(int applicantId, int jobId, String testDetails) {
        Test test = new Test();
        addTest(test);
        System.out.println("Test created and assigned to Applicant ID: " + applicantId);
    }

    // Method to shortlist an applicant
    public void shortlistApplicant(int applicantId, int jobId) {
        for (Applicant_model applicant : applicantsList) {
            if (applicant.getUserId() == applicantId) {
                System.out.println("Applicant with ID: " + applicantId + " has been shortlisted for Job ID: " + jobId);
                return;
            }
        }
        System.out.println("Applicant not found for the specified job.");
    }

    // Method to send a message to an applicant
    public void sendMessageToApplicant(int applicantId, String message) {
        Message_model messageToApplicant = new Message_model(userId, applicantId, message, "Sent");
        System.out.println("Message sent to Applicant ID: " + applicantId);
    }

    // Method to receive a message from an applicant
    public void receiveMessageFromApplicant(int applicantId, String message) {
        Message_model messageFromApplicant = new Message_model(applicantId, userId, message, "Received");
        System.out.println("Message received from Applicant ID: " + applicantId);
    }

    // Method to view an applicant's information
    public void viewApplicantInformation(int applicantId) {
        for (Applicant_model applicant : applicantsList) {
            if (applicant.getUserId() == applicantId) {
                System.out.println("Applicant ID: " + applicant.getUserId());
                System.out.println("Name: " + applicant.getName());
                System.out.println("Email: " + applicant.getEmail());
                System.out.println("Age: " + applicant.getAge());
                System.out.println("Gender: " + applicant.getGender());
                System.out.println("Experience: " + applicant.getApplicantExperience());
                return;
            }
        }
        System.out.println("Applicant not found.");
    }

    // Method to view an applicant's portfolio (this could be a list of links or documents)
    public void viewApplicantPortfolio(int applicantId) {
        for (Applicant_model applicant : applicantsList) {
            if (applicant.getUserId() == applicantId) {
                System.out.println("Portfolio for Applicant ID: " + applicantId);
                String portfolio = applicant.getPortfolioLink();
                if (portfolio == null || portfolio.isEmpty()) {
                    System.out.println("No portfolio available.");
                } else {
                    System.out.println(portfolio);
                }
                return;
            }
        }
        System.out.println("Applicant not found.");
    }
}
