plugins {
    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
}

group = "com.mohanlv"
version = "0.0.4"

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
            version = "0.0.4"
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