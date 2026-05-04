package com.bc.buildplugins

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import kotlin.jvm.optionals.getOrNull

abstract class AbstractPlugin : Plugin<Project> {
    protected val Project.libs: VersionCatalog
        get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

    protected abstract fun Project.onApply()

    final override fun apply(target: Project) = with(target) {
        onApply()
    }

    protected fun Project.applyPlugin(aliasOrPluginId: String) {
        val pluginId = libs.findPlugin(aliasOrPluginId).getOrNull()?.get()?.pluginId
            ?: aliasOrPluginId
        pluginManager.apply(pluginId)
    }

    protected fun Project.configureApplication(action: ApplicationExtension.() -> Unit) {
        pluginManager.withPlugin("com.android.application") {
            extensions.configure<ApplicationExtension>(action)
        }
    }

    protected fun Project.configureLibrary(action: LibraryExtension.() -> Unit) {
        pluginManager.withPlugin("com.android.library") {
            extensions.configure<LibraryExtension>(action)
        }
    }

    protected fun DependencyHandlerScope.platform(libs: VersionCatalog, alias: String): Any {
        val dependency = libs.findLibrary(alias).orElseThrow {
            RuntimeException("라이브러리를 찾을 수 없습니다: $alias (libs.versions.toml 확인 필요)")
        }
        return platform(dependency)
    }

    protected fun DependencyHandlerScope.library(libs: VersionCatalog, alias: String): Any {
        val dependency = libs.findLibrary(alias).orElseThrow {
            RuntimeException("라이브러리를 찾을 수 없습니다: $alias")
        }
        return dependency.get()
    }

    protected fun DependencyHandlerScope.implementation(dependencyNotation: Any) {
        add("implementation", dependencyNotation)
    }

    protected fun DependencyHandlerScope.api(dependencyNotation: Any) {
        add("api", dependencyNotation)
    }

    protected fun DependencyHandlerScope.api(dependencyNotation: Any, configure: ModuleDependency.() -> Unit) {
        (add("api", dependencyNotation) as ModuleDependency).apply(configure)
    }

    protected fun DependencyHandlerScope.ksp(dependencyNotation: Any) {
        add("ksp", dependencyNotation)
    }
}
