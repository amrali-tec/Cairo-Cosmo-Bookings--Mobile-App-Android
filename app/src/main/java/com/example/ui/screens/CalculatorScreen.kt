package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*

@Composable
fun CalculatorScreen(
    language: Language,
    onBookCalculatedPlan: (TreatmentCalculatorOption, Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = language == Language.ARABIC
    val options = CosmoData.calculatorOptions

    var selectedOption by remember { mutableStateOf(options.first()) }
    var sessionsCount by remember { mutableStateOf(selectedOption.defaultSessions) }
    var includeConsultation by remember { mutableStateOf(true) }

    // Update sessionsCount when option changes
    LaunchedEffect(selectedOption) {
        sessionsCount = selectedOption.defaultSessions
    }

    // Calculations
    val rawCost = selectedOption.basePriceEgp * sessionsCount
    // Package discount for multi-sessions
    val discountPercent = when {
        sessionsCount >= 6 -> 25
        sessionsCount >= 3 -> 15
        sessionsCount >= 2 -> 10
        else -> 0
    }
    val discountAmount = (rawCost * discountPercent) / 100
    val consultFee = if (includeConsultation) 500 else 0
    val totalCost = rawCost - discountAmount + consultFee

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("calculator_screen")
    ) {
        // Header
        Text(
            text = if (isArabic) "حاسبة خطة التجميل والعلاج" else "Aesthetic Treatment Calculator",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = BurgundyPrimary
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (isArabic) "حددي احتياجك التجميلي وعدد الجلسات لتقدير التكلفة الإجمالية والخصومات المتاحة فوراً."
                   else "Select your aesthetic goal and session count to instantly calculate total cost, discounts, and timeline.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Step 1: Select Treatment Goal
        Text(
            text = if (isArabic) "1. اختاري الإجراء المطلوب:" else "1. Choose Treatment Area:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { opt ->
                val isSelected = opt.id == selectedOption.id
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { selectedOption = opt },
                    shape = RoundedCornerShape(14.dp),
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
                            onClick = { selectedOption = opt },
                            colors = RadioButtonDefaults.colors(selectedColor = BurgundyPrimary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isArabic) opt.nameAr else opt.nameEn,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) BurgundyPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = "${opt.basePriceEgp} EGP " + if (isArabic) "للجلسة الواحدة" else "per session",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Step 2: Session Count Stepper
        Text(
            text = if (isArabic) "2. عدد الجلسات المقترحة:" else "2. Number of Sessions:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, BorderLight)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "$sessionsCount " + if (isArabic) "جلسات" else "Sessions",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = BurgundyPrimary
                        )
                    )
                    if (discountPercent > 0) {
                        Text(
                            text = if (isArabic) "وفرت $discountPercent% كباقة جلسات!" else "Saved $discountPercent% package discount!",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledTonalIconButton(
                        onClick = { if (sessionsCount > 1) sessionsCount-- },
                        enabled = sessionsCount > 1,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    FilledTonalIconButton(
                        onClick = { if (sessionsCount < 12) sessionsCount++ },
                        enabled = sessionsCount < 12,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Consultation Add-on Switch
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            border = BorderStroke(1.dp, BorderLight)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isArabic) "كشف واستشارة أستاذ الجامعة (+500 ج.م)" else "Medical Professor Consultation (+500 EGP)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (isArabic) "يتم خصمها بالكامل عند تنفيذ الإجراء داخل المركز" else "Fully deductible upon proceeding with treatment",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Switch(
                    checked = includeConsultation,
                    onCheckedChange = { includeConsultation = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = BurgundyPrimary)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Step 3: Estimated Calculation Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = BurgundyContainer),
            border = BorderStroke(1.5.dp, GoldAccent.copy(alpha = 0.7f))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isArabic) "تقدير الخطة المتكاملة" else "Estimated Plan Summary",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BurgundyDark
                        )
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GoldAccent.copy(alpha = 0.3f)
                    ) {
                        Text(
                            text = if (isArabic) "خطة مخصصة" else "Custom Plan",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BurgundyDark
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = BorderLight)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isArabic) "سعر الجلسات الأصلي:" else "Original Sessions Cost:",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(text = "$rawCost EGP", style = MaterialTheme.typography.bodySmall)
                }

                if (discountPercent > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isArabic) "خصم الباقة ($discountPercent%):" else "Package Discount ($discountPercent%):",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF2E7D32))
                        )
                        Text(
                            text = "-$discountAmount EGP",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                if (includeConsultation) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isArabic) "كشف استشاري (مسترد):" else "Consultation Fee (Refundable):",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(text = "+500 EGP", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = BorderLight)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isArabic) "الإجمالي التقديري:" else "Estimated Total:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "$totalCost EGP",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BurgundyPrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Expected Interval
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.EventRepeat,
                        contentDescription = null,
                        tint = BurgundyPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isArabic) "الفترة بين الجلسات: كل ${selectedOption.intervalWeeks} أسابيع"
                               else "Interval between sessions: Every ${selectedOption.intervalWeeks} weeks",
                        style = MaterialTheme.typography.bodySmall.copy(color = BurgundyDark)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onBookCalculatedPlan(selectedOption, sessionsCount, totalCost) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("book_calculated_plan_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isArabic) "تأكيد وحجز هذه الباقة" else "Confirm & Book This Plan",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
