package com.example.kafkaproducer.csv

import com.opencsv.bean.CsvBindByPosition

class TransactionRecord {

    @CsvBindByPosition(position = 0)
    lateinit var transactionId: String
    @CsvBindByPosition(position = 1)
    lateinit var  date: String
    @CsvBindByPosition(position = 2)
    lateinit var  accountId: String
    @CsvBindByPosition(position = 3)
    lateinit var  amount: String
}