plugins {
    id("buildsrc.convention.common")
}

dependencies {
    api(project(":Parser"))
    api(project(":Generated"))
    api(project(":Renderer"))
}
