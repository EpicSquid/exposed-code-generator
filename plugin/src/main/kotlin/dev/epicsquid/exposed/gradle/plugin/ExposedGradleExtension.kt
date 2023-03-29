package dev.epicsquid.exposed.gradle.plugin

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import java.io.Serializable
import javax.inject.Inject

const val DEFAULT_OUTPUT_DIRECTORY = "tables"

@Suppress("UnnecessaryAbstractClass")
abstract class ExposedGradleExtension @Inject constructor(
	objects: ObjectFactory,
	project: Project
) : ExtensionAware {

	var packageName: Property<String?> = objects.nullableProperty()

	var generatedFileName: Property<String?> = objects.nullableProperty()
	var collate: Property<String?> = objects.nullableProperty()

	var outputDirectory: DirectoryProperty = objects.directoryProperty().convention(
		project.layout.buildDirectory.dir(DEFAULT_OUTPUT_DIRECTORY)
	)

	var dateTimeProvider: Property<String?> = objects.nullableProperty()
	var generateSingleFile: Property<Boolean> = objects.property(Boolean::class.java).convention(false)
	var useFullNames: Property<Boolean> = objects.property(Boolean::class.java).convention(false)
	var useDao: Property<Boolean> = objects.property(Boolean::class.java).convention(false)
	var customMappings: NamedDomainObjectContainer<CustomColumnMapping> = objects.domainObjectContainer(
		CustomColumnMapping::class.java
	)
}

abstract class ExposedDatabaseConnectionExtension @Inject constructor(objects: ObjectFactory) {
	var databaseDriver: Property<String> = objects.property(String::class.java)
	var databaseName: Property<String> = objects.property(String::class.java)
	var user: Property<String?> = objects.nullableProperty()
	var password: Property<String?> = objects.nullableProperty()
	var host: Property<String?> = objects.nullableProperty()
	var port: Property<String?> = objects.nullableProperty()
	var ipv6Host: Property<String?> = objects.nullableProperty()
	var connectionUrl: Property<String?> = objects.nullableProperty()
	var connectionProperties: MapProperty<String, String> = objects.mapProperty(String::class.java, String::class.java)
}

abstract class CustomColumnMapping @Inject constructor(
	val name: String,
) : Serializable {
	var columnPropertyClassName: String? = null
	var columnFunctionName: String? = null
	var isColumnTyped: Boolean = false
	var existingColumn: String? = null
}
