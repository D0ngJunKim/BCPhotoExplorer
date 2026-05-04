import java.io.FileNotFoundException

println()

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            val tomlPath = "../gradle/libs.versions.toml"
            val tomlFile = file(tomlPath)

            println("👉 TOML 파일 확인 중: ${tomlFile.absolutePath}")

            if (!tomlFile.exists()) {
                throw FileNotFoundException("🚨 [경로 에러] TOML 파일을 찾을 수 없습니다! 경로를 확인하세요: ${tomlFile.absolutePath}")
            } else {
                println("✅ TOML 파일 확인 완료!: ${tomlFile.absolutePath}")
            }
            from(files(tomlFile))
        }
    }
}

rootProject.name = "BuildPlugins"
