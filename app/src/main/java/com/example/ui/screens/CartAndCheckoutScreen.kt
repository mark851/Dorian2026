package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItem
import com.example.data.model.Order
import com.example.ui.DoraViewModel
import com.example.ui.Screen
import com.example.ui.components.BeadCanvasPreview
import com.example.ui.components.formatUgx
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BeadBrown
import com.example.ui.theme.BeadBrownDark
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.WarmCardBeige

@Composable
fun CartScreen(
    viewModel: DoraViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cartItems by viewModel.cartItems.collectAsState()
    val subtotal = cartItems.sumOf { it.lineTotal }

    if (cartItems.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Your Cart is Empty",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = BeadBrownDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Explore our sparkling bag beads, crystals, and pearl strands to start creating.",
                    fontSize = 13.sp,
                    color = BeadBrown.copy(alpha = 0.7f),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { viewModel.navigateTo(Screen.Shop) },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Browse Shop", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your Cart (${cartItems.sumOf { it.quantity }} items)",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = BeadBrownDark
            )
        }

        // Cart Items List
        items(cartItems, key = { it.lineKey }) { item ->
            CartItemRow(
                item = item,
                onUpdateQty = { delta -> viewModel.updateCartQuantity(item.lineKey, delta) },
                onRemove = { viewModel.removeFromCart(item.lineKey) }
            )
        }

        // Summary Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Order Summary",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = BeadBrownDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", color = BeadBrown.copy(alpha = 0.8f))
                        Text(formatUgx(subtotal), fontWeight = FontWeight.Bold, color = BeadBrownDark)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Delivery fee will be calculated at checkout based on your district.",
                        fontSize = 11.sp,
                        color = BeadBrown.copy(alpha = 0.6f)
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = WarmCardBeige)

                    Button(
                        onClick = { viewModel.navigateTo(Screen.Checkout) },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Proceed to In-App Checkout", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { viewModel.openWhatsAppOrder(context, cartItems) },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Outlined.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Order on WhatsApp Hotline", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onUpdateQty: (Int) -> Unit,
    onRemove: () -> Unit
) {
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
            // Miniature bead visual
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(WarmCardBeige)
            ) {
                BeadCanvasPreview(
                    colors = listOf(item.selectedColor),
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.productName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = BeadBrownDark
                )
                Text(
                    text = "${item.selectedColor} · ${item.selectedSize}",
                    fontSize = 11.sp,
                    color = BeadBrown.copy(alpha = 0.7f)
                )
                Text(
                    text = formatUgx(item.unitPrice),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = GoldDark,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Stepper
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = { onUpdateQty(-1) },
                    modifier = Modifier
                        .size(28.dp)
                        .background(WarmCardBeige, CircleShape)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(14.dp))
                }
                Text(
                    text = "${item.quantity}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                IconButton(
                    onClick = { onUpdateQty(1) },
                    modifier = Modifier
                        .size(28.dp)
                        .background(WarmCardBeige, CircleShape)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(14.dp))
                }
                IconButton(
                    onClick = { onRemove() },
                    modifier = Modifier
                        .size(28.dp)
                        .padding(start = 4.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = AccentRed, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: DoraViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cartItems by viewModel.cartItems.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    var customerName by remember(currentUser) { mutableStateOf(currentUser?.fullName ?: "") }
    var phone by remember(currentUser) { mutableStateOf(currentUser?.phone ?: "") }
    var email by remember(currentUser) { mutableStateOf(currentUser?.email ?: "") }
    var selectedLocation by remember(currentUser) { mutableStateOf(currentUser?.defaultLocation ?: "Kampala") }
    var address by remember(currentUser) { mutableStateOf(currentUser?.defaultAddress ?: "") }
    var paymentMethod by remember { mutableStateOf("Mobile Money (MTN / Airtel Uganda)") }
    var notes by remember { mutableStateOf("") }

    var isLocationMenuExpanded by remember { mutableStateOf(false) }
    var isPaymentMenuExpanded by remember { mutableStateOf(false) }

    val subtotal = cartItems.sumOf { it.lineTotal }
    val deliveryFee = viewModel.deliveryLocations[selectedLocation] ?: 5000L
    val total = subtotal + deliveryFee

    val paymentOptions = listOf(
        "Mobile Money (MTN / Airtel Uganda)",
        "Cash on Delivery",
        "Bank Transfer"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = BeadBrown)
                }
                Text(
                    text = "Checkout & Delivery",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = BeadBrownDark
                )
            }
        }

        // Recipient Form
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Recipient Information",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = BeadBrownDark
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text("Full Name *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number (e.g. 0775803896) *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Location Dropdown
                    ExposedDropdownMenuBox(
                        expanded = isLocationMenuExpanded,
                        onExpandedChange = { isLocationMenuExpanded = !isLocationMenuExpanded }
                    ) {
                        OutlinedTextField(
                            value = "$selectedLocation — ${formatUgx(deliveryFee)}",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Delivery District *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isLocationMenuExpanded) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isLocationMenuExpanded,
                            onDismissRequest = { isLocationMenuExpanded = false }
                        ) {
                            viewModel.deliveryLocations.forEach { (loc, fee) ->
                                DropdownMenuItem(
                                    text = { Text("$loc — ${formatUgx(fee)}") },
                                    onClick = {
                                        selectedLocation = loc
                                        isLocationMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Delivery Address / Landmark *") },
                        placeholder = { Text("Street, zone, building name, or shop pickup") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Payment Method Dropdown
                    ExposedDropdownMenuBox(
                        expanded = isPaymentMenuExpanded,
                        onExpandedChange = { isPaymentMenuExpanded = !isPaymentMenuExpanded }
                    ) {
                        OutlinedTextField(
                            value = paymentMethod,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Payment Method") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPaymentMenuExpanded) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isPaymentMenuExpanded,
                            onDismissRequest = { isPaymentMenuExpanded = false }
                        ) {
                            paymentOptions.forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt) },
                                    onClick = {
                                        paymentMethod = opt
                                        isPaymentMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes / Special Instructions (Optional)") },
                        placeholder = { Text("Color preferences, timing, custom requests...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Items and Total Recap
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Order Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = BeadBrownDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    cartItems.forEach { i ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${i.productName} (${i.selectedColor}, ${i.selectedSize}) x${i.quantity}",
                                fontSize = 12.sp,
                                color = BeadBrown.copy(alpha = 0.85f),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = formatUgx(i.lineTotal),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = BeadBrownDark
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = WarmCardBeige)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", fontSize = 13.sp, color = BeadBrown)
                        Text(formatUgx(subtotal), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Delivery ($selectedLocation)", fontSize = 13.sp, color = BeadBrown)
                        Text(formatUgx(deliveryFee), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Grand Total to Pay", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = BeadBrownDark)
                        Text(formatUgx(total), fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, color = GoldDark)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.placeOrder(
                                customerName = customerName,
                                phone = phone,
                                email = email,
                                location = selectedLocation,
                                address = address,
                                paymentMethod = paymentMethod,
                                notes = notes
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Confirm Order", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            viewModel.openWhatsAppOrder(context, cartItems, customerName, selectedLocation)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Outlined.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Complete on WhatsApp", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun OrderConfirmedScreen(
    order: Order,
    viewModel: DoraViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = AccentGreen,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Order Confirmed!",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = BeadBrownDark
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Thank you, ${order.customerName}. Your order is recorded in realtime.",
                    fontSize = 13.sp,
                    color = BeadBrown.copy(alpha = 0.8f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = WarmCardBeige,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "ORDER REFERENCE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldDark
                        )
                        Text(
                            text = order.orderNo,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = BeadBrownDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Total: ${formatUgx(order.total)} (${order.paymentMethod})", fontSize = 12.sp)
                        Text("Delivery: ${order.location} (${order.address})", fontSize = 12.sp)
                        Text("Phone: ${order.phone}", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { viewModel.navigateTo(Screen.Shop) },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continue Shopping", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
