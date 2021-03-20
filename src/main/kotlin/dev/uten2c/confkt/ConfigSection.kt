package dev.uten2c.confkt

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.InputStream
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

abstract class ConfigSection(val key: String = "") {

    fun <T> option(): Option<T> = Option()

    fun reload(file: File) = reload(file.inputStream())

    fun reload(input: InputStream) {
        val parseYaml = yaml.load<ParsedYaml>(input)
        setValues(parseYaml, this, emptyList())
    }

    companion object {
        private val yaml = Yaml()

        @Suppress("UNCHECKED_CAST")
        private fun setValues(parsedYaml: ParsedYaml, section: ConfigSection, parents: List<ConfigSection>) {
            if (section.key.isEmpty() && parents.isNotEmpty()) {
                val classPath = parents.map { it::class.simpleName }.joinToString(".")
                throw IllegalArgumentException("$classPath.${section::class.simpleName}: The key is required for non-root ConfigSection.")
            }

            val kClass = section::class
            val basePath = parents
                .map { it.key }
                .filter { it.isNotEmpty() }
                .let { ArrayList(it) }
            if (section.key.isNotEmpty()) {
                basePath.add(section.key)
            }
            kClass.declaredMemberProperties.forEach { property ->
                property.isAccessible = true
                val delegate = (property as KProperty1<ConfigSection, *>).getDelegate(section)
                if (delegate is Option<*>) {
                    val name = property.annotations
                        .filterIsInstance<KeyName>()
                        .firstOrNull()?.value ?: property.name
                    val value = find(parsedYaml, ArrayList(basePath).apply { add(name) })!!
                    (delegate as Option<Any>).setValue(delegate, property, value)
                }
            }

            val p = ArrayList(parents).apply { add(section) }
            kClass.nestedClasses
                .map { it.objectInstance }
                .filterIsInstance<ConfigSection>()
                .forEach { setValues(parsedYaml, it, p) }
        }

        private fun find(parsedYaml: ParsedYaml, path: List<String>): Any? {
            var r: Any? = parsedYaml
            path.forEach { s ->
                r = (r as Map<*, *>)[s]
            }
            return r
        }
    }
}