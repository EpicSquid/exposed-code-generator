package org.jetbrains.exposed.gradle

import schemacrawler.crawl.SchemaCrawler
import schemacrawler.schema.Table
import schemacrawler.schemacrawler.*
import us.fatehi.utility.datasource.DatabaseConnectionSource
import us.fatehi.utility.datasource.DatabaseConnectionSourceBuilder
import us.fatehi.utility.datasource.MultiUseUserCredentials
import us.fatehi.utility.datasource.UserCredentials

/**
 * Connects to a database and retrieves its tables.
 */
class MetadataGetter {
    private val dataSourceBuilder: DatabaseConnectionSourceBuilder
    private val connectionString: String

    constructor(
            databaseDriver: String,
            databaseName: String,
            user: String? = null,
            password: String? = null,
            host: String? = null,
            port: String? = null,
            ipv6Host: String? = null,
            additionalProperties: Map<String, String>? = null
    ) {
        val hostPortString = buildString {
            if (ipv6Host != null || host != null) {
                append("//")
                if (ipv6Host != null) {
                    append("[$ipv6Host]")
                } else {
                    append(host)
                }
                if (port != null) {
                    append(":$port")
                }
                append("/")
            }
        }
        connectionString = "jdbc:$databaseDriver:$hostPortString$databaseName"
        dataSourceBuilder = DatabaseConnectionSourceBuilder
            .builder(connectionString)
            .withUrlx(additionalProperties.orEmpty())
        initDataSource(dataSourceBuilder, user, password)
    }

    constructor(connection: () -> String, user: String? = null, password: String? = null, additionalProperties: Map<String, String>? = null) {
        connectionString = connection()
        dataSourceBuilder = DatabaseConnectionSourceBuilder.builder(connectionString).withUrlx(additionalProperties.orEmpty())
        initDataSource(dataSourceBuilder, user, password)
    }

    private fun initDataSource(dataSource: DatabaseConnectionSourceBuilder, user: String?, password: String?) {
        if (user != null && password != null) {
            dataSource.withUserCredentials(MultiUseUserCredentials(user, password))
        }
        // to prevent exceptions at driver registration
        val driver = getDriver(connectionString)
        Class.forName(driver).getDeclaredConstructor().newInstance()
    }

    private fun getDriver(url: String) = when {
        url.startsWith("jdbc:h2") -> "org.h2.Driver"
        url.startsWith("jdbc:postgresql") -> "org.postgresql.Driver"
        url.startsWith("jdbc:pgsql") -> "com.impossibl.postgres.jdbc.PGDriver"
        url.startsWith("jdbc:mysql") -> "com.mysql.cj.jdbc.Driver"
        url.startsWith("jdbc:mariadb") -> "org.mariadb.jdbc.Driver"
        url.startsWith("jdbc:oracle") -> "oracle.jdbc.OracleDriver"
        url.startsWith("jdbc:sqlite") -> "org.sqlite.JDBC"
        url.startsWith("jdbc:sqlserver") -> "com.microsoft.sqlserver.jdbc.SQLServerDriver"
        else -> error("Database driver not found for $url")
    }

    /**
     * Returns tables from the database connected via [dataSourceBuilder].
     */
    fun getTables(): List<Table> {
        val options = SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions()
                .withLoadOptions(LoadOptionsBuilder.builder()
                        .withSchemaInfoLevel(SchemaInfoLevelBuilder.maximum())
                        .toOptions()
                )


        val ds = dataSourceBuilder.build()
        val connection = ds.get()
        val retrievalOptions = SchemaRetrievalOptionsBuilder.builder().fromConnnection(connection).toOptions()
        val catalog = SchemaCrawler(ds, retrievalOptions, options).crawl()
//        return sortTablesByDependencies(catalog.schemas.flatMap { catalog.getTables(it) })
        return catalog.schemas.flatMap { catalog.getTables(it) }
    }
}

