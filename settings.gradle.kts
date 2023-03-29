pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
	}
}

rootProject.name = "dev.epicsquid.exposed.gradle.plugin"

include(":plugin")
include(":exposed-code-generator")
