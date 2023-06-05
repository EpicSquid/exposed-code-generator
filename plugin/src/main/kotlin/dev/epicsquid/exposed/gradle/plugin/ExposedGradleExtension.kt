package dev.epicsquid.exposed.gradle.plugin

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionAware
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
	var enums: NamedDomainObjectContainer<EnumMapping> = objects.domainObjectContainer(
		EnumMapping::class.java
	)
	var defaultExpressions: NamedDomainObjectContainer<DefaultExpressionMapping> = objects.domainObjectContainer(
		DefaultExpressionMapping::class.java
	)
}

abstract class ExposedDatabaseConnectionExtension @Inject constructor() {
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
	var idColumnClassName: String? = null
}

abstract class EnumMapping @Inject constructor(
	val name: String,
) : Serializable {
	/**
	 * The name of the enum in the database, or the declaration to create it.
	 * This is not needed if the enum is already in the DB for MySQL or H2
	 */
	var databaseDeclaration: String? = null

	/**
	 * The fully qualified class name of the enum in your codebase
	 */
	var enumClassName: String? = null

	/**
	 * If using postgres, the fully qualified classname of the PGEnum implementation to handle the PGObject return from
	 * the postgresql JDBC driver
	 */
	var pgEnumClassName: String? = null
}

abstract class DefaultExpressionMapping(
	val name: String,
) : Serializable {
	/**
	 * The fully qualified class name of the expression in your codebase
	 */
	var expressionClassName: String? = null
}