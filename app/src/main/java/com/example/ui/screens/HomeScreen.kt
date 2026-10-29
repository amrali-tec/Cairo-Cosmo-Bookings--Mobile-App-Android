package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.*
import com.example.ui.components.AppTab
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    language: Language,
    onNavigateTab: (AppTab) -> Unit,
    onSelectService: (ServiceItem) -> Unit,
    onSelectDoctor: (Doctor) -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = language == Language.ARABIC
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. HERO LUXURY BANNER
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BurgundyDark, BurgundyPrimary, BurgundyLight)
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = GoldAccent.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (isArabic) "★ المركز الأول للتجميل والليزر في مصر" else "★ Egypt's Premier Aesthetic Clinic",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isArabic) "جمالك.. تحت إشراف نخبة أساتذة جراحة التجميل والجلدية" 
                               else "Your Beauty Supervised by Academic Leaders in Plastic Surgery & Laser",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 28.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isArabic) "نقدم أحدث تقنيات البوتوكس، الفيلر الروسي، ليزر كانديلا جنتل ليز، ونحت القوام بالفيزر بأعلى معايير الأمان الطبي."
                               else "Offering the latest Botox, Russian fillers, Candela laser, and 4D VASER sculpting with the highest medical safety.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.88f),
                            lineHeight = 21.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onNavigateTab(AppTab.BOOKING) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldAccent,
                                contentColor = BurgundyDark
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1.2f)
                                .testTag("hero_book_btn")
                        ) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isArabic) "احجز موعدك" else "Book Now",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        OutlinedButton(
                            onClick = { onNavigateTab(AppTab.CALCULATOR) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            ),
                            border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.7f)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isArabic) "حاسبة الأسعار" else "Calculator",
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }

        // 2. TRUST STATS ROW
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    number = "+25",
                    label = if (isArabic) "سنة خبرة" else "Years Exp",
                    icon = Icons.Default.Verified,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    number = "5",
                    label = if (isArabic) "فروع كبرى" else "Branches",
                    icon = Icons.Default.LocationOn,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    number = "4.9★",
                    label = if (isArabic) "تقييم المرضى" else "Patient Rating",
                    icon = Icons.Default.Star,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 3. FEATURED SERVICES
        item {
            SectionHeader(
                title = if (isArabic) "أبرز الخدمات الطبية" else "Featured Services",
                subtitle = if (isArabic) "أحدث إجراءات التجميل والليزر المتطورة" else "Top aesthetic & laser procedures",
                onSeeAllClick = { onNavigateTab(AppTab.SERVICES) },
                isArabic = isArabic
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(CosmoData.services) { service ->
                    ServiceHomeCard(
                        service = service,
                        isArabic = isArabic,
                        onClick = { onSelectService(service) },
                        onBookClick = {
                            onSelectService(service)
                            onNavigateTab(AppTab.BOOKING)
                        }
                    )
                }
            }
        }

        // 4. MEDICAL PROFESSORS & SPECIALISTS
        item {
            Spacer(modifier = Modifier.height(20.dp))
            SectionHeader(
                title = if (isArabic) "نخبة الأطباء والاستشاريين" else "Medical Leadership",
                subtitle = if (isArabic) "أساتذة جراحة التجميل والجلدية بجامعتي عين شمس والقاهرة" else "Professors from Ain Shams & Cairo Universities",
                onSeeAllClick = { onNavigateTab(AppTab.DOCTORS) },
                isArabic = isArabic
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CosmoData.doctors.forEach { doctor ->
                    DoctorHomeCard(
                        doctor = doctor,
                        isArabic = isArabic,
                        onClick = { onSelectDoctor(doctor) },
                        onBookClick = {
                            onSelectDoctor(doctor)
                            onNavigateTab(AppTab.BOOKING)
                        }
                    )
                }
            }
        }

        // 5. EXCLUSIVE OFFERS TEASER
        item {
            Spacer(modifier = Modifier.height(20.dp))
            SectionHeader(
                title = if (isArabic) "العروض والباقات الحصرية" else "Exclusive Packages",
                subtitle = if (isArabic) "خصومات تصل حتى 35% على باقات التجميل والليزر" else "Up to 35% off curated aesthetic plans",
                onSeeAllClick = { onNavigateTab(AppTab.OFFERS) },
                isArabic = isArabic
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(CosmoData.offers) { offer ->
                    OfferHomeCard(
                        offer = offer,
                        isArabic = isArabic,
                        onClaimClick = { onNavigateTab(AppTab.OFFERS) }
                    )
                }
            }
        }

        // 6. CLINIC BRANCHES
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = if (isArabic) "فروع مركز القاهرة الطبي" else "Our Clinic Branches",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = BurgundyPrimary
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = if (isArabic) "5 فروع مجهزة بأحدث التقنيات لخدمتكم" else "5 fully equipped clinics across Cairo & Delta",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CosmoData.branches.forEach { branch ->
                    BranchCard(
                        branch = branch,
                        isArabic = isArabic,
                        onCallClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${branch.phone}"))
                            context.startActivity(intent)
                        },
                        onMapClick = {
                            val mapUri = Uri.parse("geo:0,0?q=${Uri.encode(branch.mapQuery)}")
                            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                            context.startActivity(mapIntent)
                        }
                    )
                }
            }
        }

        // 7. PATIENT TESTIMONIALS
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = if (isArabic) "آراء وتجارب المرضى" else "Patient Experiences",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = BurgundyPrimary
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(CosmoData.testimonials) { review ->
                    TestimonialCard(review = review, isArabic = isArabic)
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    number: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BurgundyPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = number,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = BurgundyPrimary
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    onSeeAllClick: () -> Unit,
    isArabic: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = BurgundyPrimary
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
        TextButton(onClick = onSeeAllClick) {
            Text(
                text = if (isArabic) "عرض الكل" else "View All",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = BurgundyPrimary
                )
            )
        }
    }
}

