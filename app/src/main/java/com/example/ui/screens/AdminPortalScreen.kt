package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.ui.AdminTab
import com.example.ui.DoraViewModel
import com.example.ui.components.BeadCanvasPreview
import com.example.ui.components.formatUgx
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BeadBrown
import com.example.ui.theme.BeadBrownDark
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.WarmCardBeige

@Composable
fun AdminPortalScreen(
    viewModel: DoraViewModel,
    modifier: Modifier = Modifier
) {
    val isAdminAuth by viewModel.isAdminAuthenticated.collectAsState()

    if (!isAdminAuth) {
        AdminLoginGate(viewModel = viewModel, modifier = modifier)
    } else {
        AdminConsole(viewModel = viewModel, modifier = modifier)
    }
}

@Composable
fun AdminLoginGate(
    viewModel: DoraViewModel,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf(viewModel.adminUsernameInput.value) }
    var password by remember { mutableStateOf(viewModel.adminPasswordInput.value) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = WarmCardBeige,
                    modifier = Modifier.size(60.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = BeadBrown,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Store Owner Admin Access",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = BeadBrownDark
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "This administration center is separate and restricted exclusively to authorized store management.",
                    fontSize = 12.sp,
                    color = BeadBrown.copy(alpha = 0.75f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        viewModel.adminUsernameInput.value = it
                    },
                    label = { Text("Admin Username / Email") },
                    placeholder = { Text("Enter username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        viewModel.adminPasswordInput.value = it
                    },
                    label = { Text("Admin Password") },
                    placeholder = { Text("Enter password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = { viewModel.authenticateAdmin(username, password) },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Unlock Admin Console", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun AdminConsole(
    viewModel: DoraViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.adminTab.collectAsState()
    val isFormOpen by viewModel.isProductFormOpen.collectAsState()
    val editingProduct by viewModel.adminEditingProduct.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Admin Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Owner Admin Center 🔒",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = BeadBrownDark
                )
                Text(
                    text = "Authorized Administrator Session Active",
                    fontSize = 11.sp,
                    color = BeadBrown.copy(alpha = 0.7f)
                )
            }

            OutlinedButton(
                onClick = { viewModel.logoutAdmin() },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Sign Out", fontSize = 11.sp, color = AccentRed, fontWeight = FontWeight.Bold)
            }
        }

        // Admin Sub-Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val tabs = listOf(
                AdminTab.DASHBOARD to "📊 Overview",
                AdminTab.PRODUCTS to "📦 Inventory",
                AdminTab.ORDERS to "🧾 Orders",
                AdminTab.CUSTOMERS to "👥 Customers",
                AdminTab.REVIEWS to "⭐ Reviews",
                AdminTab.DOMAIN to "🌐 Live Host",
                AdminTab.BANNERS to "📢 Banners"
            )

            tabs.forEach { (tab, title) ->
                val isSelected = currentTab == tab
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) GoldPrimary else Color.White,
                    modifier = Modifier.clickable { viewModel.adminTab.value = tab }
                ) {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else BeadBrownDark,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    )
                }
            }
        }

        // Tab Content
        Box(modifier = Modifier.weight(1f)) {
            when (currentTab) {
                AdminTab.DASHBOARD -> AdminDashboardTab(viewModel)
                AdminTab.PRODUCTS -> AdminProductsTab(viewModel)
                AdminTab.ORDERS -> AdminOrdersTab(viewModel)
                AdminTab.CUSTOMERS -> AdminCustomersTab(viewModel)
                AdminTab.REVIEWS -> AdminReviewsTab(viewModel)
                AdminTab.DOMAIN -> AdminDomainTab(viewModel)
                AdminTab.BANNERS -> AdminBannersTab(viewModel)
            }
        }
    }

    // Add / Edit Product Dialog
    if (isFormOpen && editingProduct != null) {
        ProductFormDialog(
            product = editingProduct!!,
            categories = viewModel.categories.filterNot { it == "All" },
            onDismiss = { viewModel.closeProductForm() },
            onSave = { updated -> viewModel.saveAdminProduct(updated) }
        )
    }
}

