package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import kotlin.random.Random

class CosmoRepository {

    private val _language = MutableStateFlow(Language.ARABIC)
    val language: StateFlow<Language> = _language.asStateFlow()

    private val _appointments = MutableStateFlow<List<Appointment>>(CosmoData.initialAppointments)
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    private val _favoriteServiceIds = MutableStateFlow<Set<String>>(setOf("botox", "laser-hair-removal", "hydrafacial"))
    val favoriteServiceIds: StateFlow<Set<String>> = _favoriteServiceIds.asStateFlow()

    private val _selectedBranchId = MutableStateFlow("sheraton")
    val selectedBranchId: StateFlow<String> = _selectedBranchId.asStateFlow()

    fun toggleLanguage() {
        _language.value = if (_language.value == Language.ARABIC) Language.ENGLISH else Language.ARABIC
    }

    fun setLanguage(lang: Language) {
        _language.value = lang
    }

    fun selectBranch(branchId: String) {
        _selectedBranchId.value = branchId
    }

    fun toggleFavorite(serviceId: String) {
        val current = _favoriteServiceIds.value.toMutableSet()
        if (current.contains(serviceId)) {
            current.remove(serviceId)
        } else {
            current.add(serviceId)
        }
        _favoriteServiceIds.value = current
    }

    fun bookAppointment(
        customerName: String,
        customerPhone: String,
        customerEmail: String,
        branchId: String,
        serviceId: String,
        doctorId: String?,
        date: String,
        timeSlot: String,
        notes: String,
        userId: String = ""
    ): Appointment {
        val refNumber = Random.nextInt(1000, 9999)
        val newAppointment = Appointment(
            id = UUID.randomUUID().toString(),
            refCode = "CCC-$refNumber",
            userId = userId,
            customerName = customerName,
            customerPhone = customerPhone,
            customerEmail = customerEmail,
            branchId = branchId,
            serviceId = serviceId,
            doctorId = doctorId,
            date = date,
            timeSlot = timeSlot,
            notes = notes,
            status = AppointmentStatus.CONFIRMED,
            createdAt = "2026-10-29"
        )
        _appointments.value = listOf(newAppointment) + _appointments.value
        return newAppointment
    }

    fun getUserAppointments(userId: String?, phone: String?, email: String?): List<Appointment> {
        val uId = userId?.takeIf { it.isNotBlank() }
        val ph = phone?.trim()?.takeIf { it.isNotBlank() }
        val em = email?.trim()?.lowercase()?.takeIf { it.isNotBlank() }

        if (uId == null && ph == null && em == null) {
            return _appointments.value
        }

        return _appointments.value.filter { appt ->
            (uId != null && appt.userId == uId) ||
            (ph != null && appt.customerPhone.trim() == ph) ||
            (em != null && appt.customerEmail.trim().lowercase() == em)
        }
    }

    fun updateAppointmentStatus(appointmentId: String, newStatus: AppointmentStatus) {
        _appointments.value = _appointments.value.map {
            if (it.id == appointmentId) it.copy(status = newStatus) else it
        }
    }

    fun cancelAppointment(appointmentId: String) {
        updateAppointmentStatus(appointmentId, AppointmentStatus.CANCELLED)
    }

    fun getServiceById(id: String): ServiceItem? {
        return CosmoData.services.find { it.id == id }
    }

    fun getDoctorById(id: String): Doctor? {
        return CosmoData.doctors.find { it.id == id }
    }

    fun getBranchById(id: String): Branch? {
        return CosmoData.branches.find { it.id == id }
    }
}
