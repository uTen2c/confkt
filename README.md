![Maven metadata URL](https://img.shields.io/maven-metadata/v?label=ConfKt&metadataUrl=https%3A%2F%2Futen2c.github.io%2Frepo%2Fdev%2Futen2c%2Fconfkt%2Fmaven-metadata.xml)

# ConfKt

Config

### Groovy DSL
```groovy
repositories {
  maven { url 'https://uten2c.github.io/repo/' }
}

dependencies {
  implementation 'dev.uten2c:confkt:VERSION'
}
```

### Kotlin DSL
```kotlin
repositories {
  maven("https://uten2c.github.io/repo/")
}

dependencies {
  implementation("dev.uten2c:confkt:VERSION")
}
```

### Example

```yaml
key: hello

section1:
  key: 1
  double: 1.1
  list:
    - hello
    - bye

section2:
  snake_case: hello_bye
```

```kotlin
object MyConfig : ConfigSection() {
    val key by option<String>()
    
    object Section1 : ConfigSection("section1") {
        val key by option<Int>()
        val double by option<Double>()
        val list by option<List<String>>()
    }
    
    object Section2 : ConfigSection("section2") {
        @KeyName("snake_case") val snakeCase by option<String>()
    }
}
```