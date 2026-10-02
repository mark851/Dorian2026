package com.example.data

import com.example.data.dao.BannerDao
import com.example.data.dao.InquiryDao
import com.example.data.dao.OrderDao
import com.example.data.dao.ProductDao
import com.example.data.dao.ReviewDao
import com.example.data.dao.UserDao
import com.example.data.model.AnnouncementBanner
import com.example.data.model.CartItem
import com.example.data.model.CustomerInquiry
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.data.model.Review
import com.example.data.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DoraRepository(
    private val productDao: ProductDao,
    private val orderDao: OrderDao,
    private val reviewDao: ReviewDao,
    private val inquiryDao: InquiryDao,
    private val bannerDao: BannerDao,
    private val userDao: UserDao,
    private val scope: CoroutineScope
) {
    val liveDomainUrl: String = "https://ais-dev-xc3nsplej42ygfemrnru2z-684478716108.europe-west2.run.app"
    val storePhone: String = "+256775803896"
    val storeEmail: String = "Namatakadoreen89@gmail.com"

    val allProducts: Flow<List<Product>> = productDao.getAllProducts()
    val allOrders: Flow<List<Order>> = orderDao.getAllOrders()
    val approvedReviews: Flow<List<Review>> = reviewDao.getApprovedReviews()
    val allReviews: Flow<List<Review>> = reviewDao.getAllReviews()
    val allInquiries: Flow<List<CustomerInquiry>> = inquiryDao.getAllInquiries()
    val activeBanners: Flow<List<AnnouncementBanner>> = bannerDao.getActiveBanners()
    val allBanners: Flow<List<AnnouncementBanner>> = bannerDao.getAllBanners()

    // In-memory reactive Cart
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Current Customer Session
    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    // Dedicated Admin Session (Requires Dorian / Dora2026)
    private val _isAdminAuthenticated = MutableStateFlow(false)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

    init {
        // Ensure initial seeds are loaded if first launch and sync with cloud
        scope.launch(Dispatchers.IO) {
            if (productDao.countProducts() == 0) {
                // Pre-populate directly if not triggered by callback
                seedDefaults()
            }
            try {
                syncWithCloud()
            } catch (e: Exception) {
                // Keep local if offline
            }
        }
    }

    private suspend fun seedDefaults() {
        productDao.insertAll(
            listOf(
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
                    colors = "Clear, Purple",
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
                    colors = "Gold, Silver",
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
        )
        if (bannerDao.countBanners() == 0) {
            bannerDao.insertAll(
                listOf(
                    AnnouncementBanner(message = "✨ Free delivery in Kampala on bead orders above UGX 100,000 ✨"),
                    AnnouncementBanner(message = "💎 New crystal strands & pearl bag accessories just arrived!"),
                    AnnouncementBanner(message = "📱 WhatsApp Hotline: +256 775 803 896 — Fast orders & advice")
                )
            )
        }
        if (reviewDao.countReviews() == 0) {
            reviewDao.insertAll(
                listOf(
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
            )
        }
        userDao.insertUser(
            UserProfile(
                email = "markniaras@gmail.com",
                fullName = "Dorian (Store Owner)",
                phone = "+256775803896",
                defaultAddress = "Kampala Studio, Uganda",
                defaultLocation = "Kampala",
                passwordHash = "Dora2026",
                isAdmin = true
            )
        )
    }

    // --- Cart Operations ---
    fun addToCart(product: Product, color: String, size: String, quantity: Int = 1) {
        _cartItems.update { current ->
            val key = "${product.id}_${color}_${size}"
            val existing = current.find { it.lineKey == key }
            if (existing != null) {
                val newQty = (existing.quantity + quantity).coerceAtMost(product.stock)
                current.map { if (it.lineKey == key) it.copy(quantity = newQty) else it }
            } else {
                current + CartItem(
                    productId = product.id,
                    productName = product.name,
                    category = product.category,
                    selectedColor = color,
                    selectedSize = size,
                    quantity = quantity.coerceAtMost(product.stock),
                    unitPrice = product.price,
                    imageUrl = product.imageUrl
                )
            }
        }
    }

    fun updateCartQuantity(lineKey: String, delta: Int, maxStock: Int = 999) {
        _cartItems.update { current ->
            current.mapNotNull { item ->
                if (item.lineKey == lineKey) {
                    val next = item.quantity + delta
                    if (next <= 0) null else item.copy(quantity = next.coerceAtMost(maxStock))
                } else item
            }
        }
    }

    fun removeFromCart(lineKey: String) {
        _cartItems.update { current -> current.filterNot { it.lineKey == lineKey } }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // --- Order Placement ---
    suspend fun placeOrder(
        customerName: String,
        phone: String,
        email: String,
        location: String,
        address: String,
        paymentMethod: String,
        notes: String = "",
        deliveryFee: Long
    ): Order = withContext(Dispatchers.IO) {
        val items = _cartItems.value
        val subtotal = items.sumOf { it.lineTotal }
        val total = subtotal + deliveryFee
        val orderNo = "DF-" + (System.currentTimeMillis() % 1000000).toString().padStart(6, '0')

        val summary = items.joinToString("; ") {
            "${it.productName} (${it.selectedColor}, ${it.selectedSize}) x${it.quantity}"
        }

        // Decrement stock in database
        for (item in items) {
            productDao.decrementStock(item.productId, item.quantity)
        }

        val order = Order(
            orderNo = orderNo,
            customerName = customerName,
            phone = phone,
            email = email,
            location = location,
            address = address,
            paymentMethod = paymentMethod,
            notes = notes,
            itemsSummary = summary,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            total = total,
            status = "Pending"
        )
        orderDao.insertOrder(order)
        clearCart()
        order
    }

    suspend fun updateOrderStatus(orderNo: String, newStatus: String) = withContext(Dispatchers.IO) {
        orderDao.updateOrderStatus(orderNo, newStatus)
    }

    suspend fun deleteOrder(orderNo: String) = withContext(Dispatchers.IO) {
        orderDao.deleteOrder(orderNo)
    }

    // --- Product Management (Admin) ---
    suspend fun saveProduct(product: Product): Long = withContext(Dispatchers.IO) {
        val savedId = if (product.id == 0L) {
            productDao.insertProduct(product.copy(updatedAt = System.currentTimeMillis()))
        } else {
            productDao.updateProduct(product.copy(updatedAt = System.currentTimeMillis()))
            product.id
        }
        // Immediately publish to cloud so all users can see it
        scope.launch(Dispatchers.IO) {
            pushCatalogToCloud()
        }
        savedId
    }

    suspend fun deleteProduct(productId: Long) = withContext(Dispatchers.IO) {
        productDao.deleteById(productId)
        scope.launch(Dispatchers.IO) {
            pushCatalogToCloud()
        }
    }

    suspend fun syncWithCloud(): Result<Int> = withContext(Dispatchers.IO) {
        val cloudRes = com.example.data.cloud.CloudSyncManager.fetchCatalogFromCloud()
        cloudRes.onSuccess { remoteProducts ->
            if (remoteProducts.isNotEmpty()) {
                productDao.insertAll(remoteProducts)
                return@withContext Result.success(remoteProducts.size)
            } else {
                pushCatalogToCloud()
            }
        }
        cloudRes.map { it.size }
    }

    suspend fun pushCatalogToCloud(): Result<Int> = withContext(Dispatchers.IO) {
        val products = productDao.getAllProducts().first()
        com.example.data.cloud.CloudSyncManager.publishCatalogToCloud(products)
    }

    // --- Reviews ---
    suspend fun submitReview(customerName: String, rating: Int, comment: String): Long = withContext(Dispatchers.IO) {
        val rev = Review(
            customerName = customerName,
            rating = rating,
            comment = comment,
            isApproved = true // Auto-approved or moderated
        )
        reviewDao.insertReview(rev)
    }

    suspend fun setReviewApproval(reviewId: Long, isApproved: Boolean) = withContext(Dispatchers.IO) {
        reviewDao.setApproval(reviewId, isApproved)
    }

    suspend fun deleteReview(reviewId: Long) = withContext(Dispatchers.IO) {
        reviewDao.deleteReview(reviewId)
    }

    // --- Inquiries ---
    suspend fun sendInquiry(name: String, contact: String, message: String): Long = withContext(Dispatchers.IO) {
        inquiryDao.insertInquiry(CustomerInquiry(customerName = name, contact = contact, message = message))
    }

    suspend fun deleteInquiry(id: Long) = withContext(Dispatchers.IO) {
        inquiryDao.deleteInquiry(id)
    }

    // --- Announcement Banners ---
    suspend fun addBanner(message: String): Long = withContext(Dispatchers.IO) {
        bannerDao.insertBanner(AnnouncementBanner(message = message, isActive = true))
    }

    suspend fun deleteBanner(id: Long) = withContext(Dispatchers.IO) {
        bannerDao.deleteBanner(id)
    }

    // --- User Authentication ---
    suspend fun signUpCustomer(
        fullName: String,
        email: String,
        phone: String,
        address: String,
        location: String,
        password: String
    ): Result<UserProfile> = withContext(Dispatchers.IO) {
        val existing = userDao.getUserByEmail(email.trim().lowercase())
        if (existing != null) {
            return@withContext Result.failure(Exception("An account with this email already exists"))
        }
        val user = UserProfile(
            email = email.trim().lowercase(),
            fullName = fullName.trim(),
            phone = phone.trim(),
            defaultAddress = address.trim(),
            defaultLocation = location,
            passwordHash = password,
            isAdmin = false
        )
        userDao.insertUser(user)
        _currentUser.value = user
        Result.success(user)
    }

    suspend fun loginCustomer(email: String, password: String): Result<UserProfile> = withContext(Dispatchers.IO) {
        val user = userDao.getUserByEmail(email.trim().lowercase())
            ?: return@withContext Result.failure(Exception("Account not found with this email"))
        if (user.passwordHash != password) {
            return@withContext Result.failure(Exception("Invalid password"))
        }
        _currentUser.value = user
        Result.success(user)
    }

    fun logoutCustomer() {
        _currentUser.value = null
    }

    // --- Admin Authentication (Separate & Protected) ---
    // Specifically accepts username "Dorian" or "markniaras@gmail.com" with password "Dora2026"
    fun loginAdmin(username: String, pass: String): Boolean {
        val cleanUser = username.trim().lowercase()
        val isUserValid = cleanUser == "dorian" ||
                cleanUser == "markniaras@gmail.com" ||
                cleanUser == "dora" ||
                cleanUser == "admin"
        val isPassValid = pass == "Dora2026" || pass.equals("dora2026", ignoreCase = true)

        if (isUserValid && isPassValid) {
            _isAdminAuthenticated.value = true
            return true
        }
        return false
    }

    fun logoutAdmin() {
        _isAdminAuthenticated.value = false
    }

    // --- Live Web Catalog Exporter / Web Sync ---
    suspend fun generateWebSyncPayload(): String = withContext(Dispatchers.IO) {
        val products = productDao.getAllProducts().first()
        val jsonSb = StringBuilder()
        jsonSb.append("{\n")
        jsonSb.append("  \"store\": \"Dora Fashions\",\n")
        jsonSb.append("  \"liveHost\": \"$liveDomainUrl\",\n")
        jsonSb.append("  \"syncedAt\": ${System.currentTimeMillis()},\n")
        jsonSb.append("  \"productCount\": ${products.size},\n")
        jsonSb.append("  \"products\": [\n")
        products.forEachIndexed { index, p ->
            jsonSb.append("    {\n")
            jsonSb.append("      \"id\": ${p.id},\n")
            jsonSb.append("      \"name\": \"${p.name.replace("\"", "\\\"")}\",\n")
            jsonSb.append("      \"category\": \"${p.category}\",\n")
            jsonSb.append("      \"price\": ${p.price},\n")
            jsonSb.append("      \"stock\": ${p.stock},\n")
            jsonSb.append("      \"colors\": [${p.colorList().joinToString { "\"$it\"" }}],\n")
            jsonSb.append("      \"sizes\": [${p.sizeList().joinToString { "\"$it\"" }}],\n")
            jsonSb.append("      \"isFeatured\": ${p.isFeatured},\n")
            jsonSb.append("      \"isNewArrival\": ${p.isNewArrival},\n")
            jsonSb.append("      \"isBestSeller\": ${p.isBestSeller}\n")
            jsonSb.append("    }${if (index < products.size - 1) "," else ""}\n")
        }
        jsonSb.append("  ]\n}")
        jsonSb.toString()
    }
}
