plugins {
    id("com.possible-triangle.gradle") version "0.1.1"
}

withKotlin()

subprojects {
    repositories {
        mavenCentral()

        maven {
            url = uri("https://repo.spongepowered.org/repository/maven-public/")
            content {
                includeGroup("org.spongepowered")
            }
        }

        maven {
            url = uri("https://www.cursemaven.com")
            content {
                includeGroup("curse.maven")
            }
        }
        maven {
            url = uri("https://maven.theillusivec4.top/")
            content {
                includeGroup("top.theillusivec4.curios")
            }
        }

        maven {
            url = uri("https://thedarkcolour.github.io/KotlinForForge/")
            content {
                includeGroup("thedarkcolour")
            }
        }
        maven {
            url = uri("https://maven.terraformersmc.com/")
            content {
                includeGroup("dev.emi")
            }
        }
        maven {
            url = uri("https://maven.ladysnake.org/releases")
            content {
                includeGroup("dev.onyxstudios.cardinal-components-api")
            }
        }
    }

    enablePublishing {
        githubPackages()

        repositories {
            env["LOCAL_MAVEN"]?.let {
                maven {
                    url = uri(it)
                }
            }
        }
    }
}

enableSonarQube()