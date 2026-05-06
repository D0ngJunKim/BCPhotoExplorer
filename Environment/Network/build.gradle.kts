plugins {
    id("base.library")
}

android {
    namespace = "com.bc.env.network"
}

dependencies {
    implementation(libs.retrofit)
    api(libs.retrofit.converter)
    api(libs.paging)
    api(libs.paging.compose)
}