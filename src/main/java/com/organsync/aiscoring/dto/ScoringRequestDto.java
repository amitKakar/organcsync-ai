package com.organsync.aiscoring.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

import java.util.Map;
import java.util.UUID;

/**
 * Request DTO for compatibility scoring calculations
 * Contains all necessary donor and recipient information for AI analysis
 */
public class ScoringRequestDto {

    @NotNull(message = "Donor pair ID is required")
    private UUID donorPairId;

    @NotNull(message = "Recipient pair ID is required")
    private UUID recipientPairId;

    // Donor Information
    @NotNull(message = "Donor blood type is required")
    private String donorBloodType;

    @NotNull(message = "Donor age is required")
    @Min(value = 18, message = "Donor age must be at least 18")
    @Max(value = 80, message = "Donor age must be at most 80")
    private Integer donorAge;

    private String donorHlaType;
    private String donorGender;
    private Double donorBmi;
    private String donorMedicalHistory;
    private String donorLocation;
    private Double donorLatitude;
    private Double donorLongitude;

    // Recipient Information
    @NotNull(message = "Recipient blood type is required")
    private String recipientBloodType;

    @NotNull(message = "Recipient age is required")
    @Min(value = 1, message = "Recipient age must be at least 1")
    @Max(value = 80, message = "Recipient age must be at most 80")
    private Integer recipientAge;

    private String recipientHlaType;
    private String recipientGender;
    private Double recipientBmi;
    private String recipientMedicalHistory;
    private String recipientLocation;
    private Double recipientLatitude;
    private Double recipientLongitude;

    // Clinical Information
    private Integer hlaMismatches;
    private Boolean previousTransplant;
    private Integer timeOnDialysis;
    private String urgencyLevel;
    private Double crossmatchResult;
    private String panelReactiveAntibodies;

    // Calculation Parameters
    private String calculationMethod = "HYBRID"; // COX, MCDA, HYBRID
    private Boolean includeGeographic = true;
    private Boolean includeMedicalHistory = true;
    private Boolean includeUrgency = true;

    // Custom weights for MCDA
    private Map<String, Double> customWeights;

    // Additional features for ML models
    private Map<String, Object> additionalFeatures;

    // Constructors
    public ScoringRequestDto() {}

