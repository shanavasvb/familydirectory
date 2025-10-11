
```markdown
# 🏛️ Family Directory

<div align="center">

![App Icon](https://img.shields.io/badge/Android-Family_Directory-green?style=for-the-badge&logo=android)
![Version](https://img.shields.io/badge/Version-1.0-blue?style=for-the-badge)
![API](https://img.shields.io/badge/API-26+-brightgreen?style=for-the-badge)
![Language](https://img.shields.io/badge/Kotlin-100%25-purple?style=for-the-badge&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-UI-4285F4?style=for-the-badge&logo=jetpack-compose)

**A comprehensive digital family directory app for the  community, preserving family connections and heritage across Kerala and beyond.**

[Download APK](https://github.com/shanavasvb/familydirectory/releases) • [Report Bug](https://github.com/shanavasvb/familydirectory/issues) • [Request Feature](https://github.com/shanavasvb/familydirectory/issues)

</div>

---

## 📖 Table of Contents

- [About The Project](#-about-the-project)
- [Features](#-features)
- [Screenshots](#-screenshots)
- [Technology Stack](#-technology-stack)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
- [Project Structure](#-project-structure)
- [Firebase Configuration](#-firebase-configuration)
- [Building the App](#-building-the-app)
- [Usage Guide](#-usage-guide)
- [Admin Features](#-admin-features)
- [Data Structure](#-data-structure)
- [Roadmap](#-roadmap)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact](#-contact)
- [Acknowledgments](#-acknowledgments)

---

## 🌟 About The Project

The ** Family Directory** is a modern Android application designed to digitally preserve and connect the  community. Originally migrating from Wayanad to various parts of Kerala and beyond, this app serves as a comprehensive directory of family members, their locations, and relationships.

### 🎯 Purpose

- **Preserve Heritage**: Digitally document family lineage and connections
- **Community Connection**: Help family members find and connect with each other
- **Event Sharing**: Share family events, gatherings, and important announcements
- **Emergency Contacts**: Quick access to family members for urgent situations
- **Historical Record**: Maintain detailed records of family history and migrations

### 🌍 Community Reach

The app serves families across:
- **Kerala**: Ernakulam, Wayanad, Palakkad
- **Tamil Nadu**: Nilgiri District
- **International**: Kuwait, Saudi Arabia, UK, Canada, Qatar

---

## ✨ Features

### 🔍 **Smart Search & Filter**

- **Advanced Search**: Search by name, place, phone number, email, parish, region
- **Dynamic Filters**: Filter by:
  - 🏛️ Parish/Church (6+ parishes supported)
  - 📍 Region (Kumbleri, Nilgiri, Valiyaveedu, Elanthuruthy, മാത്താംപാട്ട)
  - 🩸 Blood Group (A+, A-, B+, B-, AB+, AB-, O+, O-)
  - 👥 Gender (Male/Female)
- **Real-time Results**: Instant search results with highlighted matches
- **Filter Combinations**: Apply multiple filters simultaneously

### 👨‍👩‍👧‍👦 **Family Details**

- **Complete Family Tree**: Head of family and all members
- **Detailed Information**:
  - Name, Relation, Date of Birth
  - Education, Occupation, Institution
  - Contact: Phone, Email
  - Location: Place, Post Office, Parish, Region
  - Medical: Blood Group
  - Family Connections: Father, Mother, Spouse names
- **Family Member Cards**: Beautiful UI showing all family members
- **Spouse Details**: Track marriage connections between families

### 📸 **Event Gallery**

- **Community Events**: Upload and view family gatherings, celebrations
- **Photo Gallery**: Beautiful grid layout for event photos
- **Event Details**: Event name, description, date
- **Image Upload**: Admin can upload multiple photos per event
- **Cloud Storage**: Images hosted on Cloudinary for fast loading

### 🔐 **Admin Panel**

- **Secure Login**: Password-protected admin access
- **Data Management**:
  - ✅ Add new families
  - ✏️ Edit existing records
  - 🗑️ Remove outdated entries
- **Event Management**: Upload events with multiple photos
- **Dual Input Methods**:
  - 📝 Form-based entry (user-friendly)
  - 💻 JSON bulk upload (advanced)
- **Data Validation**: Ensures data integrity
- **Offline Capable**: Changes sync when connected

### 🎨 **Beautiful UI/UX**

- **Material Design 3**: Modern, intuitive interface
- **Kerala Theme Colors**:
  - 🔵 Deep Royal Blue (primary)
  - 🟡 Heritage Gold (accent)
  - 🟠 Warm Terracotta (secondary)
- **Bilingual Support**: Malayalam and English throughout
- **Dark Mode Ready**: Optimized color schemes
- **Smooth Animations**: Polished transitions and interactions
- **Responsive Design**: Works on all screen sizes

### 🚀 **Performance & Optimization**

- **Fast Loading**: Optimized Firebase queries
- **Efficient Caching**: Smart data caching with Coil
- **Low Memory Usage**: Efficient image loading
- **Small APK Size**: Only 16MB for full app
- **Offline Support**: View cached data without internet
- **Background Sync**: Automatic data updates

---

## 📱 Screenshots

<div align="center">

| Search Screen | Family Details | Filters | Events |
|:---:|:---:|:---:|:---:|
| ![Search](screenshots/search.png) | ![Details](screenshots/details.png) | ![Filters](screenshots/filters.png) | ![Events](screenshots/events.png) |

| Admin Login | Add Family | Upload Event | Form Input |
|:---:|:---:|:---:|:---:|
| ![Admin](screenshots/admin.png) | ![Add](screenshots/add.png) | ![Upload](screenshots/upload.png) | ![Form](screenshots/form.png) |

</div>

---

## 🛠️ Technology Stack

### **Frontend**
- ![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-7F52FF?logo=kotlin)
- ![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-1.5.1-4285F4?logo=jetpack-compose)
- ![Material 3](https://img.shields.io/badge/Material_Design_3-Latest-757575?logo=material-design)

### **Backend & Database**
- ![Firebase](https://img.shields.io/badge/Firebase_Firestore-FFCA28?logo=firebase)
- ![Firebase Auth](https://img.shields.io/badge/Firebase_Auth-FFCA28?logo=firebase)

### **Image Handling**
- ![Cloudinary](https://img.shields.io/badge/Cloudinary-3448C5?logo=cloudinary)
- ![Coil](https://img.shields.io/badge/Coil-Image_Loading-blue)

### **Architecture & Libraries**
- ![MVVM](https://img.shields.io/badge/Architecture-MVVM-green)
- ![Coroutines](https://img.shields.io/badge/Coroutines-1.9.0-orange?logo=kotlin)
- ![Flow](https://img.shields.io/badge/StateFlow-Reactive-purple)
- ![Navigation](https://img.shields.io/badge/Navigation-Compose-blue)
- ![ViewModel](https://img.shields.io/badge/ViewModel-Lifecycle-green)
- ![DataStore](https://img.shields.io/badge/DataStore-Preferences-yellow)

### **Development Tools**
- ![Android Studio](https://img.shields.io/badge/Android_Studio-Ladybug-3DDC84?logo=android-studio)
- ![Gradle](https://img.shields.io/badge/Gradle-8.10.2-02303A?logo=gradle)
- ![Git](https://img.shields.io/badge/Git-Version_Control-F05032?logo=git)

---

## 🏗️ Architecture

The app follows **Clean Architecture** principles with **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                     Presentation Layer                   │
│  ┌───────────┐  ┌───────────┐  ┌───────────┐           │
│  │  Search   │  │  Details  │  │   Admin   │           │
│  │  Screen   │  │  Screen   │  │  Screen   │           │
│  └─────┬─────┘  └─────┬─────┘  └─────┬─────┘           │
│        │              │              │                   │
│  ┌─────▼──────────────▼──────────────▼─────┐           │
│  │           ViewModel Layer                 │           │
│  │  • SearchViewModel                        │           │
│  │  • FamilyDetailsViewModel                 │           │
│  │  • AdminUploadViewModel                   │           │
│  └─────────────────┬─────────────────────────┘           │
└────────────────────┼──────────────────────────────────────┘
                     │
┌────────────────────▼──────────────────────────────────────┐
│                   Domain Layer                             │
│  ┌─────────────────────────────────────────────┐          │
│  │         Use Cases (Business Logic)          │          │
│  │  • Search Families                          │          │
│  │  • Filter by Parish/Region                  │          │
│  │  • Add/Edit/Delete Family                   │          │
│  │  • Upload Events                            │          │
│  └─────────────────┬───────────────────────────┘          │
└────────────────────┼────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│                    Data Layer                                │
│  ┌──────────────────┐     ┌──────────────────┐             │
│  │   Repository     │     │   Data Models    │             │
│  │  • FamilyRepo    │     │  • Family        │             │
│  │  • EventRepo     │     │  • FamilyMember  │             │
│  └────────┬─────────┘     │  • Event         │             │
│           │               │  • SearchFilters │             │
│  ┌────────▼─────────┐     └──────────────────┘             │
│  │   Data Sources   │                                       │
│  │  • Firebase      │                                       │
│  │  • Cloudinary    │                                       │
│  │  • DataStore     │                                       │
│  └──────────────────┘                                       │
└──────────────────────────────────────────────────────────────┘
```

