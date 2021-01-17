plugins {
    kotlin("jvm") version "1.4.30-M1"
    kotlin("plugin.serialization") version "1.4.10"
}

group = "org.example"
version = "0.0.3-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
}
