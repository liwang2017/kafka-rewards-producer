package com.lilywang.rewardsproducer.config

import com.lilywang.rewardsproducer.dto.Transaction
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class TransactionSerializer(): Serializer<Transaction> {

    fun objectMapper(): ObjectMapper {
        val module = JavaTimeModule()
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        module.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(dateFormat))
        module.addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(dateFormat))
        return Jackson2ObjectMapperBuilder.json()
            .modules(
                module,
                Jdk8Module(),
                ParameterNamesModule()
            )
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build()
    }
    override fun serialize(topic: String, transaction: Transaction): ByteArray
            = objectMapper().writeValueAsString(transaction).toByteArray(Charset.forName("UTF-8"))

}

class TransactionDeserializer(val objectMapper: ObjectMapper): Deserializer<Transaction> {

    override fun deserialize(topic: String, data: ByteArray): Transaction
            = objectMapper.readValue(String(data, Charset.forName("UTF-8")), Transaction::class.java)
}


