object PluginCoordinates {
    const val ID = "dev.epicsquid.exposed.gradle.plugin"
    const val GROUP = "dev.epicsquid.exposed.gradle"
    const val VERSION = "0.3.4"
    const val IMPLEMENTATION_CLASS = "dev.epicsquid.exposed.gradle.plugin.ExposedGradlePlugin"
}

object PluginBundle { // TODO update this after repo rename
    const val VCS = "https://github.com/EpicSquid/exposed-intellij-plugin"
    const val WEBSITE = "https://github.com/EpicSquid/exposed-intellij-plugin"
    const val DESCRIPTION = "Exposed ORM framework gradle plugin"
    const val DISPLAY_NAME = "Exposed ORM framework gradle plugin"
    val TAGS = listOf(
        "plugin",
        "kotlin",
        "exposed",
        "database"
    )
}

