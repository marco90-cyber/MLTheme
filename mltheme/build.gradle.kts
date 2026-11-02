import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    `maven-publish`
}

android {
    namespace = "com.marco.mltheme"
    compileSdk = 34

    defaultConfig {
        // le MLapps partono da Android 8: sotto Android 12 i colori di sistema non esistono e si usa un colore di base
        minSdk = 26
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

kotlin {
    compilerOptions {
        // compatibile anche con le app ancora su Kotlin 1.9 (MLSkywatch, WLandroid, Magic touch remove)
        languageVersion.set(KotlinVersion.KOTLIN_1_9)
        apiVersion.set(KotlinVersion.KOTLIN_1_9)
    }
}

dependencies {
    api(platform("androidx.compose:compose-bom:2024.12.01"))
    api("androidx.compose.ui:ui")
    api("androidx.compose.foundation:foundation")
    api("androidx.compose.material3:material3")
    api("androidx.compose.material:material-icons-core")
    implementation("androidx.core:core-ktx:1.13.1")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.marco90-cyber"
            artifactId = "MLTheme"
            version = System.getenv("VERSION") ?: "dev"
            afterEvaluate { from(components["release"]) }
        }
    }
}
