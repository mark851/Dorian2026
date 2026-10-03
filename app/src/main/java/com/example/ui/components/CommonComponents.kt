package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.AnnouncementBanner
import com.example.data.model.CartItem
import com.example.data.model.Product
import com.example.ui.Screen
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BeadBrown
import com.example.ui.theme.BeadBrownDark
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.WarmBgLight
import com.example.ui.theme.WarmCardBeige
import kotlinx.coroutines.delay

fun formatUgx(amount: Long): String {
    return "UGX %,d".format(amount)
}

val BeadColorPalette = mapOf(
    "Gold" to Color(0xFFD4AF37),
    "Silver" to Color(0xFFC0C0C0),
    "Multi" to Color(0xFFE88FB0),
    "Brown" to Color(0xFF8A5A3B),
    "Orange" to Color(0xFFEE8A2B),
    "Green" to Color(0xFF2E8B57),
    "Clear" to Color(0xFFBFE3F0),
    "Pink" to Color(0xFFE88FB0),
    "Blue" to Color(0xFF2E6FBF),
    "Purple" to Color(0xFF7D4FB3),
    "Pearl" to Color(0xFFF4EDE4),
    "White" to Color(0xFFFFFFFF),
    "Black" to Color(0xFF2A2A2A),
    "Rose Gold" to Color(0xFFB76E79),
    "Neon" to Color(0xFFFF5722),
    "Pastel" to Color(0xFFCE93D8)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreTopBar(
    cartCount: Int,
    isUserLoggedIn: Boolean,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onNavigate(Screen.Home) }
                    .testTag("store_logo_button")
            ) {
                Text(
                    text = "Dora",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = BeadBrown
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Fashions",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = GoldPrimary
                )
            }
        },
        actions = {
            // Customer Account / Profile
            IconButton(
                onClick = { onNavigate(Screen.Account) },
                modifier = Modifier.testTag("account_button")
            ) {
                Icon(
                    imageVector = if (isUserLoggedIn) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "User Account",
                    tint = if (isUserLoggedIn) GoldDark else BeadBrown
                )
            }

            // Cart with Badge
            IconButton(
                onClick = { onNavigate(Screen.Cart) },
                modifier = Modifier.testTag("cart_button")
            ) {
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge(
                                containerColor = GoldPrimary,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = "$cartCount",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingCart,
                        contentDescription = "Shopping Cart",
                        tint = BeadBrown
                    )
                }
            }

            // Owner Restricted Portal Gate (Locks/Unlocks admin)
            IconButton(
                onClick = { onNavigate(Screen.AdminPortal) },
                modifier = Modifier.testTag("admin_lock_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Store Owner Admin Access",
                    tint = BeadBrownDark
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = WarmBgLight,
            titleContentColor = BeadBrown
        ),
        modifier = modifier
    )
}

