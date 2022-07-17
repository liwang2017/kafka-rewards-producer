package com.lilywang.rewardsproducer.producer

import com.lilywang.rewardsproducer.dto.Transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random

@Component
class TransactionProducer(
    private val transactionKafkaTemplate: KafkaTemplate<String, Transaction>,
) {


    val logger: Logger = LoggerFactory.getLogger(this.javaClass)


    fun publishTransaction(){
        while(true){
            for(accountId in listOf("1", "2", "3")){
                generateTransaction(accountId).let{ transaction ->
                    logger.info("Send transaction $transaction to kafka")
                    transactionKafkaTemplate.send("expenses",transaction.accountId,  transaction)
                }
            }
            Thread.sleep(3000)
        }
    }

    fun generateTransaction(accountId: String) =
        Transaction(
            accountId = accountId,
            transactionId = UUID.randomUUID().toString(),
            date = LocalDateTime.now(),
            amount = Random.nextInt(0,1000).toBigDecimal().movePointLeft(2)
        )

}