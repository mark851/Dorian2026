package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.DoraViewModel
import com.example.ui.Screen
import com.example.ui.components.StoreAnnouncementBar
import com.example.ui.components.StoreBottomBar
import com.example.ui.components.StoreTopBar
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.AdminPortalScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LiveDomainScreen
import com.example.ui.screens.OrderConfirmedScreen
import com.example.ui.screens.ReviewsScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                DoraFashionsApp()
            }
        }
    }
}

@Composable
fun DoraFashionsApp(
    viewModel: DoraViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val banners by viewModel.activeBanners.collectAsState()
    val isAdminAuth by viewModel.isAdminAuthenticated.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Listen to snackbar messages
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Handle Hardware/Gesture Back Navigation
    BackHandler(enabled = currentScreen !is Screen.Home) {
        viewModel.navigateBack()
    }

    val isCustomerBrowsing = currentScreen !is Screen.AdminPortal && currentScreen !is Screen.Checkout

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Column {
                StoreAnnouncementBar(
                    banners = banners,
                    onDomainClick = { viewModel.navigateTo(Screen.LiveDomain) }
                )
                StoreTopBar(
                    cartCount = cartItems.sumOf { it.quantity },
                    isUserLoggedIn = currentUser != null,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        },
        bottomBar = {
            if (currentScreen !is Screen.AdminPortal && currentScreen !is Screen.Checkout) {
                StoreBottomBar(
                    currentScreen = currentScreen,
                    cartCount = cartItems.sumOf { it.quantity },
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        },
        floatingActionButton = {
            // Floating WhatsApp Button for fast customer ordering on browsing screens
            AnimatedVisibility(
                visible = isCustomerBrowsing,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    onClick = { viewModel.openWhatsAppOrder(context, cartItems) },
                    containerColor = AccentGreen,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Chat,
                        contentDescription = "Order on WhatsApp Hotline",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            when (val screen = currentScreen) {
                is Screen.Home -> HomeScreen(viewModel = viewModel)
                is Screen.Shop -> ShopScreen(viewModel = viewModel)
                is Screen.Reviews -> ReviewsScreen(viewModel = viewModel)
                is Screen.Contact -> ContactScreen(viewModel = viewModel)
                is Screen.Cart -> CartScreen(viewModel = viewModel)
                is Screen.Checkout -> CheckoutScreen(viewModel = viewModel)
                is Screen.OrderConfirmed -> OrderConfirmedScreen(order = screen.order, viewModel = viewModel)
                is Screen.Account -> AccountScreen(viewModel = viewModel)
                is Screen.LiveDomain -> LiveDomainScreen(viewModel = viewModel)
                is Screen.AdminPortal -> AdminPortalScreen(viewModel = viewModel)
            }
        }
    }
}
