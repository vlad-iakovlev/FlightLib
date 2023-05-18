pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            url = uri("https://maven.minecraftforge.net/")
        }
        maven {
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            url = uri("https://repo.spongepowered.org/repository/maven-public/")
        }
    }
}

rootProject.name = "Flight Lib"
include(
    "api", "common",
    "forge-api", "forge",
    "fabric",
)

