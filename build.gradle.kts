plugins {
    `java-library`
    id("org.ajoberstar.grgit") version "5.2.2"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.5.11"
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.spongepowered.org/maven/")
    maven("https://maven.floweytf.com/releases")
}

val igniteVersion = "1.0.2-SNAPSHOT"
val paperVersion = "1.20.4-R0.1-SNAPSHOT"

dependencies {
    paperweight.paperDevBundle(paperVersion)

    implementation("space.vectrix.ignite:ignite-launcher:$igniteVersion")
    implementation("io.papermc.paper:paper-server:userdev-$paperVersion")

    // IDK why these things just aren't shipped
    implementation("org.apache.maven.resolver:maven-resolver-connector-basic:1.9.19")
    implementation("org.apache.maven.resolver:maven-resolver-transport-http:1.9.19")
    implementation("com.lmax:disruptor:3.4.2")

    compileOnly("space.vectrix.ignite:ignite-api:$igniteVersion")
    compileOnly("org.spongepowered:mixin:0.8.5")
    compileOnly("io.github.llamalad7:mixinextras-common:0.3.5")
    compileOnly("org.jetbrains:annotations:24.1.0")

    remapper("net.fabricmc:tiny-remapper:0.10.1:fat")
    implementation(project(":api"))
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

    reobfJar {
        remapperArgs.add("--mixin")
    }

    build {
        dependsOn(reobfJar)
    }
}