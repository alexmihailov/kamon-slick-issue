plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

application.mainClass = "com.alexmihailov.MainKt"

val scalaBinaryVersion = "2.13"
val slickVersion = "3.3.2"
val kamonVersion = "2.6.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.typesafe.slick:slick_$scalaBinaryVersion:$slickVersion")
    implementation("com.typesafe.slick:slick-hikaricp_$scalaBinaryVersion:$slickVersion")
    implementation("org.scala-lang.modules:scala-java8-compat_$scalaBinaryVersion:1.0.2")
    implementation("io.kamon:kamon-bundle_$scalaBinaryVersion:$kamonVersion")
    implementation("io.kamon:kamon-status-page_$scalaBinaryVersion:$kamonVersion")
    implementation("io.kamon:kamon-jdbc_$scalaBinaryVersion:$kamonVersion")
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.5.3")
    implementation("org.slf4j:slf4j-nop:2.0.12")
    implementation("com.typesafe:config:1.4.3")
    implementation("com.h2database:h2:2.2.224")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}
