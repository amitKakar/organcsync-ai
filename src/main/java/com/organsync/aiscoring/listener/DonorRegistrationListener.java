package com.organsync.aiscoring.listener;

import com.organsync.aiscoring.dto.ScoringRequestDto;
import com.organsync.aiscoring.service.AiScoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Kafka event listener for real-time donor registration processing
 * Automatically triggers compatibility scoring when new pairs are registered
 */
@Component
public class DonorRegistrationListener {

    private static final Logger logger = LoggerFactory.getLogger(DonorRegistrationListener.class);

    private final AiScoringService aiScoringService;

    public DonorRegistrationListener(AiScoringService aiScoringService) {
        this.aiScoringService = aiScoringService;
    }

    /**
     * Process donor registration events
     */
    @KafkaListener(topics = "donor.registered", groupId = "ai-scoring-service")
    public void handleDonorRegistration(@Payload Map<String, Object> event,
                                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                                       @Header(KafkaHeaders.OFFSET) long offset) {

        logger.info("Received donor registration event from topic: {}, partition: {}, offset: {}", 
                   topic, partition, offset);

        try {
            // Extract donor pair information
            String donorPairIdStr = (String) event.get("donor_pair_id");
            String donorBloodType = (String) event.get("donor_blood_type");
            String recipientBloodType = (String) event.get("recipient_blood_type");

            if (donorPairIdStr == null || donorBloodType == null || recipientBloodType == null) {
                logger.warn("Missing required fields in donor registration event: {}", event);
                return;
            }

            UUID donorPairId = UUID.fromString(donorPairIdStr);

            // Extract additional information if available
            Integer donorAge = (Integer) event.get("donor_age");
            Integer recipientAge = (Integer) event.get("recipient_age");
            String donorLocation = (String) event.get("donor_location");
            String recipientLocation = (String) event.get("recipient_location");

            logger.info("Processing donor registration for pair: {}, donor blood type: {}, recipient blood type: {}", 
                       donorPairId, donorBloodType, recipientBloodType);

            // For now, we'll create a basic scoring request
            // In a real implementation, this would trigger scoring against all compatible recipients
            ScoringRequestDto scoringRequest = createScoringRequest(event);

            if (scoringRequest != null) {
                // Calculate compatibility score
                aiScoringService.calculateCompatibilityScore(scoringRequest);

                logger.info("Successfully processed donor registration event for pair: {}", donorPairId);
            } else {
                logger.warn("Could not create scoring request from event: {}", event);
            }

        } catch (Exception e) {
            logger.error("Error processing donor registration event: {}", event, e);
        }
    }

    /**
     * Process donor update events
     */
    @KafkaListener(topics = "donor.updated", groupId = "ai-scoring-service")
    public void handleDonorUpdate(@Payload Map<String, Object> event,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        logger.info("Received donor update event from topic: {}", topic);

        try {
            String donorPairIdStr = (String) event.get("donor_pair_id");
            if (donorPairIdStr == null) {
                logger.warn("Missing donor_pair_id in donor update event: {}", event);
                return;
            }

            UUID donorPairId = UUID.fromString(donorPairIdStr);

            logger.info("Processing donor update for pair: {}", donorPairId);

            // In a real implementation, this would trigger recalculation of all scores
            // involving this donor pair

            logger.info("Successfully processed donor update event for pair: {}", donorPairId);

        } catch (Exception e) {
            logger.error("Error processing donor update event: {}", event, e);
        }
    }

    /**
     * Process graph update events
     */
    @KafkaListener(topics = "graph.updated", groupId = "ai-scoring-service")
    public void handleGraphUpdate(@Payload Map<String, Object> event,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        logger.info("Received graph update event from topic: {}", topic);

        try {
            String revisionId = (String) event.get("revision_id");
            Integer totalPairs = (Integer) event.get("total_pairs");
            Integer totalEdges = (Integer) event.get("total_edges");

            logger.info("Processing graph update: revision {}, pairs: {}, edges: {}", 
                       revisionId, totalPairs, totalEdges);

            // In a real implementation, this would trigger batch scoring updates
            // for all pairs in the updated graph

            logger.info("Successfully processed graph update event: revision {}", revisionId);

        } catch (Exception e) {
            logger.error("Error processing graph update event: {}", event, e);
        }
    }

    /**
     * Create scoring request from event data
     */
    private ScoringRequestDto createScoringRequest(Map<String, Object> event) {
        try {
            ScoringRequestDto request = new ScoringRequestDto();

            // Basic required fields
            String donorPairIdStr = (String) event.get("donor_pair_id");
            String recipientPairIdStr = (String) event.get("recipient_pair_id");

            if (donorPairIdStr != null) {
                request.setDonorPairId(UUID.fromString(donorPairIdStr));
            }

            if (recipientPairIdStr != null) {
                request.setRecipientPairId(UUID.fromString(recipientPairIdStr));
            } else {
                // Generate a placeholder recipient pair ID for demonstration
                request.setRecipientPairId(UUID.randomUUID());
            }

            // Donor information
            request.setDonorBloodType((String) event.get("donor_blood_type"));
            request.setDonorAge((Integer) event.get("donor_age"));
            request.setDonorGender((String) event.get("donor_gender"));
            request.setDonorLocation((String) event.get("donor_location"));

            // Recipient information
            request.setRecipientBloodType((String) event.get("recipient_blood_type"));
            request.setRecipientAge((Integer) event.get("recipient_age"));
            request.setRecipientGender((String) event.get("recipient_gender"));
            request.setRecipientLocation((String) event.get("recipient_location"));

            // Clinical information
            request.setHlaMismatches((Integer) event.get("hla_mismatches"));
            request.setPreviousTransplant((Boolean) event.get("previous_transplant"));
            request.setTimeOnDialysis((Integer) event.get("time_on_dialysis"));
            request.setUrgencyLevel((String) event.get("urgency_level"));

            // Set default calculation method
            request.setCalculationMethod("HYBRID");

            return request;

        } catch (Exception e) {
            logger.error("Error creating scoring request from event", e);
            return null;
        }
    }

    /**
     * Handle event processing errors
     */
    @KafkaListener(topics = "donor.registered.DLT", groupId = "ai-scoring-service")
    public void handleDeadLetterTopic(@Payload Map<String, Object> event,
                                     @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        logger.error("Received event in dead letter topic: {}", topic);
        logger.error("Failed event: {}", event);

        // In a real implementation, this would trigger alerting and manual intervention
        // or retry logic with exponential backoff
    }
}