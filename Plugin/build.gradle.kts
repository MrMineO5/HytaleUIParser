plugins {
    id("buildsrc.convention.common")
    id("com.gradleup.shadow") version "9.3.1"
    id("app.ultradev.hytalegradle") version "2.0.2"
}

dependencies {
    api(project(":Spec"))
}

hytale {
    manifest {
        mainClass = "app.ultradev.hytaleuiparser.plugin.HytaleUIParserPlugin"
        version = project.version.toString()
        serverVersion = "*"

        //author("Ellie")
        author("UltraDev")
    }
}

tasks.shadowJar {
    relocate("kotlin", "app.ultradev.hytaleuiparser.lib.kotlin")
}
