package buildsrc.convention

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

group = "app.ultradev.hytaleui"
version = "3.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(kotlin("reflect"))
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "25"
        targetCompatibility = "21"
    }
}

kotlin {
    jvmToolchain(21)
}


publishing {
    repositories {
        maven {
            name = "ultradevRepository"
            url = uri("https://mvn.ultradev.app/snapshots")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("kotlinJvm") {
            from(components["java"])
        }
    }
}
