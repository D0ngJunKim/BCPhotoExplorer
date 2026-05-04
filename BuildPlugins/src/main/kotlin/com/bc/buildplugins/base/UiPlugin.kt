package com.bc.buildplugins.base

import com.android.build.api.dsl.CommonExtension
import com.bc.buildplugins.AbstractPlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class UiPlugin : AbstractPlugin() {
    override fun Project.onApply() {
        applyPlugin("kotlin.compose")

        configureApplication {
            configureUiFeatures()
        }

        configureLibrary {
            configureUiFeatures()
        }

        dependencies {
            // Compose BOM
            implementation(platform(libs, "androidx.compose.bom"))
            implementation(library(libs, "androidx.compose.foundation"))
            implementation(library(libs, "androidx.compose.ui.tooling"))
            implementation(library(libs, "androidx.compose.ui.tooling.preview"))

            // Compose Helper
            implementation(library(libs, "androidx.activity.compose"))
            implementation(library(libs, "androidx.constraintlayout.compose"))
        }
    }

    private fun CommonExtension.configureUiFeatures() {
        buildFeatures.compose = true
    }
}