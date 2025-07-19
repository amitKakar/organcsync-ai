package com.organsync.aiscoring.controller;

import com.organsync.aiscoring.dto.ScoringRequestDto;
import com.organsync.aiscoring.dto.ScoringResponseDto;
import com.organsync.aiscoring.service.AiScoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * REST Controller for AI-powered compatibility scoring
 * Provides comprehensive endpoints for kidney exchange compatibility analysis
 */
@RestController
@RequestMapping("/api/v1/scoring")
@Validated
@Tag(name = "AI Scoring", description = "AI-powered compatibility scoring for kidney exchange")
public class AiScoringController {

    private static final Logger logger = LoggerFactory.getLogger(AiScoringController.class);

    private final AiScoringService aiScoringService;

    public AiScoringController(AiScoringService aiScoringService) {
        this.aiScoringService = aiScoringService;
    }

    /**
     * Calculate compatibility score for a donor-recipient pair
     */
    @PostMapping("/calculate")
    @Operation(summary = "Calculate compatibility score", 
               description = "Calculate comprehensive compatibility score using Cox regression and MCDA")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Score calculated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ScoringResponseDto> calculateScore(
            @Valid @RequestBody ScoringRequestDto request) {

        logger.info("Received scoring request for donor {} and recipient {}", 
                   request.getDonorPairId(), request.getRecipientPairId());

        try {
            ScoringResponseDto response = aiScoringService.calculateCompatibilityScore(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error calculating compatibility score", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Calculate compatibility scores for multiple pairs (batch processing)
     */
    @PostMapping("/calculate-batch")
    @Operation(summary = "Calculate batch compatibility scores", 
               description = "Calculate compatibility scores for multiple donor-recipient pairs")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Batch scores calculated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ScoringResponseDto>> calculateBatchScores(
            @Valid @RequestBody List<ScoringRequestDto> requests) {

        logger.info("Received batch scoring request for {} pairs", requests.size());

        try {
            List<ScoringResponseDto> responses = aiScoringService.calculateBatchScores(requests);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error calculating batch compatibility scores", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get cached compatibility score
     */
    @GetMapping("/cached/{donorPairId}/{recipientPairId}")
    @Operation(summary = "Get cached compatibility score", 
               description = "Retrieve previously calculated compatibility score from cache")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cached score retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Score not found in cache"),
        @ApiResponse(responseCode = "400", description = "Invalid pair IDs")
    })
    public ResponseEntity<ScoringResponseDto> getCachedScore(
            @Parameter(description = "Donor pair ID") @PathVariable UUID donorPairId,
            @Parameter(description = "Recipient pair ID") @PathVariable UUID recipientPairId) {

        logger.info("Retrieving cached score for donor {} and recipient {}", donorPairId, recipientPairId);

        try {
            Optional<ScoringResponseDto> cachedScore = aiScoringService.getCachedScore(donorPairId, recipientPairId);

            if (cachedScore.isPresent()) {
                return ResponseEntity.ok(cachedScore.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving cached score", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all scores for a donor pair
     */
    @GetMapping("/donor/{donorPairId}")
    @Operation(summary = "Get scores by donor pair", 
               description = "Retrieve all compatibility scores for a specific donor pair")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Scores retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No scores found for donor pair"),
        @ApiResponse(responseCode = "400", description = "Invalid donor pair ID")
    })
    public ResponseEntity<List<ScoringResponseDto>> getScoresByDonorPair(
            @Parameter(description = "Donor pair ID") @PathVariable UUID donorPairId) {

        logger.info("Retrieving scores for donor pair {}", donorPairId);

        try {
            List<ScoringResponseDto> scores = aiScoringService.getScoresByDonorPair(donorPairId);
            return ResponseEntity.ok(scores);
        } catch (Exception e) {
            logger.error("Error retrieving scores for donor pair", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all scores for a recipient pair
     */
    @GetMapping("/recipient/{recipientPairId}")
    @Operation(summary = "Get scores by recipient pair", 
               description = "Retrieve all compatibility scores for a specific recipient pair")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Scores retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No scores found for recipient pair"),
        @ApiResponse(responseCode = "400", description = "Invalid recipient pair ID")
    })
    public ResponseEntity<List<ScoringResponseDto>> getScoresByRecipientPair(
            @Parameter(description = "Recipient pair ID") @PathVariable UUID recipientPairId) {

        logger.info("Retrieving scores for recipient pair {}", recipientPairId);

        try {
            List<ScoringResponseDto> scores = aiScoringService.getScoresByRecipientPair(recipientPairId);
            return ResponseEntity.ok(scores);
        } catch (Exception e) {
            logger.error("Error retrieving scores for recipient pair", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get scoring statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get scoring statistics", 
               description = "Retrieve system-wide scoring statistics and metrics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getStatistics() {

        logger.info("Retrieving scoring statistics");

        try {
            Map<String, Object> statistics = aiScoringService.getScoringStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Error retrieving scoring statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get model performance metrics
     */
    @GetMapping("/model-performance")
    @Operation(summary = "Get model performance metrics", 
               description = "Retrieve AI model performance metrics and accuracy statistics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Model performance retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getModelPerformance() {

        logger.info("Retrieving model performance metrics");

        try {
            Map<String, Object> performance = aiScoringService.getModelPerformance();
            return ResponseEntity.ok(performance);
        } catch (Exception e) {
            logger.error("Error retrieving model performance", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", 
               description = "Check the health status of the AI scoring service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is healthy"),
        @ApiResponse(responseCode = "503", description = "Service is unhealthy")
    })
    public ResponseEntity<Map<String, Object>> healthCheck() {

        logger.debug("Health check requested");

        try {
            Map<String, Object> health = Map.of(
                "status", "UP",
                "service", "AI Scoring Service",
                "version", "1.0.0",
                "timestamp", System.currentTimeMillis()
            );

            return ResponseEntity.ok(health);
        } catch (Exception e) {
            logger.error("Health check failed", e);

            Map<String, Object> health = Map.of(
                "status", "DOWN",
                "service", "AI Scoring Service",
                "error", e.getMessage(),
                "timestamp", System.currentTimeMillis()
            );

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(health);
        }
    }

    /**
     * Get service information
     */
    @GetMapping("/info")
    @Operation(summary = "Get service information", 
               description = "Retrieve information about the AI scoring service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service information retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getServiceInfo() {

        logger.debug("Service information requested");

        Map<String, Object> info = Map.of(
            "service_name", "OrganSync AI Scoring Service",
            "version", "1.0.0",
            "description", "AI-powered compatibility scoring for kidney exchange matching",
            "algorithms", List.of("Cox Proportional Hazards Regression", "Multi-Criteria Decision Analysis"),
            "supported_methods", List.of("COX", "MCDA", "HYBRID"),
            "features", List.of(
                "Survival probability prediction",
                "Multi-criteria compatibility scoring",
                "Batch processing support",
                "Real-time event processing",
                "Caching for performance optimization"
            ),
            "endpoints", List.of(
                "POST /api/v1/scoring/calculate",
                "POST /api/v1/scoring/calculate-batch",
                "GET /api/v1/scoring/cached/{donorPairId}/{recipientPairId}",
                "GET /api/v1/scoring/donor/{donorPairId}",
                "GET /api/v1/scoring/recipient/{recipientPairId}",
                "GET /api/v1/scoring/statistics",
                "GET /api/v1/scoring/model-performance",
                "GET /api/v1/scoring/health",
                "GET /api/v1/scoring/info"
            )
        );

        return ResponseEntity.ok(info);
    }
}