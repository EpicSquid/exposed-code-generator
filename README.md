# Exposed Code Generation Gradle Plugin
[![Kotlinlang Slack Channel](https://img.shields.io/badge/slack-@kotlinlang/exposed-yellow.svg?logo=slack?style=flat)](https://kotlinlang.slack.com/archives/C0CG7E0A1)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

This Gradle plugin connects to a database and generates Exposed table definitions for all of its tables.

### Usage:

Add plugin declaration into your build script:

You will need to set up a personal access token for github packages. You can do so following the guide 
[here](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry).
```kotlin
pluginManagement {
	repositories {
     maven {
        name = "GithubPackagesExposedCodeGenerator"
        url = uri("https://maven.pkg.github.com/EpicSquid/exposed-code-generator")
        credentials {
           username = System.getenv("GITHUB_USERNAME")
           password = System.getenv("GITHUB_TOKEN")
        }
     }
  }
   plugins {
      id("dev.epicsquid.exposed.gradle.plugin")
   }
}
```
Use gradle task as

`gradle generateExposedCode`

### How to specify parameters:
Using a task configuration in a `build.gradle` file you can specify how you want all the tables to be generated.
The following parameters are available:

```kotlin
exposedCodeGeneratorConfig {
   // Set up the database connection parameters. 
   // Either all of the following can be filled out or a single connectionUrl can be provided
   database {
      user = "root"
      password = "password"
      databaseName = "postgres"
      databaseDriver = "postgreql"
      host = "localhost"
      port = "5432"
   }

   outputDirectory.set(file("build/generated/exposed")) // Specify the output directory for the generated files
   generateSingleFile = false // Should all the database tables be in a single file
   packageName = "dev.epicsquid.model.generated" // Package name to use for the generated files

   // Specify what date-time library to use for timestamp and date columns. Can be any of the following:
   // "java-time"
   // "jodatime"
   // "kotlin-datetime"
   // defaults to "java-time" if not present
   dateTimeProvider = "kotlin-datetime"

   useFullNames = false // Use schema.table name for Table class names. Defaults to true
   useDao = true // Use the DAO for generated tables to define references. Defaults to false

   // Custom mappings for classes in your project for columns not supported by exposed.
   customMappings {
      // Use the name of the column you are creating a custom mapping for
      create("json_column") {
         // The type of the column class (in the case of json, this could be the serializable data class)
         columnPropertyClassName = "dev.epicsquid.model.columns.JsonColumnType"
         // The column function to call
         columnFunctionName = "dev.epicsquid.model.columns.json"
         // Force generating a generic type definition for the column function call
         isColumnTyped = true
      }
   }

   // Custom mappings for enums using the customEnumeration function
   enums {
      create("custom_enum") {
         // The enum class name in your project 
         enumClassName = "dev.epicsquid.model.enums.CustomEnum"
         // The enum declaration in the database. In postgres this must be the name of the type created in the database.
         // For H2 and mysql, this can be the Enum() declaration in sql
         databaseDeclaration = "custom_enum"
         // If using postgres, provide your implementation of the PGEnum object for conversion
         pgEnumClassName = "dev.epicsquid.model.PGEnum"
      }
   }

   // Tables to ignore when generating code. Must be fully qualified with the schema name
   ignoreTables = listOf("public.flyway_schema_history")
}
```
### Database connection parameters:

1. `connectionURL` -- connection URL as used with JDBC (e.g. `jdbc:postgresql://localhost:12346/user=postgres&password=`)

2. Specifying each connection parameter:
    1. `databaseDriver`, as used with JDBC (e.g. `postgresql`, `h2`)
    2. `databaseName` 
    3. `user`
    4. `password`
    5. `host` (IPv4)
    6. `port`
    7. `ipv6Host`
    8. `connectionProperties` - map of properties that will be added to `connectionURL` string
    
All of those parameters are optional; however, the expected behavior is that the user does not mix a `connectionURL` with other parameters.