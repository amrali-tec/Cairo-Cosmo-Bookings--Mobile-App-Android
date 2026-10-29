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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*

@Composable
fun AdminPortalScreen(
    language: Language,
    appointments: List<Appointment>,
    registeredPatients: List<UserProfile> = emptyList(),
    onUpdateStatus: (String, AppointmentStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = language == Language.ARABIC
    val context = LocalContext.current

    var selectedAdminTab by remember { mutableStateOf(0) } // 0: Appointments, 1: Marketing CRM Database

    // Appointments Tab State
    var selectedFilterStatus by remember { mutableStateOf<AppointmentStatus?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Marketing CRM State
    var marketingSearchQuery by remember { mutableStateOf("") }
    var marketingFilterOnlyVip by remember { mutableStateOf(false) }
    var marketingFilterConsentOnly by remember { mutableStateOf(true) }

    val filteredAppointments = remember(appointments, selectedFilterStatus, searchQuery) {
        appointments.filter { appt ->
            val matchStatus = selectedFilterStatus == null || appt.status == selectedFilterStatus
            val matchSearch = searchQuery.isBlank() ||
                    appt.customerName.contains(searchQuery, ignoreCase = true) ||
                    appt.customerPhone.contains(searchQuery, ignoreCase = true) ||
                    appt.refCode.contains(searchQuery, ignoreCase = true)
            matchStatus && matchSearch
        }
    }

    val filteredPatients = remember(registeredPatients, marketingSearchQuery, marketingFilterOnlyVip, marketingFilterConsentOnly) {
        registeredPatients.filter { patient ->
            val matchVip = !marketingFilterOnlyVip || patient.vipTier == "gold_vip"
            val matchConsent = !marketingFilterConsentOnly || patient.marketingConsent
            val matchSearch = marketingSearchQuery.isBlank() ||
                    patient.fullName.contains(marketingSearchQuery, ignoreCase = true) ||
                    patient.phone.contains(marketingSearchQuery, ignoreCase = true) ||
                    patient.email.contains(marketingSearchQuery, ignoreCase = true)
            matchVip && matchConsent && matchSearch
        }
    }

    val totalCount = appointments.size
    val confirmedCount = appointments.count { it.status == AppointmentStatus.CONFIRMED }
    val pendingCount = appointments.count { it.status == AppointmentStatus.PENDING }
    val completedCount = appointments.count { it.status == AppointmentStatus.COMPLETED }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("admin_portal_screen")
    ) {
        // Admin Navigation Tabs
        TabRow(
            selectedTabIndex = selectedAdminTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = BurgundyPrimary
        ) {
            Tab(
                selected = selectedAdminTab == 0,
                onClick = { selectedAdminTab = 0 },
                text = {
                    Text(
                        text = if (isArabic) "إدارة المواعيد ($totalCount)" else "Bookings ($totalCount)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                },
                icon = { Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = selectedAdminTab == 1,
                onClick = { selectedAdminTab = 1 },
                text = {
                    Text(
                        text = if (isArabic) "قاعدة بيانات التسويق (${registeredPatients.size})" else "Marketing CRM (${registeredPatients.size})",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                },
                icon = { Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("admin_tab_crm")
            )
        }

        if (selectedAdminTab == 0) {
            // TAB 1: APPOINTMENTS MANAGEMENT
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Summary Metric Cards
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AdminMetricCard(
                            title = if (isArabic) "الإجمالي" else "Total",
                            count = totalCount,
                            color = BurgundyPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        AdminMetricCard(
                            title = if (isArabic) "مؤكد" else "Confirmed",
                            count = confirmedCount,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.weight(1f)
                        )
                        AdminMetricCard(
                            title = if (isArabic) "قيد المراجعة" else "Pending",
                            count = pendingCount,
                            color = Color(0xFFE65100),
                            modifier = Modifier.weight(1f)
                        )
                        AdminMetricCard(
                            title = if (isArabic) "مكتمل" else "Done",
                            count = completedCount,
                            color = Color(0xFF1565C0),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                text = if (isArabic) "بحث بالاسم، رقم الهاتف، أو كود الحجز..." else "Search by name, phone, or ref code...",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // Filter Pills
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = selectedFilterStatus == null,
                            onClick = { selectedFilterStatus = null },
                            label = { Text(if (isArabic) "الكل" else "All") }
                        )
                        AppointmentStatus.values().forEach { st ->
                            FilterChip(
                                selected = selectedFilterStatus == st,
                                onClick = { selectedFilterStatus = st },
                                label = { Text(if (isArabic) st.labelAr else st.labelEn) }
                            )
                        }
                    }
                }

                // Appointments List
                if (filteredAppointments.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isArabic) "لا توجد مواعيد مطابقة لهذا الفلتر" else "No matching appointments",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(filteredAppointments) { appt ->
                        AdminAppointmentCard(
                            appointment = appt,
                            isArabic = isArabic,
                            onStatusChange = { newStatus -> onUpdateStatus(appt.id, newStatus) },
                            onCallCustomer = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${appt.customerPhone}"))
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        } else {
            // TAB 2: PATIENTS & WHATSAPP MARKETING CRM
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header info
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = BurgundyContainer,
                        border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Campaign, contentDescription = null, tint = BurgundyPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isArabic) "قاعدة بيانات العملاء والتسويق الرقمي" else "Patient Marketing & Outreach CRM",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BurgundyDark
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isArabic) "قاعدة بيانات مركزية تشمل إيميلات وأرقام واتساب المرضى لإرسال العروض الترويجية والخصومات المخصصة."
                                       else "Centralized repository of patient emails and WhatsApp numbers for targeted promotional campaigns.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // Stats row
                item {
                    val consentCount = registeredPatients.count { it.marketingConsent }
                    val vipCount = registeredPatients.count { it.vipTier == "gold_vip" }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AdminMetricCard(
                            title = if (isArabic) "العملاء المسجلين" else "Registered",
                            count = registeredPatients.size,
                            color = BurgundyPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        AdminMetricCard(
                            title = if (isArabic) "مشتركو الواتساب" else "WhatsApp Opt-ins",
                            count = consentCount,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.weight(1f)
                        )
                        AdminMetricCard(
                            title = if (isArabic) "عملاء VIP" else "VIP Clients",
                            count = vipCount,
                            color = GoldDark,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = marketingSearchQuery,
                        onValueChange = { marketingSearchQuery = it },
                        placeholder = {
                            Text(
                                text = if (isArabic) "بحث بالاسم، رقم الواتساب، أو الإيميل..." else "Search by name, WhatsApp, or email...",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // Filter Chips
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = marketingFilterConsentOnly,
                            onClick = { marketingFilterConsentOnly = !marketingFilterConsentOnly },
                            label = { Text(if (isArabic) "موافقو الواتساب فقط" else "WhatsApp Opt-ins Only") },
                            leadingIcon = {
                                if (marketingFilterConsentOnly) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                        FilterChip(
                            selected = marketingFilterOnlyVip,
                            onClick = { marketingFilterOnlyVip = !marketingFilterOnlyVip },
                            label = { Text(if (isArabic) "عملاء VIP فقط ★" else "VIP Only ★") },
                            leadingIcon = {
                                if (marketingFilterOnlyVip) Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                    }
                }

                // Patients Marketing Cards
                if (filteredPatients.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isArabic) "لا توجد نتائج مطابقة في قاعدة البيانات" else "No matching patients found",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(filteredPatients) { patient ->
                        PatientMarketingCard(
                            patient = patient,
                            isArabic = isArabic,
                            onSendWhatsAppCampaign = {
                                val branchObj = CosmoData.branches.find { it.id == patient.preferredBranchId }
                                val promoMsg = if (isArabic) {
                                    "مرحباً أستاذ/ة ${patient.fullName} 🌸، يسعدنا في مركز القاهرة الطبي (${branchObj?.titleAr ?: "فرع الشيراتون"}) إهداءك كود خصم حصري 20% (كود: COSMO20) على جلسات التجميل والليزر هذا الأسبوع!"
                                } else {
                                    "Hello ${patient.fullName} 🌸, Cairo Cosmo Center is pleased to offer you an exclusive 20% discount (Code: COSMO20) on aesthetic sessions this week!"
                                }
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://wa.me/${patient.phone.replace("+", "").replace(" ", "")}?text=${Uri.encode(promoMsg)}")
                                )
                                context.startActivity(intent)
                            },
                            onSendEmailCampaign = {
                                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:${patient.email}")
                                    putExtra(Intent.EXTRA_SUBJECT, if (isArabic) "عرض خاص وحصري من مركز القاهرة الطبي" else "Exclusive Offer from Cairo Cosmo Center")
                                    putExtra(Intent.EXTRA_TEXT, if (isArabic) "عزيزي/تي ${patient.fullName}، يسعدنا دعوتك للاستفادة من أحدث عروض الموسم في مركز القاهرة الطبي." else "Dear ${patient.fullName}, you are invited to experience our newest seasonal offers at Cairo Cosmo Center.")
                                }
                                context.startActivity(emailIntent)
                            },
                            onCallPatient = {
                                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${patient.phone}"))
                                context.startActivity(dialIntent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PatientMarketingCard(
    patient: UserProfile,
    isArabic: Boolean,
    onSendWhatsAppCampaign: () -> Unit,
    onSendEmailCampaign: () -> Unit,
    onCallPatient: () -> Unit
) {
    val isGoldVip = patient.vipTier == "gold_vip"
    val branchObj = CosmoData.branches.find { it.id == patient.preferredBranchId }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, if (isGoldVip) GoldAccent else BorderLight),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isGoldVip) GoldDark else BurgundyPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = patient.fullName.take(1).uppercase().ifBlank { "C" },
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = patient.fullName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isArabic) "${patient.totalBookingsCount} مواعيد مسجلة" else "${patient.totalBookingsCount} bookings recorded",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isGoldVip) GoldAccent.copy(alpha = 0.25f) else BurgundyContainer
                ) {
                    Text(
                        text = if (isGoldVip) "VIP ★" else if (patient.totalBookingsCount > 0) "Silver ✦" else "Standard",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isGoldVip) GoldDark else BurgundyPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Contact Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Phone, contentDescription = null, tint = BurgundyPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = patient.phone.ifBlank { "لم يحدد هاتف" },
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Icon(Icons.Default.Email, contentDescription = null, tint = BurgundyPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = patient.email.ifBlank { "لم يحدد إيميل" },
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Preferred branch & marketing opt-in status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isArabic) "الفرع المفضل: ${branchObj?.titleAr ?: "الشيراتون"}" else "Branch: ${branchObj?.titleEn ?: "Sheraton"}",
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                if (patient.marketingConsent) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Text(
                            text = if (isArabic) "موافق على رسائل الواتساب ✓" else "WhatsApp Opted In ✓",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF2E7D32),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Marketing Campaign Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onSendWhatsAppCampaign,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.weight(1.3f).height(32.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isArabic) "إرسال عرض واتساب" else "Send WhatsApp Promo",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                OutlinedButton(
                    onClick = onSendEmailCampaign,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.weight(1f).height(32.dp)
                ) {
                    Icon(Icons.Default.Mail, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isArabic) "إيميل" else "Email",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                IconButton(
                    onClick = onCallPatient,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Call", tint = BurgundyPrimary, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun AdminMetricCard(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$count",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = color
                ),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun AdminAppointmentCard(
    appointment: Appointment,
    isArabic: Boolean,
    onStatusChange: (AppointmentStatus) -> Unit,
    onCallCustomer: () -> Unit
) {
    val serviceObj = CosmoData.services.find { it.id == appointment.serviceId }
    val branchObj = CosmoData.branches.find { it.id == appointment.branchId }
    val doctorObj = CosmoData.doctors.find { it.id == appointment.doctorId }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, BorderLight),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = appointment.refCode,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BurgundyPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = appointment.customerName,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                IconButton(
                    onClick = onCallCustomer,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = "Call Patient",
                        tint = BurgundyPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${appointment.customerPhone} • ${if (isArabic) branchObj?.titleAr ?: "" else branchObj?.titleEn ?: ""}",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${if (isArabic) serviceObj?.titleAr ?: "" else serviceObj?.titleEn ?: ""} (${appointment.date} - ${appointment.timeSlot})",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            if (doctorObj != null) {
                Text(
                    text = if (isArabic) "مع: ${doctorObj.nameAr}" else "With: ${doctorObj.nameEn}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = BurgundyPrimary,
                        fontSize = 11.sp
                    )
                )
            }

            if (appointment.notes.isNotEmpty()) {
                Text(
                    text = if (isArabic) "ملاحظة: ${appointment.notes}" else "Note: ${appointment.notes}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Status Modifier Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppointmentStatus.values().forEach { st ->
                    val isCurrent = apptStatusMatches(appointment.status, st)
                    val (activeBg, activeFg) = when (st) {
                        AppointmentStatus.CONFIRMED -> Color(0xFF2E7D32) to Color.White
                        AppointmentStatus.PENDING -> Color(0xFFE65100) to Color.White
                        AppointmentStatus.COMPLETED -> Color(0xFF1565C0) to Color.White
                        AppointmentStatus.CANCELLED -> Color(0xFFC62828) to Color.White
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isCurrent) activeBg else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onStatusChange(st) }
                    ) {
                        Text(
                            text = if (isArabic) st.labelAr else st.labelEn,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                color = if (isCurrent) activeFg else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun apptStatusMatches(a: AppointmentStatus, b: AppointmentStatus): Boolean {
    return a == b
}
