val trinkets_version: String by extra
val cca_version: String by extra

fabric {
    enableMixins()

    dependOn(project(":api"))
    dependOn(project(":common"))
}

dependencies {
    modImplementation("dev.emi:trinkets:${trinkets_version}")
    modImplementation("io.github.onyxstudios.Cardinal-Components-API:cardinal-components-base:${cca_version}")
}