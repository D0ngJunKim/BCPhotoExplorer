package com.bc.buildplugins.base

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.bc.buildplugins.AbstractPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class CompilePlugin : AbstractPlugin() {
    override fun Project.onApply() {
        extensions.configure<KotlinAndroidProjectExtension> {
            compilerOptions {
                jvmTarget.set(resolveJvmTarget())
            }
        }

        configureApplication {
            configureVersion(this@onApply)
            configureApplicationBuildTypes()
            configureBuildFeatures()
        }

        configureLibrary {
            configureVersion(this@onApply)
            configureLibraryBuildTypes()
            configureBuildFeatures()
        }
    }

    fun CommonExtension.configureVersion(project: Project) {
        compileSdk = 36
        defaultConfig.minSdk = 24

        with(compileOptions) {
            val javaVersion = project.resolveJavaVersion()
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

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

    private fun Project.resolveJdkVersion(): Int {
        return providers.gradleProperty("build.jdk.version")
            .orNull
            ?.toIntOrNull() ?: 11
    }

    private fun Project.resolveJvmTarget(): JvmTarget {
        return JvmTarget.fromTarget(resolveJdkVersion().toString())
    }

    private fun Project.resolveJavaVersion(): JavaVersion {
        return JavaVersion.toVersion(resolveJdkVersion())
    }
}