package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.Language
import com.example.data.UserProfile
import com.example.ui.theme.BurgundyDark
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.GoldAccent

@Composable
fun TopBar(
    language: Language,
    userProfile: UserProfile?,
    onOpenProfile: () -> Unit,
    onToggleLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isArabic = language == Language.ARABIC

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Clinic Logo and Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BurgundyDark)
                        .border(1.5.dp, GoldAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cairo_cosmo_icon_1793271536712),
                        contentDescription = "Cairo Cosmo Center Logo",
                        modifier = Modifier.size(34.dp).clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = if (isArabic) "مركز القاهرة الطبي" else "Cairo Cosmo Center",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BurgundyPrimary,
                            fontSize = 15.sp
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = if (isArabic) "لجراحات التجميل والليزر" else "Aesthetic & Laser Surgery",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        ),
                        maxLines = 1
                    )
                }
            }

            // Right actions: Language, User Account, WhatsApp
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Language Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onToggleLanguage() }
                        .testTag("language_toggle_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Switch Language",
                            tint = BurgundyPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (isArabic) "En" else "عربي",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BurgundyPrimary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                // User Account / Login Button
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (userProfile != null) BurgundyDark else BurgundyPrimary.copy(alpha = 0.1f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onOpenProfile() }
                        .testTag("topbar_user_profile_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (userProfile != null) {
                            Text(
                                text = userProfile.fullName.split(" ").firstOrNull() ?: "",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(GoldAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userProfile.fullName.take(1).uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = BurgundyDark,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Sign In",
                                tint = BurgundyPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = if (isArabic) "دخول" else "Login",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BurgundyPrimary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // WhatsApp Contact Action
                FilledTonalIconButton(
                    onClick = {
                        val waIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://wa.me/201206666390?text=${Uri.encode("مرحباً، أود الاستفسار عن مواعيد وحجوزات مركز القاهرة الطبي")}")
                        )
                        context.startActivity(waIntent)
                    },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = Color(0xFF25D366).copy(alpha = 0.15f),
                        contentColor = Color(0xFF1E7E34)
                    ),
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("whatsapp_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Contact on WhatsApp",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