### **Key Design Patterns**

- **MVVM**: Clear separation of UI and business logic
- **Repository Pattern**: Abstract data sources
- **Observer Pattern**: StateFlow for reactive UI
- **Dependency Injection**: Manual DI (can be upgraded to Hilt)
- **Single Source of Truth**: Firebase as authoritative data source

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Ladybug | 2024.2.1 or higher
- **JDK**: Version 11 or higher
- **Android SDK**: API 26 (Android 8.0) minimum
- **Git**: For version control
- **Firebase Account**: For backend services
- **Cloudinary Account**: For image hosting (optional)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/shanavasvb/familydirectory.git
   cd familydirectory
   ```

2. **Open in Android Studio**
   ```
   File → Open → Select the 'familydirectory' folder
   ```

3. **Configure Firebase**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project or use existing
   - Add Android app with package name: `com.example.familydirectory`
   - Download `google-services.json`
   - Place it in `app/` directory

4. **Configure Cloudinary (Optional)**
   
   Update in `app/build.gradle.kts`:
   ```kotlin
   buildConfigField("String", "CLOUDINARY_CLOUD_NAME", "\"your_cloud_name\"")
   buildConfigField("String", "CLOUDINARY_UPLOAD_PRESET", "\"your_preset\"")
   ```

5. **Sync Gradle**
   ```
   File → Sync Project with Gradle Files
   ```

6. **Run the app**
   ```
   Run → Run 'app'
   ```
   or press `Shift + F10`

---

## 📂 Project Structure

```
familydirectory/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/familydirectory/
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── Family.kt
│   │   │   │   │   │   ├── FamilyMember.kt
│   │   │   │   │   │   ├── Event.kt
│   │   │   │   │   │   ├── SearchFilters.kt
│   │   │   │   │   │   ├── SearchResult.kt
│   │   │   │   │   │   └── FilterOptions.kt
│   │   │   │   │   └── repository/
│   │   │   │   │       ├── FamilyRepository.kt
│   │   │   │   │       └── EventRepository.kt
│   │   │   │   │
│   │   │   │   ├── ui/
│   │   │   │   │   ├── theme/
│   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   ├── Theme.kt
│   │   │   │   │   │   └── Type.kt
│   │   │   │   │   │
│   │   │   │   │   ├── search/
│   │   │   │   │   │   ├── SearchScreen.kt
│   │   │   │   │   │   ├── SearchViewModel.kt
│   │   │   │   │   │   └── FilterBottomSheet.kt
│   │   │   │   │   │
│   │   │   │   │   ├── details/
│   │   │   │   │   │   ├── FamilyDetailsScreen.kt
│   │   │   │   │   │   └── FamilyDetailsViewModel.kt
│   │   │   │   │   │
│   │   │   │   │   ├── events/
│   │   │   │   │   │   ├── EventsScreen.kt
│   │   │   │   │   │   └── EventsViewModel.kt
│   │   │   │   │   │
│   │   │   │   │   ├── admin/
│   │   │   │   │   │   ├── AdminLoginScreen.kt
│   │   │   │   │   │   ├── AdminUploadScreen.kt
│   │   │   │   │   │   ├── AdminUploadViewModel.kt
│   │   │   │   │   │   ├── FormUploadContent.kt
│   │   │   │   │   │   └── JsonUploadContent.kt
│   │   │   │   │   │
│   │   │   │   │   └── upload/
│   │   │   │   │       ├── UploadEventScreen.kt
│   │   │   │   │       └── UploadEventViewModel.kt
│   │   │   │   │
│   │   │   │   ├── navigation/
│   │   │   │   │   └── NavGraph.kt
│   │   │   │   │
│   │   │   │   └── MainActivity.kt
│   │   │   │
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   ├── values/
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   └── mipmap/
│   │   │   │
│   │   │   └── AndroidManifest.xml
│   │   │
│   │   └── test/
│   │
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── google-services.json (gitignored)
│
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│
├── build.gradle.kts
├── settings.gradle.kts
├── .gitignore
├── README.md
└── LICENSE
```

### **Key Directories**

- **`data/model/`**: Data classes representing entities
- **`data/repository/`**: Data access layer, Firebase interactions
- **`ui/`**: All UI screens and composables
- **`ui/theme/`**: App theming, colors, typography
- **`navigation/`**: Navigation graph and routes

---

## 🔥 Firebase Configuration

### **Firestore Database Structure**

```javascript
families/
├── {familyId}/
│   ├── familyHead: String
│   ├── place: String
│   ├── postOffice: String
│   ├── region: String
│   ├── parish: String
│   ├── phone: String
│   ├── email: String
│   ├── job: String
│   ├── education: String
│   ├── bloodGroup: String
│   ├── dob: String
│   ├── gender: String
│   ├── fatherName: String
│   ├── motherName: String
│   ├── district: String
│   ├── otherInfo: String
│   ├── spouseDetails: Array<String>
│   ├── createdAt: Timestamp
│   └── familyMembers: Array<Object>
│       ├── name: String
│       ├── relation: String
│       ├── fatherName: String
│       ├── motherName: String
│       ├── spouseName: String
│       ├── dob: String
│       ├── education: String
│       ├── job: String
│       ├── institution: String
│       ├── phone: String
│       ├── email: String
│       ├── bloodGroup: String
│       └── gender: String

