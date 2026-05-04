pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "BCPhotoExplorer"

include(":ExplorerApp")
includeBuild("BuildPlugins")
includeRecursively(":Environment")
includeRecursively(":Feature")

fun includeRecursively(dirName: String) {
    val rootDirFile = File(rootDir, dirName.removePrefix(":"))

    if (!rootDirFile.exists()) {
        return
    }

    rootDirFile.walk()
        .onEnter { file -> !file.name.startsWith(".") && file.name != "build" }
        .filter { it.isDirectory }
        .filter { File(it, "build.gradle.kts").exists() }
        .forEach { file ->
            val relativePath = file.relativeTo(rootDir).path
            val gradlePath = ":${relativePath.replace(File.separator, ":")}"

            include(gradlePath)
            println("\uD83D\uDC49 모듈 추가됨: $gradlePath")
        }
}
