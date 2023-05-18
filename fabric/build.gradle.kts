val minecraft_version: String by extra
val mod_id: String by extra
val fabric_loader_version: String by extra
val fabric_version: String by extra

plugins {
    id("fabric-loom") version ("1.0-SNAPSHOT")
    id("maven-publish")
    id("idea")
    id("org.jetbrains.kotlin.jvm")
}

base {
    archivesName.set("${mod_id}-fabric-${minecraft_version}")
}

dependencies {
    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:${fabric_loader_version}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabric_version}")

    implementation("com.google.code.findbugs:jsr305:3.0.2")

    modImplementation("net.fabricmc:fabric-language-kotlin:1.9.1+kotlin.1.8.10")

    implementation (project(":common"))
}

loom {
    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir("run")
        }
        named("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
            runDir("run/server")
        }
    }
}

//tasks.withType(JavaCompile) {
//    source(project(":common").sourceSets.main.allSource)
//}