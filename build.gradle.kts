import java.io.File

plugins {
    kotlin("jvm") version "1.2.71"
}

allprojects {
    group = "com.oneeyedmen"
    version = "0.22.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}