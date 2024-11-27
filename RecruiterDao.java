package database_logic;
import Model_classes.Recruiter_model;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Model_classes.application_model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model_classes.MessageRecord;
public class RecruiterDao {
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
    public int validateLogin(String email, String password) {
        String query = "SELECT recruiter_id FROM recruiter WHERE recruiter_email = ? AND recruiter_password = ?";
        
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("recruiter_id"); // Return recruiter_id if login is successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if login fails
    }
    public int insertRecruiter(String recruiterName, String recruiterEmail, String recruiterPassword, int recruiterAge, String recruiterGender, int recruiterExperience) {
        String query = "INSERT INTO recruiter (recruiter_name, recruiter_email, recruiter_password, recruiter_age, recruiter_gender, recruiter_experience) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters for prepared statement
            stmt.setString(1, recruiterName);
            stmt.setString(2, recruiterEmail);
            stmt.setString(3, recruiterPassword);
            stmt.setInt(4, recruiterAge);
            stmt.setString(5, recruiterGender);
            stmt.setInt(6, recruiterExperience);
            
            stmt.executeUpdate();
            
            // Get the generated recruiter ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the recruiter_id of the newly inserted recruiter
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if insert fails
    }

    // Insert qualification into the qualification table
    public int insertQualification(String qualType, String universitySchoolName, Date yearCompleted, float cgpa, String recruiterField) {
        String query = "INSERT INTO qualification (qual_type, university_school_name, year_completed, CGPA, Field) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters for prepared statement
            stmt.setString(1, qualType);
            stmt.setString(2, universitySchoolName);
            stmt.setDate(3, yearCompleted);
            stmt.setFloat(4, cgpa);
            stmt.setString(5, recruiterField); // Set recruiterField for Field column
            
            stmt.executeUpdate();
            
            // Get the generated qualification ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the qual_id of the newly inserted qualification
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if insert fails
    }
    public boolean applicantHasTestScore(int applicantId, int jobId) {
        String query = """
            SELECT 1 
            FROM test_taken 
            WHERE test_takenapplicant_id = ? AND test_takenjob_id = ?
        """;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameters for applicant ID and job ID
            stmt.setInt(1, applicantId);
            stmt.setInt(2, jobId);

            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Returns true if a record exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an error
        }
    }


    // Insert a record in the recruiter_contains_qualification table to link recruiter and qualification
    public boolean insertRecruiterQualification(int recruiterId, int qualId) {
        String query = "INSERT INTO recruiter_contains_qualification (recruiter_id, quali_id) VALUES (?, ?)";
        
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set parameters for prepared statement
            stmt.setInt(1, recruiterId);
            stmt.setInt(2, qualId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if the insert was successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean insertJob(String jobTitle, String jobDescription, String companyName, String qualifications, float cgpaRequired, String jobType, int experienceRequired, Date applicationDeadline, int recruiterId) {
        String query = "INSERT INTO job (job_title, job_description, company_name, qualification_skills_required, cgpa_required, job_type, job_experience, application_deadline, recruiter_id, job_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) { 
            
        	stmt.setString(1, jobTitle);
        	stmt.setString(2, jobDescription);
        	stmt.setString(3, companyName);
        	stmt.setString(4, qualifications);
        	stmt.setFloat(5, cgpaRequired);
        	stmt.setString(6, jobType);
        	stmt.setInt(7, experienceRequired);
        	stmt.setDate(8, applicationDeadline);
        	stmt.setInt(9, recruiterId);
        	stmt.setInt(10, 1); // Assuming job_status = 1 for active jobs
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Integer> getJobIdsByRecruiter(int recruiterId) {
        String query = "SELECT jobid FROM job WHERE recruiter_id = ?";
        List<Integer> jobIds = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, recruiterId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                jobIds.add(rs.getInt("jobid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobIds;
    }
    public List<Integer> getApplicantIdsByRecruiter(int recruiterId, int jobId) {
        String query = """
            SELECT DISTINCT a.applicant_id_temp
            FROM application a
            JOIN job j ON a.job_id = j.jobid
            WHERE j.recruiter_id = ? AND j.jobid = ?
        """;

        List<Integer> applicantIds = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, recruiterId);
            stmt.setInt(2, jobId); // Set the jobId parameter
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                applicantIds.add(rs.getInt("applicant_id_temp"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return applicantIds;
    }
    public boolean insert_into_send_message_recruiter_and_applicant(int applicant_id, String message, int recruiterId) {
        String query = "INSERT INTO send_message_recruiter_and_applicant (send_messagerecruiter_id, send_messageapplicant_id, message, message_date) " +
                       "VALUES (?, ?, ?, GETDATE())";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the values for the query
            stmt.setInt(1, recruiterId);  // Set recruiter_id
            stmt.setInt(2, applicant_id); // Set applicant_id
            stmt.setString(3, message);   // Set message content

            // Execute the update
            int rowsAffected = stmt.executeUpdate();
            
            // Check if the message was successfully inserted
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
            return false;
        }
    }

    public List<application_model> getApplicationsByJobId(int jobId) {
        String query = "SELECT a.applicant_id_temp, a.application_date, a.GPA, a.experience " +
                       "FROM application a WHERE a.job_id = ?";
        List<application_model> applications = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, jobId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Map table columns to the updated model fields
                int applicantId = rs.getInt("applicant_id_temp");
                String date = rs.getString("application_date"); // Assuming Date is stored as a String
                double gpa = rs.getDouble("GPA");
                int experience = rs.getInt("experience");

                // Create an application_model object using the updated constructor
                applications.add(new application_model(applicantId, date, gpa, experience));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }
    public Recruiter_model getRecruiterByRecruiterId(int recruiterId) {
        String query = "SELECT recruiter_id, recruiter_name, recruiter_email, recruiter_password, " +
                       "recruiter_age, recruiter_gender, recruiter_experience " +
                       "FROM recruiter WHERE recruiter_id = ?";
        Recruiter_model recruiter = null;

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, recruiterId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("recruiter_id");
                String name = rs.getString("recruiter_name");
                String email = rs.getString("recruiter_email");
                String password = rs.getString("recruiter_password"); // Retrieve the password
                int age = rs.getInt("recruiter_age");
                String gender = rs.getString("recruiter_gender");
                int experience = rs.getInt("recruiter_experience");

                // Create a Recruiter_model object with all the retrieved values
                recruiter = new Recruiter_model(id, name, email, password, experience, age, gender);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception appropriately
        }

        return recruiter;
    }

    public List<MessageRecord> getMessagesByApplicantId(int applicantId) {
        List<MessageRecord> messages = new ArrayList<>();
        String query = "SELECT receive_messagerecruiter_id, message FROM receive_message_recruiter_and_applicant WHERE receive_messageapplicant_id = ?";
        
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, applicantId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int recruiterId = rs.getInt("receive_messagerecruiter_id");
                String message = rs.getString("message");
                messages.add(new MessageRecord(recruiterId, message));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    public boolean insertTest(int jobId, int recruiterId, int applicantId, float testScore) {
        String insertTestQuery = """
            INSERT INTO test_taken (test_takenjob_id, test_takenrecruiter_id, test_takenapplicant_id, test_score) 
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(insertTestQuery)) {
        	stmt.setInt(1, jobId);
        	stmt.setInt(2, recruiterId);
        	stmt.setInt(3, applicantId);
        	stmt.setFloat(4, testScore);

            return stmt.executeUpdate() > 0; // Return true if insertion succeeded
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false on failure
        }
    }
    public boolean insertShortlist(int jobId, int applicantId, float testScore) {
        String insertShortlistQuery = """
            INSERT INTO shortlist_after_test (shortlist_applicant_id, job_id, test_score) 
            VALUES (?, ?, ?)
        """;

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(insertShortlistQuery)) {
            stmt.setInt(1, applicantId);
            stmt.setInt(2, jobId);
            stmt.setFloat(3, testScore);

            return stmt.executeUpdate() > 0; // Return true if insertion succeeded
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false on failure
        }
    }


    public List<Integer> getShortlistedApplicantIds(int jobId) {
        String query = """
            SELECT DISTINCT shortlist_applicant_id
            FROM shortlist_after_test
            WHERE job_id = ?
        """;

        List<Integer> shortlistedIds = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the job_id parameter
            stmt.setInt(1, jobId);

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            // Add results to the list
            while (rs.next()) {
                shortlistedIds.add(rs.getInt("shortlist_applicant_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shortlistedIds;
    }
    public boolean insertIntoShortlistAfterInterview(int applicantId, int jobId) {
        // Define the SQL query for inserting into the shortlist_after_interview table
        String query = "INSERT INTO shortlist_after_interview (shortlistafterinterview_applicant_id, job_id) " +
                       "VALUES (?, ?)";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the values for the query
            stmt.setInt(1, applicantId); // Set applicant_id
            stmt.setInt(2, jobId);       // Set job_id

            // Execute the update
            int rowsAffected = stmt.executeUpdate();

            // Check if the insertion was successful
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
            return false;
        }
    }
    public boolean insertAppointment(int recruiterId, int applicantId, String appointmentTime, String description) {
        // Define the SQL query for inserting into the appointment table
        String query = "INSERT INTO appointment (recruiter_id, applicant_id, appointment_time, description) " +
                       "VALUES (?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the values for the query
            stmt.setInt(1, recruiterId);                 // Set recruiter_id
            stmt.setInt(2, applicantId);                // Set applicant_id
            stmt.setString(3, appointmentTime);         // Set appointment_time (as a string in 'YYYY-MM-DD HH:MM:SS' format)
            stmt.setString(4, description);             // Set description

            // Execute the update
            int rowsAffected = stmt.executeUpdate();

            // Check if the insertion was successful
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<String> getApplicantQualificationsAndRecommendations(int applicantId) {
        String qualificationQuery = """
            SELECT a.applicant_id, a.applicant_name, a.applicant_experience, 
                   q.qual_type, q.university_school_name, q.year_completed, q.CGPA
            FROM applicant a
            JOIN applicant_contains_qualification aq ON a.applicant_id = aq.applicant_id
            JOIN qualification q ON aq.quali_id = q.qual_id
            WHERE a.applicant_id = ?
        """;

        String recommendationQuery = """
            SELECT recommendation_description
            FROM recommendation
            WHERE recommendationapplicant_id = ?
        """;

        ObservableList<String> applicantDetails = FXCollections.observableArrayList();

        try (Connection conn = connect()) {
            // Fetch qualifications
            try (PreparedStatement stmt = conn.prepareStatement(qualificationQuery)) {
                stmt.setInt(1, applicantId);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        // Extract applicant data
                        String applicantName = rs.getString("applicant_name");
                        int applicantExperience = rs.getInt("applicant_experience");

                        // Extract qualification details
                        String qualificationType = rs.getString("qual_type");
                        String universityName = rs.getString("university_school_name");
                        Date yearCompleted = rs.getDate("year_completed");
                        float cgpa = rs.getFloat("CGPA");

                        // Format the qualification entry
                        String qualificationEntry = """
                            Applicant ID: %d, Name: %s, Experience: %d years,
                            Qualification Type: %s, University: %s,
                            Year Completed: %s, CGPA: %.2f
                        """.formatted(applicantId, applicantName, applicantExperience, 
                                      qualificationType, universityName, 
                                      yearCompleted.toString(), cgpa);

                        applicantDetails.add(qualificationEntry); // Add to the list
                    }
                }
            }

            // Fetch recommendations
            try (PreparedStatement stmt = conn.prepareStatement(recommendationQuery)) {
                stmt.setInt(1, applicantId);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String recommendationDescription = rs.getString("recommendation_description");

                        // Format the recommendation entry
                        String recommendationEntry = """
                            Recommendation: %s
                        """.formatted(recommendationDescription);

                        applicantDetails.add(recommendationEntry); // Add to the list
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return applicantDetails; // Return the populated list
    }


    public ObservableList<String> getRecruiterQualifications(int recruiterId) {
        String query = """
            SELECT r.recruiter_id,q.qual_type, 
                   q.university_school_name, q.year_completed, q.CGPA
            FROM recruiter r
            JOIN recruiter_contains_qualification rq ON r.recruiter_id = rq.recruiter_id
            JOIN qualification q ON rq.quali_id = q.qual_id
            WHERE r.recruiter_id = ?
        """;

        ObservableList<String> recruiterQualifications = FXCollections.observableArrayList();

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the parameter for the recruiter ID
            stmt.setInt(1, recruiterId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
              

                    // Extract qualification details
                    String qualificationType = rs.getString("qual_type");
                    String universityName = rs.getString("university_school_name");
                    Date yearCompleted = rs.getDate("year_completed");
                    float cgpa = rs.getFloat("CGPA");

                    // Format the entry
                    String entry = """
                        Recruiter ID: %d, Qualification Type: %s, University: %s,
                        Year Completed: %s, CGPA: %.2f
                    """.formatted(recruiterId,qualificationType, universityName, 
                                  yearCompleted.toString(), cgpa);

                    recruiterQualifications.add(entry); // Add to the list
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recruiterQualifications; // Return the populated list
    }
    public int insertTestInfo(int jobId, int recruiterId, int noOfQuestions, float minimumScoreRequired) {
        String query = "INSERT INTO test (job_id, recruiter_id, no_of_questions, minimum_score_required) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // Set the values for the prepared statement
            stmt.setInt(1, jobId);
            stmt.setInt(2, recruiterId);
            stmt.setInt(3, noOfQuestions);
            stmt.setFloat(4, minimumScoreRequired);

            // Execute the query
            stmt.executeUpdate();

            // Retrieve and return the generated test_id
            var rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the generated test_id
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL errors
        }
        return -1; // Return -1 if the insertion fails
    }
    public int getNumberOfQuestionsByJobId(int jobId) {
        String query = "SELECT no_of_questions FROM test WHERE job_id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, jobId); // Set the jobId parameter
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("no_of_questions"); // Return the number of questions
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log SQL errors
        }
        return -1; // Return -1 if the query fails
    }

    public float getMinimumScoreRequiredByJobId(int jobId) {
        String query = "SELECT minimum_score_required FROM test WHERE job_id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, jobId); // Set the jobId parameter
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("minimum_score_required"); // Return the minimum score required
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log SQL errors
        }
        return -1.0f; // Return -1.0f if the query fails
    }
    public boolean doesTestExist(int jobId) {
        String query = "SELECT COUNT(*) FROM test WHERE job_id = ?";
      
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, jobId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // If count > 0, test exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default to no test existing
    }
    public boolean isApplicantAlreadyShortlisted(int applicantId, int jobId) {
        String query = "SELECT COUNT(*) FROM shortlist_after_interview WHERE shortlistafterinterview_applicant_id = ? AND job_id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, applicantId);
            stmt.setInt(2, jobId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1); // Get the count of matching rows
                    return count > 0; // Return true if count is greater than 0
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if an error occurs or no match is found
    }



  

}
