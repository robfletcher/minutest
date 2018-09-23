import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.publish.maven.MavenPom
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

project.group = "com.oneeyedmen"
project.version = "0.8.0-SNAPSHOT"

repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.2.70"
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.1"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    implementation("org.junit.vintage:junit-vintage-engine:5.3.1")
    implementation("junit:junit:4.12")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:+")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:+") {
        because("enables running in IntelliJ using JUnit runner")
    }
}

tasks {
    "compileKotlin"(KotlinJvmCompile::class) { kotlinOptions.jvmTarget = "1.8" }
    "compileTestKotlin"(KotlinJvmCompile::class) { kotlinOptions.jvmTarget = "1.8" }

    withType<Test> {
        useJUnitPlatform {
            includeEngines("junit-jupiter", "junit-vintage")
        }
    }

    create<Jar>("sourceJar") {
        classifier = "sources"
        from(java.sourceSets["main"].allSource)
    }

    val readme = create("readme") {
        doFirst {
            generateReadme()
        }
    }

    "publish" {
        dependsOn(readme)
    }
}

artifacts {
    add("archives", tasks["jar"])
    add("archives", tasks["sourceJar"])
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks["sourceJar"])
            groupId = project.group as String
            artifactId = project.name
            version = project.version as String
        }
    }
}

// use ./gradlew clean publish bintrayUpload
bintray {
    user = "dmcg"
    key = System.getenv("BINTRAY_API_KEY")
    publish = true
    setPublications("mavenJava")
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "oneeyedmen-mvn"
        name = project.name
        version(delegateClosureOf<BintrayExtension.VersionConfig> {
            name = project.version as String
        })
    })
}

fun generateReadme() {
    val newReadmeLines = File("README.template.md").readLines().map { line ->
        "```insert-kotlin (.*)$".toRegex().find(line)?.groups?.get(1)?.value?.let { filename ->
            (listOf("```kotlin") + File(filename).readLines()).joinToString("\n")
        } ?: line
    }
    File("README.md").writeText(newReadmeLines.joinToString("\n"))
}
