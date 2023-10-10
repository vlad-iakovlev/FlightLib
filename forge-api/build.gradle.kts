val mod_version: String by extra
val mod_id: String by extra

forge {
    dependOn(project(":api"))
}

base {
    archivesName.set("${mod_id}-forge-api-${mod_version}")
}

tasks.withType<JavaCompile> {
    enabled = false
}