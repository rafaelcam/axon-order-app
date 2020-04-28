package com.movilepay.aggregate

import com.movilepay.command.CancelOrderCommand
import com.movilepay.command.ConfirmOrderCommand
import com.movilepay.command.CreateOrderCommand
import com.movilepay.domain.OrderStatus
import com.movilepay.event.OrderCanceledEvent
import com.movilepay.event.OrderConfirmedEvent
import com.movilepay.event.OrderCreatedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate
import java.util.UUID

@Aggregate
class Order {

    @AggregateIdentifier
    private lateinit var id: UUID
    private lateinit var customerName: String
    private lateinit var address: String
    private lateinit var status: OrderStatus
    private lateinit var canceledBy: String
    private var amount: Long = 0

    constructor() {}

    @CommandHandler
    constructor(command: CreateOrderCommand) {
        apply(OrderCreatedEvent(
            orderId = command.orderId,
            customerName = command.customerName,
            address = command.address,
            amount = command.amount
        ))
    }

    @CommandHandler
    fun handle(command: ConfirmOrderCommand) {
        if (OrderStatus.CANCELED == status) {
            throw IllegalStateException("Order is canceled")
        }
        
        apply(OrderConfirmedEvent(
            orderId = command.orderId
        ))
    }

    @CommandHandler
    fun handle(command: CancelOrderCommand) {
        apply(OrderCanceledEvent(
            orderId = command.orderId,
            username = command.username
        ))
    }

    @EventSourcingHandler
    fun on(event: OrderCreatedEvent) {
        id = event.orderId
        customerName = event.customerName
        address = event.address
        amount = event.amount
        status = OrderStatus.CREATED
    }

    @EventSourcingHandler
    fun on(event: OrderConfirmedEvent) {
        status = OrderStatus.CONFIRMED
    }

    @EventSourcingHandler
    fun on(event: OrderCanceledEvent) {
        status = OrderStatus.CANCELED
        canceledBy = event.username
    }
}