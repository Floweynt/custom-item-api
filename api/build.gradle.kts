plugins {
    id("java")
}

group = "com.floweytf.customitemapi"
version = "1.0.0"
val paperVersion: String by project

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:$paperVersion")
}