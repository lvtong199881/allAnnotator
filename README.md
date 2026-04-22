# allAnnotator

Android 注解处理器工具集，用于组件化架构中的编译时代码生成。

---

## 🎯 包含模块

| 模块 | 说明 |
|------|------|
| **init-annotator** | InitTask 注解处理器，扫描 `@InitTask` 注解生成启动任务收集器 |
| **router-annotation** | 路由注解 `@Route` 的定义 |
| **router-annotator** | 路由注解处理器，扫描 `@Route` 生成路由收集器 |

---

## 📁 项目结构

```
allAnnotator/
├── init-annotator/               # InitTask 注解处理器
│   ├── src/main/java/
│   │   └── com/mohanlv/startup/
│   │       ├── annotation/
│   │       │   └── InitTask.kt   # @InitTask 注解定义
│   │       └── InitTaskProcessor.java  # KAPT 处理器
│   └── build.gradle.kts
│
├── router-annotation/             # 路由注解
│   ├── src/main/java/
│   │   └── com/mohanlv/router/
│   │       ├── RouteCollector.kt # 路由收集器接口
│   │       └── annotation/
│   │           └── Route.kt      # @Route 注解定义
│   └── build.gradle.gradle.kts
│
├── router-annotator/             # 路由注解处理器
│   ├── src/main/java/
│   │   └── com/mohanlv/router/
│   │       └── RouteProcessor.java  # KAPT 处理器
│   └── build.gradle.kts
│
└── build.gradle.kts              # 根构建配置
```

---

## 🚀 构建发布

**发布到本地 Maven：**

```bash
./gradlew publishToMavenLocal
```

**发布到 GitHub Packages：**

```bash
./gradlew publish
```

---

## 📦 使用方式

### 1. 添加依赖

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenLocal()
        maven { url = uri("https://maven.pkg.github.com/lvtong199881/allAnnotator") }
    }
}

// build.gradle.kts
dependencies {
    // InitTask 注解处理器
    kapt("com.mohanlv:init-annotator:0.0.6")
    
    // 路由注解处理器
    kapt("com.mohanlv:router-annotator:0.0.6")
    compileOnly("com.mohanlv:router-annotation:0.0.6")
}
```

### 2. InitTask 注解使用

```kotlin
@InitTask(key = "home", priority = 10)
class HomeStartupTask(application: Application) : StartupTask {
    override fun create() {
        // 初始化逻辑
    }
}
```

### 3. Route 注解使用

```kotlin
@Route(path = "oneandroid://home/main", description = "首页")
class HomeFragment : Fragment()
```

---

## 📄 License

MIT License

---

**🌙 莫寒慕 · lvtong199881**
