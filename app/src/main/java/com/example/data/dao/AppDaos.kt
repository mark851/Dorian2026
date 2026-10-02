package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AnnouncementBanner
import com.example.data.model.CustomerInquiry
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.data.model.Review
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY updatedAt DESC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: Long): Product?

    @Query("SELECT * FROM products WHERE isFeatured = 1 ORDER BY updatedAt DESC")
    fun getFeaturedProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE category = :cat ORDER BY updatedAt DESC")
    fun getProductsByCategory(cat: String): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE products SET stock = MAX(0, stock - :quantity) WHERE id = :id")
    suspend fun decrementStock(id: Long, quantity: Int)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun countProducts(): Int
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE email = :email OR phone = :phone ORDER BY createdAt DESC")
    fun getOrdersForCustomer(email: String, phone: String): Flow<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Query("UPDATE orders SET status = :status WHERE orderNo = :orderNo")
    suspend fun updateOrderStatus(orderNo: String, status: String)

    @Query("DELETE FROM orders WHERE orderNo = :orderNo")
    suspend fun deleteOrder(orderNo: String)
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE isApproved = 1 ORDER BY createdAt DESC")
    fun getApprovedReviews(): Flow<List<Review>>

    @Query("SELECT * FROM reviews ORDER BY createdAt DESC")
    fun getAllReviews(): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reviews: List<Review>)

    @Query("UPDATE reviews SET isApproved = :approved WHERE id = :id")
    suspend fun setApproval(id: Long, approved: Boolean)

    @Query("DELETE FROM reviews WHERE id = :id")
    suspend fun deleteReview(id: Long)

    @Query("SELECT COUNT(*) FROM reviews")
    suspend fun countReviews(): Int
}

@Dao
interface InquiryDao {
    @Query("SELECT * FROM customer_inquiries ORDER BY createdAt DESC")
    fun getAllInquiries(): Flow<List<CustomerInquiry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInquiry(inquiry: CustomerInquiry): Long

    @Query("DELETE FROM customer_inquiries WHERE id = :id")
    suspend fun deleteInquiry(id: Long)
}

@Dao
interface BannerDao {
    @Query("SELECT * FROM announcement_banners WHERE isActive = 1")
    fun getActiveBanners(): Flow<List<AnnouncementBanner>>

    @Query("SELECT * FROM announcement_banners")
    fun getAllBanners(): Flow<List<AnnouncementBanner>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanner(banner: AnnouncementBanner): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(banners: List<AnnouncementBanner>)

    @Query("DELETE FROM announcement_banners WHERE id = :id")
    suspend fun deleteBanner(id: Long)

    @Query("SELECT COUNT(*) FROM announcement_banners")
    suspend fun countBanners(): Int
}

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profiles WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserProfile)

    @Update
    suspend fun updateUser(user: UserProfile)
}
