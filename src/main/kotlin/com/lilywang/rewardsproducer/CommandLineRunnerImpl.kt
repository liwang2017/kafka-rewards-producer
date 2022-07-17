package com.lilywang.rewardsproducer

import com.lilywang.rewardsproducer.producer.TransactionProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class CommandLineRunnerImpl(val transactionProducer: TransactionProducer) : CommandLineRunner{
    override fun run(vararg args: String?) {
        transactionProducer.publishTransaction()
    }
}