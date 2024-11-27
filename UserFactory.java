package Model_classes;

public class UserFactory {
    public User_model createUser(String userType, int userId, String name, String email, String password,
                                 int experience, int age, String gender) {
        switch (userType) {
            case "Recruiter":
                return new Recruiter_model(userId, name, email, password, experience, age, gender);
            case "Applicant":
                return new Applicant_model(userId, name, email, password, experience, age, gender);
            case "HR":
                return new HR_model(userId, name, email, password, experience, age, gender);
            default:
                throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }
}

