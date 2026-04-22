package com.mohanlv.router.annotation

/**
 * 路由注解，用于标记需要注册的 Fragment
 * 配合 RouteProcessor KAPT 生成路由表
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Route(
    /**
     * 路由路径，格式：oneandroid://host/path
     */
    val path: String,
    /**
     * 页面描述
     */
    val description: String = ""
)