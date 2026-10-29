package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*

@Composable
fun DoctorsScreen(
    language: Language,
    onBookWithDoctor: (Doctor) -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = language == Language.ARABIC
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("doctors_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = if (isArabic) "نخبة الأطباء والاستشاريين" else "Medical Consultants & Faculty",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = BurgundyPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isArabic) "جميع أطباء مركز القاهرة الطبي هم أساتذة واستشاريون بكليات الطب في جامعتي عين شمس والقاهرة."
                           else "All doctors at Cairo Cosmo Center are distinguished medical professors and consultants from Ain Shams and Cairo Universities.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 22.sp
                    )
                )
            }
        }

        items(CosmoData.doctors) { doctor ->
            DoctorProfileCard(
                doctor = doctor,
                isArabic = isArabic,
                onBookClick = { onBookWithDoctor(doctor) },
                onWhatsAppClick = {
                    val message = if (isArabic) {
                        "مرحباً، أود الاستفسار عن أقرب موعد استشارة مع ${doctor.nameAr}"
                    } else {
                        "Hello, I would like to inquire about the next available appointment with ${doctor.nameEn}"
                    }
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://wa.me/201206666390?text=${Uri.encode(message)}")
                    )
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
private fun DoctorProfileCard(
    doctor: Doctor,
    isArabic: Boolean,
    onBookClick: () -> Unit,
    onWhatsAppClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("doctor_card_${doctor.slug}"),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, BorderLight),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Photo + Name + Experience
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.5.dp, GoldAccent, RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = doctor.imageResId),
                        contentDescription = if (isArabic) doctor.nameAr else doctor.nameEn,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isArabic) doctor.nameAr else doctor.nameEn,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BurgundyDark
                            )
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GoldAccent.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "★ ${doctor.rating}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GoldDark,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = if (isArabic) doctor.titleAr else doctor.titleEn,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = BurgundyPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = if (isArabic) doctor.experienceAr else doctor.experienceEn,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bio
            Text(
                text = if (isArabic) doctor.bioAr else doctor.bioEn,
                style = MaterialTheme.typography.bodySmall.copy(
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Working Days
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = null,
                    tint = BurgundyPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isArabic) "مواعيد العيادة: ${doctor.workingDaysAr}" else "Clinic Hours: ${doctor.workingDaysEn}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = BurgundyDark
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Available Branches Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = if (isArabic) "الفروع:" else "Branches:",
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                doctor.branches.forEach { bId ->
                    val branchObj = CosmoData.branches.find { it.id == bId }
                    if (branchObj != null) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BurgundyContainer.copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = if (isArabic) branchObj.titleAr else branchObj.titleEn,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = BurgundyPrimary,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onWhatsAppClick,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF25D366)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1E7E34)),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isArabic) "استفسار سريع" else "WhatsApp",
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Button(
                    onClick = onBookClick,
                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .testTag("book_with_doctor_btn")
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isArabic) "حجز استشارة" else "Book Consult",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
