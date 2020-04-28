package com.movilepay.projection

import com.movilepay.domain.OrderStatus
import com.movilepay.event.OrderCanceledEvent
import com.movilepay.event.OrderConfirmedEvent
import com.movilepay.event.OrderCreatedEvent
import com.movilepay.model.Order
import com.movilepay.repository.OrderRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class OrderProjection(private val repository: OrderRepository) {

    @EventHandler
    fun on(event: OrderCreatedEvent) {
        val order = Order(
            id = event.orderId,
            amount = event.amount,
            address = event.address,
            customerName = event.customerName,
            status = OrderStatus.CREATED
        )

        repository.save(order)
    }

    @EventHandler
    fun on(event: OrderConfirmedEvent) {
        repository.confirm(event.orderId)
    }

    @EventHandler
    fun on(event: OrderCanceledEvent) {
        repository.cancel(event.orderId, event.username)
    }
}