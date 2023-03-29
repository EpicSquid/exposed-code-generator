package org.jetbrains.exposed.gradle

import java.io.Serializable

/**
 * User configuration for generating Exposed code.
 */
data class ExposedCodeGeneratorConfiguration(
        val packageName: String = "", // generated files package
        val generateSingleFile: Boolean = true, // all tables are written to a single file if true, each to a separate file otherwise
        val generatedFileName: String? = if (generateSingleFile) "" else null,
        val collate: String? = null,
        val columnMappings: Map<String, String> = emptyMap(),
        val dateTimeProvider: String? = null,
        val useFullNames: Boolean = generateSingleFile,
        val useDao: Boolean = false,
        val customMappings: Map<String, CustomMappings> = emptyMap()
)
data class CustomMappings(
        var columnPropertyClassName: String?,
        var columnFunctionName: String?,
        var isColumnTyped: Boolean = false
) : Serializable
