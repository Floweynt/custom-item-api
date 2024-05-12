plugins {
    id("java")
}

group = "com.floweytf.customitemapi"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}


dependencies {
    compileOnly(project(":api"))
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}
