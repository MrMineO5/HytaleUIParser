plugins {
    kotlin("jvm") version "2.3.0"
    `maven-publish`
}

group = "app.ultradev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(25)
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
