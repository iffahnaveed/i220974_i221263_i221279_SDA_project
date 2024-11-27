package Model_classes;
import java.sql.Date;

public class Test {
    private int testId;
    private int jobId;
    private int recruiterId;
    private int applicantId;
    private Date testDate;
    private float testScore;

    // Default Constructor
    public Test() {}

    // Parameterized Constructor
    public Test(int testId, int jobId, int recruiterId, int applicantId, Date testDate, float testScore) {
        this.testId = testId;
        this.jobId = jobId;
        this.recruiterId = recruiterId;
        this.applicantId = applicantId;
        this.testDate = testDate;
        this.testScore = testScore;
    }

    // Getters and Setters
    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
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

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public float getTestScore() {
        return testScore;
    }

    public void setTestScore(float testScore) {
        this.testScore = testScore;
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "TestTaken {" +
                "testId=" + testId +
                ", jobId=" + jobId +
                ", recruiterId=" + recruiterId +
                ", applicantId=" + applicantId +
                ", testDate=" + testDate +
                ", testScore=" + testScore +
                '}';
    }
}
