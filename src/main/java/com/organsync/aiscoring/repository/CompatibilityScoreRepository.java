package com.organsync.aiscoring.repository;

import com.organsync.aiscoring.entity.CompatibilityScore;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * MongoDB repository for CompatibilityScore entities
 * Provides advanced querying capabilities for AI scoring operations
 */
@Repository
public interface CompatibilityScoreRepository extends MongoRepository<CompatibilityScore, String> {

    /**
     * Find compatibility score by donor and recipient pair IDs
     */
    Optional<CompatibilityScore> findByDonorPairIdAndRecipientPairId(UUID donorPairId, UUID recipientPairId);

    /**
     * Find all scores for a specific donor pair
     */
    List<CompatibilityScore> findByDonorPairId(UUID donorPairId);

    /**
     * Find all scores for a specific recipient pair
     */
    List<CompatibilityScore> findByRecipientPairId(UUID recipientPairId);

    /**
     * Find scores by overall score range
     */
    List<CompatibilityScore> findByOverallScoreBetween(Double minScore, Double maxScore);

    /**
     * Find scores by Cox survival probability threshold
     */
    List<CompatibilityScore> findByCoxSurvivalProbabilityGreaterThan(Double threshold);

    /**
     * Find scores by MCDA score range
     */
    List<CompatibilityScore> findByMcdaScoreBetween(Double minScore, Double maxScore);

    /**
     * Find scores by risk assessment category
     */
    List<CompatibilityScore> findByRiskAssessment(String riskAssessment);

    /**
     * Find scores by calculation method
     */
    List<CompatibilityScore> findByCalculationMethod(String calculationMethod);

    /**
     * Find scores by algorithm version
     */
    List<CompatibilityScore> findByAlgorithmVersion(String algorithmVersion);

    /**
     * Find scores created after a specific date
     */
    List<CompatibilityScore> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find scores created between two dates
     */
    List<CompatibilityScore> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find scores by confidence level threshold
     */
    List<CompatibilityScore> findByConfidenceLevelGreaterThan(Double threshold);

    /**
     * Find high-quality scores (high confidence and good overall score)
     */
    @Query("{ 'confidenceLevel': { $gte: ?0 }, 'overallScore': { $gte: ?1 } }")
    List<CompatibilityScore> findHighQualityScores(Double minConfidence, Double minOverallScore);

    /**
     * Find top N scores by overall score
     */
    List<CompatibilityScore> findTop10ByOrderByOverallScoreDesc();

    /**
     * Find scores with specific blood type compatibility
     */
    List<CompatibilityScore> findByBloodTypeScoreGreaterThan(Double threshold);

    /**
     * Find scores with good HLA compatibility
     */
    List<CompatibilityScore> findByHlaCompatibilityScoreGreaterThan(Double threshold);

    /**
     * Find scores by geographic proximity
     */
    List<CompatibilityScore> findByGeographicScoreGreaterThan(Double threshold);

    /**
     * Find scores by urgency level
     */
    List<CompatibilityScore> findByUrgencyScoreGreaterThan(Double threshold);

    /**
     * Count scores by calculation method
     */
    long countByCalculationMethod(String calculationMethod);

    /**
     * Count scores by risk assessment
     */
    long countByRiskAssessment(String riskAssessment);

    /**
     * Find scores that need recalculation (old algorithm versions)
     */
    @Query("{ 'algorithmVersion': { $ne: ?0 } }")
    List<CompatibilityScore> findScoresNeedingRecalculation(String currentVersion);

    /**
     * Find recently calculated scores for monitoring
     */
    @Query("{ 'createdAt': { $gte: ?0 } }")
    List<CompatibilityScore> findRecentScores(LocalDateTime sinceDate);

    /**
     * Find scores by donor pair with sorting
     */
    List<CompatibilityScore> findByDonorPairIdOrderByOverallScoreDesc(UUID donorPairId);

    /**
     * Find scores by recipient pair with sorting
     */
    List<CompatibilityScore> findByRecipientPairIdOrderByOverallScoreDesc(UUID recipientPairId);

    /**
     * Check if score exists for a pair combination
     */
    boolean existsByDonorPairIdAndRecipientPairId(UUID donorPairId, UUID recipientPairId);

    /**
     * Delete old scores (for cleanup)
     */
    void deleteByCreatedAtBefore(LocalDateTime date);

    /**
     * Custom aggregation query for statistics
     */
    @Query("{ $group: { _id: '$calculationMethod', count: { $sum: 1 }, avgScore: { $avg: '$overallScore' } } }")
    List<Object> getStatisticsByCalculationMethod();
}