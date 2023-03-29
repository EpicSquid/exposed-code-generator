plugins {
	kotlin("jvm") version "1.8.10"
}

group = "dev.epicsquid.exposed.gradle"

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	implementation(Deps.Kotlin.reflect)

	implementation(Deps.Exposed.exposedDao)
	implementation(Deps.Exposed.exposedJdbc)
	implementation(Deps.Exposed.exposedJavaTime)
	implementation(Deps.Exposed.exposedKotlinDateTime)
	implementation(Deps.Exposed.exposedJodaTime)

	implementation(Deps.SchemaCrawler.schemaCrawler)
	implementation(Deps.SchemaCrawler.schemaCrawlerMysql)
	implementation(Deps.SchemaCrawler.schemaCrawlerSqlite)
	implementation(Deps.SchemaCrawler.schemaCrawlerPostgresql)

	implementation(Deps.Utils.commonsText)
	implementation(Deps.Utils.prestoParser)

	implementation(Deps.YamlConfig.hopliteYaml)

	implementation(Deps.DatabaseDrivers.mysqlConnectorJava)
	implementation(Deps.DatabaseDrivers.postgresql)
	implementation(Deps.DatabaseDrivers.sqliteJdbc)
	implementation(Deps.DatabaseDrivers.h2)
	implementation(Deps.DatabaseDrivers.mariadbJavaClient)

	implementation(Deps.KotlinCodeGeneration.kotlinPoet)
	implementation(Deps.KotlinCodeGeneration.kotlinCompileTesting)

	implementation(Deps.Logging.slf4jApi)

	testImplementation(TestingLib.JUNIT)
	testImplementation("org.assertj:assertj-core:3.16.1")
	testImplementation("com.opentable.components", "otj-pg-embedded", "0.12.0")
	testImplementation("org.testcontainers", "testcontainers", "1.14.3")
	testImplementation("org.testcontainers", "mysql", "1.14.3")
}

configure<JavaPluginExtension> {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}
tasks {
	compileKotlin {
		kotlinOptions.jvmTarget = "1.8"
	}
	compileTestKotlin {
		kotlinOptions.jvmTarget = "1.8"
	}
}

tasks.withType(JavaCompile::class) {
	targetCompatibility = "1.8"
	sourceCompatibility = "1.8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile> {
	kotlinOptions {
		jvmTarget = "1.8"
	}
}

