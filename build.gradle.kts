plugins {
    `java-library`
    id("org.ajoberstar.grgit") version "5.2.2"
    id("io.papermc.paperweight.userdev") version "1.5.11"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.spongepowered.org/maven/")
    maven("https://maven.floweytf.com/releases")

    maven {
        name = "CodeMC"
        url = uri("https://repo.codemc.io/repository/maven-public/")
    }
}

val igniteVersion: String by project
val paperVersion: String by project
val mixinVersion: String by project
val mixinExtrasVersion: String by project
val tinyRemapperVersion: String by project
val nbtApiVersion: String by project

val shadowImplementation: Configuration by configurations.creating

dependencies {
    paperweight.paperDevBundle(paperVersion)

    // We need ignite!
    implementation("space.vectrix.ignite:ignite-launcher:$igniteVersion")

    // We need paper!
    implementation("io.papermc.paper:paper-server:userdev-$paperVersion")

    // Required for server to start when running "Minecraft Server"
    implementation("org.apache.maven.resolver:maven-resolver-connector-basic:1.9.19")
    implementation("org.apache.maven.resolver:maven-resolver-transport-http:1.9.19")
    implementation("com.lmax:disruptor:3.4.2")

    // Compile time: mixins, ignite, paper
    compileOnly("space.vectrix.ignite:ignite-api:$igniteVersion")
    compileOnly("org.spongepowered:mixin:$mixinVersion")
    compileOnly("io.github.llamalad7:mixinextras-common:$mixinExtrasVersion")
    compileOnly("io.papermc.paper:paper-api:$paperVersion")

    // Tiny remapper
    remapper("net.fabricmc:tiny-remapper:$tinyRemapperVersion:fat")

    // API
    implementation(project(":api"))
    shadowImplementation(project(":api"))

    // This is automatically bundled with :api, but this dep is needed... for some reason.
    compileOnly("de.tr7zw:item-nbt-api:$nbtApiVersion")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
    withSourcesJar()
}

tasks {
    jar {
        archiveClassifier.set("dev")
        manifest {
            attributes["Git-Branch"] = grgit.branch.current().name
            attributes["Git-Hash"] = grgit.log().first().id
        }
    }

    shadowJar {
        archiveClassifier.set("")
        configurations = listOf(shadowImplementation)
    }

    reobfJar {
        remapperArgs.add("--mixin")
    }

    build {
        dependsOn(reobfJar)
    }
}