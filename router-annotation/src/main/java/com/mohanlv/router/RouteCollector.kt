package com.mohanlv.router

/**
 * 路由收集器接口
 * 由 RouteProcessor KAPT 生成实现类
 *
 * 实现类命名规则：{ModuleName}RouteCollector
 * 例如：HomeRouteCollector、LoginRouteCollector
 */
interface RouteCollector {
    /**
     * 获取该模块的所有路由
     * @return Map<路由路径, Fragment类名>
     */
    fun getRoutes(): Map<String, String>
}
