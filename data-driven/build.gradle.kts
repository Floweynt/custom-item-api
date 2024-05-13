plugins {
    id("java")
}

group = "com.floweytf.customitemapi"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

val paperVersion: String by project

dependencies {
    compileOnly(project(":api"))
    compileOnly("io.papermc.paper:paper-api:$paperVersion")
}

tasks.test {
    useJUnitPlatform()
}
