import com.bc.gradle.getVersionCodeFromFile
import com.bc.gradle.getVersionNameFromFile

plugins {
    id("app")
}

android {
    namespace = "com.bc.app"

    defaultConfig {
        applicationId = "com.bc.app"
        versionName = getVersionNameFromFile()
        versionCode = getVersionCodeFromFile()
    }
}