plugins {
    id("java")
}

group = "com.floweytf.customitemapi"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-public/")
}

val paperVersion: String by project
val nbtApiVersion: String by project

dependencies {
    compileOnly(project(":api"))
    compileOnly("io.papermc.paper:paper-api:$paperVersion")
    compileOnly("de.tr7zw:item-nbt-api:$nbtApiVersion")
}

tasks.test {
    useJUnitPlatform()
}