events/
├── {eventId}/
│   ├── title: String
│   ├── description: String
│   ├── imageUrls: Array<String>
│   ├── createdAt: Timestamp
│   └── createdBy: String
```

### **Firestore Rules**

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Allow read access to all users
    match /families/{familyId} {
      allow read: if true;
      allow write: if request.auth != null;
    }
    
    match /events/{eventId} {
      allow read: if true;
      allow write: if request.auth != null;
    }
  }
}
```

### **Firebase Authentication**

Admin authentication uses Firebase Auth:

```kotlin
// Configured for password-based login
// Admin credentials stored securely
```

### **Cloudinary Configuration**

For image uploads:

```kotlin
// Cloud name: dqst9rwur
// Upload preset: family_app_events
// Auto-optimization enabled
// Format: Auto (WebP when supported)
```

---

## 🔨 Building the App

### **Debug Build (For Testing)**

```bash
# Clean previous builds
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# APK location
app/build/outputs/apk/debug/app-debug.apk
```

### **Release Build (For Distribution)**

**Step 1: Generate Keystore (First time only)**

```bash
keytool -genkey -v \
  -keystore malikudy-release-key.jks \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -alias malikudy
```

**Step 2: Configure Signing**

Update `app/build.gradle.kts`:

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("../malikudy-release-key.jks")
            storePassword = "your_password"
            keyAlias = "malikudy"
            keyPassword = "your_password"
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

