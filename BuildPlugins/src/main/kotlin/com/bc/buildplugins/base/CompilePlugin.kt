package com.bc.buildplugins.base

import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.api.dsl.CommonExtension
import com.bc.buildplugins.AbstractPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

class CompilePlugin : AbstractPlugin() {
    override fun Project.onApply() {
        configureApplication {
            configureVersion()
            configureBuildTypes()
        }

        configureLibrary {
            configureVersion()
            configureBuildTypes()
        }
    }

    fun CommonExtension.configureVersion() {
        compileSdk = 36
        defaultConfig.minSdk = 24

        compileOptions.sourceCompatibility = JavaVersion.VERSION_11
        compileOptions.targetCompatibility = JavaVersion.VERSION_11

        compileSdk {
            version = release(36) {
                minorApiLevel = 1
            }
        }
    }

    fun CommonExtension.configureBuildTypes() {
        buildTypes {
            getByName("debug") {
                if (this is ApplicationBuildType) {
                    isDebuggable = true
                }
                isShrinkResources = false
                isMinifyEnabled = false
            }

            getByName("release") {
                if (this is ApplicationBuildType) {
                    isDebuggable = false
                }
                isShrinkResources = true
                isMinifyEnabled = true
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
        }
    }
}