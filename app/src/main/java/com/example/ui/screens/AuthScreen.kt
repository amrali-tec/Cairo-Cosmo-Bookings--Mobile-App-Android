package com.example.ui.screens

import android.content.Context
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AuthRepository
import com.example.data.Language
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    language: Language,
    authRepository: AuthRepository,
    onAuthSuccess: () -> Unit,
    onContinueAsGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = language == Language.ARABIC
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val isLoading by authRepository.isLoading.collectAsState()
    val authError by authRepository.authError.collectAsState()

    var isRegisterMode by remember { mutableStateOf(false) }

    // Form fields
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var localValidationError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .testTag("auth_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Clinic Logo Badge
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(BurgundyDark)
                .border(2.dp, GoldAccent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.cairo_cosmo_icon_1793271536712),
                contentDescription = "Clinic Logo",
                modifier = Modifier.size(64.dp).clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = if (isArabic) "مركز القاهرة الطبي لجراحات التجميل" else "Cairo Cosmo Center",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = BurgundyPrimary,
                fontSize = 20.sp
            ),
            textAlign = TextAlign.Center
        )

        Text(
            text = if (isArabic) "بوابة العملاء والمتابعة الطبية" else "Patient & Client Portal",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Mode Switcher Tabs
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(4.dp)) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            isRegisterMode = false
                            localValidationError = null
                            authRepository.clearError()
                        },
                    shape = RoundedCornerShape(12.dp),
                    color = if (!isRegisterMode) MaterialTheme.colorScheme.surface else Color.Transparent,
                    shadowElevation = if (!isRegisterMode) 2.dp else 0.dp
                ) {
                    Text(
                        text = if (isArabic) "تسجيل الدخول" else "Sign In",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (!isRegisterMode) FontWeight.Bold else FontWeight.Medium,
                            color = if (!isRegisterMode) BurgundyPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            isRegisterMode = true
                            localValidationError = null
                            authRepository.clearError()
                        },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isRegisterMode) MaterialTheme.colorScheme.surface else Color.Transparent,
                    shadowElevation = if (isRegisterMode) 2.dp else 0.dp
                ) {
                    Text(
                        text = if (isArabic) "حساب جديد" else "Register",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (isRegisterMode) FontWeight.Bold else FontWeight.Medium,
                            color = if (isRegisterMode) BurgundyPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // GOOGLE SIGN IN BUTTON
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .clickable(enabled = !isLoading) {
                    coroutineScope.launch {
                        val result = authRepository.signInWithGoogle(context)
                        if (result.isSuccess) {
                            onAuthSuccess()
                        }
                    }
                }
                .testTag("google_sign_in_btn"),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.5.dp, BorderLight),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Google "G" letter icon
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "G",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color(0xFF4285F4)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = if (isArabic) "المتابعة بحساب Google" else "Continue with Google",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Divider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(modifier = Modifier.weight(1f), color = BorderLight)
            Text(
                text = if (isArabic) " أو عبر البريد " else " OR ",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Divider(modifier = Modifier.weight(1f), color = BorderLight)
        }

        Spacer(modifier = Modifier.height(18.dp))

        // FORM FIELDS
        if (isRegisterMode) {
            // Full Name
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    localValidationError = null
                },
                label = { Text(if (isArabic) "الاسم بالكامل *" else "Full Name *") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BurgundyPrimary) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_name_field"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Phone
            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    localValidationError = null
                },
                label = { Text(if (isArabic) "رقم الهاتف / الواتساب *" else "Phone / WhatsApp *") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = BurgundyPrimary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_phone_field"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                localValidationError = null
            },
            label = { Text(if (isArabic) "البريد الإلكتروني *" else "Email Address *") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = BurgundyPrimary) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_email_field"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                localValidationError = null
            },
            label = { Text(if (isArabic) "كلمة المرور *" else "Password *") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BurgundyPrimary) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle Password"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_password_field"),
            singleLine = true
        )

        if (isRegisterMode) {
            Spacer(modifier = Modifier.height(10.dp))

            // Confirm Password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    localValidationError = null
                },
                label = { Text(if (isArabic) "تأكيد كلمة المرور *" else "Confirm Password *") },
                leadingIcon = { Icon(Icons.Default.LockReset, contentDescription = null, tint = BurgundyPrimary) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_confirm_password_field"),
                singleLine = true
            )
        }

        // Error message presentation
        val displayError = localValidationError ?: authError
        if (displayError != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = displayError,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button
        Button(
            onClick = {
                if (isRegisterMode) {
                    if (name.trim().length < 3) {
                        localValidationError = if (isArabic) "يرجى إدخال الاسم كاملاً (3 أحرف على الأقل)" else "Please enter your full name (at least 3 characters)"
                        return@Button
                    }
                    if (phone.trim().length < 8) {
                        localValidationError = if (isArabic) "يرجى إدخال رقم هاتف صحيح" else "Please enter a valid phone number"
                        return@Button
                    }
                    if (!email.contains("@") || !email.contains(".")) {
                        localValidationError = if (isArabic) "يرجى إدخال بريد إلكتروني صحيح" else "Please enter a valid email address"
                        return@Button
                    }
                    if (password.length < 6) {
                        localValidationError = if (isArabic) "كلمة المرور يجب أن تكون 6 خانات على الأقل" else "Password must be at least 6 characters"
                        return@Button
                    }
                    if (password != confirmPassword) {
                        localValidationError = if (isArabic) "كلمتا المرور غير متطابقتين" else "Passwords do not match"
                        return@Button
                    }

                    coroutineScope.launch {
                        val result = authRepository.registerWithEmail(name, email, phone, password)
                        if (result.isSuccess) {
                            onAuthSuccess()
                        }
                    }
                } else {
                    if (email.isBlank() || password.isBlank()) {
                        localValidationError = if (isArabic) "يرجى ملء البريد الإلكتروني وكلمة المرور" else "Please enter your email and password"
                        return@Button
                    }
                    coroutineScope.launch {
                        val result = authRepository.signInWithEmail(email, password)
                        if (result.isSuccess) {
                            onAuthSuccess()
                        }
                    }
                }
            },
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("auth_submit_btn")
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = if (isRegisterMode) {
                        if (isArabic) "إنشاء الحساب الآن" else "Create Account"
                    } else {
                        if (isArabic) "تسجيل الدخول" else "Sign In"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Continue as Guest Button
        TextButton(
            onClick = onContinueAsGuest,
            modifier = Modifier.testTag("continue_as_guest_btn")
        ) {
            Text(
                text = if (isArabic) "تخطي والدخول كزائر للتصفح ←" else "Continue as Guest / Browse →",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
