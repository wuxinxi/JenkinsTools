plugins {
    id("java")
    id ("idea")
    id("org.jetbrains.intellij") version "1.7.0"
    id("org.jetbrains.changelog") version "1.3.1"
    id("io.freefair.lombok") version "6.5.0.3"
    id("org.sonarqube") version "3.4.0.2513"
    id ("jacoco")
}

group = "cn.xxstudy.jenkins"
version = "1.0-SNAPSHOT"

//apply {
//    plugin("org.jetbrains.intellij")
//    plugin("java")
//    plugin("idea")
//    plugin("io.freefair.lombok")
//}


repositories {
    mavenCentral()
    maven {
        setUrl("https://www.jetbrains.com/intellij-repository/snapshots")
    }

}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("222-EAP-SNAPSHOT")
    type.set("IC") // Target IDE Platform
    downloadSources.set(true)
    plugins.set(listOf("com.intellij.java"))
}


tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    patchPluginXml {
        sinceBuild.set("200")
        untilBuild.set("222.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}


dependencies {
//    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.github.cliftonlabs:json-simple:4.0.1") {
        exclude("junit")
    }
//    implementation("com.offbytwo.jenkins:jenkins-client:0.3.8")

    implementation("com.offbytwo.jenkins:jenkins-client:0.3.8") {
        exclude("org.slf4j")
        exclude("org.apache.logging.log4j")
        // provided by Idea Platform
        //exclude group: 'commons-io' // not exists in all products (missing in PyCharm e.g)
        exclude("commons-lang")
        exclude("commons-collections")
        exclude("commons-logging")
        exclude("org.apache.httpcomponents")
        exclude("com.fasterxml.jackson.core")
        exclude("jaxen")
    }
}
