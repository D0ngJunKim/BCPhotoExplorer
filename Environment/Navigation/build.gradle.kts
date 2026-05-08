plugins {
    id("base.library")
}

android {
    namespace = "com.bc.env.nav"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.navigation.compose)
}