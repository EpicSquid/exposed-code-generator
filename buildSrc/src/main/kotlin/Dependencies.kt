object BuildPluginsVersion {
    const val KOTLIN = "1.8.10"

    const val PLUGIN_PUBLISH = "0.15.0"
    const val VERSIONS_PLUGIN = "0.28.0"
}

object TestingLib {
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
}

object Deps {
    object Exposed {
			const val exposedCore = "org.jetbrains.exposed:exposed-core:${Versions.EXPOSED}"
			const val exposedDao = "org.jetbrains.exposed:exposed-dao:${Versions.EXPOSED}"
			const val exposedJdbc = "org.jetbrains.exposed:exposed-jdbc:${Versions.EXPOSED}"
			const val exposedJavaTime = "org.jetbrains.exposed:exposed-java-time:${Versions.EXPOSED}"
			const val exposedKotlinDateTime = "org.jetbrains.exposed:exposed-kotlin-datetime:${Versions.EXPOSED}"
			const val exposedJodaTime = "org.jetbrains.exposed:exposed-jodatime:${Versions.EXPOSED}"
		}

		object SchemaCrawler {
				const val schemaCrawler = "us.fatehi:schemacrawler:${Versions.SCHEMA_CRAWLER_VERSION}"
				const val schemaCrawlerMysql = "us.fatehi:schemacrawler-mysql:${Versions.SCHEMA_CRAWLER_VERSION}"
				const val schemaCrawlerSqlite = "us.fatehi:schemacrawler-sqlite:${Versions.SCHEMA_CRAWLER_VERSION}"
				const val schemaCrawlerPostgresql = "us.fatehi:schemacrawler-postgresql:${Versions.SCHEMA_CRAWLER_VERSION}"
		}

		object Utils {
				const val commonsText = "org.apache.commons:commons-text:1.10.0"
				const val prestoParser = "com.facebook.presto:presto-parser:0.239"
		}

		object KotlinCodeGeneration {
				const val kotlinPoet = "com.squareup:kotlinpoet:1.10.1"
				const val kotlinCompileTesting = "com.github.tschuchortdev:kotlin-compile-testing:1.4.2"
		}

		object YamlConfig {
				const val hopliteYaml = "com.sksamuel.hoplite:hoplite-yaml:1.4.9"
		}

		object Logging {
				const val slf4jApi = "org.slf4j:slf4j-api:1.7.30"
		}

		object DatabaseDrivers {
				const val h2 = "com.h2database:h2:1.4.199"
				const val postgresql = "org.postgresql:postgresql:42.2.2"
				const val sqliteJdbc = "org.xerial:sqlite-jdbc:3.32.3"
				const val mariadbJavaClient = "org.mariadb.jdbc:mariadb-java-client:2.6.0"
				const val mysqlConnectorJava = "mysql:mysql-connector-java:8.0.25"
		}

	object Kotlin {
		const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"
	}
}