@Composable
private fun ServiceHomeCard(
    service: ServiceItem,
    isArabic: Boolean,
    onClick: () -> Unit,
    onBookClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(240.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column {
            Box(modifier = Modifier.height(130.dp).fillMaxWidth()) {
                Image(
                    painter = painterResource(id = service.imageResId),
                    contentDescription = if (isArabic) service.titleAr else service.titleEn,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(8.dp),
                    color = BurgundyDark.copy(alpha = 0.85f)
                ) {
                    Text(
                        text = if (isArabic) service.category.nameAr else service.category.nameEn,
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = if (isArabic) service.titleAr else service.titleEn,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isArabic) service.shortDescAr else service.shortDescEn,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    ),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isArabic) "يبدأ من" else "From",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 10.sp
                            )
                        )
                        Text(
                            text = "${service.startingPriceEgp} EGP",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = BurgundyPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    FilledTonalButton(
                        onClick = onBookClick,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = BurgundyContainer,
                            contentColor = BurgundyPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isArabic) "حجز" else "Book",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DoctorHomeCard(
    doctor: Doctor,
    isArabic: Boolean,
    onClick: () -> Unit,
    onBookClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, BorderLight),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = doctor.imageResId),
                contentDescription = if (isArabic) doctor.nameAr else doctor.nameEn,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isArabic) doctor.nameAr else doctor.nameEn,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = GoldAccent.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "★ ${doctor.rating}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GoldDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = if (isArabic) doctor.titleAr else doctor.titleEn,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = BurgundyPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = if (isArabic) doctor.workingDaysAr else doctor.workingDaysEn,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    ),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onClick,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text(
                            text = if (isArabic) "الملف الطبي" else "Profile",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Button(
                        onClick = onBookClick,
                        colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text(
                            text = if (isArabic) "حجز موعد" else "Book",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OfferHomeCard(
    offer: Offer,
    isArabic: Boolean,
    onClaimClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .clickable { onClaimClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BurgundyContainer.copy(alpha = 0.6f)),
        border = BorderStroke(1.dp, BurgundyLight.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BurgundyPrimary
                ) {
                    Text(
                        text = "-${offer.discountPercent}% OFF",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, BorderLight)
                ) {
                    Text(
                        text = offer.promoCode,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = BurgundyPrimary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isArabic) offer.titleAr else offer.titleEn,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isArabic) offer.descAr else offer.descEn,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                ),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${offer.offerPriceEgp} EGP",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = BurgundyPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${offer.originalPriceEgp} EGP",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough,
                            fontSize = 11.sp
                        )
                    )
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = BurgundyPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun BranchCard(
    branch: Branch,
    isArabic: Boolean,
    onCallClick: () -> Unit,
    onMapClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(BurgundyContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = BurgundyPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isArabic) branch.titleAr else branch.titleEn,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = onCallClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call Branch",
                            tint = BurgundyPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = onMapClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Directions,
                            contentDescription = "Directions",
                            tint = GoldDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isArabic) branch.addressAr else branch.addressEn,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isArabic) branch.openHoursAr else branch.openHoursEn,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = BurgundyPrimary,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
private fun TestimonialCard(
    review: Testimonial,
    isArabic: Boolean
) {
    Surface(
        modifier = Modifier.width(260.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "★★★★★",
                    color = GoldDark,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(6.dp))
                if (review.verified) {
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = if (isArabic) "تجربة موثقة" else "Verified",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF2E7D32),
                                fontSize = 9.sp
                            ),
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "\"${if (isArabic) review.commentAr else review.commentEn}\"",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                ),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isArabic) review.patientNameAr else review.patientNameEn,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = if (isArabic) review.treatmentAr else review.treatmentEn,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp
                )
            )
        }
    }
}
