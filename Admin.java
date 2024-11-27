package Model_classes;

import java.util.List;

public class Admin {
    private int adminId;
    private String adminEmail;
    private String adminPassword;

    // Default constructor
    public Admin() {
    }

    // Parameterized constructor
    public Admin(int adminId, String adminEmail, String adminPassword) {
        this.adminId = adminId;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    // Getters and Setters
    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    // Method to generate report of all applicants (Admin report)
    public void generateReport(List<Applicant_model> applicants) {
        if (applicants == null || applicants.isEmpty()) {
            System.out.println("No applicants available for reporting.");
            return;
        }

        System.out.println("Generating Report of Applicants...");
        System.out.println("=====================================");
        for (Applicant_model applicant : applicants) {
            System.out.println("Applicant ID: " + applicant.getUserId());
            System.out.println("Name: " + applicant.getName());
            System.out.println("Email: " + applicant.getEmail());
            System.out.println("Age: " + applicant.getAge());
            System.out.println("Gender: " + applicant.getGender());
            System.out.println("Experience: " + applicant.getApplicantExperience() + " years");
            System.out.println("Portfolio: " + (applicant.getPortfolioLink() != null ? applicant.getPortfolioLink() : "N/A"));
            System.out.println("-------------------------------------");
        }
        System.out.println("=====================================");
    }

    // Method to generate report for HR (showing HR-specific info)
    public void generateHRReport(List<HR_model> hrList) {
        if (hrList == null || hrList.isEmpty()) {
            System.out.println("No HR records available for reporting.");
            return;
        }

        System.out.println("Generating HR Report...");
        System.out.println("=====================================");
        for (HR_model hr : hrList) {
            System.out.println("HR ID: " + hr.getHrId());
            System.out.println("HR Name: " + hr.getHrName());
            System.out.println("HR Email: " + hr.getHrEmail());
            System.out.println("-------------------------------------");
        }
        System.out.println("=====================================");
    }

    // Method to generate report for Recruiter (showing Recruiter-specific info)
    public void generateRecruiterReport(List<Recruiter_model> recruiterList) {
        if (recruiterList == null || recruiterList.isEmpty()) {
            System.out.println("No Recruiter records available for reporting.");
            return;
        }

        System.out.println("Generating Recruiter Report...");
        System.out.println("=====================================");
        for (Recruiter_model recruiter : recruiterList) {
            System.out.println("Recruiter ID: " + recruiter.getUserId());
            System.out.println("Recruiter Name: " + recruiter.getName());
            System.out.println("Recruiter Email: " + recruiter.getEmail());
            System.out.println("-------------------------------------");
        }
        System.out.println("=====================================");
    }

    // toString method
    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", adminEmail='" + adminEmail + '\'' +
                ", adminPassword='" + adminPassword + '\'' +
                '}';
    }
}
