package dev.epicsquid.exposed.gradle

import com.squareup.kotlinpoet.FileSpec
import dev.epicsquid.exposed.gradle.tests.TestDB
import java.nio.file.Path
import java.nio.file.Paths


val generalTestDataPath: Path = Paths.get("src", "test", "kotlin", "org", "jetbrains", "exposed", "gradle", "databases")
val resourcesDatabasesPath: Path = Paths.get("src", "test", "resources", "databases")
val resourcesConfigFilesPath: Path = Paths.get("src", "test", "resources", "config")

fun getDatabaseExposedFileSpec(db: TestDB, tableName: String? = null, configFileName: String? = null): List<FileSpec> {
    val metadataGetter = MetadataGetter(db.connection, db.user, db.pass)
    val tables = metadataGetter.getTables().filterUtilTables()
    val exposedCodeGenerator = if (configFileName == null) {
        if (tableName != null) {
					dev.epicsquid.exposed.gradle.ExposedCodeGenerator(tables.filter {
						it.name.equals(
							tableName,
							ignoreCase = true
						)
					}, dialect = testDBtoDialect[db])
        } else {
					dev.epicsquid.exposed.gradle.ExposedCodeGenerator(tables, dialect = testDBtoDialect[db])
        }
    } else {
        if (tableName != null) {
					dev.epicsquid.exposed.gradle.ExposedCodeGenerator(tables.filter {
						it.name.equals(
							tableName,
							ignoreCase = true
						)
					}, configFileName, testDBtoDialect[db])
        } else {
					dev.epicsquid.exposed.gradle.ExposedCodeGenerator(tables, configFileName, testDBtoDialect[db])
        }
    }
    return exposedCodeGenerator.generateExposedTables()
}

private fun List<schemacrawler.schema.Table>.filterUtilTables() = this.filterNot { it.fullName.startsWith("sys.") }

val testDBtoDialect = mapOf(
        TestDB.H2 to dev.epicsquid.exposed.gradle.DBDialect.H2,
        TestDB.H2_MYSQL to dev.epicsquid.exposed.gradle.DBDialect.H2,
        TestDB.SQLITE to dev.epicsquid.exposed.gradle.DBDialect.SQLITE,
        TestDB.MYSQL to dev.epicsquid.exposed.gradle.DBDialect.MYSQL,
        TestDB.POSTGRESQL to dev.epicsquid.exposed.gradle.DBDialect.POSTGRESQL,
        TestDB.POSTGRESQLNG to dev.epicsquid.exposed.gradle.DBDialect.POSTGRESQL,
        TestDB.ORACLE to dev.epicsquid.exposed.gradle.DBDialect.ORACLE,
        TestDB.MARIADB to dev.epicsquid.exposed.gradle.DBDialect.MARIADB,
        TestDB.SQLSERVER to dev.epicsquid.exposed.gradle.DBDialect.SQLSERVER
)

