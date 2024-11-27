package database_logic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.*;

public class ApplicantDao {

	private static final String URL = "jdbc:sqlserver://DESKTOP-9LTVH1T\\SQLEXPRESS;databaseName=EHMS;integratedSecurity=true;trustServerCertificate=true";
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    // Method to establish connection
    private Connection connect() {
        try {
            // Load the SQL Server JDBC driver
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null; // Return null if connection fails
        }
    }

    // Example method for validating login
    public int validateLogin(String email, String password) {
        String query = "SELECT applicant_id FROM applicant WHERE applicant_email = ? AND applicant_password = ?";
        
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {            
            // Set parameters for the prepared statement
            stmt.setString(1, email);
            stmt.setString(2, password);
            
            // Execute the query
            ResultSet rs = stmt.executeQuery();
            
            // Check if a result exists and retrieve applicant_id
            if (rs.next()) {
                return rs.getInt("applicant_id"); // Return the applicant_id if found
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if no result is found or an error occurs
    }

    // Insert qualification and return the generated qualification ID
    public int insertQualification(String qualType, String university, String yearCompleted, double cgpa, String qualSubject) {
        String sql = "INSERT INTO qualification (qual_type, university_school_name, year_completed, CGPA, qual_subject) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, qualType); // Set qualification type
            pstmt.setString(2, university); // Set university/school name
            pstmt.setDate(3, java.sql.Date.valueOf(yearCompleted)); // Convert and set year completed
            pstmt.setDouble(4, cgpa); // Set CGPA
            pstmt.setString(5, qualSubject); // Set qualification subject

            int rowsInserted = pstmt.executeUpdate(); // Execute insertion

            if (rowsInserted > 0) {
                ResultSet keys = pstmt.getGeneratedKeys(); // Get generated keys
                if (keys.next()) {
                    return keys.getInt(1); // Return the generated qualification ID
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if insertion fails
    }

    public int insertApplicant(String name, String email, String password, int age, String gender, int experience) {
        String sql = "INSERT INTO applicant (applicant_name, applicant_email, applicant_password, applicant_age, applicant_gender, applicant_experience) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setInt(4, age);
            pstmt.setString(5, gender);
            pstmt.setInt(6, experience);
            
            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1); // Return the generated applicant ID
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if insertion fails
    }
    
    public int insertApplication(int applicantId, int jobId, int qualificationId, int experience) {
        // Query to fetch CGPA from qualification through applicant_contains_qualification
        String fetchCgpaQuery = """
            SELECT q.CGPA
            FROM applicant_contains_qualification acq
            JOIN qualification q ON acq.quali_id = q.qual_id
            WHERE acq.applicant_id = ? AND acq.quali_id = ?
        """;

        String insertApplicationQuery = """
            INSERT INTO application (applicant_id_temp, job_id, qualification_id, experience, GPA) 
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = connect()) {
            // Fetch CGPA
            Float cgpa = null;
            try (PreparedStatement fetchStmt = conn.prepareStatement(fetchCgpaQuery)) {
                fetchStmt.setInt(1, applicantId);
                fetchStmt.setInt(2, qualificationId);

                try (ResultSet rs = fetchStmt.executeQuery()) {
                    if (rs.next()) {
                        cgpa = rs.getFloat("CGPA");
                    }
                }
            }

            // Ensure CGPA was fetched successfully
            if (cgpa == null) {
                System.err.println("CGPA not found for applicantId: " + applicantId + ", qualificationId: " + qualificationId);
                return -1; // Failure
            }

            // Insert into application table
            try (PreparedStatement insertStmt = conn.prepareStatement(insertApplicationQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setInt(1, applicantId);        // applicant_id_temp
                insertStmt.setInt(2, jobId);             // job_id
                insertStmt.setInt(3, qualificationId);   // qualification_id
                insertStmt.setInt(4, experience);        // experience
                insertStmt.setFloat(5, cgpa);           // GPA

                // Execute the insert
                int affectedRows = insertStmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = insertStmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            return rs.getInt(1); // Return generated application_id
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Indicate failure
    }

    public List<Integer> getRecruiterIdsByJobId(int jobId) {
        List<Integer> recruiterIds = new ArrayList<>();

        // SQL query to fetch recruiter IDs
        String query = "SELECT recruiter_id FROM job WHERE jobid = ?";

        try (Connection conn = connect(); // Replace with your database connection method
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            // Set the job ID in the query
            pstmt.setInt(1, jobId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    recruiterIds.add(rs.getInt("recruiter_id"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving recruiter IDs for Job ID: " + jobId);
        }

        return recruiterIds;
    }
    public List<Integer> getApplicantIds() {
        List<Integer> applicantIds = new ArrayList<>();

        try (Connection conn = connect(); // Your connection method
             PreparedStatement pstmt = conn.prepareStatement("SELECT applicant_id FROM applicant");
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                applicantIds.add(rs.getInt("applicant_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching applicant IDs from the database.");
        }

        return applicantIds;
    }
    public List<Integer> getApplicationIdsForApplicant(int applicantId) {
        List<Integer> applicationIds = new ArrayList<>();

        try (Connection conn = connect(); // Your connection method
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT a.application_id " +
                     "FROM application a " +
                     "JOIN applicant ap ON a.applicant_id_temp = ap.applicant_id " +
                     "WHERE ap.applicant_id = ?")) {

            // Set the parameter for the applicant ID
            pstmt.setInt(1, applicantId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    applicationIds.add(rs.getInt("application_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching application IDs for applicant ID: " + applicantId);
        }

        return applicationIds;
    }
    public List<Integer> getHrIdsForApplicant(int applicantId) {
        List<Integer> hrIds = new ArrayList<>();

        // SQL query to fetch HR IDs for the given applicantId from send_message_hr_and_applicant table
        String query = "SELECT DISTINCT hr_id FROM send_message_hr_and_applicant WHERE send_message_hrapplicant_id = ?";

        try (Connection conn = connect(); // Your connection method
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the applicant ID in the prepared statement
            pstmt.setInt(1, applicantId);

            // Execute the query and process the results
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    hrIds.add(rs.getInt("hr_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching HR IDs for Applicant ID: " + applicantId);
        }

        return hrIds; // Return the list of HR IDs
    }

    public boolean insertMessage(int recruiterId, int applicantId, String message) {
        String sql = "INSERT INTO receive_message_recruiter_and_applicant (receive_messagerecruiter_id, receive_messageapplicant_id, message) VALUES (?, ?, ?)";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recruiterId);
            pstmt.setInt(2, applicantId);
            pstmt.setString(3, message);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Return true if insert was successful
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error inserting message");
            return false;
        }
    }
    public boolean insertMessageToHR(int hrId, int applicantId, String message) {
        String sql = "INSERT INTO recieve_message_hr_and_applicant (hr_id, recieve_message_hrapplicant_id, hr_and_applicantmessage) VALUES (?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, hrId);
            pstmt.setInt(2, applicantId);
            pstmt.setString(3, message);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getJobIdByApplicationId(int applicationId) {
        String sql = "SELECT job_id FROM application WHERE application_id = ?";
        int jobId = -1;

        try (Connection conn = connect(); // Assuming DatabaseConnection is your utility
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, applicationId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    jobId = rs.getInt("job_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving job ID for Application ID: " + applicationId);
        }

        return jobId; // Returns -1 if no job_id found
    }
    public int getQualificationIdForApplicant(int applicantId) {
        String sql = "SELECT quali_id FROM applicant_contains_qualification WHERE applicant_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, applicantId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quali_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if no qualification is found or error occurs
    }
    public int getExperienceForApplicant(int applicantId) {
        String sql = "SELECT applicant_experience FROM applicant WHERE applicant_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, applicantId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("applicant_experience");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if experience not found or error occurs
    }
    public boolean updateJobStatus(int jobId, int status) {
        String sql = "UPDATE job SET job_status = ? WHERE jobid = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, status); // New status
            pstmt.setInt(2, jobId); // Job ID
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean linkApplicantToQualification(int applicantId, int qualificationId) {
        String sql = "INSERT INTO applicant_contains_qualification (applicant_id, quali_id) VALUES (?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters
            pstmt.setInt(1, applicantId);
            pstmt.setInt(2, qualificationId);

            // Execute insertion
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0; // Return true if successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an error occurs
        }
    }
    public List<String[]> getJobs() {
        String query = """
            SELECT jobid, job_title, job_description, company_name, 
                   qualification_skills_required, cgpa_required, 
                   job_type, job_experience, application_deadline, recruiter_id, job_status 
            FROM job 
            WHERE job_status = 1
        """;

        List<String[]> jobList = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String[] job = new String[11];
                job[0] = String.valueOf(rs.getInt("jobid"));
                job[1] = rs.getString("job_title");
                job[2] = rs.getString("job_description");
                job[3] = rs.getString("company_name");
                job[4] = rs.getString("qualification_skills_required");
                job[5] = String.valueOf(rs.getFloat("cgpa_required"));
                job[6] = rs.getString("job_type");
                job[7] = String.valueOf(rs.getInt("job_experience"));
                job[8] = rs.getDate("application_deadline").toString();
                job[9] = String.valueOf(rs.getInt("recruiter_id"));
                job[10] = String.valueOf(rs.getInt("job_status"));
                jobList.add(job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobList;
    }

   
    
    
    public List<Integer> getJobIds() {
        List<Integer> jobIds = new ArrayList<>();
        String query = "SELECT jobid FROM job WHERE job_status = 1";
// Adjust table and column names if needed

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                jobIds.add(rs.getInt("jobid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobIds;
    }
    public List<String> getMessagesByHrAndApplicant(int hrId, int applicantId) {
        String query = """
                SELECT hr_and_applicantmessage, message_date
                FROM send_message_hr_and_applicant
                WHERE hr_id = ? AND send_message_hrapplicant_id = ?
                ORDER BY message_date DESC
                """;

        List<String> messages = new ArrayList<>();

        try (Connection conn = connect(); // Replace with your DB connection utility
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters
            pstmt.setInt(1, hrId);
            pstmt.setInt(2, applicantId);

            // Execute query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String message = rs.getString("hr_and_applicantmessage");
                    Date messageDate = rs.getDate("message_date");
                    messages.add("Date: " + messageDate + "\nMessage: " + message + "\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }
    public List<String> getMessagesByRecruiterAndApplicant(int recruiterId, int applicantId) {
        String query = """
                SELECT  Top 1 message, message_date
                FROM send_message_recruiter_and_applicant
                WHERE send_messagerecruiter_id = ? AND send_messageapplicant_id = ?
                ORDER BY message_date DESC
                """;

        List<String> messages = new ArrayList<>();

        try (Connection conn = connect(); // Replace with your DB connection utility
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters
            pstmt.setInt(1, recruiterId);
            pstmt.setInt(2, applicantId);

            // Execute query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String message = rs.getString("message");
                    Date messageDate = rs.getDate("message_date");
                    messages.add("Date: " + messageDate + "\nMessage: " + message + "\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }
    public int getJobStatusByJobId(int jobId) throws SQLException {
        String query = "SELECT job_status_applicant FROM job WHERE jobid = ?";
        try (Connection conn = connect();
        		PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, jobId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("job_status_applicant");
                } else {
                    return -1; // Return -1 if no status is found
                }
            }
        }
    }
    public boolean isHired(int applicantId, int jobId) throws SQLException {
        String query = """
            SELECT 1 
            FROM shortlist_after_hr 
            WHERE shortlist_after_hrapplicant_id = ? AND job_id = ?
        """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, applicantId);
            pstmt.setInt(2, jobId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if both applicant ID and job ID exist
            }
        }
    }

    public boolean isWaitingForHRResponse(int applicantId, int jobId) throws SQLException {
        String query = """
            SELECT 1 
            FROM shortlist_after_interview 
            WHERE shortlistafterinterview_applicant_id = ? AND job_id = ?
        """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, applicantId);
            pstmt.setInt(2, jobId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if both applicant ID and job ID exist
            }
        }
    }

    public boolean isQualifiedForInterview(int applicantId, int jobId) throws SQLException {
        String query = """
            SELECT 1 
            FROM shortlist_after_test 
            WHERE shortlist_applicant_id = ? AND job_id = ?
        """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, applicantId);
            pstmt.setInt(2, jobId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if both applicant ID and job ID exist
            }
        }
    }

    public List<String> getRecommendationsByApplicantId(int applicantId) throws SQLException {
        List<String> recommendations = new ArrayList<>();
        String query = "SELECT recommendation_description FROM recommendation WHERE recommendationapplicant_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, applicantId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    recommendations.add(rs.getString("recommendation_description"));
                }
            }
        }
        return recommendations;
    }
    public List<String> getApplicantDetails(int applicantId) {
        List<String> applicantDetails = new ArrayList<>();
        String query = "SELECT * FROM applicant WHERE applicant_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, applicantId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    applicantDetails.add("Name: " + rs.getString("applicant_name"));
                    applicantDetails.add("Gender: " + rs.getString("applicant_gender"));
                    applicantDetails.add("Age: " + rs.getInt("applicant_age"));
                    applicantDetails.add("Email: " + rs.getString("applicant_email"));
                    applicantDetails.add("Experience: " + rs.getInt("applicant_experience") + " years");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return applicantDetails;
    }
    public List<String> getApplicantQualifications(int applicantId) {
        List<String> qualifications = new ArrayList<>();
        String query = """
                SELECT q.qual_type, q.university_school_name, q.year_completed, q.CGPA
                FROM qualification q
                JOIN applicant_contains_qualification acq ON q.qual_id = acq.quali_id
                WHERE acq.applicant_id = ?
                """;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, applicantId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    qualifications.add("Qualification: " + rs.getString("qual_type"));
                    qualifications.add("University: " + rs.getString("university_school_name"));
                    qualifications.add("Year Graduated: " + rs.getDate("year_completed"));
                    qualifications.add("CGPA: " + rs.getFloat("CGPA"));
                    qualifications.add("------------------------------------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return qualifications;
    }
    public boolean hasAlreadyApplied(int applicantId, int jobId) {
        String query = "SELECT COUNT(*) FROM application WHERE applicant_id_temp = ? AND job_id = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, applicantId);
            stmt.setInt(2, jobId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Return true if the count is greater than 0
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Return false in case of an error
    }
    public Date getApplicationDeadline(int jobId) {
        String query = "SELECT application_deadline FROM job WHERE jobid = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, jobId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate("application_deadline");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the job is not found or an error occurs
    }
    public float getApplicantCgpa(int qualificationId) {
        String query = "SELECT CGPA FROM qualification WHERE qual_id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, qualificationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat("CGPA");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if CGPA is not found
    }

    public float getJobCgpaRequirement(int jobId) {
        String query = "SELECT cgpa_required FROM job WHERE jobid = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, jobId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat("cgpa_required");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if CGPA requirement is not found
    }

    public int getJobExperienceRequirement(int jobId) {
        String query = "SELECT job_experience FROM job WHERE jobid = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, jobId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("job_experience");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if experience requirement is not found
    }
    public List<String> getAllAppointments(int applicantID, int recruiterID) {
        String query = """
                SELECT appointment_id, recruiter_id, applicant_id, appointment_time, description
                FROM appointment
                WHERE applicant_id = ? AND recruiter_id = ?
                """;

        List<String> appointments = new ArrayList<>();

        try (Connection conn = connect(); // Replace with your DB connection utility
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the parameters for applicantID and recruiterID
            pstmt.setInt(1, applicantID);
            pstmt.setInt(2, recruiterID);

            // Execute query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int appointmentId = rs.getInt("appointment_id");
                    int recruiterId = rs.getInt("recruiter_id");
                    int applicantId = rs.getInt("applicant_id");
                    String appointmentTime = rs.getString("appointment_time");
                    String description = rs.getString("description");

                    appointments.add("Appointment ID: " + appointmentId +
                            "\nRecruiter ID: " + recruiterId +
                            "\nApplicant ID: " + applicantId +
                            "\nTime: " + appointmentTime +
                            "\nDescription: " + description + "\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }
    public List<String> getAllContracts(int jobID) {
        String query = """
                SELECT contract_id, hr_id, salary, probation_period, hr_contractstart_date, 
                       hr_contractend_date, benefits
                FROM hr_contract
                WHERE job_id = ?
                """;

        List<String> contracts = new ArrayList<>();

        try (Connection conn = connect(); // Replace with your DB connection utility
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the parameter for jobID
            pstmt.setInt(1, jobID);

            // Execute query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int contractId = rs.getInt("contract_id");
                    int hrId = rs.getInt("hr_id");
                    String salary = rs.getString("salary");
                    int probationPeriod = rs.getInt("probation_period");
                    String contractStartDate = rs.getString("hr_contractstart_date");
                    String contractEndDate = rs.getString("hr_contractend_date");
                    String benefits = rs.getString("benefits");

                    contracts.add("Contract ID: " + contractId +
                            "\nHR ID: " + hrId +
                            "\nSalary: " + salary +
                            "\nProbation Period: " + probationPeriod + " months" +
                            "\nStart Date: " + contractStartDate +
                            "\nEnd Date: " + contractEndDate +
                            "\nBenefits: " + benefits + "\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contracts;
    }


    // Other DAO methods can go here, e.g., creating, updating, deleting users
}