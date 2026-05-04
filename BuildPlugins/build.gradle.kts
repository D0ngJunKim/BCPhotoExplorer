plugins {
    `kotlin-dsl`

}
group = "com.bc.buildplugins"

dependencies {
    implementation(gradleApi())
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
}

fun PluginDeclaration.impl(simpleClassName: String) {
    implementationClass = "${group}.$simpleClassName"
}

fun PluginDeclaration.implBase(simpleClassName: String) {
    implementationClass = "${group}.base.$simpleClassName"
}

fun PluginDeclaration.implFeature(simpleClassName: String) {
    implementationClass = "${group}.feature.$simpleClassName"
}

gradlePlugin {
    plugins {
        register("app") {
            id = "app"
            impl("ExplorerAppPlugin")
        }

        register("base-application") {
            id = "base.application"
            implBase("ApplicationPlugin")
        }

        register("base-compile") {
            id = "base.compile"
            implBase("CompilePlugin")
        }

        register("base-ui") {
            id = "base.ui"
            implBase("UiPlugin")
        }

        register("base-hilt") {
            id = "base.hilt"
            implBase("HiltPlugin")
        }

        register("base-library") {
            id = "base.library"
            implBase("LibraryPlugin")
        }
    }
}