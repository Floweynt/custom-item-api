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
}

val igniteVersion: String by project
val paperVersion: String by project
val mixinVersion: String by project
val mixinExtrasVersion: String by project
val tinyRemapperVersion: String by project

val shadowImplementation: Configuration by configurations.creating

dependencies {
    paperweight.paperDevBundle(paperVersion)

    // real runtime classpath dependencies
    implementation("space.vectrix.ignite:ignite-launcher:$igniteVersion")
    implementation("io.papermc.paper:paper-server:userdev-$paperVersion")

    // for some reason, these things are not dependants of paper userdev...
    // we have to add them
    implementation("org.apache.maven.resolver:maven-resolver-connector-basic:1.9.19")
    implementation("org.apache.maven.resolver:maven-resolver-transport-http:1.9.19")
    implementation("com.lmax:disruptor:3.4.2")

    // compile time deps, just mixins and stuff
    compileOnly("space.vectrix.ignite:ignite-api:$igniteVersion")
    compileOnly("org.spongepowered:mixin:$mixinVersion")
    compileOnly("io.github.llamalad7:mixinextras-common:$mixinExtrasVersion")
    compileOnly("io.papermc.paper:paper-api:$paperVersion")

    // misc
    remapper("net.fabricmc:tiny-remapper:$tinyRemapperVersion:fat")

    implementation(project(":api"))
    shadowImplementation(project(":api"))
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