    public ScoringRequestDto(UUID donorPairId, UUID recipientPairId, 
                           String donorBloodType, Integer donorAge,
                           String recipientBloodType, Integer recipientAge) {
        this.donorPairId = donorPairId;
        this.recipientPairId = recipientPairId;
        this.donorBloodType = donorBloodType;
        this.donorAge = donorAge;
        this.recipientBloodType = recipientBloodType;
        this.recipientAge = recipientAge;
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

    public String getDonorBloodType() {
        return donorBloodType;
    }

    public void setDonorBloodType(String donorBloodType) {
        this.donorBloodType = donorBloodType;
    }

    public Integer getDonorAge() {
        return donorAge;
    }

    public void setDonorAge(Integer donorAge) {
        this.donorAge = donorAge;
    }

    public String getDonorHlaType() {
        return donorHlaType;
    }

    public void setDonorHlaType(String donorHlaType) {
        this.donorHlaType = donorHlaType;
    }

    public String getDonorGender() {
        return donorGender;
    }

    public void setDonorGender(String donorGender) {
        this.donorGender = donorGender;
    }

    public Double getDonorBmi() {
        return donorBmi;
    }

    public void setDonorBmi(Double donorBmi) {
        this.donorBmi = donorBmi;
    }

    public String getDonorMedicalHistory() {
        return donorMedicalHistory;
    }

    public void setDonorMedicalHistory(String donorMedicalHistory) {
        this.donorMedicalHistory = donorMedicalHistory;
    }

    public String getDonorLocation() {
        return donorLocation;
    }

    public void setDonorLocation(String donorLocation) {
        this.donorLocation = donorLocation;
    }

    public Double getDonorLatitude() {
        return donorLatitude;
    }

    public void setDonorLatitude(Double donorLatitude) {
        this.donorLatitude = donorLatitude;
    }

    public Double getDonorLongitude() {
        return donorLongitude;
    }

    public void setDonorLongitude(Double donorLongitude) {
        this.donorLongitude = donorLongitude;
    }

    public String getRecipientBloodType() {
        return recipientBloodType;
    }

    public void setRecipientBloodType(String recipientBloodType) {
        this.recipientBloodType = recipientBloodType;
    }

    public Integer getRecipientAge() {
        return recipientAge;
    }

    public void setRecipientAge(Integer recipientAge) {
        this.recipientAge = recipientAge;
    }

    public String getRecipientHlaType() {
        return recipientHlaType;
    }

    public void setRecipientHlaType(String recipientHlaType) {
        this.recipientHlaType = recipientHlaType;
    }

    public String getRecipientGender() {
        return recipientGender;
    }

    public void setRecipientGender(String recipientGender) {
        this.recipientGender = recipientGender;
    }

    public Double getRecipientBmi() {
        return recipientBmi;
    }

    public void setRecipientBmi(Double recipientBmi) {
        this.recipientBmi = recipientBmi;
    }

    public String getRecipientMedicalHistory() {
        return recipientMedicalHistory;
    }

    public void setRecipientMedicalHistory(String recipientMedicalHistory) {
        this.recipientMedicalHistory = recipientMedicalHistory;
    }

    public String getRecipientLocation() {
        return recipientLocation;
    }

    public void setRecipientLocation(String recipientLocation) {
        this.recipientLocation = recipientLocation;
    }

    public Double getRecipientLatitude() {
        return recipientLatitude;
    }

    public void setRecipientLatitude(Double recipientLatitude) {
        this.recipientLatitude = recipientLatitude;
    }

    public Double getRecipientLongitude() {
        return recipientLongitude;
    }

    public void setRecipientLongitude(Double recipientLongitude) {
        this.recipientLongitude = recipientLongitude;
    }

    public Integer getHlaMismatches() {
        return hlaMismatches;
    }

    public void setHlaMismatches(Integer hlaMismatches) {
        this.hlaMismatches = hlaMismatches;
    }

    public Boolean getPreviousTransplant() {
        return previousTransplant;
    }

    public void setPreviousTransplant(Boolean previousTransplant) {
        this.previousTransplant = previousTransplant;
    }

    public Integer getTimeOnDialysis() {
        return timeOnDialysis;
    }

    public void setTimeOnDialysis(Integer timeOnDialysis) {
        this.timeOnDialysis = timeOnDialysis;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public Double getCrossmatchResult() {
        return crossmatchResult;
    }

    public void setCrossmatchResult(Double crossmatchResult) {
        this.crossmatchResult = crossmatchResult;
    }

    public String getPanelReactiveAntibodies() {
        return panelReactiveAntibodies;
    }

    public void setPanelReactiveAntibodies(String panelReactiveAntibodies) {
        this.panelReactiveAntibodies = panelReactiveAntibodies;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    public Boolean getIncludeGeographic() {
        return includeGeographic;
    }

    public void setIncludeGeographic(Boolean includeGeographic) {
        this.includeGeographic = includeGeographic;
    }

    public Boolean getIncludeMedicalHistory() {
        return includeMedicalHistory;
    }

    public void setIncludeMedicalHistory(Boolean includeMedicalHistory) {
        this.includeMedicalHistory = includeMedicalHistory;
    }

    public Boolean getIncludeUrgency() {
        return includeUrgency;
    }

    public void setIncludeUrgency(Boolean includeUrgency) {
        this.includeUrgency = includeUrgency;
    }

    public Map<String, Double> getCustomWeights() {
        return customWeights;
    }

    public void setCustomWeights(Map<String, Double> customWeights) {
        this.customWeights = customWeights;
    }

    public Map<String, Object> getAdditionalFeatures() {
        return additionalFeatures;
    }

    public void setAdditionalFeatures(Map<String, Object> additionalFeatures) {
        this.additionalFeatures = additionalFeatures;
    }

    @Override
    public String toString() {
        return "ScoringRequestDto{" +
                "donorPairId=" + donorPairId +
                ", recipientPairId=" + recipientPairId +
                ", donorBloodType='" + donorBloodType + "'" +
                ", donorAge=" + donorAge +
                ", recipientBloodType='" + recipientBloodType + "'" +
                ", recipientAge=" + recipientAge +
                ", calculationMethod='" + calculationMethod + "'" +
                '}';
    }
}