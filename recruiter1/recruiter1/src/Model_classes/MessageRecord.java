package Model_classes;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MessageRecord {
    private final SimpleIntegerProperty applicantId;
    private final SimpleStringProperty message;

    public MessageRecord(int applicantId, String message) {
        this.applicantId = new SimpleIntegerProperty(applicantId);
        this.message = new SimpleStringProperty(message);
    }

    public int getApplicantId() {
        return applicantId.get();
    }

    public void setApplicantId(int applicantId) {
        this.applicantId.set(applicantId);
    }

    public String getMessage() {
        return message.get();
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    // You can create getter methods for properties if needed for JavaFX bindings
    public SimpleIntegerProperty applicantIdProperty() {
        return applicantId;
    }

    public SimpleStringProperty messageProperty() {
        return message;
    }
}
