plugins {
    id("base.library")
    id("base.ui")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.bc.env.nav"
}

dependencies {
    api(libs.kotlinx.serialization.core)
    api(libs.androidx.navigation.compose)
    api(libs.hilt.navigation.compose)
    api(libs.kotlin.reflect)
}
