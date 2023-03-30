package dev.epicsquid.exposed.gradle

data class EnumColumnConfig(
	/**
	 * The name of the enum in the database, or the declaration to create it.
	 * This is not needed if the enum is already in the DB for MySQL or H2
	 */
	val databaseDeclaration: String? = null,
	/**
	 * The fully qualified class name of the enum in your codebase
	 */
	var enumClassName: String,
	/**
	 * If using postgres, the fully qualified classname of the PGEnum implementation to handle the PGObject return from
	 * the postgresql JDBC driver
	 */
	var pgEnumClassName: String? = null,
)
