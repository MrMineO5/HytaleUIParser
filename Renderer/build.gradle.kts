plugins {
    id("buildsrc.convention.common")
    kotlin("plugin.serialization") version "2.3.0"
}

repositories {
    maven("https://jogamp.org/deployment/maven")
}

dependencies {
    implementation(project(":Generated"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")

    implementation("org.jogamp.gluegen:gluegen-rt-main:2.6.0")
    implementation("org.jogamp.jogl:jogl-all-main:2.6.0")
}
