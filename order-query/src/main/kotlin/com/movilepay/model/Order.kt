package com.movilepay.model

import com.movilepay.domain.OrderStatus
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "orders")
data class Order(
    @Id
    val id: UUID,
    val customerName: String,
    val address: String,
    @Enumerated(EnumType.STRING)
    val status: OrderStatus,
    val amount: Long,
    val canceledBy: String? = null
)