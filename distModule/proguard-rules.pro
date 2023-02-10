# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers enum * { *; }
-keepclassmembers enum * {
   public *;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepattributes JavascriptInterface
-keepattributes *Annotation*

-dontwarn com.razorpay.**
-keep class com.razorpay.** {*;}

-optimizations !method/inlining/*

-keepclasseswithmembers class * {
  public void onPayment*(...);
}



-keepclassmembernames interface * {
    @retrofit.http.* <methods>;
}

-keep class * extends androidx.fragment.app.Fragment{}
-keepnames class * extends android.os.Parcelable
-keepnames class * extends java.io.Serializable

-keep class com.a2z.app.data.model.**  { *; }
-keep class com.a2z.app.data.model.dmt.**  { *; }
-keep class com.a2z.app.data.model.report.**  { *; }
-keep class com.a2z.app.data.model.matm.**  { *; }
-keep class com.a2z.app.model.**  { *; }
-keep class com.a2z.app.model.pancard.**  { *; }
-keep class com.a2z.app.model.ParentPaymentRefund.**  { *; }
-keep class com.a2z.app.model.pg.**  { *; }
-keep class com.a2z.app.model.upiData.**  { *; }
-keep class okhttp3.**  { *; }
-keep interface okhttp3.**  { *; }

-keepnames class androidx.navigation.fragment.NavHostFragment
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn okhttp3.**

