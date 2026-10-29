package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.data.*
import com.example.ui.components.AppTab
import com.example.ui.components.BottomNavBar
import com.example.ui.components.TopBar
import com.example.ui.screens.*

@Composable
fun CosmoApp(
    repository: CosmoRepository = remember { CosmoRepository() }
) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }

    val language by repository.language.collectAsState()
    val appointments by repository.appointments.collectAsState()
    val userProfile by authRepository.userProfile.collectAsState()
    val registeredPatients by authRepository.allRegisteredPatients.collectAsState()

    var currentTab by remember { mutableStateOf(AppTab.HOME) }
    var showAuthScreen by remember { mutableStateOf(false) }
    var showEditProfileScreen by remember { mutableStateOf(false) }

    var selectedServiceForDetail by remember { mutableStateOf<ServiceItem?>(null) }
    var preselectedServiceId by remember { mutableStateOf<String?>(null) }
    var preselectedDoctorId by remember { mutableStateOf<String?>(null) }

    val layoutDirection = if (language == Language.ARABIC) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        if (showAuthScreen) {
            AuthScreen(
                language = language,
                authRepository = authRepository,
                onAuthSuccess = {
                    showAuthScreen = false
                    currentTab = AppTab.PROFILE
                },
                onContinueAsGuest = {
                    showAuthScreen = false
                }
            )
        } else {
            Scaffold(
                topBar = {
                    TopBar(
                        language = language,
                        userProfile = userProfile,
                        onOpenProfile = {
                            if (userProfile != null) {
                                currentTab = AppTab.PROFILE
                            } else {
                                showAuthScreen = true
                            }
                        },
                        onToggleLanguage = { repository.toggleLanguage() }
                    )
                },
                bottomBar = {
                    BottomNavBar(
                        selectedTab = currentTab,
                        onTabSelected = {
                            showEditProfileScreen = false
                            currentTab = it
                        },
                        language = language
                    )
                },
                contentWindowInsets = WindowInsets.safeDrawing
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentTab) {
                        AppTab.HOME -> {
                            HomeScreen(
                                language = language,
                                onNavigateTab = { currentTab = it },
                                onSelectService = { service ->
                                    selectedServiceForDetail = service
                                },
                                onSelectDoctor = { doctor ->
                                    preselectedDoctorId = doctor.id
                                    currentTab = AppTab.BOOKING
                                }
                            )
                        }

                        AppTab.SERVICES -> {
                            ServicesScreen(
                                language = language,
                                selectedServiceForDetail = selectedServiceForDetail,
                                onDismissDetail = { selectedServiceForDetail = null },
                                onSelectServiceForDetail = { selectedServiceForDetail = it },
                                onBookService = { service ->
                                    preselectedServiceId = service.id
                                    currentTab = AppTab.BOOKING
                                }
                            )
                        }

                        AppTab.DOCTORS -> {
                            DoctorsScreen(
                                language = language,
                                onBookWithDoctor = { doctor ->
                                    preselectedDoctorId = doctor.id
                                    currentTab = AppTab.BOOKING
                                }
                            )
                        }

                        AppTab.CALCULATOR -> {
                            CalculatorScreen(
                                language = language,
                                onBookCalculatedPlan = { opt, _, _ ->
                                    val linkedServiceId = when (opt.category) {
                                        ServiceCategory.LASER -> "laser-hair-removal"
                                        ServiceCategory.BODY -> "body-contouring"
                                        ServiceCategory.SKIN -> if (opt.id.contains("hydra")) "hydrafacial" else "chemical-peels"
                                        else -> if (opt.id.contains("filler")) "fillers" else "botox"
                                    }
                                    preselectedServiceId = linkedServiceId
                                    currentTab = AppTab.BOOKING
                                }
                            )
                        }

                        AppTab.BOOKING -> {
                            BookingScreen(
                                language = language,
                                appointments = appointments,
                                preselectedServiceId = preselectedServiceId,
                                preselectedDoctorId = preselectedDoctorId,
                                userProfile = userProfile,
                                onBookAppointment = { name, phone, email, branchId, serviceId, docId, date, time, notes ->
                                    val newAppt = repository.bookAppointment(
                                        customerName = name,
                                        customerPhone = phone,
                                        customerEmail = email,
                                        branchId = branchId,
                                        serviceId = serviceId,
                                        doctorId = docId,
                                        date = date,
                                        timeSlot = time,
                                        notes = notes,
                                        userId = userProfile?.id ?: ""
                                    )
                                    val matched = repository.getUserAppointments(
                                        userId = userProfile?.id,
                                        phone = phone.ifBlank { userProfile?.phone },
                                        email = email.ifBlank { userProfile?.email }
                                    )
                                    authRepository.syncUserBookingsCount(matched.size)
                                    newAppt
                                },
                                onCancelAppointment = { apptId ->
                                    repository.cancelAppointment(apptId)
                                }
                            )
                        }

                        AppTab.OFFERS -> {
                            OffersScreen(
                                language = language,
                                onBookWithOffer = { offer ->
                                    val linkedServiceId = when (offer.category) {
                                        ServiceCategory.LASER -> "laser-hair-removal"
                                        ServiceCategory.BODY -> "body-contouring"
                                        else -> if (offer.id.contains("laser")) "laser-hair-removal" else "botox"
                                    }
                                    preselectedServiceId = linkedServiceId
                                    currentTab = AppTab.BOOKING
                                }
                            )
                        }

                        AppTab.PROFILE -> {
                            if (showEditProfileScreen && userProfile != null) {
                                BackHandler {
                                    showEditProfileScreen = false
                                }
                                EditProfileScreen(
                                    language = language,
                                    userProfile = userProfile!!,
                                    onSaveProfile = { updated ->
                                        authRepository.saveUserProfile(updated)
                                        showEditProfileScreen = false
                                    },
                                    onBack = {
                                        showEditProfileScreen = false
                                    }
                                )
                            } else {
                                val matchedAppts = repository.getUserAppointments(
                                    userId = userProfile?.id,
                                    phone = userProfile?.phone,
                                    email = userProfile?.email
                                )
                                // Dynamically sync VIP badge if count changed
                                LaunchedEffect(matchedAppts.size, userProfile?.id) {
                                    if (userProfile != null && userProfile!!.totalBookingsCount != matchedAppts.size) {
                                        authRepository.syncUserBookingsCount(matchedAppts.size)
                                    }
                                }

                                ProfileScreen(
                                    language = language,
                                    userProfile = userProfile,
                                    matchedAppointments = matchedAppts,
                                    onOpenAuth = { showAuthScreen = true },
                                    onOpenEditProfile = { showEditProfileScreen = true },
                                    onSignOut = {
                                        showEditProfileScreen = false
                                        authRepository.signOut()
                                    },
                                    onBookAppointment = { currentTab = AppTab.BOOKING }
                                )
                            }
                        }

                        AppTab.ADMIN -> {
                            AdminPortalScreen(
                                language = language,
                                appointments = appointments,
                                registeredPatients = registeredPatients,
                                onUpdateStatus = { id, status ->
                                    repository.updateAppointmentStatus(id, status)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
