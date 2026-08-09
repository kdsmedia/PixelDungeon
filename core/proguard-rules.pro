# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Program Files (x86)\Android\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn **
# Keep the actual app package (was previously com.shatteredpixel.**, which did
# not match this fork's com.egoal.darkestpixeldungeon package). The codebase
# relies on Class.getName() for message lookups and reflection-based
# deserialization, so these classes must not be renamed.
-keep class com.egoal.darkestpixeldungeon.** { *; }
-keep class com.watabou.** { *; }
-keepattributes SourceFile,LineNumberTable