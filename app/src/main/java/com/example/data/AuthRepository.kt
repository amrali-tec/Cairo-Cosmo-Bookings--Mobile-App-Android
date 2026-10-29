package com.example.data

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.R
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore
) {
    // Secondary constructor resolving databaseId from string resources
    constructor(context: Context) : this(
        auth = FirebaseAuth.getInstance(),
        db = FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _allRegisteredPatients = MutableStateFlow<List<UserProfile>>(emptyList())
    val allRegisteredPatients: StateFlow<List<UserProfile>> = _allRegisteredPatients.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _currentUser.value = user
            if (user != null) {
                fetchUserProfile(user)
            } else {
                _userProfile.value = null
            }
        }
        loadAllPatients()
    }

    private fun fetchUserProfile(user: FirebaseUser) {
        scope.launch {
            try {
                val doc = db.collection("users").document(user.uid).get().await()
                if (doc.exists()) {
                    val bookingsCount = doc.getLong("totalBookingsCount")?.toInt() ?: 0
                    val tier = doc.getString("vipTier") ?: calculateVipTier(bookingsCount)
                    _userProfile.value = UserProfile(
                        id = user.uid,
                        fullName = doc.getString("fullName") ?: user.displayName ?: "مريض مركز القاهرة",
                        email = doc.getString("email") ?: user.email ?: "",
                        phone = doc.getString("phone") ?: user.phoneNumber ?: "",
                        photoUrl = doc.getString("photoUrl") ?: user.photoUrl?.toString() ?: "",
                        role = doc.getString("role") ?: "patient",
                        preferredBranchId = doc.getString("preferredBranchId") ?: "sheraton",
                        preferredCategory = doc.getString("preferredCategory") ?: "all",
                        notesOrMedicalHistory = doc.getString("notesOrMedicalHistory") ?: "",
                        marketingConsent = doc.getBoolean("marketingConsent") ?: true,
                        totalBookingsCount = bookingsCount,
                        vipTier = tier,
                        createdAt = doc.getString("createdAt") ?: "2026-10-29"
                    )
                } else {
                    // Create initial profile
                    val profile = UserProfile(
                        id = user.uid,
                        fullName = user.displayName ?: "مريض مركز القاهرة",
                        email = user.email ?: "",
                        phone = user.phoneNumber ?: "",
                        photoUrl = user.photoUrl?.toString() ?: "",
                        role = "patient",
                        preferredBranchId = "sheraton",
                        preferredCategory = "all",
                        notesOrMedicalHistory = "",
                        marketingConsent = true,
                        totalBookingsCount = 0,
                        vipTier = "standard",
                        createdAt = "2026-10-29"
                    )
                    saveUserProfile(profile)
                    _userProfile.value = profile
                }
            } catch (e: Exception) {
                // If firestore read fails, fallback to FirebaseUser info
                _userProfile.value = UserProfile(
                    id = user.uid,
                    fullName = user.displayName ?: "مريض مركز القاهرة",
                    email = user.email ?: "",
                    phone = user.phoneNumber ?: "",
                    photoUrl = user.photoUrl?.toString() ?: "",
                    role = "patient",
                    preferredBranchId = "sheraton",
                    preferredCategory = "all",
                    notesOrMedicalHistory = "",
                    marketingConsent = true,
                    totalBookingsCount = 0,
                    vipTier = "standard",
                    createdAt = "2026-10-29"
                )
            }
        }
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        val data = mapOf(
            "id" to profile.id,
            "fullName" to profile.fullName,
            "email" to profile.email,
            "phone" to profile.phone,
            "photoUrl" to profile.photoUrl,
            "role" to profile.role,
            "preferredBranchId" to profile.preferredBranchId,
            "preferredCategory" to profile.preferredCategory,
            "notesOrMedicalHistory" to profile.notesOrMedicalHistory,
            "marketingConsent" to profile.marketingConsent,
            "totalBookingsCount" to profile.totalBookingsCount,
            "vipTier" to profile.vipTier,
            "createdAt" to profile.createdAt
        )
        try {
            db.collection("users").document(profile.id).set(data).await()
            _userProfile.value = profile
            loadAllPatients()
        } catch (_: Exception) {
            // Local state preserved
            _userProfile.value = profile
        }
    }

    fun syncUserBookingsCount(bookingsCount: Int) {
        val current = _userProfile.value ?: return
        val calculatedTier = calculateVipTier(bookingsCount)
        val updated = current.copy(
            totalBookingsCount = bookingsCount,
            vipTier = calculatedTier
        )
        _userProfile.value = updated
        scope.launch {
            try {
                db.collection("users").document(updated.id).update(
                    mapOf(
                        "totalBookingsCount" to bookingsCount,
                        "vipTier" to calculatedTier
                    )
                ).await()
            } catch (_: Exception) {}
        }
    }

    fun loadAllPatients() {
        scope.launch {
            try {
                val snapshot = db.collection("users").get().await()
                val list = snapshot.documents.mapNotNull { doc ->
                    val id = doc.getString("id") ?: doc.id
                    val name = doc.getString("fullName") ?: return@mapNotNull null
                    val email = doc.getString("email") ?: ""
                    val phone = doc.getString("phone") ?: ""
                    val bCount = doc.getLong("totalBookingsCount")?.toInt() ?: 0
                    val tier = doc.getString("vipTier") ?: calculateVipTier(bCount)
                    UserProfile(
                        id = id,
                        fullName = name,
                        email = email,
                        phone = phone,
                        photoUrl = doc.getString("photoUrl") ?: "",
                        role = doc.getString("role") ?: "patient",
                        preferredBranchId = doc.getString("preferredBranchId") ?: "sheraton",
                        preferredCategory = doc.getString("preferredCategory") ?: "all",
                        notesOrMedicalHistory = doc.getString("notesOrMedicalHistory") ?: "",
                        marketingConsent = doc.getBoolean("marketingConsent") ?: true,
                        totalBookingsCount = bCount,
                        vipTier = tier,
                        createdAt = doc.getString("createdAt") ?: "2026-10-29"
                    )
                }
                if (list.isNotEmpty()) {
                    _allRegisteredPatients.value = list
                } else {
                    _allRegisteredPatients.value = defaultMarketingPatients
                }
            } catch (_: Exception) {
                _allRegisteredPatients.value = defaultMarketingPatients
            }
        }
    }

    suspend fun signInWithGoogle(context: Context): Result<FirebaseUser> {
        _isLoading.value = true
        _authError.value = null
        return try {
            val credentialManager = CredentialManager.create(context)
            val serverClientId = context.getString(R.string.default_web_client_id)

            val googleIdOption = GetSignInWithGoogleOption.Builder(serverClientId).build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val response = credentialManager.getCredential(context, request)
            val credential = response.credential

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                val authResult = auth.signInWithCredential(firebaseCredential).await()
                val user = authResult.user ?: error("FirebaseUser is null")

                fetchUserProfile(user)
                _isLoading.value = false
                Result.success(user)
            } else {
                _isLoading.value = false
                val err = "نوع بيانات الاعتماد غير مدعوم"
                _authError.value = err
                Result.failure(Exception(err))
            }
        } catch (e: Exception) {
            _isLoading.value = false
            val msg = e.localizedMessage ?: "فشل تسجيل الدخول باستخدام حساب Google"
            _authError.value = msg
            Result.failure(e)
        }
    }

    suspend fun signInWithEmail(email: String, pass: String): Result<FirebaseUser> {
        _isLoading.value = true
        _authError.value = null
        return try {
            val result = auth.signInWithEmailAndPassword(email.trim(), pass).await()
            val user = result.user ?: error("User is null")
            fetchUserProfile(user)
            _isLoading.value = false
            Result.success(user)
        } catch (e: Exception) {
            _isLoading.value = false
            val msg = e.localizedMessage ?: "بيانات الدخول غير صحيحة"
            _authError.value = msg
            Result.failure(e)
        }
    }

    suspend fun registerWithEmail(
        name: String,
        email: String,
        phone: String,
        pass: String,
        preferredBranch: String = "sheraton",
        preferredCategory: String = "all",
        marketingConsent: Boolean = true
    ): Result<FirebaseUser> {
        _isLoading.value = true
        _authError.value = null
        return try {
            val result = auth.createUserWithEmailAndPassword(email.trim(), pass).await()
            val user = result.user ?: error("User is null")

            val profile = UserProfile(
                id = user.uid,
                fullName = name.trim(),
                email = email.trim(),
                phone = phone.trim(),
                photoUrl = "",
                role = "patient",
                preferredBranchId = preferredBranch,
                preferredCategory = preferredCategory,
                notesOrMedicalHistory = "",
                marketingConsent = marketingConsent,
                totalBookingsCount = 0,
                vipTier = "standard",
                createdAt = "2026-10-29"
            )
            saveUserProfile(profile)

            _isLoading.value = false
            Result.success(user)
        } catch (e: Exception) {
            _isLoading.value = false
            val msg = e.localizedMessage ?: "فشل إنشاء الحساب الجديد"
            _authError.value = msg
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
        _currentUser.value = null
        _userProfile.value = null
    }

    fun clearError() {
        _authError.value = null
    }

    companion object {
        fun calculateVipTier(bookingsCount: Int): String {
            return when {
                bookingsCount >= 3 -> "gold_vip"
                bookingsCount >= 1 -> "silver"
                else -> "standard"
            }
        }

        val defaultMarketingPatients = listOf(
            UserProfile(
                id = "p-01",
                fullName = "مريم الأحمدي",
                email = "mariam.ahmadi@gmail.com",
                phone = "+201012345678",
                preferredBranchId = "sheraton",
                preferredCategory = "injectables",
                marketingConsent = true,
                totalBookingsCount = 4,
                vipTier = "gold_vip",
                createdAt = "2026-08-15"
            ),
            UserProfile(
                id = "p-02",
                fullName = "شريف النحاس",
                email = "sherif.alnahas@yahoo.com",
                phone = "+201123456789",
                preferredBranchId = "dokki",
                preferredCategory = "body",
                marketingConsent = true,
                totalBookingsCount = 2,
                vipTier = "silver",
                createdAt = "2026-09-02"
            ),
            UserProfile(
                id = "p-03",
                fullName = "دينا الشاذلي",
                email = "dina.elshazly@outlook.com",
                phone = "+201234567890",
                preferredBranchId = "october",
                preferredCategory = "laser",
                marketingConsent = true,
                totalBookingsCount = 5,
                vipTier = "gold_vip",
                createdAt = "2026-07-20"
            ),
            UserProfile(
                id = "p-04",
                fullName = "نوران حسن كمال",
                email = "nouran.kamal@gmail.com",
                phone = "+201098765432",
                preferredBranchId = "mansoura",
                preferredCategory = "skin",
                marketingConsent = true,
                totalBookingsCount = 1,
                vipTier = "silver",
                createdAt = "2026-10-10"
            )
        )
    }
}
