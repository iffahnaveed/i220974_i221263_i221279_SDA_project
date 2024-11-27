package Model_classes;

import java.util.ArrayList;
import java.util.List;

public class HR_model extends User_model {
    // New fields to store recommendations, contracts, shortlisted applicants, and messages
    private List<Recommendation_model> recommendation_list = new ArrayList<>(); 
    private List<Contract_model> contracts_list = new ArrayList<>();
    private List<Integer> shortlistedApplicants = new ArrayList<>(); // List to store shortlisted applicant IDs
    private List<Message_model> messagesToApplicants = new ArrayList<>(); // List for messages sent to applicants
    private List<Message_model> messagesFromApplicants = new ArrayList<>(); // List for messages received from applicants
    public HR_model(int userId, String name, String email, String password,int applicantExperience, int age, String gender) {
   	 super(userId, name, email, password,applicantExperience, age, gender);
   }

    // Getters and Setters for HR attributes
    public int getHrId() {
        return userId;
    }

    public void setHrId(int hrId) {
        this.userId = hrId;
    }

    public String getHrName() {
        return name;
    }

    public void setHrName(String hrName) {
        this.name = hrName;
    }

    public String getHrEmail() {
        return email;
    }

    public void setHrEmail(String hrEmail) {
        this.email = hrEmail;
    }

    public String getHrPassword() {
        return password;
    }

    public void setHrPassword(String hrPassword) {
        this.password = hrPassword;
    }

    public int getHrAge() {
        return age;
    }

    public void setHrAge(int hrAge) {
        this.age = hrAge;
    }

    public String getHrGender() {
        return gender;
    }

    public void setHrGender(String hrGender) {
        this.gender = hrGender;
    }

    public int getHrExperience() {
        return experience;
    }

    public void setHrExperience(int hrExperience) {
        this.experience = hrExperience;
    }

    // Getters and Setters for Recommendations and Contracts
    public List<Recommendation_model> getRecommendations() {
        return recommendation_list;
    }

    public void addRecommendation(Recommendation_model recommendation) {
        this.recommendation_list.add(recommendation);
        System.out.println("Recommendation added.");
    }

    public List<Contract_model> getContracts() {
        return contracts_list;
    }

    public void addContract(Contract_model contract) {
        this.contracts_list.add(contract);
        System.out.println("Contract added.");
    }

    // Methods for Shortlisting Applicants
    public void shortlistApplicant(int applicantId) {
        if (!shortlistedApplicants.contains(applicantId)) {
            shortlistedApplicants.add(applicantId);
            System.out.println("Applicant with ID " + applicantId + " has been shortlisted.");
        } else {
            System.out.println("Applicant with ID " + applicantId + " is already shortlisted.");
        }
    }

    public List<Integer> getShortlistedApplicants() {
        return shortlistedApplicants;
    }

    // Methods for Sending Messages
    public void sendMessageToApplicant(int applicantId, String message) {
        Message_model messageToApplicant = new Message_model(userId, applicantId, message, "Sent");
        messagesToApplicants.add(messageToApplicant);
        System.out.println("Message sent to applicant with ID " + applicantId);
    }

    // Methods for Receiving Messages
    public void receiveMessageFromApplicant(int applicantId, String message) {
        Message_model messageFromApplicant = new Message_model(applicantId, userId, message, "Received");
        messagesFromApplicants.add(messageFromApplicant);
        System.out.println("Message received from applicant with ID " + applicantId);
    }

    // Getters for Messages
    public List<Message_model> getMessagesToApplicants() {
        return messagesToApplicants;
    }

    public List<Message_model> getMessagesFromApplicants() {
        return messagesFromApplicants;
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "HR {" +
                "hrId=" + userId +
                ", hrName='" + name + '\'' +
                ", hrEmail='" + email + '\'' +
                ", hrPassword='" + password + '\'' +
                ", hrAge=" + age +
                ", hrGender='" + gender + '\'' +
                ", hrExperience=" + experience +
                ", recommendations=" + recommendation_list +
                ", contracts=" + contracts_list +
                ", shortlistedApplicants=" + shortlistedApplicants +
                '}';
    }
}
