package Model_classes;

public class User_model {
    protected int userId;
    protected String name;
    protected String email;
    protected String password;  // Added password
    protected int experience;   // Added experience
    protected int age;
    protected String gender;

    // Constructor
    public User_model(int userId, String name, String email, String password, int experience, int age, String gender) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;  // Initialize password
        this.experience = experience;  // Initialize experience
        this.age = age;
        this.gender = gender;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;  // Getter for password
    }

    public void setPassword(String password) {
        this.password = password;  // Setter for password
    }

    public int getExperience() {
        return experience;  // Getter for experience
    }

    public void setExperience(int experience) {
        this.experience = experience;  // Setter for experience
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Common method to display user information (can be overridden by subclasses)
    public void displayUserInfo() {
        System.out.println("User ID: " + userId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);  // Display password
        System.out.println("Experience: " + experience + " years");  // Display experience
        System.out.println("Age: " + age);
        System.out.println("Gender: " + gender);
    }
}
