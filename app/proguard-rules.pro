# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}


# https://github.com/delight-im/Android-AdvancedWebView
-keep class * extends android.webkit.WebChromeClient { *; }
-dontwarn im.delight.android.webview.**

#Rxbinding
-dontwarn com.google.auto.value.AutoValue

-dontwarn dagger.android.*
-dontwarn kotlin.reflect.jvm.internal.*
-dontwarn kotlin.reflect.jvm.internal.impl.protobuf.*

# Retrofit 2.X
## https://square.github.io/retrofit/ ##

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

#http
-dontwarn okhttp3.**
-dontwarn okio.**
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

#databinding
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }
-keep class android.databinding.annotationprocessor.** { *; }


-dontwarn io.rx_cache.internal.**
-keepclassmembers enum io.rx_cache.Source { *; }
-keep class io.rx_cache2.** { *; }

# bottom navigation
-keep public class android.support.design.widget.BottomNavigationView { *; }
-keep public class android.support.design.internal.BottomNavigationMenuView { *; }
-keep public class android.support.design.internal.BottomNavigationPresenter { *; }
-keep public class android.support.design.internal.BottomNavigationItemView { *; }


# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-dontwarn com.google.gson.Gson$6

# For using GSON @Expose annotation
-keepattributes *Annotation*


-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**

# Serializable
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# slf4j
-dontwarn javax.naming.**
-dontwarn javax.servlet.**
-dontwarn org.slf4j.**

# Model
-keep class com.hucet.flickr.vo.** { *; }

# crashlytics
-keep class com.crashlytics.** { *; }
-keep class com.crashlytics.android.**
-keepattributes SourceFile, LineNumberTable, *Annotation*

# If you are using custom exceptions, add this line so that custom exception types are skipped during obfuscation:
-keep public class * extends java.lang.Exception

# For Fabric to properly de-obfuscate your crash reports, you need to remove this line from your ProGuard config:
-printmapping build/output/mapping/prod/release/mapping.txt