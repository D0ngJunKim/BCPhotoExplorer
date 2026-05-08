package com.bc.buildplugins.base

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.bc.buildplugins.AbstractPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Project

class CompilePlugin : AbstractPlugin() {
    override fun Project.onApply() {
        configureApplication {
            configureVersion()
            configureApplicationBuildTypes()
            configureBuildFeatures()
        }

        configureLibrary {
            configureVersion()
            configureLibraryBuildTypes()
            configureBuildFeatures()
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

    fun ApplicationExtension.configureApplicationBuildTypes() {
        buildFeatures {
            buildConfig = true
        }

        buildTypes {
            getByName("debug") {
                isDebuggable = true
                isShrinkResources = false
                isMinifyEnabled = false
            }

            getByName("release") {
                isDebuggable = false
                isShrinkResources = true
                isMinifyEnabled = true
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
        }
    }

    fun LibraryExtension.configureLibraryBuildTypes() {
        buildTypes {
            getByName("debug") {
                isMinifyEnabled = false
            }

            getByName("release") {
                isMinifyEnabled = true
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
        }
    }

    fun CommonExtension.configureBuildFeatures() {
        buildFeatures.buildConfig = true
    }
}