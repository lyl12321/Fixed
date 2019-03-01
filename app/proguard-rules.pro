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

# removes such information by default, so configure it to keep all of it. 
# 保留所有的本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
# 保留support下的所有类及其内部类
-keep class android.support.** {*;}

# 保留R下面的资源
-keep class **.R$* {*;}

-keep class util.JsonBean.**{*;}

#gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }

-keep class fragment.OrderActivity
-keep class org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keep class com.google.code.gson
-keep class com.squareup.okhttp3