package com.lilywang.rewardsproducer.producer

import com.example.kafkaproducer.csv.TransactionRecord
import com.lilywang.rewardsproducer.dto.Transaction
import com.opencsv.bean.CsvToBeanBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.io.FileReader
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Component
class TransactionProducer(
    private val transactionKafkaTemplate: KafkaTemplate<String, Transaction>,
) {


    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    //Bind CSV File location from properties
    @Value("\${transaction.file.location}")
    private lateinit var fileLocation: String

    //Bind topic name from properties
    @Value("\${kafka.produce.topic}")
    private lateinit var topicName: String

    /**
     * Read Transaction from CSV and publish to Kafka topic expense
     */
    fun publishTransaction(){
        readTransactions().forEach {transaction ->
            logger.info("Send transaction $transaction to kafka")
            transactionKafkaTemplate.send(topicName,transaction.accountId,  transaction)
        }
    }

    /**
     * Read Transactions from CSV resource
     */
    private fun readTransactions(): List<Transaction> =
        CsvToBeanBuilder<TransactionRecord>(FileReader(fileLocation))
            .withType(TransactionRecord::class.java)
            .build()
            .parse()
            .map{ csvRecord-> csvRecord.toTransaction() }


    /**
     * Helper method to convert CSV record to internal Transaction DTO
     */
    private fun TransactionRecord.toTransaction() =
        this.let{
            logger.info("Convert transaction record: transactionId = ${this.transactionId}, date=${this.date}, accountId = ${this.accountId}, amount=${this.amount}")
            Transaction(
                transactionId = this.transactionId.trim(),
                date = LocalDateTime.parse(this.date.trim(),  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                accountId = this.accountId.trim(),
                amount = BigDecimal(this.amount.trim()),
            )
        }


}