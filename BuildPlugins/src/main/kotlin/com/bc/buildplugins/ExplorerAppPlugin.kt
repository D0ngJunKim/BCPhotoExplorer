package com.bc.buildplugins

import org.gradle.api.Project

class ExplorerAppPlugin : AbstractPlugin() {
    override fun Project.onApply() {
        applyPlugin("base.application")
    }
}