**Step 3: Build Release APK**

```bash
# Build release APK
./gradlew assembleRelease

# APK location
app/build/outputs/apk/release/app-release.apk
```

### **Build Script**

Create `build.sh`:

```bash
#!/bin/bash

echo "🚀 Building  Kudumbayogam App..."

# Get version
VERSION=$(grep "versionName" app/build.gradle.kts | cut -d'"' -f2)

# Clean and build
./gradlew clean assembleRelease

if [ -f "app/build/outputs/apk/release/app-release.apk" ]; then
    SIZE=$(du -h app/build/outputs/apk/release/app-release.apk | cut -f1)
    
    echo "✅ Build Successful!"
    echo "📦 Size: $SIZE"
    echo "📍 Version: $VERSION"
    
    cp app/build/outputs/apk/release/app-release.apk \
       ~/MalikudyKudumbayogam-v${VERSION}.apk
    
    echo "📋 Saved to: ~/MalikudyKudumbayogam-v${VERSION}.apk"
else
    echo "❌ Build Failed!"
fi
```

---

## 📘 Usage Guide

### **For Regular Users**

#### **1. Search for Families**

```
Open App → Search Bar → Type name/place/phone
```

- Real-time search results
- Highlighted matching terms
- Shows matched field (name, place, etc.)

