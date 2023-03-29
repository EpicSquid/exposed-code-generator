package dev.epicsquid.exposed.gradle.plugin

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.MapProperty
import java.io.Serializable
import javax.inject.Inject

const val DEFAULT_OUTPUT_DIRECTORY = "tables"

@Suppress("UnnecessaryAbstractClass")
abstract class ExposedGradleExtension @Inject constructor(
	objects: ObjectFactory,
	project: Project
) : ExtensionAware {

	var packageName: String? = null

	var generatedFileName: String? = null
	var collate: String? = null

	var outputDirectory: DirectoryProperty = objects.directoryProperty().convention(
		project.layout.buildDirectory.dir(DEFAULT_OUTPUT_DIRECTORY)
	)

	var dateTimeProvider: String? = null
	var generateSingleFile: Boolean = false
	var useFullNames: Boolean = false
	var useDao: Boolean = false
	var customMappings: NamedDomainObjectContainer<CustomColumnMapping> = objects.domainObjectContainer(
		CustomColumnMapping::class.java
	)
	var ignoreTables: List<String> = listOf()
}

abstract class ExposedDatabaseConnectionExtension @Inject constructor(objects: ObjectFactory) {
	var databaseDriver: String? = null
	var databaseName: String? = null
	var user: String? = null
	var password: String? = null
	var host: String? = null
	var port: String? = null
	var ipv6Host: String? = null
	var connectionUrl: String? = null
	var connectionProperties: Map<String, String> = mapOf()
}

abstract class CustomColumnMapping @Inject constructor(
	val name: String,
) : Serializable {
	var columnPropertyClassName: String? = null
	var columnFunctionName: String? = null
	var isColumnTyped: Boolean = false
	var existingColumn: String? = null
}
