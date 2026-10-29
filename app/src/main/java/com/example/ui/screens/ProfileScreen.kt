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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    language: Language,
    userProfile: UserProfile?,
    matchedAppointments: List<Appointment>,
    onOpenAuth: () -> Unit,
    onOpenEditProfile: () -> Unit,
    onSignOut: () -> Unit,
    onBookAppointment: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = language == Language.ARABIC
    val context = LocalContext.current

    val totalBookings = matchedAppointments.size
    val isGoldVip = totalBookings >= 3
    val isSilver = totalBookings in 1..2
    val vipProgress = (totalBookings.toFloat() / 3f).coerceIn(0f, 1f)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (userProfile == null) {
            // Unauthenticated Guest View
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BurgundyContainer),
                    border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(BurgundyDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(38.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (isArabic) "حساب العميل والمتابعة الطبية" else "Patient Account & Follow-up",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = BurgundyDark
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isArabic) "سجلي دخولك بحساب Google لمزامنة مواعيدك، الحصول على شارة العميل المميز، وتلقي العروض الحصرية."
                                   else "Sign in with Google to sync appointments, earn the VIP Client badge, and receive exclusive offers.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 21.sp
                            ),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = onOpenAuth,
                            colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("profile_login_btn")
                        ) {
                            Icon(Icons.Default.Login, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isArabic) "تسجيل الدخول / حساب جديد" else "Sign In / Register",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        } else {
            // 1. Authenticated User Profile Summary Card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, BorderLight),
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(BurgundyDark)
                                    .border(2.dp, GoldAccent, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userProfile.fullName.take(1).uppercase(),
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        color = GoldAccent,
                                        fontWeight = FontWeight.Bold
                                    )
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
                                        text = userProfile.fullName,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BurgundyDark
                                        ),
                                        maxLines = 1
                                    )

                                    // Badge Chip based on actual DB appointments
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = when {
                                            isGoldVip -> GoldAccent.copy(alpha = 0.25f)
                                            isSilver -> Color(0xFFE0E0E0)
                                            else -> BurgundyContainer
                                        }
                                    ) {
                                        Text(
                                            text = when {
                                                isGoldVip -> if (isArabic) "عميل مميز ذهبي ★" else "Gold VIP Client ★"
                                                isSilver -> if (isArabic) "عميل فضي ✦" else "Silver Client ✦"
                                                else -> if (isArabic) "عميل جديد" else "New Client"
                                            },
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (isGoldVip) GoldDark else BurgundyPrimary,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = userProfile.email,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    maxLines = 1
                                )

                                if (userProfile.phone.isNotEmpty()) {
                                    Text(
                                        text = userProfile.phone,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = BurgundyPrimary,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Custom Preferences Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val branchObj = CosmoData.branches.find { it.id == userProfile.preferredBranchId }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = BurgundyPrimary, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isArabic) branchObj?.titleAr ?: "الشيراتون" else branchObj?.titleEn ?: "Sheraton",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                        maxLines = 1
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (userProfile.marketingConsent) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (userProfile.marketingConsent) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isArabic) "عروض الواتساب مفعلة" else "WhatsApp Opt-in",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            color = if (userProfile.marketingConsent) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Edit / Customize Profile Button
                        OutlinedButton(
                            onClick = onOpenEditProfile,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp)
                                .testTag("customize_profile_btn")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isArabic) "تخصيص بياناتي وتفضيلاتي الطبية" else "Customize My Profile & Preferences",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            // 2. VIP BADGE CRITERIA & PROGRESS CARD
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isGoldVip) GoldContainer.copy(alpha = 0.5f) else BurgundyContainer.copy(alpha = 0.4f)
                    ),
                    border = BorderStroke(1.5.dp, if (isGoldVip) GoldAccent else BurgundyLight.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isGoldVip) GoldDark else BurgundyPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WorkspacePremium,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isArabic) "شروط شارة العميل المميز (VIP)" else "VIP Client Badge Criteria",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = if (isArabic) "مطابقة تلقائية بناءً على مواعيدك بقاعدة البيانات" else "Dynamically matched from database bookings",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Progress to Gold VIP
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isGoldVip) {
                                    if (isArabic) "مبروك! حققت شارة العميل المميز الذهبية ★" else "Unlocked Gold VIP Status! ★"
                                } else {
                                    if (isArabic) "التقدم للشارة الذهبية: $totalBookings من 3 مواعيد" else "Progress to Gold VIP: $totalBookings of 3 bookings"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isGoldVip) GoldDark else BurgundyDark
                                )
                            )
                            Text(
                                text = "${(vipProgress * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { vipProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (isGoldVip) GoldDark else BurgundyPrimary,
                            trackColor = BorderLight
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // VIP Perks / Requirements explanation
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            VipConditionRow(
                                title = if (isArabic) "حجز 3 مواعيد أو جلسات تجميلية مؤكدة" else "Book 3 confirmed clinic treatments",
                                isMet = isGoldVip
                            )
                            VipConditionRow(
                                title = if (isArabic) "خصم حصري 15% على جميع جلسات الليزر والبوتوكس" else "15% exclusive discount on laser & botox",
                                isMet = isGoldVip
                            )
                            VipConditionRow(
                                title = if (isArabic) "أولوية الحجز مع أساتذة جراحة التجميل" else "Priority booking with Senior Professors",
                                isMet = isGoldVip
                            )
                        }
                    }
                }
            }

            // 3. User Matched Appointments List
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isArabic) "مواعيدك المسجلة بقاعدة البيانات (${matchedAppointments.size})"
                               else "Your Database Bookings (${matchedAppointments.size})",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BurgundyPrimary
                        )
                    )
                    TextButton(onClick = onBookAppointment) {
                        Text(
                            text = if (isArabic) "+ حجز موعد جديد" else "+ Book Appointment",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            if (matchedAppointments.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.EventNote, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (isArabic) "لم تسجل أي مواعيد بحسابك حتى الآن" else "No appointments registered under your account yet",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = onBookAppointment,
                                colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(if (isArabic) "احجز موعدك الأول" else "Book First Appointment")
                            }
                        }
                    }
                }
            } else {
                items(matchedAppointments) { appt ->
                    AppointmentCard(
                        appointment = appt,
                        isArabic = isArabic,
                        onCancel = {},
                        onWhatsApp = {
                            val msg = "مرحباً مركز القاهرة الطبي، بخصوص موعدي رقم ${appt.refCode}"
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://wa.me/201206666390?text=${Uri.encode(msg)}")
                            )
                            context.startActivity(intent)
                        }
                    )
                }
            }

            // 4. Sign Out & Direct WhatsApp Assistance
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val msg = "مرحباً، أود التحدث مع خدمة عملاء مركز القاهرة الطبي"
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://wa.me/201206666390?text=${Uri.encode(msg)}")
                            )
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, tint = Color(0xFF25D366), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "WhatsApp")
                    }

                    OutlinedButton(
                        onClick = onSignOut,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
                        modifier = Modifier.weight(1f).testTag("sign_out_btn")
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (isArabic) "تسجيل الخروج" else "Sign Out")
                    }
                }
            }
        }
    }
}

@Composable
private fun VipConditionRow(title: String, isMet: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (isMet) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isMet) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                color = if (isMet) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = if (isMet) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}
