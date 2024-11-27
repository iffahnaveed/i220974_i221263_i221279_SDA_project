package Model_classes;
import java.util.Date;

public class Appointment {
    private int appointmentId;
    private int recruiterId;
    private int applicantId;
    private Date appointmentTime;
    private String description;

    // Constructor
    public Appointment(int appointmentId, int recruiterId, int applicantId, Date appointmentTime, String description) {
        this.appointmentId = appointmentId;
        this.recruiterId = recruiterId;
        this.applicantId = applicantId;
        this.appointmentTime = appointmentTime;
        this.description = description;
    }

    // Getters and Setters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(int recruiterId) {
        this.recruiterId = recruiterId;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
