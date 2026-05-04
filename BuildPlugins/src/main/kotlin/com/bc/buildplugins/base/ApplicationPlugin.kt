package com.bc.buildplugins.base

import com.bc.buildplugins.AbstractPlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class ApplicationPlugin : AbstractPlugin() {
    override fun Project.onApply() {
        applyPlugin("android.application")
        applyPlugin("base.compile")
        applyPlugin("base.ui")
        applyPlugin("base.hilt")

        configureApplication {
            dependencies {
                implementation(project(":Feature"))
            }
        }
    }
}