plugins {
    id("base.library")
    id("base.ui")
    id("base.hilt")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
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
    kotlin {
        compilerOptions {
            freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
        }
    }
}

ksp {
    arg("mainContainer.package", "com.bc.feature.generated.nav")
    arg("mainContainer.object", "MainContainerRoutes")
    arg("overlayContainer.package", "com.bc.feature.generated.nav")
    arg("overlayContainer.object", "OverlayContainerRoutes")
}

dependencies {
    implementation(project(":Environment:Network"))
    api(project(":Environment:Navigation"))
    ksp(project(":Environment:Navigation:Ksp"))
    api(project(":Environment:DesignSystem"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    api(libs.coil.compose)
    implementation(libs.coil.network)

    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
}
