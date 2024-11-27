package database_logic;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class HrDao {

	private static final String URL = "jdbc:sqlserver://DESKTOP-9LTVH1T\\SQLEXPRESS;databaseName=EHMS;integratedSecurity=true;trustServerCertificate=true";
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private Connection connect() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    } 
    public int validateLogin(String email, String password) {
        String query = "SELECT hr_id FROM hr WHERE hr_email = ? AND hr_password = ?";
        
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            // Check if a result is found
            if (rs.next()) {
                return rs.getInt("hr_id"); // Return the HR ID
            } else {
                return -1; // Return -1 if no matching record is found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 in case of an exception
        }
    }


    public int insertQualification(String qualType, String universitySchoolName, LocalDate yearCompleted, float cgpa,String field) {
        // Print the values being inserted for debugging purposes
        System.out.println("Inserting qualification with the following details:");
        System.out.println("Qualification Type: " + qualType);
        System.out.println("University/School Name: " + universitySchoolName);
        System.out.println("Year Completed: " + yearCompleted);
        System.out.println("CGPA: " + cgpa);
        System.out.println("CGPA: " + field);
        String query = "INSERT INTO qualification (qual_type, university_school_name, year_completed, CGPA, field) VALUES (?, ?, ?, ?,?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, qualType);
            stmt.setString(2, universitySchoolName);
            stmt.setDate(3, Date.valueOf(yearCompleted));
            stmt.setFloat(4, cgpa);
            stmt.setString(5, field);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the generated ID
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if insertion fails
    }
    
    

    
    public boolean insertHRQualification(int hrId, int qualId) {
        String query = "INSERT INTO hr_contains_qualification (hr_id, quali_id) VALUES (?, ?)";
        
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set parameters for prepared statement
            stmt.setInt(1, hrId); // HR ID
            stmt.setInt(2, qualId); // Qualification ID
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if the insert was successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public int inserthrinfo(String name, String email, String pass, int age, String gender, int experience) {
        // Check if the email already exists

        // Proceed with insertion
        String query = "INSERT INTO hr (hr_name, hr_email, hr_password, hr_age, hr_gender, hr_experience) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, pass);
            stmt.setInt(4, age);
            stmt.setString(5, gender);
            stmt.setInt(6, experience);

            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the generated ID
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if insertion fails
    }

 
    

    public Integer fetchHrIdByEmail(String email) {
        String query = "SELECT hr_id FROM hr WHERE hr_email = ?";  // Query to fetch HR ID based on the email
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);  // Set the email parameter

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("hr_id");  // Return HR ID if found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no HR ID is found or on error
    }

    
    public List<Integer> fetchApplicantIds() {
        List<Integer> applicantIds = new ArrayList<>();
        String query = "SELECT distinct shortlist_after_hrapplicant_id FROM shortlist_after_hr ";  // Adjust table/column names as needed

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                applicantIds.add(rs.getInt("shortlist_after_hrapplicant_id"));  // Add each applicant ID to the list
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return applicantIds;  // Return the list of applicant IDs
    }
    public boolean isContractExistsForJob(int jobId) {
        String query = "SELECT COUNT(*) FROM hr_contract WHERE job_id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, jobId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Returns true if count > 0
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default to false if an exception occurs
    }
    public boolean insertShortlistedApplicant(int applicantId, int hrId, int jobId) throws SQLException {
        // Check if the applicant has already been shortlisted for this HR and job
        String checkQuery = "SELECT COUNT(*) FROM shortlist_after_hr WHERE shortlist_after_hrapplicant_id = ? AND shortlist_after_hr_id = ? AND job_id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(checkQuery)) {
            stmt.setInt(1, applicantId);
            stmt.setInt(2, hrId);
            stmt.setInt(3, jobId);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false;  // The applicant is already shortlisted for this HR and job
            }
        }
        // Insert into shortlist_after_hr
        String insertQuery = "INSERT INTO shortlist_after_hr (shortlist_after_hrapplicant_id, shortlist_after_hr_id, job_id) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setInt(1, applicantId);
            stmt.setInt(2, hrId);
            stmt.setInt(3, jobId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Return true if the insert was successful
        }
        
    }
    public void updateJobStatus(int jobId) throws SQLException {
        String updateQuery = "UPDATE job SET job_status = 0 WHERE jobid = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setInt(1, jobId);  // Set the job ID parameter
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Job status updated to 0 successfully.");
            } else {
                System.out.println("No job found with the specified job ID.");
            }
        }
    }

    public List<Integer> fetchRecruiterIds() {
        List<Integer> recruiterIds = new ArrayList<>();
        String query = "SELECT distinct recruiter_id FROM recruiter";  // Adjust table/column names if needed

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                recruiterIds.add(rs.getInt("recruiter_id"));  // Add each recruiter ID to the list
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recruiterIds;  // Return the list of recruiter IDs
    }
    public List<Integer> fetchApplicantIdsForJob() {
        List<Integer> applicantIds = new ArrayList<>();
        String query = "SELECT DISTINCT job_id FROM shortlist_after_interview";  // Fetch distinct job_ids

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                applicantIds.add(rs.getInt("job_id"));  // Add job_id to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return applicantIds;  // Return list of job_ids
    }
    public List<Integer> fetchAllApplicantIds(int job_id) {
        List<Integer> applicantIds = new ArrayList<>();
        // Modify the query to filter by jobId
        String query = "SELECT shortlistafterinterview_applicant_id FROM shortlist_after_interview WHERE job_id = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the jobId parameter in the prepared statement
            stmt.setInt(1, job_id);

            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Add applicant_id to the list
                    applicantIds.add(rs.getInt("shortlistafterinterview_applicant_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return applicantIds;  // Return list of applicant_ids
    }


    public List<Integer> fetchHrIds() {
        List<Integer> hrIds = new ArrayList<>();
        String query = "SELECT distinct hr_id FROM hr";  // Adjust table/column names if needed

        try (Connection conn = connect(); 
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                hrIds.add(rs.getInt("hr_id"));  // Add each HR ID to the list
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hrIds;  // Return the list of HR IDs
    }

    public List<Integer> fetchJobIds() {
        List<Integer> jobIds = new ArrayList<>();
        String query = "SELECT distinct jobid FROM job";  // Replace "job_id" with the actual column name if needed

        try (Connection conn = connect();  // Assuming connect() is a method that establishes your DB connection
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                jobIds.add(rs.getInt("jobid"));  // Add each job ID to the list
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobIds;  // Return the list of job IDs
    }
    




    public boolean insertRecommendation(int hrId, int applicantId, String description) {
        // SQL query to check if a recommendation already exists for the given HR and applicant
        String checkQuery = "SELECT COUNT(*) FROM recommendation WHERE recommendationhr_id = ? AND recommendationapplicant_id = ?";
        
        // SQL query to insert a recommendation if it does not already exist
        String insertQuery = "INSERT INTO recommendation (recommendationhr_id, recommendationapplicant_id, recommendation_description) "
                + "VALUES (?, ?, ?)";
       
        try (Connection conn = connect(); 
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            // Set the parameters for checking existing recommendations
            checkStmt.setInt(1, hrId);  
            checkStmt.setInt(2, applicantId);  

            // Execute the check query
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // If a recommendation already exists, show an alert using JavaFX Alert
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Recommendation Alert");
                    alert.setHeaderText("Recommendation already given");
                    alert.setContentText("This applicant has already received a recommendation from this HR.");
                    alert.showAndWait();
                    return false;  // Return false if a recommendation already exists
                }
            }

            // If no recommendation exists, proceed with the insertion
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setInt(1, hrId);  // Set HR ID
                stmt.setInt(2, applicantId);  // Set Applicant ID
                stmt.setString(3, description);  // Set the description

                // Execute the insert statement
                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0;  // Return true if the recommendation was successfully inserted
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Log the error
            return false;  // Return false if any exception occurs
        }
    }
    public int insertMessageToApplicant(int hrId, int applicantId, String message, int jobId) {
        // Define the SQL query to insert data, including job_id
        String query = "INSERT INTO send_message_hr_and_applicant (hr_id, send_message_hrapplicant_id, hr_and_applicantmessage) " +
                       "VALUES (?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Set the values in the PreparedStatement
            stmt.setInt(1, hrId);  // HR ID
            stmt.setInt(2, applicantId);  // Applicant ID
            stmt.setString(3, message);  // Message content
           

            // Execute the query and get the number of affected rows
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    // Check if there are any generated keys and return the generated ID
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);  // Return the generated ID of the inserted message
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;  // Return -1 if insertion fails
    }




    public int insertHrContractInfo(int jobId, int hrId, String salary, int probationPeriod, 
	            LocalDate startDate, LocalDate endDate, String benefits) {
	// Define the SQL query to insert data
	String query = "INSERT INTO hr_contract (job_id, hr_id, salary, probation_period, hr_contractstart_date, " +
	"hr_contractend_date, benefits) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	stmt.setInt(1, jobId);
	stmt.setInt(2, hrId);
	stmt.setString(3, salary);
	stmt.setInt(4, probationPeriod);
	  // Convert LocalDate to java.sql.Date and set it in the prepared statement
    stmt.setDate(5, Date.valueOf(startDate)); // Convert LocalDate to SQL Date
    stmt.setDate(6, Date.valueOf(endDate));
	stmt.setString(7, benefits);
	
	int rowsAffected = stmt.executeUpdate();
	if (rowsAffected > 0) {
	try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	if (generatedKeys.next()) {
	return generatedKeys.getInt(1);  // Return the generated ID of the inserted contract
	}
	}
	}
	
	} catch (SQLException e) {
	e.printStackTrace();
	}
	return -1;  // Return -1 if insertion fails
	}
    
    
    
    public List<String> getJobDetailsFromId(int jobId) {
        // SQL query updated to match your table structure
        String query = "SELECT job_title, job_description, company_name, qualification_skills_required, cgpa_required, " +
                       "job_type, job_experience, application_deadline " +
                       "FROM job WHERE jobid = ?";
        List<String> jobDetails = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, jobId); // Set the jobId in the query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Add details to the list with appropriate labels
                    jobDetails.add("Job Title: " + rs.getString("job_title"));
                    jobDetails.add("Description: " + rs.getString("job_description"));
                    jobDetails.add("Company Name: " + rs.getString("company_name"));
                    jobDetails.add("Skills Required: " + rs.getString("qualification_skills_required"));
                    jobDetails.add("CGPA Required: " + rs.getFloat("cgpa_required"));
                    jobDetails.add("Job Type: " + rs.getString("job_type"));
                    jobDetails.add("Experience Required: " + rs.getInt("job_experience") + " years");
                    jobDetails.add("Application Deadline: " + rs.getDate("application_deadline").toString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for debugging
        }
        return jobDetails;
    }



    
    public List<String> receivemsghrapplicant(int hrId, String message) {
        List<String> messages = new ArrayList<>();

        // Define the SQL query to insert data into the applicant_receive_msg_from_hr table
        String insertQuery = "INSERT INTO  recieve_message_hr_and_applicant (receivemsg_hr_id, message) " +
                             "VALUES (?, ?)";

        // Define the SQL query to fetch all messages for the given hrId
        String selectQuery = "SELECT Distinct message FROM  recieve_message_hr_and_applicant WHERE receivemsg_hr_id = ? ORDER BY message_date DESC";

        try (Connection conn = connect(); 
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {

            // Insert the new message into the database
            insertStmt.setInt(1, hrId);          // HR ID
            insertStmt.setString(2, message);    // Message content
            int rowsAffected = insertStmt.executeUpdate();

            // If the message was inserted successfully, fetch all the messages for the HR
            if (rowsAffected > 0) {
                // Set parameters for the select statement
                selectStmt.setInt(1, hrId);

                // Execute the select query to fetch the messages
                try (ResultSet rs = selectStmt.executeQuery()) {
                    while (rs.next()) {
                        messages.add(rs.getString("message")); // Add each message to the list
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages; // Return the list of messages
    }

    public List<String> getMessagesForApplicant(int applicantId, int hrId) {
        List<String> messages = new ArrayList<>();

        // SQL query to fetch messages for a given applicantId and hrId
        String query = "SELECT hr_and_applicantmessage FROM recieve_message_hr_and_applicant WHERE recieve_message_hrapplicant_id = ? AND hr_id = ?";

        // Establish connection and fetch data
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, applicantId);  // Set the applicantId
            stmt.setInt(2, hrId);          // Set the hrId

            try (ResultSet rs = stmt.executeQuery()) {
                // Add all the messages to the list
                while (rs.next()) {
                    messages.add(rs.getString("hr_and_applicantmessage"));
                }
            }
        } catch (SQLException e) {
            // Log the exception or rethrow as a runtime exception
            System.err.println("Error fetching messages: " + e.getMessage());
        }

        return messages; // Return the list of messages
    }

    public boolean validateadminLogin(String adminemail, String adminpassword) {
        String query = "SELECT * FROM admin WHERE admin_email = ? AND admin_password = ?";
        
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, adminemail);
            stmt.setString(2, adminpassword);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public List<String> fetchRecruiterInfo(Integer recruiterId) {
        List<String> recruiterInfo = new ArrayList<>();
        String query = "SELECT * FROM recruiter WHERE recruiter_id = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, recruiterId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                  
                    recruiterInfo.add("Name: " + rs.getString("recruiter_name"));
                    recruiterInfo.add("Email: " + rs.getString("recruiter_email"));
                    recruiterInfo.add("Experience: " + rs.getString("recruiter_experience"));
                   
                   
                    // Add other details as needed
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recruiterInfo;
    }
    
    public List<String> fetchHrInfo(Integer hrId) {
        List<String> hrInfo = new ArrayList<>();
        String query = "SELECT * FROM hr WHERE hr_id = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, hrId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    hrInfo.add("Name: " + rs.getString("hr_name"));
                    hrInfo.add("Email: " + rs.getString("hr_email"));
                    hrInfo.add("Age: " + rs.getInt("hr_age"));
                    hrInfo.add("Gender: " + rs.getString("hr_gender"));
                    hrInfo.add("Experience: " + rs.getInt("hr_experience"));
                    
                    // Add other details as needed
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hrInfo;
    }
    
    public int getTotalJobsPosted(Integer recruiterId) {
        int totalJobs = 0;
        String query = "SELECT COUNT(*) FROM job WHERE recruiter_id = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, recruiterId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalJobs = rs.getInt(1);  // Get the count from the query result
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalJobs;
    }

	
    public List<String> getRecommendationCounts(int hrId) {
        // Initialize counts for each recommendation type
        int highlyRecommendedCount = 0;
        int recommendedCount = 0;
        int neutralCount = 0;

        // SQL query to fetch the count of applicants based on recommendation description for a specific HR ID
        String query = "SELECT recommendation_description, COUNT(recommendationapplicant_id) AS count " +
                       "FROM recommendation " +
                       "WHERE recommendationhr_id = ? " +
                       "GROUP BY recommendation_description";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, hrId);  // Set the HR ID parameter

            try (ResultSet rs = stmt.executeQuery()) {
                // Process the results and update the counts based on the recommendation description
                while (rs.next()) {
                    String description = rs.getString("recommendation_description");
                    int count = rs.getInt("count");

                    // Update counts based on the recommendation description
                    if ("Highly Recommended".equals(description)) {
                        highlyRecommendedCount = count;
                    } else if ("Recommended".equals(description)) {
                        recommendedCount = count;
                    } else if ("Neutral".equals(description)) {
                        neutralCount = count;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Prepare the result list with counts for each recommendation type
        List<String> recommendationCounts = new ArrayList<>();
        recommendationCounts.add("Highly Recommended: " + highlyRecommendedCount);
        recommendationCounts.add("Recommended: " + recommendedCount);
        recommendationCounts.add("Neutral: " + neutralCount);

        return recommendationCounts;
    }
    public List<String> getSortedApplicants(String sortBy, String sortOrder) {
        String orderByClause = sortOrder.equals("Ascending") ? "ASC" : "DESC";  // Determine order direction
        String query = "";

        // Construct the query based on the sorting option
        if ("Experience".equals(sortBy)) {
            // Sorting by applicant experience
            query = "SELECT a.applicant_id, a.applicant_name, a.applicant_experience " +
                    "FROM applicant a " +
                    "ORDER BY a.applicant_experience " + orderByClause;
        } else if ("CGPA".equals(sortBy)) {
            // Sorting by CGPA (from the qualification table)
            query = "SELECT a.applicant_id, a.applicant_name, q.cgpa " +
                    "FROM applicant a " +
                    "JOIN applicant_contains_qualification acq ON a.applicant_id = acq.applicant_id " +
                    "JOIN qualification q ON acq.quali_id = q.qual_id " +
                    "ORDER BY q.cgpa " + orderByClause;
        }

        List<String> sortedApplicants = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int applicantId = rs.getInt("applicant_id");  // Get applicant ID
                    String applicantName = rs.getString("applicant_name");  // Get applicant name
                    String cgpa = null;

                    // Get CGPA if it's available (for "CGPA" sorting)
                    if ("CGPA".equals(sortBy)) {
                        cgpa = rs.getString("CGPA");  // Get CGPA from qualification table
                    }

                    // Construct the result string
                    String applicantInfo = "ID: " + applicantId + ", Name: " + applicantName;
                    if (cgpa != null) {
                        applicantInfo += ", CGPA: " + cgpa;
                    } else {
                        applicantInfo += ", Experience: " + rs.getInt("applicant_experience");
                    }

                    // Add the applicant info to the list
                    sortedApplicants.add(applicantInfo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sortedApplicants;
    }


    
}