package com.example.ui.screens

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    language: Language,
    selectedServiceForDetail: ServiceItem?,
    onDismissDetail: () -> Unit,
    onSelectServiceForDetail: (ServiceItem) -> Unit,
    onBookService: (ServiceItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = language == Language.ARABIC
    var selectedCategory by remember { mutableStateOf(ServiceCategory.ALL) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredServices = remember(selectedCategory, searchQuery) {
        CosmoData.services.filter { item ->
            val matchCategory = selectedCategory == ServiceCategory.ALL || item.category == selectedCategory
            val matchSearch = searchQuery.isBlank() ||
                    item.titleAr.contains(searchQuery, ignoreCase = true) ||
                    item.titleEn.contains(searchQuery, ignoreCase = true) ||
                    item.shortDescAr.contains(searchQuery, ignoreCase = true) ||
                    item.shortDescEn.contains(searchQuery, ignoreCase = true)
            matchCategory && matchSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("services_screen")
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = if (isArabic) "ابحث عن خدمة تجميلية، فيلر، ليزر، بوتوكس..." else "Search aesthetic procedures, laser, filler...",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = BurgundyPrimary)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("services_search_field"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BurgundyPrimary,
                unfocusedBorderColor = BorderLight
            ),
            singleLine = true
        )

        // Category Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ServiceCategory.values()) { cat ->
                val isSelected = cat == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = cat },
                    label = {
                        Text(
                            text = if (isArabic) cat.nameAr else cat.nameEn,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BurgundyPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) BurgundyPrimary else BorderLight
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        // Services List
        if (filteredServices.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(54.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isArabic) "لا توجد خدمات مطابقة لبحثك" else "No services matched your search",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredServices) { service ->
                    ServiceItemCard(
                        service = service,
                        isArabic = isArabic,
                        onClick = { onSelectServiceForDetail(service) },
                        onBookClick = { onBookService(service) }
                    )
                }
            }
        }
    }

    // Modal Bottom Sheet for Service Details
    selectedServiceForDetail?.let { service ->
        ModalBottomSheet(
            onDismissRequest = onDismissDetail,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            ServiceDetailSheetContent(
                service = service,
                isArabic = isArabic,
                onBookClick = {
                    onDismissDetail()
                    onBookService(service)
                }
            )
        }
    }
}

@Composable
private fun ServiceItemCard(
    service: ServiceItem,
    isArabic: Boolean,
    onClick: () -> Unit,
    onBookClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("service_card_${service.slug}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column {
            Box(modifier = Modifier.height(150.dp).fillMaxWidth()) {
                Image(
                    painter = painterResource(id = service.imageResId),
                    contentDescription = if (isArabic) service.titleAr else service.titleEn,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(8.dp),
                    color = BurgundyDark.copy(alpha = 0.85f)
                ) {
                    Text(
                        text = if (isArabic) service.category.nameAr else service.category.nameEn,
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = if (isArabic) service.titleAr else service.titleEn,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isArabic) service.shortDescAr else service.shortDescEn,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    ),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Highlights pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val highlights = if (isArabic) service.highlightsAr else service.highlightsEn
                    highlights.take(2).forEach { hl ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BurgundyContainer.copy(alpha = 0.7f)
                        ) {
                            Text(
                                text = "✓ $hl",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = BurgundyPrimary,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AccessTime,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isArabic) service.durationAr else service.durationEn,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        Text(
                            text = "${service.startingPriceEgp} EGP",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = BurgundyPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = onClick,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(
                                text = if (isArabic) "التفاصيل" else "Details",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Button(
                            onClick = onBookClick,
                            colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(
                                text = if (isArabic) "احجز الآن" else "Book Now",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceDetailSheetContent(
    service: ServiceItem,
    isArabic: Boolean,
    onBookClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .padding(bottom = 32.dp)
    ) {
        Box(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
        ) {
            Image(
                painter = painterResource(id = service.imageResId),
                contentDescription = if (isArabic) service.titleAr else service.titleEn,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = if (isArabic) service.titleAr else service.titleEn,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = BurgundyPrimary
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (isArabic) service.fullDescAr else service.fullDescEn,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Ideal For Section
        Text(
            text = if (isArabic) "لمن يناسب هذا الإجراء؟" else "Ideal Candidates",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(6.dp))
        val idealList = if (isArabic) service.idealForAr else service.idealForEn
        idealList.forEach { item ->
            Row(
                modifier = Modifier.padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = BurgundyPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item, style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Steps Section
        Text(
            text = if (isArabic) "مراحل وخطوات الجلسة" else "Procedure Steps",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(6.dp))
        val steps = if (isArabic) service.stepsAr else service.stepsEn
        steps.forEachIndexed { idx, step ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = BurgundyContainer,
                    modifier = Modifier.size(22.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${idx + 1}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BurgundyPrimary
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = step, style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Aftercare Section
        Text(
            text = if (isArabic) "إرشادات ما بعد الجلسة" else "Post-Treatment Care",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(6.dp))
        val aftercare = if (isArabic) service.aftercareAr else service.aftercareEn
        aftercare.forEach { tip ->
            Row(
                modifier = Modifier.padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Spa,
                    contentDescription = null,
                    tint = GoldDark,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = tip, style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bottom Booking Action
        Button(
            onClick = onBookClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("detail_book_now_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isArabic) "احجز موعد لهذه الخدمة" else "Book Appointment for this Service",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
