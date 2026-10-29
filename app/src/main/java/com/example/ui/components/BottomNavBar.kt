package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Language
import com.example.ui.theme.BurgundyPrimary

enum class AppTab(
    val labelAr: String,
    val labelEn: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("الرئيسية", "Home", Icons.Filled.Home, Icons.Outlined.Home),
    SERVICES("الخدمات", "Services", Icons.Filled.Spa, Icons.Outlined.Spa),
    CALCULATOR("الحاسبة", "Calculator", Icons.Filled.Calculate, Icons.Outlined.Calculate),
    BOOKING("الحجز", "Bookings", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth),
    OFFERS("العروض", "Offers", Icons.Filled.LocalOffer, Icons.Outlined.LocalOffer),
    PROFILE("حسابي", "Profile", Icons.Filled.Person, Icons.Outlined.Person),
    DOCTORS("الأطباء", "Doctors", Icons.Filled.MedicalServices, Icons.Outlined.MedicalServices),
    ADMIN("الإدارة", "Admin", Icons.Filled.AdminPanelSettings, Icons.Outlined.AdminPanelSettings)
}

val BottomBarTabs = listOf(
    AppTab.HOME,
    AppTab.SERVICES,
    AppTab.CALCULATOR,
    AppTab.BOOKING,
    AppTab.OFFERS,
    AppTab.PROFILE
)

@Composable
fun BottomNavBar(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    language: Language,
    modifier: Modifier = Modifier
) {
    val isArabic = language == Language.ARABIC

    NavigationBar(
        modifier = modifier.testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        BottomBarTabs.forEach { tab ->
            val isSelected = tab == selectedTab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = if (isArabic) tab.labelAr else tab.labelEn,
                        modifier = Modifier.size(20.dp),
                        tint = if (isSelected) BurgundyPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                label = {
                    Text(
                        text = if (isArabic) tab.labelAr else tab.labelEn,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            color = if (isSelected) BurgundyPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 1
                    )
                },
                alwaysShowLabel = true,
                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
            )
        }
    }
}
