package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerName: String,
    val rating: Int = 5,
    val comment: String,
    val isApproved: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "customer_inquiries")
data class CustomerInquiry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerName: String,
    val contact: String,
    val message: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "announcement_banners")
data class AnnouncementBanner(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val message: String,
    val isActive: Boolean = true
)

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey
    val email: String,
    val fullName: String,
    val phone: String = "",
    val defaultAddress: String = "",
    val defaultLocation: String = "Kampala",
    val passwordHash: String = "",
    val isAdmin: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class CartItem(
    val productId: Long,
    val productName: String,
    val category: String,
    val selectedColor: String,
    val selectedSize: String,
    val quantity: Int,
    val unitPrice: Long,
    val imageUrl: String = ""
) {
    val lineKey: String get() = "${productId}_${selectedColor}_${selectedSize}"
    val lineTotal: Long get() = unitPrice * quantity
}
