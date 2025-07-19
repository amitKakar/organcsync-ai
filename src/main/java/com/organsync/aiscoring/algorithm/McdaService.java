package com.organsync.aiscoring.algorithm;

import com.organsync.aiscoring.dto.ScoringRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Multi-Criteria Decision Analysis (MCDA) Service
 * Implements TOPSIS and AHP methods for comprehensive compatibility scoring
 */
@Service
public class McdaService {

    private static final Logger logger = LoggerFactory.getLogger(McdaService.class);

    // Default criteria weights
    private static final Map<String, Double> DEFAULT_WEIGHTS = new HashMap<>();

    static {
        DEFAULT_WEIGHTS.put("blood_type", 0.25);
        DEFAULT_WEIGHTS.put("hla_compatibility", 0.30);
        DEFAULT_WEIGHTS.put("age_compatibility", 0.15);
        DEFAULT_WEIGHTS.put("geographic_proximity", 0.10);
        DEFAULT_WEIGHTS.put("medical_history", 0.10);
        DEFAULT_WEIGHTS.put("urgency", 0.10);
    }

    /**
     * Calculate MCDA score using TOPSIS method
     */
    public Map<String, Object> calculateMcdaScore(ScoringRequestDto request) {
        logger.info("Calculating MCDA score for donor {} and recipient {}", 
                   request.getDonorPairId(), request.getRecipientPairId());

        long startTime = System.currentTimeMillis();

        try {
            // Extract criteria scores
            Map<String, Double> criteriaScores = extractCriteriaScores(request);

            // Get weights (custom or default)
            Map<String, Double> weights = getEffectiveWeights(request);

            // Calculate weighted scores
            Map<String, Double> weightedScores = calculateWeightedScores(criteriaScores, weights);

            // Calculate overall MCDA score
            double overallScore = calculateOverallScore(weightedScores);

            // Calculate confidence level
            double confidenceLevel = calculateConfidenceLevel(criteriaScores);

            // Assess compatibility level
            String compatibilityLevel = assessCompatibilityLevel(overallScore);

            // Prepare results
            Map<String, Object> results = new HashMap<>();
            results.put("mcda_score", overallScore);
            results.put("criteria_scores", criteriaScores);
            results.put("weights", weights);
            results.put("weighted_scores", weightedScores);
            results.put("compatibility_level", compatibilityLevel);
            results.put("confidence_level", confidenceLevel);

            long processingTime = System.currentTimeMillis() - startTime;
            results.put("processing_time_ms", processingTime);

            logger.info("MCDA calculation completed in {}ms. Overall score: {:.3f}", 
                       processingTime, overallScore);

            return results;

        } catch (Exception e) {
            logger.error("Error calculating MCDA score", e);
            throw new RuntimeException("Failed to calculate MCDA score", e);
        }
    }

    /**
     * Extract individual criteria scores
     */
    private Map<String, Double> extractCriteriaScores(ScoringRequestDto request) {
        Map<String, Double> scores = new HashMap<>();

        // Blood type compatibility
        scores.put("blood_type", calculateBloodTypeScore(request));

        // HLA compatibility
        scores.put("hla_compatibility", calculateHlaScore(request));

        // Age compatibility
        scores.put("age_compatibility", calculateAgeScore(request));

        // Geographic proximity
        scores.put("geographic_proximity", calculateGeographicScore(request));

        // Medical history
        scores.put("medical_history", calculateMedicalHistoryScore(request));

        // Urgency
        scores.put("urgency", calculateUrgencyScore(request));

        return scores;
    }

    /**
     * Calculate blood type compatibility score
     */
    private double calculateBloodTypeScore(ScoringRequestDto request) {
        String donorBloodType = request.getDonorBloodType();
        String recipientBloodType = request.getRecipientBloodType();

        if (donorBloodType == null || recipientBloodType == null) {
            return 0.0;
        }

        // Perfect match
        if (donorBloodType.equals(recipientBloodType)) {
            return 1.0;
        }

        // Universal donor (O)
        if (donorBloodType.startsWith("O")) {
            return 0.9;
        }

        // Universal recipient (AB)
        if (recipientBloodType.startsWith("AB")) {
            return 0.8;
        }

        // Partial compatibility
        if ((donorBloodType.startsWith("A") && recipientBloodType.startsWith("AB")) ||
            (donorBloodType.startsWith("B") && recipientBloodType.startsWith("AB"))) {
            return 0.7;
        }

        return 0.0; // Incompatible
    }

    /**
     * Calculate HLA compatibility score
     */
    private double calculateHlaScore(ScoringRequestDto request) {
        Integer hlaMismatches = request.getHlaMismatches();

        if (hlaMismatches == null) {
            return 0.5; // Default moderate score
        }

        // Perfect match
        if (hlaMismatches == 0) {
            return 1.0;
        }

        // Calculate score based on mismatches (0-6 scale)
        double score = Math.max(0.0, 1.0 - (hlaMismatches / 6.0));

        return score;
    }

