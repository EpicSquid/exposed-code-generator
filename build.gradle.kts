import com.github.benmanes.gradle.versions.updates.*

plugins {
	kotlin("jvm") version BuildPluginsVersion.KOTLIN apply false
	id("com.gradle.plugin-publish") version BuildPluginsVersion.PLUGIN_PUBLISH apply false
	id("com.github.ben-manes.versions") version BuildPluginsVersion.VERSIONS_PLUGIN
}


allprojects {
	group = PluginCoordinates.GROUP
	version = PluginCoordinates.VERSION

	repositories {
		google()
		mavenCentral()
	}
}