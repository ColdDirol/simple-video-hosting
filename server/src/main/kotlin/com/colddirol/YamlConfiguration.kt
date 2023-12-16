package com.colddirol

import io.ktor.server.config.*
import io.ktor.server.config.yaml.*


/**
 * The YamlConfiguration object allows to get data from application.yaml config file.
 *
 * @author Vladimir Kartashev
 */
object YamlConfiguration {
    /**
     * The function *getApplicationConfig()* allows to get ApplicationConfig
     * with the specified path.
     *
     * The function makes it easy to get ApplicationConfig
     * from application.yaml along the path to the graph.
     *
     * Example:
     *
     * val config = YamlConfiguration.getApplicationConfig("graph")
     *
     * val value = databaseConfig?.propertyOrNull("value")?.getString()
     *
     * @param path
     *
     * @return ApplicationConfig
     */
    fun getApplicationConfig(path: String): ApplicationConfig {
        return YamlConfig("application.yaml")?.config(path) ?: throw ApplicationConfigurationException("Path $path not found!")
    }
}