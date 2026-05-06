package com.bc.buildplugins.base

import com.bc.buildplugins.AbstractPlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class LibraryPlugin : AbstractPlugin() {
    override fun Project.onApply() {
        applyPlugin("android.library")
        applyPlugin("base.compile")

        dependencies {
            api(library(libs, "timber") )
        }
    }
}