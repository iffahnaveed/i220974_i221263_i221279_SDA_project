package Model_classes;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class application_model {
    private SimpleIntegerProperty applicantId;
    private SimpleStringProperty date;
    private SimpleDoubleProperty gpa;
    private SimpleIntegerProperty experience;
    private int Job_id;
    public application_model()
    {
    	
    }
    // Constructor
    public application_model(int applicantId, String date, double gpa, int experience) {
        this.applicantId = new SimpleIntegerProperty(applicantId);
        this.date = new SimpleStringProperty(date);
        this.gpa = new SimpleDoubleProperty(gpa);
        this.experience = new SimpleIntegerProperty(experience);
    }

    // Getters and Setters
    public int getApplicantId() {
        return applicantId.get();
    }

    public void setApplicantId(int applicantId) {
        this.applicantId.set(applicantId);
    }
    public int getJobId() {
        return Job_id;
    }

    public void setJobId(int job_id) {
       this.Job_id=job_id;
    }
    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public double getGpa() {
        return gpa.get();
    }

    public void setGpa(double gpa) {
        this.gpa.set(gpa);
    }

    public int getExperience() {
        return experience.get();
    }

    public void setExperience(int experience) {
        this.experience.set(experience);
    }
}