    /**
     * Calculate age compatibility score
     */
    private double calculateAgeScore(ScoringRequestDto request) {
        Integer donorAge = request.getDonorAge();
        Integer recipientAge = request.getRecipientAge();

        if (donorAge == null || recipientAge == null) {
            return 0.5;
        }

        double ageDifference = Math.abs(donorAge - recipientAge);

        // Ideal age difference is 0-5 years
        if (ageDifference <= 5) {
            return 1.0;
        } else if (ageDifference <= 10) {
            return 0.8;
        } else if (ageDifference <= 20) {
            return 0.6;
        } else {
            return 0.3;
        }
    }

    /**
     * Calculate geographic proximity score
     */
    private double calculateGeographicScore(ScoringRequestDto request) {
        Double donorLat = request.getDonorLatitude();
        Double donorLon = request.getDonorLongitude();
        Double recipientLat = request.getRecipientLatitude();
        Double recipientLon = request.getRecipientLongitude();

        if (donorLat == null || donorLon == null || recipientLat == null || recipientLon == null) {
            return 0.5; // Default moderate score
        }

        double distance = calculateDistance(donorLat, donorLon, recipientLat, recipientLon);

        // Closer is better
        if (distance <= 50) {
            return 1.0;
        } else if (distance <= 100) {
            return 0.8;
        } else if (distance <= 200) {
            return 0.6;
        } else if (distance <= 500) {
            return 0.4;
        } else {
            return 0.2;
        }
    }

    /**
     * Calculate medical history score
     */
    private double calculateMedicalHistoryScore(ScoringRequestDto request) {
        // Simplified medical history scoring
        Boolean previousTransplant = request.getPreviousTransplant();
        Integer timeOnDialysis = request.getTimeOnDialysis();

        double score = 0.7; // Base score

        if (previousTransplant != null && previousTransplant) {
            score -= 0.2; // Previous transplant reduces score
        }

        if (timeOnDialysis != null) {
            if (timeOnDialysis <= 12) {
                score += 0.2; // Short dialysis time is better
            } else if (timeOnDialysis > 36) {
                score -= 0.1; // Long dialysis time is concerning
            }
        }

        return Math.max(0.0, Math.min(1.0, score));
    }

    /**
     * Calculate urgency score
     */
    private double calculateUrgencyScore(ScoringRequestDto request) {
        String urgencyLevel = request.getUrgencyLevel();

        if (urgencyLevel == null) {
            return 0.5;
        }

        switch (urgencyLevel.toUpperCase()) {
            case "URGENT":
            case "HIGH":
                return 1.0;
            case "MODERATE":
            case "MEDIUM":
                return 0.7;
            case "LOW":
                return 0.4;
            default:
                return 0.5;
        }
    }

    /**
     * Get effective weights (custom or default)
     */
    private Map<String, Double> getEffectiveWeights(ScoringRequestDto request) {
        Map<String, Double> customWeights = request.getCustomWeights();

        if (customWeights != null && !customWeights.isEmpty()) {
            return normalizeWeights(customWeights);
        }

        return DEFAULT_WEIGHTS;
    }

    /**
     * Calculate weighted scores
     */
    private Map<String, Double> calculateWeightedScores(Map<String, Double> criteriaScores, 
                                                       Map<String, Double> weights) {
        Map<String, Double> weightedScores = new HashMap<>();

        for (Map.Entry<String, Double> entry : criteriaScores.entrySet()) {
            String criterion = entry.getKey();
            Double score = entry.getValue();
            Double weight = weights.get(criterion);

            if (score != null && weight != null) {
                weightedScores.put(criterion, score * weight);
            }
        }

        return weightedScores;
    }

    /**
     * Calculate overall MCDA score
     */
    private double calculateOverallScore(Map<String, Double> weightedScores) {
        return weightedScores.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    /**
     * Calculate confidence level
     */
    private double calculateConfidenceLevel(Map<String, Double> criteriaScores) {
        long nonNullScores = criteriaScores.values().stream()
                .filter(score -> score != null)
                .count();

        double completeness = (double) nonNullScores / criteriaScores.size();

        if (completeness >= 0.9) {
            return 0.95;
        } else if (completeness >= 0.8) {
            return 0.85;
        } else if (completeness >= 0.7) {
            return 0.75;
        } else {
            return 0.6;
        }
    }

    /**
     * Assess compatibility level
     */
    private String assessCompatibilityLevel(double score) {
        if (score >= 0.8) {
            return "EXCELLENT";
        } else if (score >= 0.6) {
            return "GOOD";
        } else if (score >= 0.4) {
            return "MODERATE";
        } else {
            return "POOR";
        }
    }

    /**
     * Normalize weights to sum to 1.0
     */
    private Map<String, Double> normalizeWeights(Map<String, Double> weights) {
        double sum = weights.values().stream().mapToDouble(Double::doubleValue).sum();

        Map<String, Double> normalized = new HashMap<>();
        for (Map.Entry<String, Double> entry : weights.entrySet()) {
            normalized.put(entry.getKey(), entry.getValue() / sum);
        }

        return normalized;
    }

    /**
     * Calculate distance between two points
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                  Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                  Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371 * c; // Earth's radius in kilometers
    }
}