#### **2. Apply Filters**

```
Click Filter Icon → Select filters → Apply
```

**Available Filters:**
- **Parish**: 6 churches supported
- **Region**: 5 regions (including Malayalam)
- **Blood Group**: All 8 types
- **Gender**: Male/Female

#### **3. View Family Details**

```
Search Results → Tap Family Card → View Full Details
```

Shows:
- Family head information
- All family members
- Contact details
- Relationships
- Spouse connections

#### **4. View Events**

```
Bottom Navigation → Events → Browse Gallery
```

- View community events
- See event photos
- Read descriptions

### **For Administrators**

#### **1. Admin Login**

```
Search Screen → Admin Icon (Top Right) → Enter Password
```

Default admin password configured in Firebase Auth.

#### **2. Add New Family**

**Method A: Form Input**
```
Admin Panel → Add Family → Form Tab → Fill Details → Submit
```

**Method B: JSON Upload**
```
Admin Panel → Add Family → JSON Tab → Paste JSON → Upload
```

#### **3. Upload Events**

```
Admin Panel → Upload Event
→ Enter Title & Description
→ Select Images
→ Upload
```

**Supported:**
- Multiple images per event
- Auto-optimization via Cloudinary
- Progress indication

#### **4. Data Management**

```
Admin Panel → Manage Data
→ Edit/Delete families
→ Normalize data (clean duplicates)
```

---

## 🔐 Admin Features

### **Admin Password**

Set in `AdminLoginScreen.kt`:

```kotlin
private val ADMIN_PASSWORD = "your_secure_password"
```

**Recommendation**: Use Firebase Remote Config for dynamic password management.

### **Data Normalization**

Standardizes inconsistent data:

```kotlin
// Normalizes parish names
"St.Marys Church, Kuruppampady" 
→ "St. Mary's Orthodox Church, Kuruppampady"

// Infers missing regions
place = "Iringole", region = "" 
→ region = "Valiyaveedu"
```

Run via admin panel or programmatically:

```kotlin
viewModel.normalizeData { count ->
    println("Normalized $count families")
}
```

### **Bulk Data Upload**

JSON format for family data:

```json
{
  "familyHead": "John Mathew",
  "place": "Iringole",
  "postOffice": "Iringole",
  "region": "Valiyaveedu",
  "parish": "St. Mary's Orthodox Church, Kuruppampady",
  "phone": "9497547284",
  "email": "john@example.com",
  "job": "Teacher",
  "education": "M.A.",
  "bloodGroup": "B positive (B+)",
  "dob": "1975-05-15",
  "gender": "Male",
  "fatherName": "Mathew K",
  "motherName": "Mary M",
  "familyMembers": [
    {
      "name": "Sarah John",
      "relation": "Wife",
      "dob": "1978-08-20",
      "gender": "Female",
      "bloodGroup": "A positive (A+)"
    }
  ]
}
```

---

## 📊 Data Structure

### **Family Model**

```kotlin
data class Family(
    val id: String = "",
    val familyHead: String = "",
    val place: String = "",
    val postOffice: String = "",
    val region: String = "",
    val parish: String = "",
    val phone: String = "",
    val email: String = "",
    val job: String = "",
    val education: String = "",
    val bloodGroup: String = "",
    val dob: String = "",
    val gender: String = "",
    val fatherName: String = "",
    val motherName: String = "",
    val district: String = "",
    val otherInfo: String = "",
    val spouseDetails: List<String> = emptyList(),
    val familyMembers: List<FamilyMember> = emptyList(),
    val createdAt: Timestamp? = null
)
```

### **Family Member Model**

```kotlin
data class FamilyMember(
    val id: String = "",
    val name: String = "",
    val relation: String = "",
    val fatherName: String = "",
    val motherName: String = "",
    val spouseName: String = "",
    val dob: String = "",
    val education: String = "",
    val job: String = "",
    val institution: String = "",
    val phone: String = "",
    val email: String = "",
    val bloodGroup: String = "",
    val gender: String = ""
)
```

### **Event Model**

```kotlin
data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrls: List<String> = emptyList(),
    val createdAt: Timestamp? = null,
    val createdBy: String = ""
)
```

### **Search Filters Model**

```kotlin
data class SearchFilters(
    val parish: String? = null,
    val region: String? = null,
    val bloodGroup: String? = null,
    val gender: String? = null
)
```

