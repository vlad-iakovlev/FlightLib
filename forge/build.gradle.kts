import org.spongepowered.asm.gradle.plugins.MixinExtension

val minecraft_version: String by extra
val mod_id: String by extra
val forge_version: String by extra
val curios_forge_version: String by extra

buildscript {
    dependencies {
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}

plugins {
    id("eclipse")
    id("maven-publish")
    id("net.minecraftforge.gradle") version ("5.1.+")
    id("org.jetbrains.kotlin.jvm")
}

base {
    archivesName.set("${mod_id}-forge-${minecraft_version}")
}

apply(plugin = "org.spongepowered.mixin")

configure<MixinExtension> {
    add(sourceSets.main.get(), "${mod_id}.refmap.json")
    config("${mod_id}.mixins.json")
}

minecraft {
    mappings("official", minecraft_version)

    val ideaModule = "${rootProject.name.replace(" ", "_")}.${project.name}.main"

    runs {
        create("client") {
            workingDirectory(project.file("run"))
            ideaModule(ideaModule)
            taskName("Client")
            property("test", ideaModule)
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")
            mods {
                create(mod_id) {
                    source(sourceSets.main.get())
                    source(project(":common").sourceSets.main.get())
                }
            }
        }

        create("server") {
            workingDirectory(project.file("run/server"))
            ideaModule(ideaModule)
            taskName("Server")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")
            mods {
                create(mod_id) {
                    source(sourceSets.main.get())
                    source(project(":common").sourceSets.main.get())
                }
            }
        }
    }
}

dependencies {
    minecraft("net.minecraftforge:forge:${minecraft_version}-${forge_version}")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly(project(":common"))

    implementation(fg.deobf("top.theillusivec4.curios:curios-forge:${curios_forge_version}"))

    implementation("thedarkcolour:kotlinforforge:3.9.1")
}

//tasks.withType<JavaCompile> {
//    source(project(":common").sourceSets.main.allSource)
//}
//
//tasks.named("compileKotlin") {
//    source(project(":common").sourceSets.main.allSource)
//}

//processResources {
//    from(project(":common").sourceSets.main.resources)
//}

tasks.jar {
    finalizedBy("reobfJar")
}

val sourcesJar = tasks.named<Jar>("sourcesJar")

artifacts {
    archives(tasks.jar.get())
    archives(sourcesJar.get())
}