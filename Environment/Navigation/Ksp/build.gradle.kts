plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(
        providers.gradleProperty("build.jdk.version")
            .map(String::toInt)
            .get()
    )
}

dependencies {
    implementation(libs.ksp.symbol.processing.api)
}
