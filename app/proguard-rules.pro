# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/ZhanTao/Documents/04_Program Files/android-sdk-macosx/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-dontwarn java.lang.invoke**
-dontwarn org.apache.lang.**
-dontwarn org.apache.commons.**
-dontwarn com.nhaarman.**
-dontwarn se.emilsjolander.**

-dontwarn com.aphidmobile.**

#umeng start#
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep public class com.kuaipao.manager.R$*{
    public static final int *;
}
-keep public class com.umeng.fb.ui.ThreadView {
}
-dontwarn com.umeng.**
-keep public class * extends com.umeng.**
-keep class com.umeng.** { *; }

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#umeng end#
