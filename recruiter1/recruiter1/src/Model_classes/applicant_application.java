package Model_classes;
import java.util.Date;

public class applicant_application {
    private int applicationId;
    private int applicantIdTemp;
    private int jobId;
    private int qualificationId;
    private int experience;
    private float GPA;
    private Date applicationDate;  // Will store the application date

    // Default constructor
    public applicant_application() {
        this.applicationId = 0;
        this.applicantIdTemp = 0;
        this.jobId = 0;
        this.qualificationId = 0;
        this.experience = 0;
        this.GPA = 0.0f;
        this.applicationDate = new Date();  // Sets current date by default
    }

    // Parameterized constructor for creating a new application
    public applicant_application(int jobId, int applicantIdTemp) {
        this.jobId = jobId;
        this.applicantIdTemp = applicantIdTemp;
        this.applicationDate = new Date();  // Sets current date by default
    }

    // Getters and Setters
    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getApplicantIdTemp() {
        return applicantIdTemp;
    }

    public void setApplicantIdTemp(int applicantIdTemp) {
        this.applicantIdTemp = applicantIdTemp;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(int qualificationId) {
        this.qualificationId = qualificationId;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public float getGPA() {
        return GPA;
    }

    public void setGPA(float GPA) {
        this.GPA = GPA;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    // Override toString() method for debugging and displaying details of the application
    @Override
    public String toString() {
        return "ApplicationModel{" +
                "applicationId=" + applicationId +
                ", applicantIdTemp=" + applicantIdTemp +
                ", jobId=" + jobId +
                ", qualificationId=" + qualificationId +
                ", experience=" + experience +
                ", GPA=" + GPA +
                ", applicationDate=" + applicationDate +
                '}';
    }
}
