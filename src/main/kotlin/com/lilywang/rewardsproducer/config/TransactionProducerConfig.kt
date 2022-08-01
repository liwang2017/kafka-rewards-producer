package com.lilywang.rewardsproducer.config

import com.lilywang.rewardsproducer.dto.Transaction
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import java.util.*
import kotlin.collections.HashMap


@Configuration
class TransactionProducerConfig {

    /**
     * Create default KafkaFactory Bean with predefined configuration from properties file
     */
    @Bean
    fun transactionProducerFactory(
        producerConfig: Map<String, Any>
    ): ProducerFactory<String, Transaction> = DefaultKafkaProducerFactory(producerConfig)

    @Bean
    fun producerConfigs(kafkaConfig: Properties): Map<String, Any> {
        val props:MutableMap<String, Any> = HashMap()
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, TransactionSerializer::class.java)
        props.putAll(kafkaConfig.toMap() as Map<String, String>)
        return props
    }

    @Bean
    fun transactionKafkaTemplate(
        transactionProducerFactory: ProducerFactory<String, Transaction>
    ): KafkaTemplate<String, Transaction> = KafkaTemplate<String, Transaction>(transactionProducerFactory)

    // Load properties from resource with prefix kafka
    @Bean
    @ConfigurationProperties(prefix = "kafka")
    fun kafkaConfig() = Properties()



}