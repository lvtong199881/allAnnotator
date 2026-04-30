// publish.gradle.kts - 发布配置
// 在根项目应用此配置

subprojects {
    apply(plugin = "maven-publish")

    afterEvaluate {
        if (plugins.hasPlugin("com.android.library") || plugins.hasPlugin("java-library") || plugins.hasPlugin("org.jetbrains.kotlin.jvm")) {
            configure<PublishingExtension> {
                repositories {
                    maven {
                        name = "GitHubPackages"
                        url = uri("https://maven.pkg.github.com/lvtong199881/PackagesMaven")
                        credentials {
                            username = "lvtong199881"
                            password = System.getenv("GITHUB_TOKEN") ?: run {
                                val tokenFile = File(System.getProperty("user.home"), ".github_token")
                                if (tokenFile.exists()) tokenFile.readText().trim() else ""
                            }
                        }
                    }
                }
            }
        }
    }
}