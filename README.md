# 🌸 Cairo Cosmo Center - Android Application
# مركز القاهرة الطبي لجراحات التجميل والليزر

[![Android](https://img.shields.io/badge/Android-SDK%2036-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-M3-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Gradle](https://img.shields.io/badge/Gradle-9.3.1-02303A?logo=gradle&logoColor=white)](https://gradle.org/)

---

## 📖 Overview | نظرة عامة

**Cairo Cosmo Center** is a native Android application rewritten from the Cairo Cosmo beauty clinic web platform into modern Kotlin and Jetpack Compose (Material Design 3). It preserves the complete clinic experience for patients and medical staff:

- **Bilingual Experience**: Full Arabic (RTL) and English (LTR) with instant one-tap language switching.
- **Academic Medical Team**: Detailed profiles of senior university faculty from Ain Shams and Cairo Universities (Prof. Dr. Basim Zaki, Prof. Dr. Ahmed El Sherif, Prof. Dr. Nahed Samir).
- **Comprehensive Procedures Catalog**: Botox, Russian Dermal Fillers, Candela GentleLase Hair Removal, HydraFacial Vortex Glow, Chemical & Cold Peeling, VASER 4D Sculpting.
- **Interactive Beauty Calculator**: Instant calculation of recommended sessions, estimated cost in EGP, and multi-session savings.
- **Smart Appointment Booking**: Streamlined booking with clinic branch selection, doctor preference, date & time slots, automated booking reference code (`#CCC-XXXX`), and instant WhatsApp sharing.
- **Exclusive Packages & Offers**: Limited-time cosmetic discounts with copyable promo codes.
- **Clinic Administration Portal**: Real-time receptionist dashboard to review, search, and update appointment statuses (Confirmed, Pending, Completed, Cancelled).

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 2.2.10
- **UI Toolkit**: Jetpack Compose with Material Design 3 (M3)
- **Design Tokens**: Luxury Rose Gold & Deep Burgundy Silk palette (`#7B1E4A`, `#D4AF37`)
- **Typography**: Authentic Cairo Google Font locally bundled
- **Adaptive Launcher Icon**: Custom adaptive vector/bitmap icon in Burgundy and Gold
- **Architecture**: MVVM with Repository pattern and Kotlin StateFlow

---

## 📂 Project Structure

```text
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/
│       │   ├── MainActivity.kt
│       │   ├── data/
│       │   │   ├── Models.kt
│       │   │   ├── CosmoData.kt
│       │   │   └── CosmoRepository.kt
│       │   └── ui/
│       │       ├── CosmoApp.kt
│       │       ├── theme/
│       │       │   ├── Color.kt
│       │       │   ├── Type.kt
│       │       │   └── Theme.kt
│       │       ├── components/
│       │       │   ├── TopBar.kt
│       │       │   └── BottomNavBar.kt
│       │       └── screens/
│       │           ├── HomeScreen.kt
│       │           ├── ServicesScreen.kt
│       │           ├── DoctorsScreen.kt
│       │           ├── CalculatorScreen.kt
│       │           ├── BookingScreen.kt
│       │           ├── OffersScreen.kt
│       │           └── AdminPortalScreen.kt
│       └── res/
│           ├── drawable/ (clinic doctor photos & service images)
│           ├── font/ (cairo.ttf)
│           ├── mipmap-*/ (adaptive & legacy launcher icons)
│           └── values/ (strings.xml, colors.xml, themes.xml)
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── metadata.json
```
