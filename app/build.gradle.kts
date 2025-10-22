import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("jacoco")
}

android {
    namespace = "com.example.androidarticlessample"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.androidarticlessample"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val props = Properties().apply {
            val file = rootProject.file("local.properties")
            if (file.exists()) file.inputStream().use { load(it) }
        }
        val nytKey = (props.getProperty("NYT_API_KEY") ?: System.getenv("NYT_API_KEY") ?: "")
        buildConfigField("String", "NYTIMES_API_KEY", "\"$nytKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    hilt {
        enableAggregatingTask = false
    }
}

dependencies {
    // Compose BOM
    implementation(platform(libs.compose.bom))

    // Bundles
    implementation(libs.bundles.composeCore)
    implementation(libs.bundles.networkCore)

    // Tooling/debug
    debugImplementation(libs.compose.ui.tooling)

    // ViewModel
    implementation(libs.lifecycle.viewmodel)

    // Coroutines
    implementation(libs.coroutines.android)

    // Images
    implementation(libs.coil.compose)

    // Logging
    implementation(libs.timber)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Tests
    testImplementation(libs.bundles.testUnit)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.testAndroid)
    debugImplementation(libs.compose.ui.test.manifest)
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/R.class", "**/R$*.class",
        "**/BuildConfig.*", "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*"
    )

    val javaClasses = fileTree("${buildDir}/intermediates/javac/debug") {
        exclude(fileFilter)
    }
    val kotlinClasses = fileTree("${buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }
    val mainSrc = files("src/main/java")

    classDirectories.setFrom(files(javaClasses, kotlinClasses))
    sourceDirectories.setFrom(mainSrc)
    executionData.setFrom(fileTree(buildDir) {
        include(
            "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
            "jacoco/testDebugUnitTest.exec"
        )
    })
}