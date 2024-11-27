package logic_layer;
public abstract class UserService {
    protected int userId; // Common user ID for all services
    protected String email;
    protected String password;

    // Constructor
    public UserService() {}

    // Abstract method to validate user credentials, returning the user ID if valid
    public abstract int validateUser(String email, String password);
    // Abstract method for user-specific registration logic
    public abstract boolean register(String name, String email, String password, int age, String gender, int experience);
    // Common method to validate email format
    protected boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }
    protected boolean isValidPassword(String password) {
        return password.length() >= 8; // Adjust this rule as needed
    }
    
}
