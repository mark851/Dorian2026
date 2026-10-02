package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Review
import com.example.ui.DoraViewModel
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.BeadBrown
import com.example.ui.theme.BeadBrownDark
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.WarmCardBeige

@Composable
fun ReviewsScreen(
    viewModel: DoraViewModel,
    modifier: Modifier = Modifier
) {
    val reviews by viewModel.approvedReviews.collectAsState()

    var reviewerName by remember { mutableStateOf("") }
    var reviewRating by remember { mutableIntStateOf(5) }
    var reviewComment by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Customer Reviews",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = BeadBrownDark
            )
            Text(
                text = "Authentic feedback from bag artisans and jewelry makers across Uganda.",
                fontSize = 12.sp,
                color = BeadBrown.copy(alpha = 0.75f)
            )
        }

        // Write a review card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Write a Review",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = BeadBrownDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = reviewerName,
                        onValueChange = { reviewerName = it },
                        label = { Text("Your Name *") },
                        placeholder = { Text("e.g. Sarah K.") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Star Rating Picker
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Rating: ", fontSize = 13.sp, color = BeadBrown, fontWeight = FontWeight.SemiBold)
                        for (i in 1..5) {
                            IconButton(
                                onClick = { reviewRating = i },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(
                                    imageVector = if (i <= reviewRating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                    contentDescription = "Rating $i",
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = reviewComment,
                        onValueChange = { reviewComment = it },
                        label = { Text("Your Experience / Comments *") },
                        placeholder = { Text("Tell us how your beaded bags turned out...") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            viewModel.submitReview(reviewerName, reviewRating, reviewComment)
                            reviewerName = ""
                            reviewComment = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Submit Review", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Reviews list
        item {
            Text(
                text = "Community Feedback (${reviews.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = BeadBrownDark
            )
        }

        items(reviews, key = { it.id }) { rev ->
            Card(
                shape = RoundedCornerShape(14.dp),
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
                        Text(
                            text = rev.customerName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = BeadBrownDark
                        )
                        Row {
                            repeat(rev.rating) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "“${rev.comment}”",
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = BeadBrown.copy(alpha = 0.9f)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ContactScreen(
    viewModel: DoraViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var inquiryName by remember { mutableStateOf("") }
    var inquiryContact by remember { mutableStateOf("") }
    var inquiryMessage by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Contact & Studio",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = BeadBrownDark
            )
            Text(
                text = "Reach our Kampala bead studio or send a custom handbag design request.",
                fontSize = 12.sp,
                color = BeadBrown.copy(alpha = 0.75f)
            )
        }

        // Contact Info Box
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Store Details", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BeadBrownDark)
                    Spacer(modifier = Modifier.height(12.dp))

                    ContactRow(
                        icon = Icons.Outlined.Chat,
                        title = "WhatsApp Hotline",
                        subtitle = "+256 775 803 896 (Fast orders & photos)",
                        onClick = { viewModel.openWhatsAppOrder(context, emptyList()) },
                        actionColor = AccentGreen
                    )

                    ContactRow(
                        icon = Icons.Default.Phone,
                        title = "Phone Calls",
                        subtitle = "+256 775 803 896",
                        onClick = { viewModel.dialStoreHotline(context) },
                        actionColor = BeadBrown
                    )

                    ContactRow(
                        icon = Icons.Default.Email,
                        title = "Email Support",
                        subtitle = "Namatakadoreen89@gmail.com",
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:Namatakadoreen89@gmail.com"))
                            context.startActivity(intent)
                        },
                        actionColor = GoldDark
                    )

                    ContactRow(
                        icon = Icons.Default.LocationOn,
                        title = "Location",
                        subtitle = "Kampala, Uganda (Doorstep delivery nationwide)",
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=Kampala+Uganda"))
                            context.startActivity(intent)
                        },
                        actionColor = BeadBrown
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "🕘 Opening Hours: Mon-Sat 8:00am - 7:00pm · Sun 10:00am - 4:00pm",
                        fontSize = 11.sp,
                        color = BeadBrown.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Send Inquiry Form
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Send Inquiry or Custom Bead Request",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = BeadBrownDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = inquiryName,
                        onValueChange = { inquiryName = it },
                        label = { Text("Your Name *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inquiryContact,
                        onValueChange = { inquiryContact = it },
                        label = { Text("Phone or Email *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inquiryMessage,
                        onValueChange = { inquiryMessage = it },
                        label = { Text("Message / Bead Requirements *") },
                        placeholder = { Text("Describe bead colors, sizes, or custom handbag designs...") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            viewModel.sendInquiry(inquiryName, inquiryContact, inquiryMessage)
                            inquiryName = ""
                            inquiryContact = ""
                            inquiryMessage = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Send Message", fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ContactRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    actionColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = WarmCardBeige,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = actionColor, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BeadBrownDark)
            Text(subtitle, fontSize = 11.sp, color = BeadBrown.copy(alpha = 0.75f))
        }
    }
}
