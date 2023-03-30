package dev.epicsquid.exposed.gradle.plugin

import dev.epicsquid.exposed.gradle.*
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.jetbrains.exposed.sql.vendors.*


abstract class ExposedGenerateCodeTask : DefaultTask() {

	init {
		description = "Generate Exposed table code for DB"
		group = BasePlugin.BUILD_GROUP
	}

	@get:Input
	@get:Option(
		option = "databaseDriver",
		description = "Which database to connect to, in form of JDBC driver string, such as jdbc:sqlite"
	)
	@get:Optional
	abstract val databaseDriver: Property<String>

	@get:Input
	@get:Option(option = "databaseName", description = "The name of the database to connect to")
	@get:Optional
	abstract val databaseName: Property<String>

	@get:Input
	@get:Option(option = "user", description = "Database user name")
	@get:Optional
	abstract val user: Property<String>

	@get:Input
	@get:Option(option = "pass", description = "Database password")
	@get:Optional
	abstract val password: Property<String>

	@get:Input
	@get:Option(option = "host", description = "Database host using IPv4")
	@get:Optional
	abstract val host: Property<String>

	@get:Input
	@get:Option(option = "port", description = "Database port")
	@get:Optional
	abstract val port: Property<String>

	@get:Input
	@get:Option(option = "ipv6Host", description = "Database host using IPv6; use either this or host")
	@get:Optional
	abstract val ipv6Host: Property<String>

	@get:Input
	@get:Option(option = "connectionURL", description = "full connection URL")
	@get:Optional
	abstract val connectionUrl: Property<String>

	@get:Input
	@get:Option(
		option = "connectionProperties",
		description = "Additional connection properties. Will be added to jdbc connection"
	)
	@get:Optional
	abstract val connectionProperties: MapProperty<String, String>

	@get:Input
	@get:Option(option = "packageName", description = "Generated files will be placed in this package")
	@get:Optional
	abstract val packageName: Property<String>

	@get:Input
	@get:Option(
		option = "generateSingleFile",
		description = "Set to true for generating all tables in one file; a separate file for each table is generated otherwise"
	)
	@get:Optional
	abstract val generateSingleFile: Property<Boolean>

	@get:Input
	@get:Option(
		option = "useFullNames",
		description = "If generateSingleFile is false, set to false to use the same name as the Table object for the file name"
	)
	@get:Optional
	abstract val useFullNames: Property<Boolean>

	@get:Input
	@get:Option(
		option = "useDao",
		description = "Enable the use of DAO classes when generating files."
	)
	@get:Optional
	abstract val useDao: Property<Boolean>

	@get:Input
	@get:Option(
		option = "generatedFileName",
		description = "If generatedSingleFile is set to true, this will be the name of the file generated"
	)
	@get:Optional
	abstract val generatedFileName: Property<String>

	@get:Input
	@get:Option(option = "collate", description = "String collation method for all string columns in DB")
	@get:Optional
	abstract val collate: Property<String>


	@get:OutputDirectory
	abstract val outputDirectory: DirectoryProperty

	@get:Input
	@get:Option(
		option = "dateTimeProvider",
		description = "Choose the datetime library to generate with. Options are: \"java-time\", \"kotlin-datetime\"" +
				"and \"jodatime\". Defaults to \"java-time\" if not provided"
	)
	@get:Optional
	abstract val dateTimeProvider: Property<String>

	@get:Input
	@get:Option(
		option = "customMappings",
		description = "Set column mappings manually, in the form of [type] = ([fully-qualified class name], " +
				"[fully-qualified function name]), " +
				"e.g. jsonb = (com.example.jsonb.Jsonb, com.example.jsonb.jsonb)"
	)
	@get:Optional
	abstract var customMappings: NamedDomainObjectContainer<CustomColumnMapping>

	@get:Input
	@get:Option(
		option = "enums",
		description = "Set enum mappings manually, in the form of [type] = ([fully-qualified class name], " +
				"[fully-qualified function name]), " +
				"e.g. jsonb = (com.example.jsonb.Jsonb, com.example.jsonb.jsonb)"
	)
	@get:Optional
	abstract var enums: NamedDomainObjectContainer<EnumMapping>

	@get:Input
	@get:Option(
		option = "ignoreTables",
		description = "List of tables to ignore when generating code"
	)
	@get:Optional
	abstract val ignoreTables: ListProperty<String>

	@TaskAction
	fun generateExposedCode() {
		val metadataGetter = if (connectionUrl.orNull != null) {
			MetadataGetter({ connectionUrl.get() }, user.orNull, password.orNull, connectionProperties.orNull)
		} else {
			MetadataGetter(
				databaseDriver.get(),
				databaseName.get(),
				user.orNull,
				password.orNull,
				host.orNull,
				port.orNull,
				ipv6Host.orNull,
				connectionProperties.orNull
			)
		}


		val tables = metadataGetter.getTables().filterUtilTables()
		val config = ExposedCodeGeneratorConfiguration(
			packageName.getOrElse(""),
			generateSingleFile.getOrElse(true),
			generatedFileName.orNull,
			collate.orNull,
			dateTimeProvider.orNull,
			useFullNames.getOrElse(true),
			useDao.getOrElse(false),
			customMappings.asMap.map { (key, value) ->
				key to CustomMappings(
					value.columnPropertyClassName,
					value.columnFunctionName,
					value.isColumnTyped,
					value.existingColumn
				)
			}.toMap(),
			ignoreTables.getOrElse(emptyList()),
			enums.asMap.map { (key, value) ->
				key to EnumColumnConfig(
					value.databaseDeclaration,
					value.enumClassName!!,
					value.pgEnumClassName
				)
			}.toMap()
		)
		val dialect = when(databaseDriver.get()) {
			"postgresql" -> DBDialect.POSTGRESQL
			"mysql" -> DBDialect.MYSQL
			"mariadb" -> DBDialect.MARIADB
			"sqlserver" -> DBDialect.SQLSERVER
			"sqlite" -> DBDialect.SQLITE
			"h2" -> DBDialect.H2
			"oracle" -> DBDialect.ORACLE
			else -> null
		}

		val exposedCodeGenerator = ExposedCodeGenerator(
			tables = tables,
			config = config,
			dialect = dialect
		)

		val files = exposedCodeGenerator.generateExposedTables()

		files.forEach {
			val directory = outputDirectory.get()
			it.writeTo(directory.asFile)
			val generatedFile = directory.file(it.toJavaFileObject().name).asFile
			val generatedContent = generatedFile.readText()
			generatedFile.writeText(ExposedCodeGenerator.postProcessOutput(generatedContent))
		}
	}

	private fun List<schemacrawler.schema.Table>.filterUtilTables() = this.filterNot { it.fullName.startsWith("sys.") }
}
