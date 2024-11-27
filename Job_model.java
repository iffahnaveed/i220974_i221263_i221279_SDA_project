package Model_classes;

import java.sql.Date;

public class Job_model {
    private int jobId;
    private String jobTitle;
    private String jobDescription;
    private String companyName;
    private String qualificationSkillsRequired;
    private float cgpaRequired;
    private String jobType;
    private int jobExperience;
    private Date applicationDeadline;
    private int recruiterId;
    private int jobStatus;

    // Default Constructor
    public Job_model() {}

    // Parameterized Constructor
    public Job_model(int jobId, String jobTitle, String jobDescription, String companyName,
               String qualificationSkillsRequired, float cgpaRequired, String jobType,
               int jobExperience, Date applicationDeadline, int recruiterId, int jobStatus) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.companyName = companyName;
        this.qualificationSkillsRequired = qualificationSkillsRequired;
        this.cgpaRequired = cgpaRequired;
        this.jobType = jobType;
        this.jobExperience = jobExperience;
        this.applicationDeadline = applicationDeadline;
        this.recruiterId = recruiterId;
        this.jobStatus = jobStatus;
    }

    // Getters and Setters
    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getQualificationSkillsRequired() {
        return qualificationSkillsRequired;
    }

    public void setQualificationSkillsRequired(String qualificationSkillsRequired) {
        this.qualificationSkillsRequired = qualificationSkillsRequired;
    }

    public float getCgpaRequired() {
        return cgpaRequired;
    }

    public void setCgpaRequired(float cgpaRequired) {
        this.cgpaRequired = cgpaRequired;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public int getJobExperience() {
        return jobExperience;
    }

    public void setJobExperience(int jobExperience) {
        this.jobExperience = jobExperience;
    }

    public Date getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(Date applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public int getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(int recruiterId) {
        this.recruiterId = recruiterId;
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "Job {" +
                "jobId=" + jobId +
                ", jobTitle='" + jobTitle + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                ", companyName='" + companyName + '\'' +
                ", qualificationSkillsRequired='" + qualificationSkillsRequired + '\'' +
                ", cgpaRequired=" + cgpaRequired +
                ", jobType='" + jobType + '\'' +
                ", jobExperience=" + jobExperience +
                ", applicationDeadline=" + applicationDeadline +
                ", recruiterId=" + recruiterId +
                ", jobStatus=" + jobStatus +
                '}';
    }
}
