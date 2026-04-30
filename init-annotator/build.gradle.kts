plugins {
    id("java-library")
    id("maven-publish")
}

group = "com.mohanlv"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:2.1.20")
    compileOnly("com.google.auto.service:auto-service-annotations:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.mohanlv"
            artifactId = "init-annotator"
            val moduleVersion = project.findProperty("$artifactId.version")?.toString() ?: "1.0.0"
            version = moduleVersion
            from(components["java"])
            pom {
                name.set("init-annotator")
                description.set("KAPT processor for @InitTask annotation")
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