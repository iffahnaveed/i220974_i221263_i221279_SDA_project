package Model_classes;

import java.sql.Date;

public class Contract_model {
    private int contractId;
    private int jobId;
    private int hrId;
    private String salary;
    private int probationPeriod;
    private Date contractStartDate;
    private Date contractEndDate;
    private String benefits;

    // Default Constructor
    public Contract_model() {}

    // Parameterized Constructor
    public Contract_model(int contractId, int jobId, int hrId, String salary, int probationPeriod,
                      Date contractStartDate, Date contractEndDate, String benefits) {
        this.contractId = contractId;
        this.jobId = jobId;
        this.hrId = hrId;
        this.salary = salary;
        this.probationPeriod = probationPeriod;
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.benefits = benefits;
    }

    // Getters and Setters
    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getHrId() {
        return hrId;
    }

    public void setHrId(int hrId) {
        this.hrId = hrId;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public int getProbationPeriod() {
        return probationPeriod;
    }

    public void setProbationPeriod(int probationPeriod) {
        this.probationPeriod = probationPeriod;
    }

    public Date getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Date contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "HrContract {" +
                "contractId=" + contractId +
                ", jobId=" + jobId +
                ", hrId=" + hrId +
                ", salary='" + salary + '\'' +
                ", probationPeriod=" + probationPeriod +
                ", contractStartDate=" + contractStartDate +
                ", contractEndDate=" + contractEndDate +
                ", benefits='" + benefits + '\'' +
                '}';
    }
}