---

## 🗺️ Roadmap

### **Version 1.1** (Planned)
- [ ] Family tree visualization
- [ ] Export family data to PDF
- [ ] Birthday notifications
- [ ] Advanced search with date ranges
- [ ] Multi-language support (Malayalam, English, Hindi)

### **Version 1.2** (Planned)
- [ ] Family chat groups
- [ ] Event RSVP system
- [ ] Family photo albums
- [ ] Anniversary reminders
- [ ] Family news feed

### **Version 2.0** (Future)
- [ ] Interactive family tree with zoom
- [ ] DNA/ancestry integration
- [ ] Historical document storage
- [ ] Family wiki/encyclopedia
- [ ] Video interviews archive
- [ ] Migration maps
- [ ] Genealogy reports

### **Long-term Vision**
- [ ] Multi-family federation support
- [ ] Cross-community connections
- [ ] Academic research tools
- [ ] Historical preservation archive
- [ ] Mobile + Web versions
- [ ] AI-powered relationship discovery

---

## 🤝 Contributing

Contributions are what make the open-source community amazing! Any contributions you make are **greatly appreciated**.

### **How to Contribute**

1. **Fork the Project**
   ```bash
   # Click 'Fork' on GitHub
   ```

2. **Clone Your Fork**
   ```bash
   git clone https://github.com/YOUR_USERNAME/familydirectory.git
   cd familydirectory
   ```

3. **Create Feature Branch**
   ```bash
   git checkout -b feature/AmazingFeature
   ```

4. **Make Changes**
   - Write clean, documented code
   - Follow Kotlin coding conventions
   - Test thoroughly

5. **Commit Changes**
   ```bash
   git add .
   git commit -m "Add: Amazing new feature"
   ```

6. **Push to Branch**
   ```bash
   git push origin feature/AmazingFeature
   ```

7. **Open Pull Request**
   - Go to GitHub
   - Click "New Pull Request"
   - Describe your changes

### **Contribution Guidelines**

- ✅ Follow MVVM architecture
- ✅ Use Jetpack Compose for UI
- ✅ Write meaningful commit messages
- ✅ Add comments for complex logic
- ✅ Test on multiple devices
- ✅ Update documentation
- ✅ Respect bilingual requirements (Malayalam + English)

### **Code Style**

```kotlin
// Use descriptive names
val familyMembersList = getFamilyMembers()

// Add documentation
/**
 * Searches families based on query and filters
 * @param query Search term
 * @param filters Active filters
 * @return Result with list of SearchResult
 */
suspend fun searchFamilies(query: String, filters: SearchFilters)

// Use proper spacing
fun example() {
    // Code here
}
```

### **Reporting Bugs**

Found a bug? Please open an issue with:

```markdown
**Bug Description**
Clear description of the bug

**Steps to Reproduce**
1. Go to '...'
2. Click on '....'
3. See error

**Expected Behavior**
What should happen

**Screenshots**
If applicable

**Device Info**
- Device: [e.g. Samsung Galaxy S21]
- OS: [e.g. Android 12]
- App Version: [e.g. 1.0]
```

---

## 📜 License

Distributed under the MIT License. See `LICENSE` for more information.

