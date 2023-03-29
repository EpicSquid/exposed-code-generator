package dev.epicsquid.exposed.gradle.plugin

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import java.io.Serializable
import javax.inject.Inject

const val DEFAULT_OUTPUT_DIRECTORY = "tables"

@Suppress("UnnecessaryAbstractClass")
abstract class ExposedGradleExtension @Inject constructor(project: Project) {

	private val objects = project.objects

	var propertiesFilename: String? = null

	var databaseDriver: String? = null
	var databaseName: String? = null
	var user: String? = null
	var password: String? = null
	var host: String? = null
	var port: String? = null
	var ipv6Host: String? = null
	var connectionProperties: Map<String, String> = mutableMapOf()


	var connectionURL: String? = null

	var packageName: String? = null

	//    var generateSingleFile: Boolean = true
	var generatedFileName: String? = null
	var collate: String? = null
	var columnMappings: Map<String, String> = mutableMapOf()

	var configFilename: String? = null

	var outputDirectory: DirectoryProperty = objects.directoryProperty().convention(
		project.layout.buildDirectory.dir(DEFAULT_OUTPUT_DIRECTORY)
	)

	var dateTimeProvider: String? = null
	var generateSingleFile: Boolean = false
	var useFullNames: Boolean = true
	var useDao: Boolean = false
	var customMappings: NamedDomainObjectContainer<CustomColumnMapping> = objects.domainObjectContainer(
		CustomColumnMapping::class.java
	)
}

abstract class CustomColumnMapping @Inject constructor(
	val name: String,
) : Serializable {
	var columnPropertyClassName: String? = null
	var columnFunctionName: String? = null
	var isColumnTyped: Boolean = false
}
