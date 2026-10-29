package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    language: Language,
    userProfile: UserProfile,
    onSaveProfile: suspend (UserProfile) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = language == Language.ARABIC
    val coroutineScope = rememberCoroutineScope()

    var fullName by remember { mutableStateOf(userProfile.fullName) }
    var phone by remember { mutableStateOf(userProfile.phone) }
    var email by remember { mutableStateOf(userProfile.email) }
    var preferredBranchId by remember { mutableStateOf(userProfile.preferredBranchId) }
    var preferredCategory by remember { mutableStateOf(userProfile.preferredCategory) }
    var notesOrMedicalHistory by remember { mutableStateOf(userProfile.notesOrMedicalHistory) }
    var marketingConsent by remember { mutableStateOf(userProfile.marketingConsent) }

    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isArabic) "تخصيص بيانات العميل" else "Customize Profile",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = BurgundyPrimary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = BurgundyPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
                .testTag("edit_profile_screen")
        ) {
            // Header Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = BurgundyContainer,
                border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(BurgundyDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = fullName.take(1).uppercase().ifBlank { "C" },
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = GoldAccent,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = if (isArabic) "الملف الشخصي والطبي" else "Medical & Client Profile",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BurgundyDark
                            )
                        )
                        Text(
                            text = if (isArabic) "خصصي تفضيلاتك الطبية والتواصل لتجربة علاجية مثالية"
                                   else "Customize medical and communication preferences",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 1. Personal Information Section
            Text(
                text = if (isArabic) "1. البيانات الأساسية:" else "1. Personal Information:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                    errorMessage = null
                },
                label = { Text(if (isArabic) "الاسم بالكامل *" else "Full Name *") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BurgundyPrimary) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    errorMessage = null
                },
                label = { Text(if (isArabic) "رقم هاتف الواتساب (للتأكيدات والعروض) *" else "WhatsApp / Mobile Number *") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = BurgundyPrimary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                label = { Text(if (isArabic) "البريد الإلكتروني *" else "Email Address *") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = BurgundyPrimary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Preferred Clinic Branch
            Text(
                text = if (isArabic) "2. الفرع المفضل لزياراتك:" else "2. Preferred Branch:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                CosmoData.branches.forEach { branch ->
                    val isSelected = branch.id == preferredBranchId
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { preferredBranchId = branch.id },
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
                                onClick = { preferredBranchId = branch.id },
                                colors = RadioButtonDefaults.colors(selectedColor = BurgundyPrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isArabic) branch.titleAr else branch.titleEn,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) BurgundyPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Primary Aesthetic Interest
            Text(
                text = if (isArabic) "3. الاهتمام التجميلي الرئيسي:" else "3. Primary Aesthetic Interest:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            val interestCategories = listOf(
                "all" to (if (isArabic) "جميع التخصصات" else "All Procedures"),
                "skin" to (if (isArabic) "العناية بالبشرة والهيدرافيشل" else "Skin Care & HydraFacial"),
                "injectables" to (if (isArabic) "حقن البوتوكس والفيلر" else "Botox & Dermal Fillers"),
                "laser" to (if (isArabic) "ليزر إزالة الشعر كانديلا" else "Candela Laser Hair Removal"),
                "body" to (if (isArabic) "نحت وتنسيق القوام والفيزر" else "Body Contouring & VASER")
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                interestCategories.forEach { (catId, catName) ->
                    val isSelected = catId == preferredCategory
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { preferredCategory = catId },
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
                                onClick = { preferredCategory = catId },
                                colors = RadioButtonDefaults.colors(selectedColor = BurgundyPrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = catName,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) BurgundyPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Medical Notes / Sensitive Skin
            Text(
                text = if (isArabic) "4. ملاحظات طبية وحساسية البشرة (اختياري):" else "4. Medical History / Skin Notes (Optional):",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = notesOrMedicalHistory,
                onValueChange = { notesOrMedicalHistory = it },
                placeholder = {
                    Text(
                        text = if (isArabic) "مثال: بشرة حساسة، حساسية ضد أدوية معينة، إجراءات تجميلية سابقة..."
                               else "e.g., Sensitive skin, medication allergies, previous treatments...",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 5. Marketing & WhatsApp Promotions Consent
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isArabic) "استقبال العروض الحصرية والخصومات" else "Receive Exclusive Offers & Deals",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isArabic) "الموافقة على إرسال العروض الموسمية وباقات VIP عبر واتساب والبريد الإلكتروني"
                                   else "Opt-in to seasonal deals and VIP vouchers via WhatsApp and email",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Switch(
                        checked = marketingConsent,
                        onCheckedChange = { marketingConsent = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = BurgundyPrimary)
                    )
                }
            }

            // Error display
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = {
                    if (fullName.trim().length < 3) {
                        errorMessage = if (isArabic) "يرجى كتابة الاسم بالكامل" else "Please enter your full name"
                        return@Button
                    }
                    if (phone.trim().length < 8) {
                        errorMessage = if (isArabic) "يرجى كتابة رقم هاتف صحيح للواتساب" else "Please enter a valid WhatsApp phone number"
                        return@Button
                    }
                    if (!email.contains("@") || !email.contains(".")) {
                        errorMessage = if (isArabic) "يرجى إدخال بريد إلكتروني صحيح" else "Please enter a valid email address"
                        return@Button
                    }

                    isSaving = true
                    coroutineScope.launch {
                        val updatedProfile = userProfile.copy(
                            fullName = fullName.trim(),
                            phone = phone.trim(),
                            email = email.trim(),
                            preferredBranchId = preferredBranchId,
                            preferredCategory = preferredCategory,
                            notesOrMedicalHistory = notesOrMedicalHistory.trim(),
                            marketingConsent = marketingConsent
                        )
                        onSaveProfile(updatedProfile)
                        isSaving = false
                        onBack()
                    }
                },
                enabled = !isSaving,
                colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_profile_customization_btn")
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isArabic) "حفظ التعديلات والتفضيلات" else "Save Changes & Preferences",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
