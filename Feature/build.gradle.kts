plugins {
    id("base.library")
    id("base.ui")
    id("base.hilt")
}

android {
    namespace = "com.bc.feature"
}

dependencies {
    implementation(project(":Environment:Network"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
}