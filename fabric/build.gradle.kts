val mc_version: String by extra
val mod_version: String by extra
val mod_id: String by extra
val fabric_loader_version: String by extra
val fabric_version: String by extra
val kotlin_fabric_version: String by extra
val trinkets_version: String by extra
val cca_version: String by extra

fabric {
    enableMixins()

    dependOn(project(":api"))
    dependOn(project(":common"))
}

dependencies {
    modImplementation("dev.emi:trinkets:${trinkets_version}")
    modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${cca_version}")
}