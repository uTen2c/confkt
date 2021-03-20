plugins {
    kotlin("jvm") version "1.4.31"
    `maven-publish`
}

base {
    archivesBaseName = "confkt"
    group = "dev.uten2c"
    version = "1.0.0"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.yaml:snakeyaml:1.26")
}

val sourcesJar = tasks.create<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.base.archivesBaseName
            version = project.version.toString()

            from(components["java"])
            artifact(sourcesJar)
        }
    }
    repositories {
        maven {
            url = uri("${System.getProperty("user.home")}/maven-repo")
            println(uri("${System.getProperty("user.home")}/maven-repo"))
        }
    }
}