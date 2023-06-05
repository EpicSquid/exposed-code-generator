package dev.epicsquid.exposed.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.FileReader
import java.util.*

const val EXTENSION_NAME = "exposedCodeGeneratorConfig"
const val DATABASE_CONNECTION_EXTENSION_NAME = "database"
const val TASK_NAME = "generateExposedCode"

abstract class ExposedGradlePlugin : Plugin<Project> {
	override fun apply(project: Project) {
		val extension = project.extensions.create(EXTENSION_NAME, ExposedGradleExtension::class.java, project)
		val databaseConnectionExtension = extension.extensions.create(DATABASE_CONNECTION_EXTENSION_NAME, ExposedDatabaseConnectionExtension::class.java)

		// Add a task that uses configuration from the extension object
		project.tasks.register(TASK_NAME, ExposedGenerateCodeTask::class.java) {
			it.databaseDriver.set(databaseConnectionExtension.databaseDriver)
			it.databaseName.set(databaseConnectionExtension.databaseName)
			it.user.set(databaseConnectionExtension.user)
			it.password.set(databaseConnectionExtension.password)
			it.host.set(databaseConnectionExtension.host)
			it.port.set(databaseConnectionExtension.port)
			it.ipv6Host.set(databaseConnectionExtension.ipv6Host)
			it.connectionProperties.set(databaseConnectionExtension.connectionProperties)
			it.connectionUrl.set(databaseConnectionExtension.connectionUrl)

			it.dateTimeProvider.set(extension.dateTimeProvider)
			it.packageName.set(extension.packageName)
			it.generateSingleFile.set(extension.generateSingleFile)
			it.useFullNames.set(extension.useFullNames)
			it.useDao.set(extension.useDao)
			it.generatedFileName.set(extension.generatedFileName)
			it.collate.set(extension.collate)
			it.outputDirectory.set(extension.outputDirectory)
			it.customMappings = extension.customMappings
			it.enums = extension.enums
			it.defaultExpressions = extension.defaultExpressions
			it.ignoreTables.set(extension.ignoreTables)
		}
	}
}
