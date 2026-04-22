plugins {
    id("java-library")
    id("maven-publish")
}

group = "com.mohanlv"
version = "0.0.4"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(project(":router-annotation"))
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:2.1.20")
    compileOnly("com.google.auto.service:auto-service-annotations:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.mohanlv"
            artifactId = "router-annotator"
            version = "0.0.4"
            from(components["java"])
            pom {
                name.set("router-annotator")
                description.set("KAPT processor for @Route annotation")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
            }
        }
    }
    repositories {
        mavenLocal()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/lvtong199881/allAnnotator")
            credentials {
                username = "lvtong199881"
                password = "\u0067hp_K4MX0aj0qJhu88vPpl3CWwgfzoRwUK4Lu4Fn"
            }
        }
    }
}