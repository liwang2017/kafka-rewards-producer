package com.lilywang.rewardsproducer.config

import com.lilywang.rewardsproducer.dto.Transaction
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory


@Configuration
class TransactionProducerConfig {

    @Bean
    fun transactionProducerFactory(
        producerConfig: Map<String, Any>
    ): ProducerFactory<String, Transaction> = DefaultKafkaProducerFactory(producerConfig)

    @Bean
    fun producerConfigs(): Map<String, Any> {
        val props:MutableMap<String, Any> = HashMap()
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, TransactionSerializer::class.java)
        return props
    }

    @Bean
    fun transactionKafkaTemplate(
        transactionProducerFactory: ProducerFactory<String, Transaction>
    ): KafkaTemplate<String, Transaction> = KafkaTemplate<String, Transaction>(transactionProducerFactory)


}