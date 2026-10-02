package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Order
import com.example.ui.DoraViewModel
import com.example.ui.components.formatUgx
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.BeadBrown
import com.example.ui.theme.BeadBrownDark
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.WarmCardBeige

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    viewModel: DoraViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Sign In, 1 = Sign Up

    // Login Form State
    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }

    // Sign Up Form State
    var suName by remember { mutableStateOf("") }
    var suEmail by remember { mutableStateOf("") }
    var suPhone by remember { mutableStateOf("") }
    var suAddress by remember { mutableStateOf("") }
    var suLocation by remember { mutableStateOf("Kampala") }
    var suPassword by remember { mutableStateOf("") }
    var isLocDropdownOpen by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (currentUser != null) "Customer Profile" else "Customer Account",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = BeadBrownDark
            )
            Text(
                text = if (currentUser != null) "Manage your orders and saved delivery preferences" else "Sign in or create an account for fast checkout and order tracking",
                fontSize = 12.sp,
                color = BeadBrown.copy(alpha = 0.75f)
            )
        }

        if (currentUser != null) {
            val user = currentUser!!
            val myOrders = allOrders.filter { it.email.equals(user.email, ignoreCase = true) || it.phone == user.phone }

            // Logged in Profile Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = WarmCardBeige,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(28.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(user.fullName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BeadBrownDark)
                                Text(user.email, fontSize = 12.sp, color = BeadBrown.copy(alpha = 0.7f))
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = WarmCardBeige)

                        Text("Phone: ${user.phone.ifEmpty { "Not specified" }}", fontSize = 12.sp, color = BeadBrownDark)
                        Text("Default District: ${user.defaultLocation}", fontSize = 12.sp, color = BeadBrownDark)
                        Text("Address: ${user.defaultAddress.ifEmpty { "Not specified" }}", fontSize = 12.sp, color = BeadBrownDark)

                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedButton(
                            onClick = { viewModel.logout() },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Sign Out", color = BeadBrown, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Customer Orders List
            item {
                Text(
                    text = "My Order History (${myOrders.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BeadBrownDark
                )
            }

            if (myOrders.isEmpty()) {
                item {
                    Text(
                        text = "You haven't placed any orders yet.",
                        fontSize = 13.sp,
                        color = BeadBrown.copy(alpha = 0.7f)
                    )
                }
            } else {
                items(myOrders, key = { it.orderNo }) { order ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(order.orderNo, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GoldDark)
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = WarmCardBeige
                                ) {
                                    Text(
                                        text = order.status,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BeadBrownDark,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(order.itemsSummary, fontSize = 12.sp, color = BeadBrown.copy(alpha = 0.85f))
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total: ${formatUgx(order.total)}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BeadBrownDark)
                                Text(order.paymentMethod, fontSize = 11.sp, color = BeadBrown.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }
        } else {
            // Tab Selector
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = WarmCardBeige,
                    contentColor = BeadBrown,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = GoldPrimary
                        )
                    },
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Sign In", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Create Account", fontWeight = FontWeight.Bold) }
                    )
                }
            }

            // Auth Forms
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (selectedTab == 0) {
                            // Sign In Form
                            Text("Welcome Back", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BeadBrownDark)
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = loginEmail,
                                onValueChange = { loginEmail = it },
                                label = { Text("Email Address") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = loginPassword,
                                onValueChange = { loginPassword = it },
                                label = { Text("Password") },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { viewModel.login(loginEmail, loginPassword) },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Sign In", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            // Sign Up Form
                            Text("Create Customer Account", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BeadBrownDark)
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = suName,
                                onValueChange = { suName = it },
                                label = { Text("Full Name *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = suEmail,
                                onValueChange = { suEmail = it },
                                label = { Text("Email Address *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = suPhone,
                                onValueChange = { suPhone = it },
                                label = { Text("Phone Number (e.g. 0775803896)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            ExposedDropdownMenuBox(
                                expanded = isLocDropdownOpen,
                                onExpandedChange = { isLocDropdownOpen = !isLocDropdownOpen }
                            ) {
                                OutlinedTextField(
                                    value = suLocation,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Default District") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isLocDropdownOpen) },
                                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = isLocDropdownOpen,
                                    onDismissRequest = { isLocDropdownOpen = false }
                                ) {
                                    viewModel.deliveryLocations.keys.forEach { loc ->
                                        DropdownMenuItem(
                                            text = { Text(loc) },
                                            onClick = {
                                                suLocation = loc
                                                isLocDropdownOpen = false
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = suAddress,
                                onValueChange = { suAddress = it },
                                label = { Text("Delivery Address / Landmark") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = suPassword,
                                onValueChange = { suPassword = it },
                                label = { Text("Password (Min 4 chars) *") },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    viewModel.signUp(suName, suEmail, suPhone, suAddress, suLocation, suPassword)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Create Account", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun LiveDomainScreen(
    viewModel: DoraViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val liveUrl = viewModel.repository.liveDomainUrl

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Live Custom Domain & Hosting",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = BeadBrownDark
            )
            Text(
                text = "Dora Fashions is deployed live online. Anyone worldwide can access and shop from this web address.",
                fontSize = 12.sp,
                color = BeadBrown.copy(alpha = 0.75f)
            )
        }

        // Live Host Status Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PRIMARY LIVE HOST",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldDark,
                            letterSpacing = 0.5.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(AccentGreen, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Live & Active", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGreen)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = WarmCardBeige,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = liveUrl,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = BeadBrownDark,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                cm.setPrimaryClip(ClipData.newPlainText("Dora Fashions Domain", liveUrl))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy URL", fontSize = 12.sp)
                        }

                        Button(
                            onClick = { viewModel.openWebsiteInBrowser(context, liveUrl) },
                            colors = ButtonDefaults.buttonColors(containerColor = BeadBrown),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open in Web", fontSize = 12.sp)
                        }

                        IconButton(
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "Shop beads, pearls & bag accessories at Dora Fashions: $liveUrl")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Dora Fashions"))
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .background(WarmCardBeige, RoundedCornerShape(10.dp))
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = BeadBrown)
                        }
                    }
                }
            }
        }

        // Install on iPhone & Android Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📱 Running on Android & iPhone",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = BeadBrownDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Dora Fashions is built to run smoothly on both Android and Apple iOS devices:",
                        fontSize = 12.sp,
                        color = BeadBrown.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    DomainStepItem("🤖 For Android Phones", "Download the native APK package directly from the top-right Settings menu (Download APK) or install through your mobile browser with 'Add to Home Screen'.")
                    DomainStepItem("🍏 For iPhones (iOS)", "Open the live store link in Safari on any iPhone, tap the Share button (square with arrow up), and tap 'Add to Home Screen'. It runs in full-screen standalone mode like a native iOS app with offline caching and instant cloud updates.")
                    DomainStepItem("☁️ Unified Cloud Catalog", "Both Android and iPhone users see newly posted products and real-time inventory instantly as you update them from the Admin Portal.")
                }
            }
        }

        // Custom Domain Instructions Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Custom Domain Configuration",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = BeadBrownDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "To point your own brand domain (e.g. dorafashions.com):",
                        fontSize = 12.sp,
                        color = BeadBrown.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    DomainStepItem("1. Register Domain", "Purchase 'dorafashions.com' on Namecheap, GoDaddy or Google Domains.")
                    DomainStepItem("2. Configure DNS Record", "Add a CNAME record with Host '@' or 'www' pointing to 'ghs.googlehosted.com'.")
                    DomainStepItem("3. Automatic SSL Security", "Google Cloud automatically provisions free managed SSL certificates within minutes.")
                    DomainStepItem("4. Realtime Synchronization", "All product additions, price updates, and inventory changes from your Admin Panel sync immediately to your live domain.")
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DomainStepItem(step: String, detail: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(step, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GoldDark)
        Text(detail, fontSize = 11.sp, color = BeadBrown.copy(alpha = 0.75f), lineHeight = 15.sp)
    }
}
