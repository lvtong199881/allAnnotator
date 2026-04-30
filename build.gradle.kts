// 根项目配置 - 供 Components 目录下的组件使用
plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.20" apply false
}

// 应用发布配置
apply(from = "publish.gradle.kts")