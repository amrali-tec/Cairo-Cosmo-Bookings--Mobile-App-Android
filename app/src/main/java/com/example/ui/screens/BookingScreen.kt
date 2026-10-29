package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun BookingScreen(
    language: Language,
    appointments: List<Appointment>,
    preselectedServiceId: String?,
    preselectedDoctorId: String?,
    userProfile: UserProfile? = null,
    onBookAppointment: (
        customerName: String,
        customerPhone: String,
        customerEmail: String,
        branchId: String,
        serviceId: String,
        doctorId: String?,
        date: String,
        timeSlot: String,
        notes: String
    ) -> Appointment,
    onCancelAppointment: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = language == Language.ARABIC
    val context = LocalContext.current
    var selectedScreenTab by remember { mutableStateOf(0) } // 0: Book New, 1: My Appointments

    // Form states
    var selectedBranchId by remember { mutableStateOf("sheraton") }
    var selectedServiceId by remember { mutableStateOf(preselectedServiceId ?: "botox") }
    var selectedDoctorId by remember { mutableStateOf<String?>(preselectedDoctorId) }
    
    // Dates chips
    val dateSlots = remember {
        listOf(
            "2026-11-01",
            "2026-11-02",
            "2026-11-03",
            "2026-11-04",
            "2026-11-05",
            "2026-11-06",
            "2026-11-07"
        )
    }
    var selectedDate by remember { mutableStateOf(dateSlots.first()) }

    val timeSlots = remember {
        listOf("11:00 ص", "01:30 م", "03:00 م", "05:30 م", "07:00 م", "08:30 م")
    }
    var selectedTimeSlot by remember { mutableStateOf(timeSlots.first()) }

    var customerName by remember { mutableStateOf(userProfile?.fullName ?: "") }
    var customerPhone by remember { mutableStateOf(userProfile?.phone ?: "") }
    var customerEmail by remember { mutableStateOf(userProfile?.email ?: "") }
    var customerNotes by remember { mutableStateOf("") }

    LaunchedEffect(userProfile) {
        if (userProfile != null) {
            if (customerName.isBlank()) customerName = userProfile.fullName
            if (customerPhone.isBlank()) customerPhone = userProfile.phone
            if (customerEmail.isBlank()) customerEmail = userProfile.email
            if (userProfile.preferredBranchId.isNotBlank()) selectedBranchId = userProfile.preferredBranchId
            if (customerNotes.isBlank() && userProfile.notesOrMedicalHistory.isNotBlank()) {
                customerNotes = userProfile.notesOrMedicalHistory
            }
        }
    }

    var confirmedAppointment by remember { mutableStateOf<Appointment?>(null) }
    var validationError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("booking_screen")
    ) {
        // Tab Row: Book New vs My Appointments
        TabRow(
            selectedTabIndex = selectedScreenTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = BurgundyPrimary
        ) {
            Tab(
                selected = selectedScreenTab == 0,
                onClick = { selectedScreenTab = 0 },
                text = {
                    Text(
                        text = if (isArabic) "حجز موعد جديد" else "New Appointment",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                },
                icon = { Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("tab_new_booking")
            )
            Tab(
                selected = selectedScreenTab == 1,
                onClick = { selectedScreenTab = 1 },
                text = {
                    Text(
                        text = if (isArabic) "مواعيدي (${appointments.size})" else "My Bookings (${appointments.size})",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                },
                icon = { Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("tab_my_bookings")
            )
        }

        if (selectedScreenTab == 0) {
            // BOOK NEW APPOINTMENT FORM
            if (confirmedAppointment != null) {
                // Booking Success Confirmation View
                val appt = confirmedAppointment!!
                val serviceObj = CosmoData.services.find { it.id == appt.serviceId }
                val branchObj = CosmoData.branches.find { it.id == appt.branchId }
                val doctorObj = CosmoData.doctors.find { it.id == appt.doctorId }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isArabic) "تم تأكيد حجزك بنجاح!" else "Appointment Confirmed!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BurgundyPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isArabic) "رقم الحجز المرجعي: ${appt.refCode}" else "Booking Reference: ${appt.refCode}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = GoldDark,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, BorderLight)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            InfoRow(
                                label = if (isArabic) "اسم المريض:" else "Patient:",
                                value = appt.customerName
                            )
                            InfoRow(
                                label = if (isArabic) "الخدمة المطلوبة:" else "Service:",
                                value = if (isArabic) serviceObj?.titleAr ?: "" else serviceObj?.titleEn ?: ""
                            )
                            InfoRow(
                                label = if (isArabic) "الفرع:" else "Branch:",
                                value = if (isArabic) branchObj?.titleAr ?: "" else branchObj?.titleEn ?: ""
                            )
                            if (doctorObj != null) {
                                InfoRow(
                                    label = if (isArabic) "الطبيب:" else "Doctor:",
                                    value = if (isArabic) doctorObj.nameAr else doctorObj.nameEn
                                )
                            }
                            InfoRow(
                                label = if (isArabic) "التاريخ والوقت:" else "Date & Time:",
                                value = "${appt.date} | ${appt.timeSlot}"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // WhatsApp Instant Confirmation
                    Button(
                        onClick = {
                            val msg = if (isArabic) {
                                "مرحباً مركز القاهرة الطبي، قمت بحجز موعد برقم ${appt.refCode} باسم ${appt.customerName} لتاريخ ${appt.date} الساعة ${appt.timeSlot}"
                            } else {
                                "Hello Cairo Cosmo Center, I booked an appointment ref ${appt.refCode} for ${appt.customerName} on ${appt.date} at ${appt.timeSlot}"
                            }
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://wa.me/201206666390?text=${Uri.encode(msg)}")
                            )
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isArabic) "إرسال تأكيد الموعد عبر واتساب" else "Send Confirmation to WhatsApp",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = {
                            confirmedAppointment = null
                            selectedScreenTab = 1
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = if (isArabic) "عرض قائمة مواعيدي" else "View My Bookings")
                    }
                }
            } else {
                // FORM INPUTS
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Select Branch
                    item {
                        Text(
                            text = if (isArabic) "1. اختاري الفرع الأقرب إليك:" else "1. Select Clinic Branch:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(CosmoData.branches) { branch ->
                                val isSelected = branch.id == selectedBranchId
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedBranchId = branch.id },
                                    label = {
                                        Text(
                                            text = if (isArabic) branch.titleAr else branch.titleEn,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BurgundyPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    // 2. Select Service
                    item {
                        Text(
                            text = if (isArabic) "2. الخدمة المطلوبة:" else "2. Select Procedure:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            CosmoData.services.forEach { service ->
                                val isSelected = service.id == selectedServiceId
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { selectedServiceId = service.id },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) BurgundyContainer else MaterialTheme.colorScheme.surface,
                                    border = BorderStroke(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) BurgundyPrimary else BorderLight
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { selectedServiceId = service.id },
                                            colors = RadioButtonDefaults.colors(selectedColor = BurgundyPrimary)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = if (isArabic) service.titleAr else service.titleEn,
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) BurgundyPrimary else MaterialTheme.colorScheme.onSurface
                                                )
                                            )
                                            Text(
                                                text = "${service.startingPriceEgp} EGP",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = BurgundyPrimary,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 3. Select Doctor (Optional)
                    item {
                        Text(
                            text = if (isArabic) "3. الطبيب المفضل (اختياري):" else "3. Preferred Doctor (Optional):",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item {
                                val isAny = selectedDoctorId == null
                                FilterChip(
                                    selected = isAny,
                                    onClick = { selectedDoctorId = null },
                                    label = {
                                        Text(
                                            text = if (isArabic) "أي استشاري متاح" else "Any Available",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BurgundyPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                            items(CosmoData.doctors) { doc ->
                                val isSelected = doc.id == selectedDoctorId
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedDoctorId = doc.id },
                                    label = {
                                        Text(
                                            text = if (isArabic) doc.nameAr else doc.nameEn,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BurgundyPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    // 4. Select Date & Time Slot
                    item {
                        Text(
                            text = if (isArabic) "4. موعد الحجز:" else "4. Date & Time:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isArabic) "التاريخ:" else "Date:",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(dateSlots) { d ->
                                val isSelected = d == selectedDate
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedDate = d },
                                    label = { Text(text = d, style = MaterialTheme.typography.labelSmall) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BurgundyPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isArabic) "الوقت المتاح:" else "Available Slot:",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(timeSlots) { slot ->
                                val isSelected = slot == selectedTimeSlot
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedTimeSlot = slot },
                                    label = { Text(text = slot, style = MaterialTheme.typography.labelSmall) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BurgundyPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    // 5. Patient Contact Info
                    item {
                        Text(
                            text = if (isArabic) "5. بيانات المريض:" else "5. Patient Details:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = customerName,
                            onValueChange = {
                                customerName = it
                                validationError = null
                            },
                            label = { Text(if (isArabic) "الاسم بالكامل *" else "Full Name *") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("booking_name_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = customerPhone,
                            onValueChange = {
                                customerPhone = it
                                validationError = null
                            },
                            label = { Text(if (isArabic) "رقم الهاتف / الواتساب *" else "Phone / WhatsApp *") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("booking_phone_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = customerEmail,
                            onValueChange = { customerEmail = it },
                            label = { Text(if (isArabic) "البريد الإلكتروني (اختياري)" else "Email (Optional)") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = customerNotes,
                            onValueChange = { customerNotes = it },
                            label = { Text(if (isArabic) "ملاحظات أو استفسار للطبيب" else "Notes or questions for doctor") },
                            leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            maxLines = 3
                        )
                    }

                    // Validation Error Banner
                    if (validationError != null) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.errorContainer,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = validationError!!,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }

                    // Submit Button
                    item {
                        Button(
                            onClick = {
                                if (customerName.trim().length < 3) {
                                    validationError = if (isArabic) "يرجى إدخال اسم المريض بشكل صحيح (3 أحرف على الأقل)" else "Please enter patient name (at least 3 characters)"
                                    return@Button
                                }
                                if (customerPhone.trim().length < 8) {
                                    validationError = if (isArabic) "يرجى إدخال رقم هاتف صحيح للتواصل" else "Please enter a valid phone number"
                                    return@Button
                                }

                                val created = onBookAppointment(
                                    customerName.trim(),
                                    customerPhone.trim(),
                                    customerEmail.trim(),
                                    selectedBranchId,
                                    selectedServiceId,
                                    selectedDoctorId,
                                    selectedDate,
                                    selectedTimeSlot,
                                    customerNotes.trim()
                                )
                                confirmedAppointment = created
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_booking_btn")
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isArabic) "تأكيد حجز الموعد الآن" else "Confirm Appointment Now",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        } else {
            // MY APPOINTMENTS LIST
            if (appointments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.EventBusy,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isArabic) "لا توجد مواعيد مسجلة حالياً" else "No appointments booked yet",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { selectedScreenTab = 0 },
                            colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary)
                        ) {
                            Text(text = if (isArabic) "احجز موعدك الأول" else "Book First Appointment")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(appointments) { appt ->
                        AppointmentCard(
                            appointment = appt,
                            isArabic = isArabic,
                            onCancel = { onCancelAppointment(appt.id) },
                            onWhatsApp = {
                                val msg = if (isArabic) {
                                    "مرحباً مركز القاهرة الطبي، بخصوص موعدي رقم ${appt.refCode} بتاريخ ${appt.date}"
                                } else {
                                    "Hello Cairo Cosmo Center, regarding appointment ${appt.refCode} on ${appt.date}"
                                }
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://wa.me/201206666390?text=${Uri.encode(msg)}")
                                )
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun AppointmentCard(
    appointment: Appointment,
    isArabic: Boolean,
    onCancel: () -> Unit,
    onWhatsApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val serviceObj = CosmoData.services.find { it.id == appointment.serviceId }
    val branchObj = CosmoData.branches.find { it.id == appointment.branchId }
    val doctorObj = CosmoData.doctors.find { it.id == appointment.doctorId }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, BorderLight),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Ref Code + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = appointment.refCode,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BurgundyPrimary
                        )
                    )
                }

                // Status Badge
                val (badgeBg, badgeFg) = when (appointment.status) {
                    AppointmentStatus.CONFIRMED -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
                    AppointmentStatus.PENDING -> Color(0xFFFFF3E0) to Color(0xFFE65100)
                    AppointmentStatus.COMPLETED -> Color(0xFFE3F2FD) to Color(0xFF1565C0)
                    AppointmentStatus.CANCELLED -> Color(0xFFFFEBEE) to Color(0xFFC62828)
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badgeBg
                ) {
                    Text(
                        text = if (isArabic) appointment.status.labelAr else appointment.status.labelEn,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = badgeFg,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isArabic) serviceObj?.titleAr ?: "استشارة طبية" else serviceObj?.titleEn ?: "Consultation",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isArabic) branchObj?.titleAr ?: "" else branchObj?.titleEn ?: "",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                if (doctorObj != null) {
                    Text(text = " • ", style = MaterialTheme.typography.bodySmall)
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = if (isArabic) doctorObj.nameAr else doctorObj.nameEn,
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = BurgundyPrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${appointment.date}  •  ${appointment.timeSlot}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = BurgundyDark,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            if (appointment.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isArabic) "ملاحظات: ${appointment.notes}" else "Notes: ${appointment.notes}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onWhatsApp,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFF25D366)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1E7E34)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "WhatsApp", style = MaterialTheme.typography.labelSmall)
                }

                if (appointment.status != AppointmentStatus.CANCELLED && appointment.status != AppointmentStatus.COMPLETED) {
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = onCancel,
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text(
                            text = if (isArabic) "إلغاء الموعد" else "Cancel",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}
