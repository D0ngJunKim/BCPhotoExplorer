plugins {
    id("base.library")
    alias(libs.plugins.secrets.gradle.plugin)
}

android {
    namespace = "com.bc.env.network"
}

secrets {
    propertiesFileName = "local.properties"
}

dependencies {
    implementation(libs.retrofit)
    api(libs.retrofit.converter)
    api(libs.paging)
    api(libs.paging.compose)
}