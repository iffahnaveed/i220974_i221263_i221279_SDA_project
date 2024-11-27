package Model_classes;
import java.sql.Date;
import java.time.LocalDate;

public class Qualification_model {
    private int qualId;
    private String qualType;
    private String universitySchoolName;
    private Date yearCompleted;
    private float cgpa;
    private String field;  // Added field for the area of specialization

    // Default Constructor
    public Qualification_model() {}

    // Parameterized Constructor
    public Qualification_model(int qualId, String qualType, String universitySchoolName, Date yearCompleted, float cgpa, String field) {
        this.qualId = qualId;
        this.qualType = qualType;
        this.universitySchoolName = universitySchoolName;
        this.yearCompleted = yearCompleted;
        this.cgpa = cgpa;
        this.field = field; // Initialize the field
    }

    // Getters and Setters
    public int getQualId() {
        return qualId;
    }

    public void setQualId(int qualId) {
        this.qualId = qualId;
    }

    public String getQualType() {
        return qualType;
    }

    public void setQualType(String qualType) {
        this.qualType = qualType;
    }

    public String getUniversitySchoolName() {
        return universitySchoolName;
    }

    public void setUniversitySchoolName(String universitySchoolName) {
        this.universitySchoolName = universitySchoolName;
    }

    public Date getYearCompleted() {
        return yearCompleted;
    }

    public void setYearCompleted(Date yearCompleted2) {
        this.yearCompleted = yearCompleted2;
    }

    public float getCgpa() {
        return cgpa;
    }

    public void setCgpa(float cgpa) {
        this.cgpa = cgpa;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "Qualification {" +
                "qualId=" + qualId +
                ", qualType='" + qualType + '\'' +
                ", universitySchoolName='" + universitySchoolName + '\'' +
                ", yearCompleted=" + yearCompleted +
                ", cgpa=" + cgpa +
                ", field='" + field + '\'' +  // Add field in toString
                '}';
    }
}
