package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.BannerDao
import com.example.data.dao.InquiryDao
import com.example.data.dao.OrderDao
import com.example.data.dao.ProductDao
import com.example.data.dao.ReviewDao
import com.example.data.dao.UserDao
import com.example.data.model.AnnouncementBanner
import com.example.data.model.CustomerInquiry
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.data.model.Review
import com.example.data.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Product::class,
        Order::class,
        Review::class,
        CustomerInquiry::class,
        AnnouncementBanner::class,
        UserProfile::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun reviewDao(): ReviewDao
    abstract fun inquiryDao(): InquiryDao
    abstract fun bannerDao(): BannerDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dora_fashions_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database)
                }
            }
        }

        private suspend fun populateInitialData(db: AppDatabase) {
            val productDao = db.productDao()
            val bannerDao = db.bannerDao()
            val reviewDao = db.reviewDao()
            val userDao = db.userDao()

            // Seed initial store products
            val initialProducts = listOf(
                Product(
                    id = 1,
                    name = "Gold Bag Bead Charm Set",
                    category = "Bag Beads",
                    price = 15000,
                    stock = 40,
                    colors = "Gold, Silver",
                    sizes = "8mm, 10mm",
                    isFeatured = true,
                    isBestSeller = true,
                    description = "Heavy metallic coated acrylic beads for handbag handles, closures and luxury accents."
                ),
                Product(
                    id = 2,
                    name = "Rainbow Bag Bead Strand",
                    category = "Bag Beads",
                    price = 12000,
                    stock = 25,
                    colors = "Multi, Pastel",
                    sizes = "8mm, 12mm",
                    isNewArrival = true,
                    description = "Vibrant multi-colored bead strands ideal for colorful statement woven tote bags."
                ),
                Product(
                    id = 3,
                    name = "Handmade Clay Bead Pack",
                    category = "Handmade Beads",
                    price = 18000,
                    stock = 18,
                    colors = "Brown, Orange, Green",
                    sizes = "Small, Medium",
                    isFeatured = true,
                    description = "Authentic kiln-fired artisan clay beads crafted locally in Uganda."
                ),
                Product(
                    id = 4,
                    name = "African Print Handmade Beads",
                    category = "Handmade Beads",
                    price = 22000,
                    stock = 12,
                    colors = "Multi, Earthy",
                    sizes = "Medium, Large",
                    isBestSeller = true,
                    description = "Rich traditional patterns wrapped on durable core beads for ethnic jewelry."
                ),
                Product(
                    id = 5,
                    name = "Crystal Glass Strand",
                    category = "Crystal Beads",
                    price = 25000,
                    stock = 30,
                    colors = "Clear, Pink, Blue",
                    sizes = "6mm, 8mm",
                    isFeatured = true,
                    isNewArrival = true,
                    description = "Faceted high-refraction glass crystals that catch light with brilliant sparkle."
                ),
                Product(
                    id = 6,
                    name = "Bicone Crystal Beads (500pcs)",
                    category = "Crystal Beads",
                    price = 35000,
                    stock = 15,
                    colors = "Clear, Purple, Emerald",
                    sizes = "4mm, 6mm",
                    description = "Bulk pack of precision cut bicone crystals for intricate bag weaving."
                ),
                Product(
                    id = 7,
                    name = "Freshwater Pearl Strand",
                    category = "Pearl Beads",
                    price = 30000,
                    stock = 22,
                    colors = "Pearl, White, Pink",
                    sizes = "6mm, 8mm",
                    isFeatured = true,
                    isBestSeller = true,
                    description = "Natural cultured pearls with lustrous iridescent finish for bridal & formal bags."
                ),
                Product(
                    id = 8,
                    name = "Pearl Bag Chain Beads",
                    category = "Pearl Beads",
                    price = 28000,
                    stock = 15,
                    colors = "Pearl, Gold",
                    sizes = "10mm, 14mm",
                    isNewArrival = true,
                    description = "Large diameter faux pearls with reinforced inner hole for heavy handbag chains."
                ),
                Product(
                    id = 9,
                    name = "Chunky Acrylic Bead Mix",
                    category = "Acrylic Beads",
                    price = 10000,
                    stock = 60,
                    colors = "Multi, Neon, Pastel",
                    sizes = "12mm, 16mm",
                    isBestSeller = true,
                    description = "Lightweight chunky beads in brilliant opaque and jelly shades for modern purses."
                ),
                Product(
                    id = 10,
                    name = "Acrylic Letter Beads (A-Z)",
                    category = "Acrylic Beads",
                    price = 14000,
                    stock = 35,
                    colors = "White, Black, Pink",
                    sizes = "7mm",
                    isNewArrival = true,
                    description = "Complete alphabet letter beads for personalizing bags, wristlets and necklaces."
                ),
                Product(
                    id = 11,
                    name = "Beading Needle & Thread Kit",
                    category = "Bead Accessories",
                    price = 8000,
                    stock = 50,
                    colors = "Black, White, Clear",
                    sizes = "Standard",
                    isFeatured = true,
                    description = "Extra-strong nylon beading filament and high-tensile flexible needles."
                ),
                Product(
                    id = 12,
                    name = "Gold Clasps & Findings Set",
                    category = "Bead Accessories",
                    price = 12000,
                    stock = 45,
                    colors = "Gold, Silver, Rose Gold",
                    sizes = "Assorted",
                    isBestSeller = true,
                    description = "Lobster clasps, jump rings, bag D-rings and magnetic snap locks."
                ),
                Product(
                    id = 13,
                    name = "Custom Bag Bead Design",
                    category = "Custom Orders",
                    price = 50000,
                    stock = 99,
                    colors = "Custom Colors",
                    sizes = "Custom",
                    isNewArrival = true,
                    description = "Bespoke handcrafted bead styling curated specifically for your bag project."
                )
            )
            productDao.insertAll(initialProducts)

            // Seed initial announcements
            val initialBanners = listOf(
                AnnouncementBanner(message = "✨ Free delivery in Kampala on bead orders above UGX 100,000 ✨"),
                AnnouncementBanner(message = "💎 New crystal strands & pearl bag accessories just arrived!"),
                AnnouncementBanner(message = "📱 WhatsApp Hotline: +256 775 803 896 — Fast orders & advice")
            )
            bannerDao.insertAll(initialBanners)

            // Seed initial reviews
            val initialReviews = listOf(
                Review(
                    customerName = "Sarah N.",
                    rating = 5,
                    comment = "The pearl strands are gorgeous and arrived quickly. My beaded bags have never looked better!",
                    isApproved = true
                ),
                Review(
                    customerName = "Grace A.",
                    rating = 5,
                    comment = "Great quality beads and very fair prices. Ordering on WhatsApp was so easy.",
                    isApproved = true
                ),
                Review(
                    customerName = "Miriam K.",
                    rating = 5,
                    comment = "Dora made a custom bead set for my wedding party. Absolutely beautiful craftsmanship.",
                    isApproved = true
                )
            )
            reviewDao.insertAll(initialReviews)

            // Seed Admin profile
            userDao.insertUser(
                UserProfile(
                    email = "markniaras@gmail.com",
                    fullName = "Dorian (Owner)",
                    phone = "+256775803896",
                    defaultAddress = "Kampala Studio, Uganda",
                    defaultLocation = "Kampala",
                    passwordHash = "Dora2026",
                    isAdmin = true
                )
            )
        }
    }
}