```
MIT License

Copyright (c) 2025 Shanavasvb

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 📞 Contact

**Developer**: Shanavasvb

- GitHub: [@shanavasvb](https://github.com/shanavasvb)
- Email: shanavasvb@example.com
- LinkedIn: [linkedin.com/in/shanavasvb](https://linkedin.com/in/shanavasvb)

**Project Link**: [https://github.com/shanavasvb/familydirectory](https://github.com/shanavasvb/familydirectory)

**Community**:
- Report bugs: [GitHub Issues](https://github.com/shanavasvb/familydirectory/issues)
- Request features: [GitHub Discussions](https://github.com/shanavasvb/familydirectory/discussions)
- Ask questions: [GitHub Q&A](https://github.com/shanavasvb/familydirectory/discussions/categories/q-a)

---

## 🙏 Acknowledgments

### **Special Thanks**

- **Malikudy Kudumbayogam Committee** - For vision and support
- **Family Elders** - For providing historical data and genealogy
- **Beta Testers** - For valuable feedback
- **Community Members** - For data contribution

### **Technologies Used**

- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern Android UI
- [Firebase](https://firebase.google.com/) - Backend infrastructure
- [Cloudinary](https://cloudinary.com/) - Image management
- [Coil](https://coil-kt.github.io/coil/) - Image loading
- [Material Design 3](https://m3.material.io/) - Design system
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - Async operations

### **Inspiration**

This project was inspired by the need to preserve family heritage and strengthen community bonds in an increasingly digital world.

### **Resources**

- [Android Developer Documentation](https://developer.android.com/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Material Design Guidelines](https://material.io/design)

---

## 📈 Statistics

![Repository Size](https://img.shields.io/github/repo-size/shanavasvb/familydirectory)
![Code Size](https://img.shields.io/github/languages/code-size/shanavasvb/familydirectory)
![Last Commit](https://img.shields.io/github/last-commit/shanavasvb/familydirectory)
![Issues](https://img.shields.io/github/issues/shanavasvb/familydirectory)
![Pull Requests](https://img.shields.io/github/issues-pr/shanavasvb/familydirectory)
![Stars](https://img.shields.io/github/stars/shanavasvb/familydirectory)
![Forks](https://img.shields.io/github/forks/shanavasvb/familydirectory)
![License](https://img.shields.io/github/license/shanavasvb/familydirectory)

---

## 🌟 Star History

[![Star History Chart](https://api.star-history.com/svg?repos=shanavasvb/familydirectory&type=Date)](https://star-history.com/#shanavasvb/familydirectory&Date)

---

<div align="center">

### Made with ❤️ for the  Community

**If this project helps you, please consider giving it a ⭐!**

[⬆ Back to Top](#-മാളികുടി-കുടുംബയോഗം--malikudy-kudumbayogam-family-directory)

</div>

---

## 📝 Changelog

### Version 1.0 (October 11, 2025)

**Initial Release** 🎉

**Features:**
- ✅ Complete family directory (100+ families)
- ✅ Advanced search and filtering
- ✅ Family details with all members
- ✅ Event gallery system
- ✅ Admin panel for data management
- ✅ Bilingual UI (Malayalam + English)
- ✅ Beautiful Material Design 3 interface
- ✅ Firebase integration
- ✅ Cloudinary image hosting
- ✅ Optimized performance

**Coverage:**
- 12 families documented
- 6 parishes supported
- 5 regions covered
- 3 districts (Ernakulam, Wayanad, Nilgiri)
- Multiple international locations

**Performance:**
- APK size: 16 MB
- Load time: <2 seconds
- Search speed: Real-time
- Image loading: Optimized with Coil

---

<div align="center">

**Built with passion for preserving family heritage** 🏛️

**Serving the Malikudy community since 2025** 👨‍👩‍👧‍👦

</div>
```

---

## 📁 Additional Files to Create

### **1. LICENSE file**

Create `LICENSE`:

```
MIT License

Copyright (c) 2025 Shanavasvb

[Full MIT License text as shown above]
```

### **2. .gitignore**

Create/update `.gitignore`:

```gitignore
# Built application files
*.apk
*.ap_
*.aab

# Files for the ART/Dalvik VM
*.dex

# Java class files
*.class

# Generated files
bin/
gen/
out/
release/

# Gradle files
.gradle/
build/

# Local configuration file (sdk path, etc)
local.properties

# IntelliJ
*.iml
.idea/
misc.xml
deploymentTargetDropDown.xml
render.experimental.xml

# Keystore files
*.jks
*.keystore

# Google Services (API keys)
google-services.json

# OS-specific files
.DS_Store
.DS_Store?
._*
.Spotlight-V100
.Trashes
ehthumbs.db
Thumbs.db

# Android Studio
captures/
.externalNativeBuild
.cxx
*.apk
output.json

# Signing files
.signing/
```

### **3. CONTRIBUTING.md**

Create `CONTRIBUTING.md`:

```markdown
# Contributing to Malikudy Kudumbayogam Family Directory

Thank you for your interest in contributing! 🎉

[Full contribution guidelines as outlined in the README]
```

### **4. CODE_OF_CONDUCT.md**

Create `CODE_OF_CONDUCT.md`:

```markdown
# Code of Conduct

## Our Pledge

We pledge to make participation in our project a harassment-free experience for everyone.

## Our Standards

- Be respectful
- Be welcoming
- Be collaborative
- Focus on what is best for the community

## Enforcement

Report issues to shanavasvb@example.com
```

---

