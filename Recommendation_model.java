package Model_classes;

public class Recommendation_model {
    private int recommendationId;
    private int recommendationHrId;
    private int recommendationApplicantId;
    private String recommendationDescription;

    // Default Constructor
    public Recommendation_model() {}

    // Parameterized Constructor
    public Recommendation_model(int recommendationId, int recommendationHrId, int recommendationApplicantId, String recommendationDescription) {
        this.recommendationId = recommendationId;
        this.recommendationHrId = recommendationHrId;
        this.recommendationApplicantId = recommendationApplicantId;
        this.recommendationDescription = recommendationDescription;
    }

    // Getters and Setters
    public int getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(int recommendationId) {
        this.recommendationId = recommendationId;
    }

    public int getRecommendationHrId() {
        return recommendationHrId;
    }

    public void setRecommendationHrId(int recommendationHrId) {
        this.recommendationHrId = recommendationHrId;
    }

    public int getRecommendationApplicantId() {
        return recommendationApplicantId;
    }

    public void setRecommendationApplicantId(int recommendationApplicantId) {
        this.recommendationApplicantId = recommendationApplicantId;
    }

    public String getRecommendationDescription() {
        return recommendationDescription;
    }

    public void setRecommendationDescription(String recommendationDescription) {
        this.recommendationDescription = recommendationDescription;
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "Recommendation {" +
                "recommendationId=" + recommendationId +
                ", recommendationHrId=" + recommendationHrId +
                ", recommendationApplicantId=" + recommendationApplicantId +
                ", recommendationDescription='" + recommendationDescription + '\'' +
                '}';
    }
}