@Composable
fun StoreAnnouncementBar(
    banners: List<AnnouncementBanner>,
    onDomainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (banners.isEmpty()) return

    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(banners.size) {
        if (banners.size > 1) {
            while (true) {
                delay(4000)
                currentIndex = (currentIndex + 1) % banners.size
            }
        }
    }

    val currentBanner = banners.getOrNull(currentIndex)?.message ?: banners.first().message

    Surface(
        color = BeadBrown,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDomainClick() }
            .testTag("announcement_banner")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = currentBanner,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun StoreBottomBar(
    currentScreen: Screen,
    cartCount: Int,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = BeadBrown,
        tonalElevation = 6.dp,
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = currentScreen is Screen.Home,
            onClick = { onNavigate(Screen.Home) },
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BeadBrown,
                indicatorColor = WarmCardBeige
            ),
            modifier = Modifier.testTag("nav_home")
        )
        NavigationBarItem(
            selected = currentScreen is Screen.Shop,
            onClick = { onNavigate(Screen.Shop) },
            icon = { Icon(Icons.Outlined.ShoppingBag, contentDescription = "Shop") },
            label = { Text("Shop", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BeadBrown,
                indicatorColor = WarmCardBeige
            ),
            modifier = Modifier.testTag("nav_shop")
        )
        NavigationBarItem(
            selected = currentScreen is Screen.Reviews,
            onClick = { onNavigate(Screen.Reviews) },
            icon = { Icon(Icons.Outlined.RateReview, contentDescription = "Reviews") },
            label = { Text("Reviews", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BeadBrown,
                indicatorColor = WarmCardBeige
            ),
            modifier = Modifier.testTag("nav_reviews")
        )
        NavigationBarItem(
            selected = currentScreen is Screen.Contact,
            onClick = { onNavigate(Screen.Contact) },
            icon = { Icon(Icons.Outlined.Chat, contentDescription = "Contact") },
            label = { Text("Contact", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BeadBrown,
                indicatorColor = WarmCardBeige
            ),
            modifier = Modifier.testTag("nav_contact")
        )
        NavigationBarItem(
            selected = currentScreen is Screen.Cart,
            onClick = { onNavigate(Screen.Cart) },
            icon = {
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge(containerColor = GoldPrimary) {
                                Text("$cartCount", fontSize = 10.sp)
                            }
                        }
                    }
                ) {
                    Icon(Icons.Outlined.ShoppingCart, contentDescription = "Cart")
                }
            },
            label = { Text("Cart", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BeadBrown,
                indicatorColor = WarmCardBeige
            ),
            modifier = Modifier.testTag("nav_cart")
        )
    }
}

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: (Product, String, String, Int) -> Unit,
    onBuyNow: (Product, String, String, Int) -> Unit,
    onQuickWhatsApp: (Product, String, String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = remember(product.colors) { product.colorList() }
    val sizes = remember(product.sizes) { product.sizeList() }

    var selectedColor by remember(product.id) { mutableStateOf(colors.firstOrNull() ?: "Multi") }
    var selectedSize by remember(product.id) { mutableStateOf(sizes.firstOrNull() ?: "Standard") }
    var quantity by remember(product.id) { mutableIntStateOf(1) }

    val isOutOfStock = product.stock <= 0

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}")
    ) {
        Column {
            // Visual Image / Bead Art Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(WarmCardBeige)
            ) {
                if (product.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(160.dp)
                    )
                } else {
                    BeadCanvasPreview(
                        colors = colors,
                        modifier = Modifier.fillMaxWidth().height(160.dp)
                    )
                }

                // Badges
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (product.isNewArrival) {
                        Surface(
                            color = GoldPrimary,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "New",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                    }
                    if (product.isBestSeller) {
                        Surface(
                            color = BeadBrown,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Best Seller",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Body
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.category.uppercase(),
                    fontSize = 10.sp,
                    color = BeadBrown.copy(alpha = 0.7f),
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = BeadBrownDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = formatUgx(product.price),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = GoldDark
                )

                // Stock Indicator
                val stockText = when {
                    isOutOfStock -> "Out of stock"
                    product.stock < 10 -> "Only ${product.stock} left in stock"
                    else -> "In stock (${product.stock})"
                }
                val stockColor = when {
                    isOutOfStock -> AccentRed
                    product.stock < 10 -> Color(0xFFD98A1A)
                    else -> AccentGreen
                }
                Text(
                    text = stockText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = stockColor,
                    modifier = Modifier.padding(top = 2.dp, bottom = 6.dp)
                )

                // Color Selection Chips
                if (colors.size > 1) {
                    Text("Color: $selectedColor", fontSize = 11.sp, color = Color.Gray)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        colors.forEach { c ->
                            val isSelected = c == selectedColor
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) GoldPrimary else WarmCardBeige,
                                modifier = Modifier
                                    .clickable { selectedColor = c }
                                    .padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = c,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else BeadBrownDark,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }

                // Size Selection Chips
                if (sizes.size > 1) {
                    Text("Size: $selectedSize", fontSize = 11.sp, color = Color.Gray)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        sizes.forEach { s ->
                            val isSelected = s == selectedSize
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) BeadBrown else WarmCardBeige,
                                modifier = Modifier
                                    .clickable { selectedSize = s }
                                    .padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = s,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else BeadBrownDark,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }

                // Quantity Stepper
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Text("Quantity", fontSize = 12.sp, color = BeadBrown)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            enabled = !isOutOfStock && quantity > 1,
                            modifier = Modifier
                                .size(28.dp)
                                .background(WarmCardBeige, CircleShape)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(14.dp))
                        }
                        Text(
                            text = "$quantity",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                        IconButton(
                            onClick = { if (quantity < product.stock) quantity++ },
                            enabled = !isOutOfStock && quantity < product.stock,
                            modifier = Modifier
                                .size(28.dp)
                                .background(WarmCardBeige, CircleShape)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(14.dp))
                        }
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Button(
                        onClick = { onAddToCart(product, selectedColor, selectedSize, quantity) },
                        enabled = !isOutOfStock,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .testTag("add_cart_${product.id}")
                    ) {
                        Text("Add to Cart", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { onBuyNow(product, selectedColor, selectedSize, quantity) },
                        enabled = !isOutOfStock,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .testTag("buy_now_${product.id}")
                    ) {
                        Text("Buy Now", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BeadBrown)
                    }

                    // Quick WhatsApp button
                    IconButton(
                        onClick = { onQuickWhatsApp(product, selectedColor, selectedSize, quantity) },
                        modifier = Modifier
                            .size(36.dp)
                            .background(AccentGreen, RoundedCornerShape(10.dp))
                            .testTag("quick_wa_${product.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Chat,
                            contentDescription = "Order on WhatsApp",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BeadCanvasPreview(
    colors: List<String>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val colorItems = colors.map { BeadColorPalette[it] ?: GoldPrimary }
        val numBeads = 14
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.height * 0.32f

        // Draw beaded circle strand
        for (i in 0 until numBeads) {
            val angle = (2 * Math.PI * i / numBeads)
            val bx = centerX + (radius * Math.cos(angle)).toFloat()
            val by = centerY + (radius * Math.sin(angle)).toFloat()
            val beadColor = colorItems[i % colorItems.size]

            drawCircle(
                color = beadColor,
                radius = 16f,
                center = Offset(bx, by)
            )
            // Bead highlight
            drawCircle(
                color = Color.White.copy(alpha = 0.5f),
                radius = 5f,
                center = Offset(bx - 4f, by - 4f)
            )
        }

        // Center luxury gem
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(GoldLight, GoldDark),
                center = Offset(centerX, centerY),
                radius = 28f
            ),
            radius = 24f,
            center = Offset(centerX, centerY)
        )
    }
}
