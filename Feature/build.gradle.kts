plugins {
    id("base.library")
    id("base.ui")
    id("base.hilt")
}

android {
    namespace = "com.bc.feature"

    buildTypes {
        defaultConfig {
            manifestPlaceholders["networkSecurityConfig"] = "@xml/network_proxyman_https_capture"
        }
        release {
            manifestPlaceholders["networkSecurityConfig"] = "@null"
        }
    }
}

dependencies {
    implementation(project(":Environment:Network"))
    implementation(project(":Environment:Navigation"))
    api(project(":Environment:DesignSystem"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
}