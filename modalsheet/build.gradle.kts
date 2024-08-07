plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.mavenpublish)
}

android {
    namespace = "eu.wewox.modalsheet"

    compileSdk = libs.versions.sdk.compile.get().toInt()

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.sourceCompatibility.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.targetCompatibility.get())
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    kotlinOptions {
        jvmTarget = libs.versions.java.jvmTarget.get()
        freeCompilerArgs = freeCompilerArgs +
            "-Xexplicit-api=strict" +
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi"
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.material2)
    implementation(libs.androidx.activitycompose)
    implementation(libs.androidx.savedstate)
    implementation(libs.androidx.viewmodel)
}
