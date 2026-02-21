plugins {
    id("buildsrc.convention.common")
}

dependencies {
    api(project(":Parser"))
    api("org.mongodb:bson:5.6.3")
}

// -------------------------
// -------- CODEGEN --------
// -------------------------
sourceSets {
    val main by getting
    val codegen by creating {
        kotlin.srcDir("src/codegen/kotlin")
        compileClasspath += configurations.compileClasspath.get()
        runtimeClasspath += output + compileClasspath
    }
    main {
        kotlin.srcDir(layout.buildDirectory.dir("generated/sources/codegen/main/kotlin"))
    }
}
val codegenImplementation by configurations.getting {
    extendsFrom(configurations["implementation"])
}
dependencies {
    codegenImplementation("com.squareup:kotlinpoet:1.15.3")
}

val generateSources by tasks.registering(JavaExec::class) {
    group = "codegen"
    description = "Generate Kotlin sources"

    classpath = sourceSets["codegen"].runtimeClasspath
    mainClass.set("app.ultradev.hytaleuiparser.codegen.MainKt")

    args(layout.buildDirectory.dir("generated/sources/codegen/main/kotlin").get().asFile.absolutePath)
}
tasks.compileKotlin {
    dependsOn(generateSources)
}
