package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CartItem
import com.example.data.model.Product
import com.example.ui.DoraViewModel
import com.example.ui.Screen
import com.example.ui.components.ProductCard
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.BeadBrown
import com.example.ui.theme.BeadBrownDark
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.WarmCardBeige

@Composable
fun HomeScreen(
    viewModel: DoraViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allProducts by viewModel.allProducts.collectAsState()
    val reviews by viewModel.approvedReviews.collectAsState()

    // Products ordered by most recent update/addition so new admin posts are immediately visible
    val sortedByRecent = allProducts.sortedByDescending { it.updatedAt }
    val latestAdditions = sortedByRecent.take(6)
    val featuredProducts = sortedByRecent.filter { it.isFeatured }.take(6)
    val newArrivals = sortedByRecent.filter { it.isNewArrival }.take(6)
    val bestSellers = sortedByRecent.filter { it.isBestSeller }.take(6)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Hero Section
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmCardBeige),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column {
                    // Hero Image Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.dora_hero_banner_1790965092473),
                            contentDescription = "Handmade beaded bag creations and supplies",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Hero Content
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Beautiful Beads. Beautiful Creations.",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            lineHeight = 30.sp,
                            color = BeadBrownDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Dora Fashions supplies premium bag beads, faceted crystals, freshwater cultured pearls, and complete bead-making supplies across Uganda.",
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = BeadBrown.copy(alpha = 0.85f)
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // CTA Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.navigateTo(Screen.Shop) },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Shop Catalog", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            OutlinedButton(
                                onClick = { viewModel.navigateTo(Screen.Contact) },
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Custom Orders", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = BeadBrown)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.openWhatsAppOrder(context, emptyList())
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Outlined.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WhatsApp Hotline (+256 775 803 896)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Category Chips
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Explore Categories",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = BeadBrownDark
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.categories) { cat ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (cat == "All") GoldPrimary else WarmCardBeige,
                            modifier = Modifier.clickable {
                                viewModel.selectedCategory.value = cat
                                viewModel.navigateTo(Screen.Shop)
                            }
                        ) {
                            Text(
                                text = cat,
                                color = if (cat == "All") Color.White else BeadBrownDark,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Latest Additions (Newly posted products by Admin)
        if (latestAdditions.isNotEmpty()) {
            item {
                ProductSectionRow(
                    title = "🔥 Fresh Arrivals & Live Updates",
                    subtitle = "Newly posted bead designs available in realtime",
                    products = latestAdditions,
                    onViewAll = { viewModel.navigateTo(Screen.Shop) },
                    viewModel = viewModel
                )
            }
        }

        // Featured Products
        if (featuredProducts.isNotEmpty()) {
            item {
                ProductSectionRow(
                    title = "Featured Beads & Sets",
                    subtitle = "Hand-picked favorites from our Kampala studio",
                    products = featuredProducts,
                    onViewAll = {
                        viewModel.selectedCategory.value = "All"
                        viewModel.navigateTo(Screen.Shop)
                    },
                    viewModel = viewModel
                )
            }
        }

        // New Arrivals
        if (newArrivals.isNotEmpty()) {
            item {
                ProductSectionRow(
                    title = "New Arrivals ✨",
                    subtitle = "Fresh designs & sparkling crystal arrivals",
                    products = newArrivals,
                    onViewAll = { viewModel.navigateTo(Screen.Shop) },
                    viewModel = viewModel
                )
            }
        }

        // Best Sellers
        if (bestSellers.isNotEmpty()) {
            item {
                ProductSectionRow(
                    title = "Best Sellers 🏆",
                    subtitle = "What handbag & jewelry makers love most",
                    products = bestSellers,
                    onViewAll = { viewModel.navigateTo(Screen.Shop) },
                    viewModel = viewModel
                )
            }
        }

        // Why Choose Dora Fashions
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Why Choose Dora Fashions",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = BeadBrownDark
                )
                Text(
                    text = "Quality you can see and feel in every bead strand",
                    fontSize = 12.sp,
                    color = BeadBrown.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureCard(
                        icon = Icons.Outlined.Diamond,
                        title = "Premium Luster",
                        desc = "Faceted & cultured pearls with lasting sparkle",
                        modifier = Modifier.weight(1f)
                    )
                    FeatureCard(
                        icon = Icons.Outlined.Palette,
                        title = "Endless Choice",
                        desc = "Rich colors & sizes for all bag styles",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureCard(
                        icon = Icons.Outlined.LocalShipping,
                        title = "Uganda Delivery",
                        desc = "Doorstep delivery in Kampala & nationwide",
                        modifier = Modifier.weight(1f)
                    )
                    FeatureCard(
                        icon = Icons.Outlined.Chat,
                        title = "WhatsApp Care",
                        desc = "Instant friendly advice and custom designs",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Reviews Preview
        if (reviews.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Customer Reviews",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = BeadBrownDark
                        )
                        Text(
                            text = "View All →",
                            color = GoldDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { viewModel.navigateTo(Screen.Reviews) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    reviews.take(3).forEach { rev ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = rev.customerName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = BeadBrownDark
                                    )
                                    Row {
                                        repeat(rev.rating) {
                                            Icon(
                                                Icons.Default.Star,
                                                contentDescription = null,
                                                tint = GoldPrimary,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "“${rev.comment}”",
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp,
                                    color = BeadBrown.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductSectionRow(
    title: String,
    subtitle: String,
    products: List<Product>,
    onViewAll: () -> Unit,
    viewModel: DoraViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = BeadBrownDark
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = BeadBrown.copy(alpha = 0.7f)
                )
            }
            Text(
                text = "See All",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = GoldDark,
                modifier = Modifier.clickable { onViewAll() }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(products, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    onAddToCart = { p, c, s, q -> viewModel.addToCart(p, c, s, q) },
                    onBuyNow = { p, c, s, q -> viewModel.addToCart(p, c, s, q, proceedToCheckout = true) },
                    onQuickWhatsApp = { p, c, s, q ->
                        val item = CartItem(
                            productId = p.id,
                            productName = p.name,
                            category = p.category,
                            selectedColor = c,
                            selectedSize = s,
                            quantity = q,
                            unitPrice = p.price
                        )
                        viewModel.openWhatsAppOrder(context, listOf(item))
                    },
                    modifier = Modifier.width(240.dp)
                )
            }
        }
    }
}

@Composable
fun FeatureCard(
    icon: ImageVector,
    title: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(WarmCardBeige, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BeadBrownDark)
            Spacer(modifier = Modifier.height(2.dp))
            Text(desc, fontSize = 10.sp, color = BeadBrown.copy(alpha = 0.7f), lineHeight = 13.sp)
        }
    }
}
