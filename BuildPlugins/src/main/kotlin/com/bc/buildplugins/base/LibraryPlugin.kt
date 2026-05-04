package com.bc.buildplugins.base

import com.bc.buildplugins.AbstractPlugin
import org.gradle.api.Project

class LibraryPlugin : AbstractPlugin() {
    override fun Project.onApply() {
        applyPlugin("android.library")
        applyPlugin("base.compile")
    }
}