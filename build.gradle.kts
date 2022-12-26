import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
}

group = "com.bory"
version = "1.0-SNAPSHOT"

val reactorVersion = "3.5.1"
val kotlinVersion = "1.7.22"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.projectreactor:reactor-core:$reactorVersion")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")

    testImplementation(kotlin("test"))
    testImplementation("io.projectreactor:reactor-test:$reactorVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}