@Composable
fun AdminDashboardTab(viewModel: DoraViewModel) {
    val orders by viewModel.allOrders.collectAsState()
    val products by viewModel.allProducts.collectAsState()

    val grossSales = orders.filterNot { it.status == "Cancelled" }.sumOf { it.total }
    val pendingOrders = orders.filter { it.status == "Pending" }.size
    val totalStock = products.sumOf { it.stock }
    val lowStockCount = products.filter { it.stock in 1..9 }.size

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            // Metrics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard("Total Gross Sales", formatUgx(grossSales), modifier = Modifier.weight(1f))
                MetricCard("Total Orders", "${orders.size}", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard("Pending Orders", "$pendingOrders", valueColor = if (pendingOrders > 0) Color(0xFFD98A1A) else BeadBrownDark, modifier = Modifier.weight(1f))
                MetricCard("Active Stock Units", "$totalStock", modifier = Modifier.weight(1f))
            }
        }

        if (lowStockCount > 0) {
            item {
                Surface(
                    color = Color(0xFFFFF3CD),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚠️ Stock Alert: $lowStockCount bead product(s) have fewer than 10 units remaining.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF856404),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        item {
            Text("Recent Orders", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BeadBrownDark)
        }

        if (orders.isEmpty()) {
            item {
                Text("No orders placed yet.", fontSize = 12.sp, color = BeadBrown.copy(alpha = 0.7f))
            }
        } else {
            items(orders.take(5), key = { it.orderNo }) { order ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(order.orderNo, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GoldDark)
                            Text(order.customerName, fontSize = 12.sp, color = BeadBrownDark)
                            Text(formatUgx(order.total), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BeadBrownDark)
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = WarmCardBeige
                        ) {
                            Text(
                                text = order.status,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BeadBrownDark,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    valueColor: Color = GoldDark,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, fontSize = 11.sp, color = BeadBrown.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, color = valueColor)
        }
    }
}

@Composable
fun AdminProductsTab(viewModel: DoraViewModel) {
    val products by viewModel.allProducts.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Surface(
                color = WarmCardBeige,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(AccentGreen, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Cloud Sync Active · Realtime for All Users",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BeadBrownDark
                        )
                    }
                    Text(
                        text = "Sync Now ☁️",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldDark,
                        modifier = Modifier.clickable { viewModel.syncCloudCatalog() }
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = { viewModel.openNewProductForm() },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add New Bead Product", fontWeight = FontWeight.Bold)
            }
        }

        items(products, key = { it.id }) { product ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(WarmCardBeige)
                    ) {
                        BeadCanvasPreview(colors = product.colorList(), modifier = Modifier.fillMaxSize())
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(product.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BeadBrownDark)
                        Text("${product.category} · ${formatUgx(product.price)}", fontSize = 11.sp, color = GoldDark, fontWeight = FontWeight.SemiBold)
                        Text("Stock: ${product.stock} units", fontSize = 11.sp, color = if (product.stock < 10) AccentRed else AccentGreen)
                    }

                    Row {
                        IconButton(onClick = { viewModel.openEditProductForm(product) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = BeadBrown)
                        }
                        IconButton(onClick = { viewModel.deleteAdminProduct(product) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AccentRed)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrdersTab(viewModel: DoraViewModel) {
    val context = LocalContext.current
    val orders by viewModel.allOrders.collectAsState()
    val statuses = listOf("Pending", "Processing", "Shipped", "Delivered", "Cancelled")

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        if (orders.isEmpty()) {
            item {
                Text("No orders recorded.", fontSize = 13.sp, color = BeadBrown.copy(alpha = 0.7f))
            }
        } else {
            items(orders, key = { it.orderNo }) { order ->
                var isStatusExpanded by remember { mutableStateOf(false) }

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(order.orderNo, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = GoldDark)

                            // Status Dropdown
                            ExposedDropdownMenuBox(
                                expanded = isStatusExpanded,
                                onExpandedChange = { isStatusExpanded = !isStatusExpanded }
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = WarmCardBeige,
                                    modifier = Modifier
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                        .clickable { isStatusExpanded = true }
                                ) {
                                    Text(
                                        text = "${order.status} ▾",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = BeadBrownDark,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                                ExposedDropdownMenu(
                                    expanded = isStatusExpanded,
                                    onDismissRequest = { isStatusExpanded = false }
                                ) {
                                    statuses.forEach { s ->
                                        DropdownMenuItem(
                                            text = { Text(s) },
                                            onClick = {
                                                viewModel.updateOrderStatus(order.orderNo, s)
                                                isStatusExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Customer: ${order.customerName} · 📞 ${order.phone}", fontSize = 12.sp, color = BeadBrownDark)
                        Text("Address: ${order.location} (${order.address})", fontSize = 11.sp, color = BeadBrown.copy(alpha = 0.75f))
                        Text("Items: ${order.itemsSummary}", fontSize = 11.sp, color = BeadBrown.copy(alpha = 0.85f), modifier = Modifier.padding(vertical = 3.dp))
                        if (order.notes.isNotBlank()) {
                            Text("Notes: ${order.notes}", fontSize = 11.sp, color = Color(0xFF856404))
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = WarmCardBeige)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total: ${formatUgx(order.total)} (${order.paymentMethod})", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BeadBrownDark)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = {
                                        val intent = android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse("tel:${order.phone}"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BeadBrown),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("Call", fontSize = 10.sp)
                                }
                                IconButton(
                                    onClick = { viewModel.deleteOrder(order.orderNo) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AccentRed, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun AdminCustomersTab(viewModel: DoraViewModel) {
    val inquiries by viewModel.allInquiries.collectAsState()
    val orders by viewModel.allOrders.collectAsState()

    // Unique customers from orders
    val customers = orders.groupBy { it.phone }.map { (phone, ords) ->
        val name = ords.first().customerName
        val totalSpent = ords.sumOf { it.total }
        Triple(name, phone, totalSpent)
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text("Customer Contacts (${customers.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BeadBrownDark)
        }

        items(customers) { (name, phone, spent) ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BeadBrownDark)
                        Text("📞 $phone", fontSize = 11.sp, color = BeadBrown.copy(alpha = 0.75f))
                    }
                    Text("Spent: ${formatUgx(spent)}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GoldDark)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text("Customer Messages & Inquiries (${inquiries.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BeadBrownDark)
        }

        if (inquiries.isEmpty()) {
            item {
                Text("No inquiries received.", fontSize = 12.sp, color = BeadBrown.copy(alpha = 0.7f))
            }
        } else {
            items(inquiries, key = { it.id }) { inq ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${inq.customerName} (${inq.contact})", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BeadBrownDark)
                            IconButton(onClick = { viewModel.deleteInquiry(inq.id) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AccentRed, modifier = Modifier.size(16.dp))
                            }
                        }
                        Text(inq.message, fontSize = 12.sp, color = BeadBrown.copy(alpha = 0.85f), modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun AdminReviewsTab(viewModel: DoraViewModel) {
    val reviews by viewModel.allReviews.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text("Review Moderation (${reviews.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BeadBrownDark)
            Text("Approve or hide customer reviews from the live website and mobile catalog.", fontSize = 11.sp, color = BeadBrown.copy(alpha = 0.7f))
        }

        items(reviews, key = { it.id }) { rev ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(rev.customerName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BeadBrownDark)
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (rev.isApproved) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                        ) {
                            Text(
                                text = if (rev.isApproved) "Approved" else "Hidden",
                                color = if (rev.isApproved) AccentGreen else AccentRed,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text("“${rev.comment}”", fontSize = 12.sp, color = BeadBrown.copy(alpha = 0.85f), modifier = Modifier.padding(vertical = 4.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { viewModel.toggleReviewApproval(rev) },
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text(if (rev.isApproved) "Hide Review" else "Approve Review", fontSize = 10.sp)
                        }
                        OutlinedButton(
                            onClick = { viewModel.deleteReview(rev.id) },
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("Delete", fontSize = 10.sp, color = AccentRed)
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun AdminDomainTab(viewModel: DoraViewModel) {
    val context = LocalContext.current
    val liveUrl = viewModel.repository.liveDomainUrl
    var syncPayload by remember { mutableStateOf("") }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Live Web Host Connection", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BeadBrownDark)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Target URL: $liveUrl", fontSize = 12.sp, color = GoldDark, fontFamily = FontFamily.Monospace)

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            viewModel.syncToWebsite { payload ->
                                syncPayload = payload
                                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                cm.setPrimaryClip(ClipData.newPlainText("Dora Fashions Catalog", payload))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Sync, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sync & Copy Live Web Catalog", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.openWebsiteInBrowser(context, liveUrl) },
                        colors = ButtonDefaults.buttonColors(containerColor = BeadBrown),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.OpenInBrowser, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Open Live Store Website", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (syncPayload.isNotBlank()) {
            item {
                Surface(
                    color = WarmCardBeige,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Live Web Sync Payload Generated (Copied to Clipboard):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BeadBrownDark)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = syncPayload.take(400) + if (syncPayload.length > 400) "\n... [truncated]" else "",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = BeadBrownDark
                        )
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun AdminBannersTab(viewModel: DoraViewModel) {
    val banners by viewModel.allBanners.collectAsState()
    var newBannerText by remember { mutableStateOf("") }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text("Announcement Banners", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BeadBrownDark)
            Text("These messages rotate in the top ticker bar on both web and mobile app.", fontSize = 11.sp, color = BeadBrown.copy(alpha = 0.7f))

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newBannerText,
                onValueChange = { newBannerText = it },
                label = { Text("New Announcement Banner") },
                placeholder = { Text("e.g. Free delivery this weekend on orders above UGX 80,000") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = {
                    viewModel.addBanner(newBannerText)
                    newBannerText = ""
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publish Announcement", fontWeight = FontWeight.Bold)
            }
        }

        items(banners, key = { it.id }) { b ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(b.message, fontSize = 12.sp, color = BeadBrownDark, modifier = Modifier.weight(1f))
                    IconButton(onClick = { viewModel.deleteBanner(b.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AccentRed, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormDialog(
    product: Product,
    categories: List<String>,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    var name by remember { mutableStateOf(product.name) }
    var selectedCat by remember { mutableStateOf(product.category) }
    var priceText by remember { mutableStateOf(product.price.toString()) }
    var stockText by remember { mutableStateOf(product.stock.toString()) }
    var colors by remember { mutableStateOf(product.colors) }
    var sizes by remember { mutableStateOf(product.sizes) }
    var isFeatured by remember { mutableStateOf(product.isFeatured) }
    var isNewArrival by remember { mutableStateOf(product.isNewArrival) }
    var isBestSeller by remember { mutableStateOf(product.isBestSeller) }
    var description by remember { mutableStateOf(product.description) }

    var isCatDropdownOpen by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.imePadding(),
        title = {
            Text(
                text = if (product.id == 0L) "Add New Bead Product" else "Edit Bead Product",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = BeadBrownDark
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Product Name *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    ExposedDropdownMenuBox(
                        expanded = isCatDropdownOpen,
                        onExpandedChange = { isCatDropdownOpen = !isCatDropdownOpen }
                    ) {
                        OutlinedTextField(
                            value = selectedCat,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCatDropdownOpen) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isCatDropdownOpen,
                            onDismissRequest = { isCatDropdownOpen = false }
                        ) {
                            categories.forEach { c ->
                                DropdownMenuItem(
                                    text = { Text(c) },
                                    onClick = {
                                        selectedCat = c
                                        isCatDropdownOpen = false
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            label = { Text("Price (UGX)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = stockText,
                            onValueChange = { stockText = it },
                            label = { Text("Stock Quantity") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = colors,
                        onValueChange = { colors = it },
                        label = { Text("Colors (comma separated)") },
                        placeholder = { Text("Gold, Silver, Multi") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = sizes,
                        onValueChange = { sizes = it },
                        label = { Text("Sizes (comma separated)") },
                        placeholder = { Text("8mm, 10mm") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    // Badges checkboxes
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isFeatured,
                            onCheckedChange = { isFeatured = it },
                            colors = CheckboxDefaults.colors(checkedColor = GoldPrimary)
                        )
                        Text("Featured Product", fontSize = 12.sp, color = BeadBrownDark)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isNewArrival,
                            onCheckedChange = { isNewArrival = it },
                            colors = CheckboxDefaults.colors(checkedColor = GoldPrimary)
                        )
                        Text("New Arrival", fontSize = 12.sp, color = BeadBrownDark)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isBestSeller,
                            onCheckedChange = { isBestSeller = it },
                            colors = CheckboxDefaults.colors(checkedColor = GoldPrimary)
                        )
                        Text("Best Seller", fontSize = 12.sp, color = BeadBrownDark)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = priceText.toLongOrNull() ?: 15000L
                    val stock = stockText.toIntOrNull() ?: 10
                    onSave(
                        product.copy(
                            name = name.trim(),
                            category = selectedCat,
                            price = price,
                            stock = stock,
                            colors = colors.ifBlank { "Multi" },
                            sizes = sizes.ifBlank { "Standard" },
                            isFeatured = isFeatured,
                            isNewArrival = isNewArrival,
                            isBestSeller = isBestSeller,
                            description = description.trim(),
                            updatedAt = System.currentTimeMillis()
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
            ) {
                Text("Save Product", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel", color = BeadBrown)
            }
        }
    )
}
