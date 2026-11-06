# Proguard rules for FolioReader
-keepattributes *Annotation*
-keepclassmembers class * {
    native <methods>;
}
-keep class com.hmndev.folioreader.core.** { *; }
-keep class com.google.gson.** { *; }
