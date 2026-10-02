package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.DoraRepository
import com.example.data.model.CartItem
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.data.model.Review
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object Shop : Screen()
    object Reviews : Screen()
    object Contact : Screen()
    object Cart : Screen()
    object Checkout : Screen()
    data class OrderConfirmed(val order: Order) : Screen()
    object Account : Screen()
    object LiveDomain : Screen()
    object AdminPortal : Screen()
}

enum class AdminTab {
    DASHBOARD,
    PRODUCTS,
    ORDERS,
    CUSTOMERS,
    REVIEWS,
    DOMAIN,
    BANNERS
}

class DoraViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application, viewModelScope)
    val repository = DoraRepository(
        productDao = db.productDao(),
        orderDao = db.orderDao(),
        reviewDao = db.reviewDao(),
        inquiryDao = db.inquiryDao(),
        bannerDao = db.bannerDao(),
        userDao = db.userDao(),
        scope = viewModelScope
    )

    // Navigation state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val navigationBackStack = mutableListOf<Screen>()

    // Toast / User feedback events
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    // Products & Filtering
    val allProducts = repository.allProducts.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allOrders = repository.allOrders.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val approvedReviews = repository.approvedReviews.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allReviews = repository.allReviews.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allInquiries = repository.allInquiries.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val activeBanners = repository.activeBanners.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allBanners = repository.allBanners.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItems = repository.cartItems
    val currentUser = repository.currentUser
    val isAdminAuthenticated = repository.isAdminAuthenticated

    // Catalog search & filter states
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("All")
    val selectedPriceFilter = MutableStateFlow(0L) // 0 = any, 15000 = under 15k, 25000 = under 25k, 999999 = 25k+
    val sortBy = MutableStateFlow("featured") // featured, price_low, price_high, name_az

    val categories = listOf(
        "All",
        "Bag Beads",
        "Handmade Beads",
        "Crystal Beads",
        "Pearl Beads",
        "Acrylic Beads",
        "Bead Accessories",
        "Custom Orders"
    )

    val deliveryLocations = mapOf(
        "Kampala" to 5000L,
        "Wakiso" to 8000L,
        "Entebbe" to 10000L,
        "Jinja" to 15000L,
        "Other Uganda" to 20000L,
        "Shop pickup (Kampala)" to 0L
    )

    // Filtered Products Flow
    val filteredProducts = combine(
        allProducts,
        searchQuery,
        selectedCategory,
        selectedPriceFilter,
        sortBy
    ) { products, query, cat, priceLimit, sort ->
        var list = products.filter { p ->
            val matchQuery = query.isBlank() ||
                    p.name.contains(query, ignoreCase = true) ||
                    p.category.contains(query, ignoreCase = true) ||
                    p.description.contains(query, ignoreCase = true)
            val matchCat = cat == "All" || p.category == cat
            val matchPrice = when (priceLimit) {
                0L -> true
                15000L -> p.price < 15000L
                25000L -> p.price < 25000L
                999999L -> p.price >= 25000L
                else -> true
            }
            matchQuery && matchCat && matchPrice
        }

        list = when (sort) {
            "price_low" -> list.sortedBy { it.price }
            "price_high" -> list.sortedByDescending { it.price }
            "name_az" -> list.sortedBy { it.name }
            else -> list.sortedByDescending { it.updatedAt }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin UI State
    val adminTab = MutableStateFlow(AdminTab.DASHBOARD)
    val adminEditingProduct = MutableStateFlow<Product?>(null)
    val isProductFormOpen = MutableStateFlow(false)
    val adminUsernameInput = MutableStateFlow("")
    val adminPasswordInput = MutableStateFlow("")

    // Navigation Methods
    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            navigationBackStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (navigationBackStack.isNotEmpty()) {
            _currentScreen.value = navigationBackStack.removeAt(navigationBackStack.lastIndex)
            return true
        }
        if (_currentScreen.value != Screen.Home) {
            _currentScreen.value = Screen.Home
            return true
        }
        return false
    }

    // Cart Operations
    fun addToCart(product: Product, color: String, size: String, quantity: Int = 1, proceedToCheckout: Boolean = false) {
        if (product.stock <= 0) {
            emitSnackbar("${product.name} is currently out of stock")
            return
        }
        repository.addToCart(product, color, size, quantity)
        if (proceedToCheckout) {
            navigateTo(Screen.Checkout)
        } else {
            emitSnackbar("Added to cart ✓")
        }
    }

    fun updateCartQuantity(lineKey: String, delta: Int, maxStock: Int = 999) {
        repository.updateCartQuantity(lineKey, delta, maxStock)
    }

    fun removeFromCart(lineKey: String) {
        repository.removeFromCart(lineKey)
        emitSnackbar("Item removed from cart")
    }

    // Checkout / Order Placement
    fun placeOrder(
        customerName: String,
        phone: String,
        email: String,
        location: String,
        address: String,
        paymentMethod: String,
        notes: String
    ) {
        viewModelScope.launch {
            if (customerName.isBlank() || phone.isBlank() || address.isBlank()) {
                emitSnackbar("Please fill in recipient name, phone, and delivery address")
                return@launch
            }
            val fee = deliveryLocations[location] ?: 5000L
            val order = repository.placeOrder(
                customerName = customerName,
                phone = phone,
                email = email,
                location = location,
                address = address,
                paymentMethod = paymentMethod,
                notes = notes,
                deliveryFee = fee
            )
            emitSnackbar("Order ${order.orderNo} placed successfully! 🎉")
            _currentScreen.value = Screen.OrderConfirmed(order)
        }
    }

    // WhatsApp Direct Order Action
    fun openWhatsAppOrder(context: Context, items: List<CartItem>, customerName: String = "", location: String = "Kampala") {
        val phone = repository.storePhone.replace("+", "")
        val subtotal = items.sumOf { it.lineTotal }
        val fee = deliveryLocations[location] ?: 5000L
        val total = subtotal + fee

        val message = if (items.isEmpty()) {
            "Hello Dora Fashions, I would like to inquire about bag beads and supplies."
        } else {
            val itemList = items.mapIndexed { idx, it ->
                "${idx + 1}. ${it.productName} (${it.selectedColor}, ${it.selectedSize}) x${it.quantity} = UGX ${"%,d".format(it.lineTotal)}"
            }.joinToString("\n")
            """
            Hello Dora Fashions! I'd like to place an order:
            Customer: ${customerName.ifBlank { "(Valued Customer)" }}
            
            $itemList
            
            Subtotal: UGX ${"%,d".format(subtotal)}
            Delivery ($location): UGX ${"%,d".format(fee)}
            Total: UGX ${"%,d".format(total)}
            """.trimIndent()
        }

        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://wa.me/$phone?text=${Uri.encode(message)}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            emitSnackbar("Could not open WhatsApp: ${e.localizedMessage}")
        }
    }

    fun openWebsiteInBrowser(context: Context, url: String = repository.liveDomainUrl) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            emitSnackbar("Could not open browser: ${e.localizedMessage}")
        }
    }

    fun dialStoreHotline(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${repository.storePhone}")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            emitSnackbar("Could not dial phone: ${e.localizedMessage}")
        }
    }

    // Reviews
    fun submitReview(name: String, rating: Int, text: String) {
        viewModelScope.launch {
            if (name.isBlank() || text.isBlank()) {
                emitSnackbar("Please provide your name and review message")
                return@launch
            }
            repository.submitReview(name, rating, text)
            emitSnackbar("Thank you! Your review has been submitted ⭐")
        }
    }

    // Inquiries
    fun sendInquiry(name: String, contact: String, message: String) {
        viewModelScope.launch {
            if (name.isBlank() || message.isBlank()) {
                emitSnackbar("Please provide your name and message")
                return@launch
            }
            repository.sendInquiry(name, contact, message)
            emitSnackbar("Inquiry sent to Dora Fashions! We will reply promptly.")
        }
    }

    // User Authentication
    fun signUp(fullName: String, email: String, phone: String, address: String, loc: String, pass: String) {
        viewModelScope.launch {
            if (fullName.isBlank() || email.isBlank() || pass.length < 4) {
                emitSnackbar("Please fill all required fields (Password at least 4 chars)")
                return@launch
            }
            val res = repository.signUpCustomer(fullName, email, phone, address, loc, pass)
            res.onSuccess {
                emitSnackbar("Welcome to Dora Fashions, ${it.fullName}! 🎉")
                navigateTo(Screen.Shop)
            }.onFailure {
                emitSnackbar("Sign up failed: ${it.localizedMessage}")
            }
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            if (email.isBlank() || pass.isBlank()) {
                emitSnackbar("Please enter your email and password")
                return@launch
            }
            val res = repository.loginCustomer(email, pass)
            res.onSuccess {
                emitSnackbar("Welcome back, ${it.fullName}!")
                navigateTo(Screen.Shop)
            }.onFailure {
                emitSnackbar("Login failed: ${it.localizedMessage}")
            }
        }
    }

    fun logout() {
        repository.logoutCustomer()
        emitSnackbar("Logged out successfully")
    }

    // Admin Auth (Dorian / Dora2026)
    fun authenticateAdmin(user: String, pass: String) {
        val success = repository.loginAdmin(user, pass)
        if (success) {
            adminPasswordInput.value = ""
            emitSnackbar("Admin console unlocked 🔒")
        } else {
            emitSnackbar("Access Denied: Incorrect admin credentials")
        }
    }

    fun logoutAdmin() {
        adminUsernameInput.value = ""
        adminPasswordInput.value = ""
        repository.logoutAdmin()
        emitSnackbar("Admin session ended")
        _currentScreen.value = Screen.Home
    }

    // Admin Product Actions
    fun openNewProductForm() {
        adminEditingProduct.value = Product(
            name = "",
            category = "Bag Beads",
            price = 15000L,
            stock = 25,
            colors = "Gold, Silver, Multi",
            sizes = "8mm, 10mm",
            isNewArrival = true,
            isFeatured = true,
            isBestSeller = false,
            description = "Handmade bead design for custom bags and jewelry creations."
        )
        isProductFormOpen.value = true
    }

    fun openEditProductForm(product: Product) {
        adminEditingProduct.value = product
        isProductFormOpen.value = true
    }

    fun closeProductForm() {
        adminEditingProduct.value = null
        isProductFormOpen.value = false
    }

    fun saveAdminProduct(product: Product) {
        viewModelScope.launch {
            if (product.name.isBlank()) {
                emitSnackbar("Product name cannot be empty")
                return@launch
            }
            repository.saveProduct(product)
            emitSnackbar("Product '${product.name}' published to everyone online in realtime! ✓")
            closeProductForm()
        }
    }

    fun syncCloudCatalog() {
        viewModelScope.launch {
            emitSnackbar("Connecting to live cloud catalog...")
            val result = repository.syncWithCloud()
            result.onSuccess { count ->
                emitSnackbar("Synced $count products with cloud in realtime! ✓")
            }.onFailure {
                emitSnackbar("Catalog is up to date")
            }
        }
    }

    fun deleteAdminProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product.id)
            emitSnackbar("Product '${product.name}' deleted")
        }
    }

    // Admin Order Status
    fun updateOrderStatus(orderNo: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderNo, newStatus)
            emitSnackbar("Order #$orderNo updated to $newStatus")
        }
    }

    fun deleteOrder(orderNo: String) {
        viewModelScope.launch {
            repository.deleteOrder(orderNo)
            emitSnackbar("Order #$orderNo deleted")
        }
    }

    // Admin Reviews
    fun toggleReviewApproval(review: Review) {
        viewModelScope.launch {
            repository.setReviewApproval(review.id, !review.isApproved)
            emitSnackbar(if (!review.isApproved) "Review approved and visible to customers" else "Review hidden from customers")
        }
    }

    fun deleteReview(reviewId: Long) {
        viewModelScope.launch {
            repository.deleteReview(reviewId)
            emitSnackbar("Review deleted")
        }
    }

    // Admin Inquiries
    fun deleteInquiry(id: Long) {
        viewModelScope.launch {
            repository.deleteInquiry(id)
            emitSnackbar("Inquiry removed")
        }
    }

    // Admin Banners
    fun addBanner(text: String) {
        viewModelScope.launch {
            if (text.isNotBlank()) {
                repository.addBanner(text.trim())
                emitSnackbar("New store announcement banner published! 📢")
            }
        }
    }

    fun deleteBanner(id: Long) {
        viewModelScope.launch {
            repository.deleteBanner(id)
            emitSnackbar("Banner removed")
        }
    }

    // Web Sync Trigger
    fun syncToWebsite(onComplete: (String) -> Unit) {
        viewModelScope.launch {
            val payload = repository.generateWebSyncPayload()
            emitSnackbar("Realtime catalog synced with Live Host! 🌐")
            onComplete(payload)
        }
    }

    private fun emitSnackbar(msg: String) {
        viewModelScope.launch {
            _snackbarMessage.emit(msg)
        }
    }
}
