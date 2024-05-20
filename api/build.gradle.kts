plugins {
    id("java")
}

group = "com.floweytf.customitemapi"
version = "1.0.0"
val paperVersion: String by project
val nbtApiVersion: String by project

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:$paperVersion")
    implementation("de.tr7zw:item-nbt-api:$nbtApiVersion")
}