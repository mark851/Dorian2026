package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItem
import com.example.ui.DoraViewModel
import com.example.ui.components.ProductCard
import com.example.ui.theme.BeadBrown
import com.example.ui.theme.BeadBrownDark
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.WarmCardBeige

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    viewModel: DoraViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val products by viewModel.filteredProducts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedPriceFilter by viewModel.selectedPriceFilter.collectAsState()
    val sortBy by viewModel.sortBy.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.searchQuery.value = it },
            placeholder = { Text("Search beads, pearl strands, crystals...", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BeadBrown) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = BeadBrown)
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldPrimary,
                unfocusedBorderColor = WarmCardBeige,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            viewModel.categories.forEach { cat ->
                val isSelected = cat == selectedCategory
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) GoldPrimary else WarmCardBeige,
                    modifier = Modifier.clickable { viewModel.selectedCategory.value = cat }
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) Color.White else BeadBrownDark,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Price Filter and Sort Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Filter:", fontSize = 11.sp, color = BeadBrown, fontWeight = FontWeight.Bold)

            val priceOptions = listOf(
                0L to "Any Price",
                15000L to "< 15,000",
                25000L to "< 25,000",
                999999L to "25,000+"
            )
            priceOptions.forEach { (limit, label) ->
                val isSelected = selectedPriceFilter == limit
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) BeadBrown else Color.White,
                    modifier = Modifier.clickable { viewModel.selectedPriceFilter.value = limit }
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        color = if (isSelected) Color.White else BeadBrownDark,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))
            Text("Sort:", fontSize = 11.sp, color = BeadBrown, fontWeight = FontWeight.Bold)
            val sortOptions = listOf(
                "featured" to "Featured",
                "price_low" to "Price ↑",
                "price_high" to "Price ↓",
                "name_az" to "Name A-Z"
            )
            sortOptions.forEach { (key, label) ->
                val isSelected = sortBy == key
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) GoldPrimary else Color.White,
                    modifier = Modifier.clickable { viewModel.sortBy.value = key }
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        color = if (isSelected) Color.White else BeadBrownDark,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Results Count Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${products.size} bead items",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BeadBrown.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = WarmCardBeige,
                    modifier = Modifier.clickable { viewModel.syncCloudCatalog() }
                ) {
                    Text(
                        text = "☁️ Sync Live",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldDark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
            if (searchQuery.isNotEmpty() || selectedCategory != "All" || selectedPriceFilter != 0L) {
                Text(
                    text = "Reset Filters",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldDark,
                    modifier = Modifier.clickable {
                        viewModel.searchQuery.value = ""
                        viewModel.selectedCategory.value = "All"
                        viewModel.selectedPriceFilter.value = 0L
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Product Grid
        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No bead items found",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = BeadBrownDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Try clearing your search query or choosing another category.",
                        fontSize = 12.sp,
                        color = BeadBrown.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 165.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
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
                        }
                    )
                }
            }
        }
    }
}
