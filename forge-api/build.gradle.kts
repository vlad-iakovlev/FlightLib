val minecraft_version: String by extra
val mod_version: String by extra
val mod_id: String by extra
val forge_version: String by extra

plugins {
    id("eclipse")
    id("net.minecraftforge.gradle") version ("5.1.+")
    id("org.jetbrains.kotlin.jvm")
}

base {
    archivesName.set("${mod_id}-forge-api-${mod_version}")
}

minecraft {
    mappings("official", minecraft_version)
}

dependencies {
    minecraft("net.minecraftforge:forge:${minecraft_version}-${forge_version}")
    implementation("com.google.code.findbugs:jsr305:3.0.2")

    implementation(project(":api"))
}

tasks.jar {
    finalizedBy("reobfJar")
}

val sourcesJar = tasks.named<Jar>("sourcesJar")

artifacts {
    archives(tasks.jar.get())
    archives(sourcesJar.get())
}