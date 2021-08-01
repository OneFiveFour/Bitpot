package net.onefivefour.android.bitpot.app

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.paging.ExperimentalPagingApi
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import net.onefivefour.android.bitpot.BitpotApplication
import net.onefivefour.android.bitpot.customviews.pullrequest.DiffView
import net.onefivefour.android.bitpot.data.model.converter.DiffConverter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PerformanceBenchmarks {

    @get:Rule
    var benchmarkRule = BenchmarkRule()

    @ExperimentalPagingApi
    @Test
    fun simpleViewInflate() {
        val context = ApplicationProvider.getApplicationContext<BitpotApplication>()

        val fileDiffs = DiffConverter().toAppModel(diffString)

        benchmarkRule.measureRepeated {
            for (fileDiff in fileDiffs) {
                val diffView = DiffView(context)
                diffView.setDiff(fileDiff)
            }
        }
    }

    private val diffString = "diff --git a/app/proguard-rules.pro b/app/proguard-rules.pro\n" +
            "index 910601c..73a4e8c 100644\n" +
            "--- a/app/proguard-rules.pro\n" +
            "+++ b/app/proguard-rules.pro\n" +
            "@@ -1,3 +1,4 @@\n" +
            "+<<<<<<< destination:aaef8a692f03505b2549cb2f828bb0844290230c\n" +
            " # Add project specific ProGuard rules here.\n" +
            " # You can control the set of applied configuration files using the\n" +
            " # proguardFiles setting in build.gradle.\n" +
            "@@ -8,3 +9,15 @@\n" +
            " # If you keep the line number information, uncomment this to\n" +
            " # hide the original source file name.\n" +
            " #-renamesourcefileattribute SourceFile\n" +
            "+=======\n" +
            "+# If your project uses WebView with JS, uncomment the following\n" +
            "+# and specify the fully qualified class name to the JavaScript interface\n" +
            "+# class:\n" +
            "+#-keepclassmembers class fqcn.of.javascript.interface.for.webview {\n" +
            "+#   public *;\n" +
            "+#}\n" +
            "+\n" +
            "+# Uncomment this to preserve the line number information for\n" +
            "+# debugging stack traces.\n" +
            "+#-keepattributes SourceFile,LineNumberTable\n" +
            "+>>>>>>> source:c806a5a56aa9e8cd6208fbf3865debac14043fc0\n" +
            "diff --git a/app/src/main/AndroidManifest.xml b/app/src/main/AndroidManifest.xml\n" +
            "index d368f48..ee53899 100644\n" +
            "--- a/app/src/main/AndroidManifest.xml\n" +
            "+++ b/app/src/main/AndroidManifest.xml\n" +
            "@@ -12,9 +12,15 @@\n" +
            "         <activity android:name=\".MainActivity\">\n" +
            "             <intent-filter>\n" +
            "                 <action android:name=\"android.intent.action.MAIN\"/>\n" +
            "-\n" +
            "+                <!-- adding a comment to fuck up\n" +
            "+                 more lines in hope of creating a really nice merge conflict -->\n" +
            "+                <category android:name=\"android.intent.category.DEFAULT\"/>\n" +
            "                 <category android:name=\"android.intent.category.LAUNCHER\"/>\n" +
            "+<<<<<<< destination:aaef8a692f03505b2549cb2f828bb0844290230c\n" +
            "                 <category android:name=\"android.intent.category.DEFAULT\"/>\n" +
            "+=======\n" +
            "+                <!-- SAME here -->\n" +
            "+>>>>>>> source:c806a5a56aa9e8cd6208fbf3865debac14043fc0\n" +
            "             </intent-filter>\n" +
            "         </activity>\n" +
            "     </application>\n" +
            "diff --git a/app/src/main/java/de/company/android/catify/MainActivityCopy.kt b/app/src/main/java/de/company/android/catify/MainActivityCopy.kt\n" +
            "new file mode 100644\n" +
            "index 0000000..e645851\n" +
            "--- /dev/null\n" +
            "+++ b/app/src/main/java/de/company/android/catify/MainActivityCopy.kt\n" +
            "@@ -0,0 +1,12 @@\n" +
            "+package de.company.android.catify\n" +
            "+\n" +
            "+import androidx.appcompat.app.AppCompatActivity\n" +
            "+import android.os.Bundle\n" +
            "+\n" +
            "+class MainActivity : AppCompatActivity() {\n" +
            "+\n" +
            "+    override fun onCreate(savedInstanceState: Bundle?) {\n" +
            "+        super.onCreate(savedInstanceState)\n" +
            "+        setContentView(R.layout.activity_main)\n" +
            "+    }\n" +
            "+}\n" +
            "diff --git a/app/src/main/java/de/company/android/catify/emptyFile.txt b/app/src/main/java/de/company/android/catify/emptyFile.txt\n" +
            "new file mode 100644\n" +
            "index 0000000..e69de29\n" +
            "diff --git a/app/src/main/new-file-added.txt b/app/src/main/new-file-added.txt\n" +
            "new file mode 100644\n" +
            "index 0000000..6ffe0d1\n" +
            "--- /dev/null\n" +
            "+++ b/app/src/main/new-file-added.txt\n" +
            "@@ -0,0 +1,4 @@\n" +
            "+This is the content\n" +
            "+of the newly added file.\n" +
            "+\n" +
            "+There should only be plus signs in the diff file\n" +
            "\\ No newline at end of file\n" +
            "diff --git a/app/src/main/res/drawable/ic_launcher_background.json b/app/src/main/res/drawable/ic_launcher_background.json\n" +
            "new file mode 100644\n" +
            "index 0000000..a0ad202\n" +
            "--- /dev/null\n" +
            "+++ b/app/src/main/res/drawable/ic_launcher_background.json\n" +
            "@@ -0,0 +1,74 @@\n" +
            "+<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "+<vector\n" +
            "+        xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "+        android:height=\"108dp\"\n" +
            "+        android:width=\"108dp\"\n" +
            "+        android:viewportHeight=\"108\"\n" +
            "+        android:viewportWidth=\"108\">\n" +
            "+    <path android:fillColor=\"#008577\"\n" +
            "+          android:pathData=\"M0,0h108v108h-108z\"/>\n" +
            "+    <path android:fillColor=\"#00000000\" android:pathData=\"M9,0L9,108\"\n" +
            "+          android:strokeColor=\"#33FFFFFF\" android:strokeWidth=\"0.8\"/>\n" +
            "+    <path android:fillColor=\"#00000000\" android:pathData=\"M19,0L19,108\"\n" +
            "+          android:strokeColor=\"#33FFFFFF\" android:strokeWidth=\"0.8\"/>\n" +
            "+    <path android:fillColor=\"#00000000\" android:pathData=\"M59,19L59,89\"\n" +
            "+          android:strokeColor=\"#33FFFFFF\" android:strokeWidth=\"0.8\"/>\n" +
            "+    <path android:fillColor=\"#00000000\" android:pathData=\"M69,19L69,89\"\n" +
            "+          android:strokeColor=\"#33FFFFFF\" android:strokeWidth=\"0.8\"/>\n" +
            "+    <path android:fillColor=\"#00000000\" android:pathData=\"M79,19L79,89\"\n" +
            "+          android:strokeColor=\"#33FFFFFF\" android:strokeWidth=\"0.8\"/>\n" +
            "+</vector>\n" +
            "diff --git a/app/src/main/res/drawable/ic_launcher_background.xml b/app/src/main/res/drawable/ic_launcher_background.xml\n" +
            "deleted file mode 100644\n" +
            "index a0ad202..0000000\n" +
            "--- a/app/src/main/res/drawable/ic_launcher_background.xml\n" +
            "+++ /dev/null\n" +
            "@@ -1,74 +0,0 @@\n" +
            "-<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "-<vector\n" +
            "-        xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "-        android:height=\"108dp\"\n" +
            "-        android:width=\"108dp\"\n" +
            "-        android:viewportHeight=\"108\"\n" +
            "-        android:viewportWidth=\"108\">\n" +
            "-    <path android:fillColor=\"#008577\"\n" +
            "-          android:pathData=\"M0,0h108v108h-108z\"/>\n" +
            "-    <path android:fillColor=\"#00000000\" android:pathData=\"M9,0L9,108\"\n" +
            "-          android:strokeColor=\"#33FFFFFF\" android:strokeWidth=\"0.8\"/>\n" +
            "-    <path android:fillColor=\"#00000000\" android:pathData=\"M19,0L19,108\"\n" +
            "-          android:strokeColor=\"#33FFFFFF\" android:strokeWidth=\"0.8\"/>\n" +
            "-    <path android:fillColor=\"#00000000\" android:pathData=\"M79,19L79,89\"\n" +
            "-          android:strokeColor=\"#33FFFFFF\" android:strokeWidth=\"0.8\"/>\n" +
            "-</vector>\n" +
            "diff --git a/app/src/main/res/values/colors.xml b/app/src/main/res/values/colors.xml\n" +
            "deleted file mode 100644\n" +
            "index 69b2233..0000000\n" +
            "--- a/app/src/main/res/values/colors.xml\n" +
            "+++ /dev/null\n" +
            "@@ -1,6 +0,0 @@\n" +
            "-<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "-<resources>\n" +
            "-    <color name=\"colorPrimary\">#008577</color>\n" +
            "-    <color name=\"colorPrimaryDark\">#00574B</color>\n" +
            "-    <color name=\"colorAccent\">#D81B60</color>\n" +
            "-</resources>\n" +
            "diff --git a/app/src/main/res/values/colorsrenamed.xml b/app/src/main/res/values/colorsrenamed.xml\n" +
            "new file mode 100644\n" +
            "index 0000000..69b2233\n" +
            "--- /dev/null\n" +
            "+++ b/app/src/main/res/values/colorsrenamed.xml\n" +
            "@@ -0,0 +1,6 @@\n" +
            "+<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "+<resources>\n" +
            "+    <color name=\"colorPrimary\">#008577</color>\n" +
            "+    <color name=\"colorPrimaryDark\">#00574B</color>\n" +
            "+    <color name=\"colorAccent\">#D81B60</color>\n" +
            "+</resources>\n" +
            "diff --git a/app/src/main/res/values/strings.xml b/app/src/main/res/values/strings.xml\n" +
            "index a7f1bb3..ca6e888 100644\n" +
            "--- a/app/src/main/res/values/strings.xml\n" +
            "+++ b/app/src/main/res/values/strings.xml\n" +
            "@@ -1,3 +1,4 @@\n" +
            " <resources>\n" +
            "     <string name=\"app_name\">Catify</string>\n" +
            "+    <string name=\"modified_string\">This content was modified in this file</string>\n" +
            " </resources>\n" +
            "diff --git a/app/src/test/java/de/company/android/catify/ExampleUnitTest.kt b/app/src/test/java/de/company/android/catify/ExampleUnitTest.kt\n" +
            "deleted file mode 100644\n" +
            "index 3ab3f09..0000000\n" +
            "--- a/app/src/test/java/de/company/android/catify/ExampleUnitTest.kt\n" +
            "+++ /dev/null\n" +
            "@@ -1,17 +0,0 @@\n" +
            "-package de.company.android.catify\n" +
            "-\n" +
            "-import org.junit.Test\n" +
            "-\n" +
            "-import org.junit.Assert.*\n" +
            "-\n" +
            "-/**\n" +
            "- * Example local unit test, which will execute on the development machine (host).\n" +
            "- *\n" +
            "- * See [testing documentation](http://d.android.com/tools/testing).\n" +
            "- */\n" +
            "-class ExampleUnitTest {\n" +
            "-    @Test\n" +
            "-    fun addition_isCorrect() {\n" +
            "-        assertEquals(4, 2 + 2)\n" +
            "-    }\n" +
            "-}\n"
}