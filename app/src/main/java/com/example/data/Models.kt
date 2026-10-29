package com.example.data

enum class Language(val code: String, val displayName: String) {
    ARABIC("ar", "العربية"),
    ENGLISH("en", "English")
}

enum class ServiceCategory(val slug: String, val nameAr: String, val nameEn: String) {
    ALL("all", "الكل", "All"),
    LASER("laser", "الليزر", "Laser"),
    SURGERY("surgery", "جراحات التجميل", "Plastic Surgery"),
    INJECTABLES("injectables", "الحقن التجميلية", "Injectables"),
    SKIN("skin", "العناية بالبشرة", "Skin Care"),
    BODY("body", "نحت وتنسيق القوام", "Body Contouring"),
    HAIR("hair", "علاجات الشعر", "Hair Care"),
    THREADS("threads", "الخيوط التجميلية", "Threads")
}

data class ServiceItem(
    val id: String,
    val slug: String,
    val category: ServiceCategory,
    val titleAr: String,
    val titleEn: String,
    val shortDescAr: String,
    val shortDescEn: String,
    val fullDescAr: String,
    val fullDescEn: String,
    val highlightsAr: List<String>,
    val highlightsEn: List<String>,
    val idealForAr: List<String>,
    val idealForEn: List<String>,
    val stepsAr: List<String>,
    val stepsEn: List<String>,
    val aftercareAr: List<String>,
    val aftercareEn: List<String>,
    val durationAr: String,
    val durationEn: String,
    val startingPriceEgp: Int,
    val doctorIds: List<String>,
    val imageResId: Int,
    val faqs: List<ServiceFaq>
)

data class ServiceFaq(
    val questionAr: String,
    val questionEn: String,
    val answerAr: String,
    val answerEn: String
)

data class Doctor(
    val id: String,
    val slug: String,
    val nameAr: String,
    val nameEn: String,
    val titleAr: String,
    val titleEn: String,
    val bioAr: String,
    val bioEn: String,
    val experienceAr: String,
    val experienceEn: String,
    val workingDaysAr: String,
    val workingDaysEn: String,
    val imageResId: Int,
    val rating: Float,
    val reviewCount: Int,
    val branches: List<String>,
    val serviceIds: List<String>
)

data class Branch(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val addressAr: String,
    val addressEn: String,
    val phone: String,
    val email: String,
    val openHoursAr: String,
    val openHoursEn: String,
    val mapQuery: String
)

data class Offer(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val descAr: String,
    val descEn: String,
    val discountPercent: Int,
    val originalPriceEgp: Int,
    val offerPriceEgp: Int,
    val promoCode: String,
    val category: ServiceCategory,
    val expiresAr: String,
    val expiresEn: String
)

data class Testimonial(
    val id: String,
    val patientNameAr: String,
    val patientNameEn: String,
    val treatmentAr: String,
    val treatmentEn: String,
    val commentAr: String,
    val commentEn: String,
    val rating: Int,
    val verified: Boolean
)

enum class AppointmentStatus(val labelAr: String, val labelEn: String) {
    CONFIRMED("مؤكد", "Confirmed"),
    PENDING("قيد المراجعة", "Pending Review"),
    COMPLETED("مكتمل", "Completed"),
    CANCELLED("ملغي", "Cancelled")
}

data class Appointment(
    val id: String,
    val refCode: String,
    val userId: String = "",
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String,
    val branchId: String,
    val serviceId: String,
    val doctorId: String?,
    val date: String,
    val timeSlot: String,
    val notes: String,
    val status: AppointmentStatus,
    val createdAt: String
)

data class TreatmentCalculatorOption(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val basePriceEgp: Int,
    val defaultSessions: Int,
    val intervalWeeks: Int,
    val category: ServiceCategory,
    val benefitsAr: List<String>,
    val benefitsEn: List<String>
)

data class UserProfile(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val photoUrl: String = "",
    val role: String = "patient",
    val preferredBranchId: String = "sheraton",
    val preferredCategory: String = "all",
    val notesOrMedicalHistory: String = "",
    val marketingConsent: Boolean = true,
    val totalBookingsCount: Int = 0,
    val vipTier: String = "standard", // "standard", "silver", "gold_vip"
    val createdAt: String = ""
)
