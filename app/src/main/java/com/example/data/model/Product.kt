package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val price: Long, // UGX
    val stock: Int,
    val colors: String, // Comma-separated: "Gold, Silver, Multi"
    val sizes: String,  // Comma-separated: "8mm, 10mm"
    val imageUrl: String = "",
    val isFeatured: Boolean = false,
    val isNewArrival: Boolean = false,
    val isBestSeller: Boolean = false,
    val description: String = "",
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun colorList(): List<String> = colors.split(",").map { it.trim() }.filter { it.isNotEmpty() }.ifEmpty { listOf("Multi") }
    fun sizeList(): List<String> = sizes.split(",").map { it.trim() }.filter { it.isNotEmpty() }.ifEmpty { listOf("Standard") }
}
