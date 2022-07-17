package com.lilywang.rewardsproducer.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction (
    val transactionId: String,
    val date: LocalDateTime,
    val accountId: String,
    val amount: BigDecimal,
)

