plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.20" apply false
}

group = "com.mohanlv"
version = "0.0.3"

// All projects will use repositories from settings
allprojects {
    apply(plugin = "maven-publish")
}