package Model_classes;

public class Message_model {
    private int senderId;
    private int receiverId;
    private String messageContent;
    private String messageStatus;  // Can be "Sent" or "Received"

    // Constructor
    public Message_model(int senderId, int receiverId, String messageContent, String messageStatus) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageContent = messageContent;
        this.messageStatus = messageStatus;
    }

    // Getters and Setters
    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "Message from " + senderId + " to " + receiverId + ": " + messageContent + " [Status: " + messageStatus + "]";
    }
}
