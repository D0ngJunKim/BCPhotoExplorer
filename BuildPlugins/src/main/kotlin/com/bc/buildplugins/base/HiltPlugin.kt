package com.bc.buildplugins.base

import com.bc.buildplugins.AbstractPlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltPlugin : AbstractPlugin() {
    override fun Project.onApply() {
        applyPlugin("ksp")
        applyPlugin("hilt")

        dependencies {
            implementation(library(libs, "hilt"))
            ksp(library(libs, "hilt-compiler"))
        }
    }
}