plugins {
    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
}

group = "com.mohanlv"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:2.1.20")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.mohanlv"
            artifactId = "router-annotation"
            val moduleVersion = project.findProperty("$artifactId.version")?.toString() ?: "0.0.1"
            version = moduleVersion
            from(components["java"])
            pom {
                name.set("router-annotation")
                description.set("@Route annotation for router")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
            }
        }
    }
}