# Add project specific ProGuard rules here.

# Keep Firebase
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Firebase Firestore
-keep class com.google.firebase.firestore.** { *; }
-keep class com.google.firebase.** { *; }
-keepnames class com.google.firebase.** { *; }

# Keep your data models
-keep class com.example.familydirectory.data.model.** { *; }

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }
-dontwarn kotlin.**
-dontwarn kotlinx.**

# Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Cloudinary
-keep class com.cloudinary.** { *; }
-dontwarn com.cloudinary.**

# ✅ Ignore Glide warnings (we use Coil instead)
-dontwarn com.bumptech.glide.**
-dontwarn com.squareup.picasso.**

# Coil (our actual image library)
-keep class coil.** { *; }
-dontwarn coil.**

# OkHttp & Okio (used by Coil)
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# Remove println in release
-assumenosideeffects class java.io.PrintStream {
    public void println(%);
    public void println(**);
}

# Keep R8 compatibility
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}