package com.organsync.aiscoring.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * MongoDB entity for storing compatibility scores and AI predictions
 * Supports both Cox regression and MCDA scoring methodologies
 */
@Document(collection = "compatibility_scores")
public class CompatibilityScore {

    @Id
    private String id;

    @Indexed
    @Field("donor_pair_id")
    private UUID donorPairId;

    @Indexed
    @Field("recipient_pair_id")
    private UUID recipientPairId;

    @Field("overall_score")
    private Double overallScore;

    @Field("cox_survival_probability")
    private Double coxSurvivalProbability;

    @Field("mcda_score")
    private Double mcdaScore;

    @Field("blood_type_score")
    private Double bloodTypeScore;

    @Field("hla_compatibility_score")
    private Double hlaCompatibilityScore;

    @Field("age_compatibility_score")
    private Double ageCompatibilityScore;

    @Field("geographic_score")
    private Double geographicScore;

    @Field("medical_history_score")
    private Double medicalHistoryScore;

    @Field("urgency_score")
    private Double urgencyScore;

    @Field("confidence_level")
    private Double confidenceLevel;

    @Field("prediction_features")
    private Map<String, Object> predictionFeatures;

    @Field("algorithm_version")
    private String algorithmVersion;

    @Field("calculation_method")
    private String calculationMethod;

    @Field("risk_assessment")
    private String riskAssessment;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("calculated_by")
    private String calculatedBy;

    // Constructors
    public CompatibilityScore() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.algorithmVersion = "1.0.0";
    }

    public CompatibilityScore(UUID donorPairId, UUID recipientPairId) {
        this();
        this.donorPairId = donorPairId;
        this.recipientPairId = recipientPairId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID getDonorPairId() {
        return donorPairId;
    }

    public void setDonorPairId(UUID donorPairId) {
        this.donorPairId = donorPairId;
    }

    public UUID getRecipientPairId() {
        return recipientPairId;
    }

    public void setRecipientPairId(UUID recipientPairId) {
        this.recipientPairId = recipientPairId;
    }

    public Double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }

    public Double getCoxSurvivalProbability() {
        return coxSurvivalProbability;
    }

    public void setCoxSurvivalProbability(Double coxSurvivalProbability) {
        this.coxSurvivalProbability = coxSurvivalProbability;
    }

    public Double getMcdaScore() {
        return mcdaScore;
    }

    public void setMcdaScore(Double mcdaScore) {
        this.mcdaScore = mcdaScore;
    }

    public Double getBloodTypeScore() {
        return bloodTypeScore;
    }

    public void setBloodTypeScore(Double bloodTypeScore) {
        this.bloodTypeScore = bloodTypeScore;
    }

    public Double getHlaCompatibilityScore() {
        return hlaCompatibilityScore;
    }

    public void setHlaCompatibilityScore(Double hlaCompatibilityScore) {
        this.hlaCompatibilityScore = hlaCompatibilityScore;
    }

    public Double getAgeCompatibilityScore() {
        return ageCompatibilityScore;
    }

    public void setAgeCompatibilityScore(Double ageCompatibilityScore) {
        this.ageCompatibilityScore = ageCompatibilityScore;
    }

    public Double getGeographicScore() {
        return geographicScore;
    }

    public void setGeographicScore(Double geographicScore) {
        this.geographicScore = geographicScore;
    }

    public Double getMedicalHistoryScore() {
        return medicalHistoryScore;
    }

    public void setMedicalHistoryScore(Double medicalHistoryScore) {
        this.medicalHistoryScore = medicalHistoryScore;
    }

    public Double getUrgencyScore() {
        return urgencyScore;
    }

    public void setUrgencyScore(Double urgencyScore) {
        this.urgencyScore = urgencyScore;
    }

    public Double getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(Double confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public Map<String, Object> getPredictionFeatures() {
        return predictionFeatures;
    }

    public void setPredictionFeatures(Map<String, Object> predictionFeatures) {
        this.predictionFeatures = predictionFeatures;
    }

    public String getAlgorithmVersion() {
        return algorithmVersion;
    }

    public void setAlgorithmVersion(String algorithmVersion) {
        this.algorithmVersion = algorithmVersion;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    public String getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(String riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCalculatedBy() {
        return calculatedBy;
    }

    public void setCalculatedBy(String calculatedBy) {
        this.calculatedBy = calculatedBy;
    }

    @Override
    public String toString() {
        return "CompatibilityScore{" +
                "id='" + id + "'" +
                ", donorPairId=" + donorPairId +
                ", recipientPairId=" + recipientPairId +
                ", overallScore=" + overallScore +
                ", coxSurvivalProbability=" + coxSurvivalProbability +
                ", mcdaScore=" + mcdaScore +
                ", confidenceLevel=" + confidenceLevel +
                ", calculationMethod='" + calculationMethod + "'" +
                ", createdAt=" + createdAt +
                '}';
    }
}