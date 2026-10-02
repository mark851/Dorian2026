package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey
    val orderNo: String,
    val customerName: String,
    val phone: String,
    val email: String,
    val location: String,
    val address: String,
    val paymentMethod: String,
    val notes: String = "",
    val itemsSummary: String, // E.g. "Gold Bag Bead (Gold, 8mm) x2; Crystal Glass x1"
    val subtotal: Long,
    val deliveryFee: Long,
    val total: Long,
    val status: String = "Pending", // Pending, Processing, Shipped, Delivered, Cancelled
    val createdAt: Long = System.currentTimeMillis()
)
