package dev.epicsquid.exposed.gradle

import org.apache.commons.text.CaseUtils
import schemacrawler.schema.Column
import schemacrawler.schema.Index
import schemacrawler.schema.Table
import java.util.*

fun String.toCamelCase(capitalizeFirst: Boolean = false): String =
	CaseUtils.toCamelCase(this, capitalizeFirst, '_')

// kotlin property names should be in camel case without capitalization
fun getPropertyNameForColumn(column: Column) = when {
	column.name.contains('_') -> column.name.toCamelCase()
	column.name.all { it.isUpperCase() } -> column.name.lowercase()
	else -> column.name.replaceFirstChar { it.lowercase(Locale.getDefault()) }
}

// column names should be exactly as in the database; using lowercase for uniformity
fun getColumnName(column: Column) = column.name.lowercase()

fun getObjectNameForTable(table: Table) = when {
	table.name.contains('_') -> table.name.toCamelCase(true)
	table.name.all { it.isUpperCase() } -> table.name.lowercase()
		.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

	else -> table.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun getTableName(table: Table) = table.name.lowercase()

// used in config files for mappings and such
fun getColumnConfigName(column: Column) = "${column.parent.name}.${column.name}".lowercase()

fun getIndexName(index: Index) = index.name.lowercase()