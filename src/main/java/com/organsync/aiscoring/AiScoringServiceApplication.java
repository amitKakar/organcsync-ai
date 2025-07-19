package com.organsync.aiscoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * OrganSync AI Scoring Service - Advanced Machine Learning for Kidney Exchange
 * 
 * Features:
 * - Cox Proportional Hazards Regression for survival prediction
 * - Multi-Criteria Decision Analysis (MCDA) for compatibility scoring
 * - Real-time event processing with Kafka
 * - MongoDB for ML model storage
 * - Redis for performance caching
 * - Healthcare-grade security and compliance
 */
@SpringBootApplication
@EnableKafka
public class AiScoringServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiScoringServiceApplication.class, args);
    }
}