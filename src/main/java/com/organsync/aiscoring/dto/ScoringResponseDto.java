package com.organsync.aiscoring.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Response DTO for compatibility scoring results
 * Contains comprehensive AI analysis results and predictions
 */
public class ScoringResponseDto {

    private UUID donorPairId;
    private UUID recipientPairId;
    private String scoreId;

    // Overall Scoring Results
    private Double overallScore;
    private Double confidenceLevel;
    private String riskAssessment;
    private String recommendation;

    // Cox Regression Results
    private Double coxSurvivalProbability;
    private Double hazardRatio;
    private Map<String, Double> survivalProbabilities; // 1yr, 3yr, 5yr, 10yr

    // MCDA Results
    private Double mcdaScore;
    private Map<String, Double> criteriaScores;
    private Map<String, Double> criteriaWeights;

    // Component Scores
    private Double bloodTypeScore;
    private Double hlaCompatibilityScore;
    private Double ageCompatibilityScore;
    private Double geographicScore;
    private Double medicalHistoryScore;
    private Double urgencyScore;
    private Double crossmatchScore;

    // Detailed Analysis
    private String bloodTypeCompatibility;
    private Integer hlaMismatches;
    private String hlaCompatibilityLevel;
    private Double ageDiscrepancy;
    private Double geographicDistance;
    private String medicalRiskFactors;
    private String urgencyLevel;

    // Prediction Features
    private Map<String, Object> predictionFeatures;
    private Map<String, Double> featureImportance;

    // Metadata
    private String algorithmVersion;
    private String calculationMethod;
    private LocalDateTime calculatedAt;
    private String calculatedBy;
    private Long processingTimeMs;

    // Additional Information
    private String notes;
    private Map<String, Object> additionalInfo;

    // Constructors
    public ScoringResponseDto() {
        this.calculatedAt = LocalDateTime.now();
        this.algorithmVersion = "1.0.0";
    }

    public ScoringResponseDto(UUID donorPairId, UUID recipientPairId) {
        this();
        this.donorPairId = donorPairId;
        this.recipientPairId = recipientPairId;
    }

    // Getters and Setters
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

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public Double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }

    public Double getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(Double confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public String getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(String riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public Double getCoxSurvivalProbability() {
        return coxSurvivalProbability;
    }

    public void setCoxSurvivalProbability(Double coxSurvivalProbability) {
        this.coxSurvivalProbability = coxSurvivalProbability;
    }

    public Double getHazardRatio() {
        return hazardRatio;
    }

    public void setHazardRatio(Double hazardRatio) {
        this.hazardRatio = hazardRatio;
    }

    public Map<String, Double> getSurvivalProbabilities() {
        return survivalProbabilities;
    }

    public void setSurvivalProbabilities(Map<String, Double> survivalProbabilities) {
        this.survivalProbabilities = survivalProbabilities;
    }

    public Double getMcdaScore() {
        return mcdaScore;
    }

    public void setMcdaScore(Double mcdaScore) {
        this.mcdaScore = mcdaScore;
    }

    public Map<String, Double> getCriteriaScores() {
        return criteriaScores;
    }

    public void setCriteriaScores(Map<String, Double> criteriaScores) {
        this.criteriaScores = criteriaScores;
    }

    public Map<String, Double> getCriteriaWeights() {
        return criteriaWeights;
    }

    public void setCriteriaWeights(Map<String, Double> criteriaWeights) {
        this.criteriaWeights = criteriaWeights;
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

    public Double getCrossmatchScore() {
        return crossmatchScore;
    }

    public void setCrossmatchScore(Double crossmatchScore) {
        this.crossmatchScore = crossmatchScore;
    }

    public String getBloodTypeCompatibility() {
        return bloodTypeCompatibility;
    }

    public void setBloodTypeCompatibility(String bloodTypeCompatibility) {
        this.bloodTypeCompatibility = bloodTypeCompatibility;
    }

    public Integer getHlaMismatches() {
        return hlaMismatches;
    }

    public void setHlaMismatches(Integer hlaMismatches) {
        this.hlaMismatches = hlaMismatches;
    }

    public String getHlaCompatibilityLevel() {
        return hlaCompatibilityLevel;
    }

    public void setHlaCompatibilityLevel(String hlaCompatibilityLevel) {
        this.hlaCompatibilityLevel = hlaCompatibilityLevel;
    }

    public Double getAgeDiscrepancy() {
        return ageDiscrepancy;
    }

    public void setAgeDiscrepancy(Double ageDiscrepancy) {
        this.ageDiscrepancy = ageDiscrepancy;
    }

    public Double getGeographicDistance() {
        return geographicDistance;
    }

    public void setGeographicDistance(Double geographicDistance) {
        this.geographicDistance = geographicDistance;
    }

    public String getMedicalRiskFactors() {
        return medicalRiskFactors;
    }

    public void setMedicalRiskFactors(String medicalRiskFactors) {
        this.medicalRiskFactors = medicalRiskFactors;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public Map<String, Object> getPredictionFeatures() {
        return predictionFeatures;
    }

    public void setPredictionFeatures(Map<String, Object> predictionFeatures) {
        this.predictionFeatures = predictionFeatures;
    }

    public Map<String, Double> getFeatureImportance() {
        return featureImportance;
    }

    public void setFeatureImportance(Map<String, Double> featureImportance) {
        this.featureImportance = featureImportance;
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

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(LocalDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }

    public String getCalculatedBy() {
        return calculatedBy;
    }

    public void setCalculatedBy(String calculatedBy) {
        this.calculatedBy = calculatedBy;
    }

    public Long getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(Long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public String toString() {
        return "ScoringResponseDto{" +
                "donorPairId=" + donorPairId +
                ", recipientPairId=" + recipientPairId +
                ", overallScore=" + overallScore +
                ", confidenceLevel=" + confidenceLevel +
                ", riskAssessment='" + riskAssessment + "'" +
                ", coxSurvivalProbability=" + coxSurvivalProbability +
                ", mcdaScore=" + mcdaScore +
                ", calculationMethod='" + calculationMethod + "'" +
                ", calculatedAt=" + calculatedAt +
                '}';
    }
}