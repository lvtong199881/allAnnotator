package com.mohanlv.router;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.lang.annotation.Annotation;
import com.google.auto.service.AutoService;

/**
 * KAPT Processor：扫描 @Route 注解，生成 RouteCollector 实现类
 * 生成 META-INF/services 文件供 ServiceLoader 运行时发现
 */
@SupportedOptions({
    RouteProcessor.OPTION_COLLECTOR_PACKAGE,
    RouteProcessor.OPTION_MODULE_NAME
})
@AutoService(Processor.class)
public class RouteProcessor extends AbstractProcessor {
    
    static final String OPTION_COLLECTOR_PACKAGE = "routerCollectorPackage";
    static final String OPTION_MODULE_NAME = "routerCollectorModuleName";
    
    private String packageName = "com.mohanlv.router";
    private String moduleName = "";
    
    private static final String ANNOTATION_CLASS = "com.mohanlv.router.annotation.Route";
    
    @Override
    public Set<String> getSupportedOptions() {
        return Set.of(OPTION_COLLECTOR_PACKAGE, OPTION_MODULE_NAME);
    }
    
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(ANNOTATION_CLASS);
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_17;
    }
    
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        packageName = processingEnv.getOptions().getOrDefault(OPTION_COLLECTOR_PACKAGE, "com.mohanlv.router");
        moduleName = processingEnv.getOptions().getOrDefault(OPTION_MODULE_NAME, "");
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> routes;
        try {
            @SuppressWarnings("unchecked")
            Class<? extends Annotation> annotationClass =
                (Class<? extends Annotation>) Class.forName(ANNOTATION_CLASS);
            routes = roundEnv.getElementsAnnotatedWith(annotationClass);
        } catch (ClassNotFoundException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Failed to load Route annotation: " + e.getMessage());
            return false;
        }
        
        if (routes.isEmpty()) return false;
        
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found " + routes.size() + " @Route annotations");
        generateCollector(routes);
        
        return true;
    }
    
    private void generateCollector(Set<? extends Element> routes) {
        String collectorClassName = moduleName.substring(0, 1).toUpperCase() + moduleName.substring(1) + "RouteCollector";
        if (collectorClassName.startsWith("_")) {
            collectorClassName = moduleName.replaceAll("^_+", "") + "RouteCollector";
            if (collectorClassName.isEmpty()) collectorClassName = "RouteCollector";
        }
        String qualifiedCollectorName = packageName + "." + collectorClassName;
        
        try {
            JavaFileObject collectorFile = processingEnv.getFiler().createSourceFile(qualifiedCollectorName);
            try (Writer writer = collectorFile.openWriter()) {
                writer.write(buildCollectorCode(collectorClassName, routes));
            }
            
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Generated " + qualifiedCollectorName);
        } catch (Exception e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Failed to generate collector: " + e.getMessage());
        }
    }
    
    private String buildCollectorCode(String className, Set<? extends Element> routes) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("package ").append(packageName).append(";\n\n");
        sb.append("/**\n");
        sb.append(" * 自动生成的路由注册类\n");
        sb.append(" * 由 RouteProcessor KAPT 生成\n");
        sb.append(" * 在模块初始化时调用 register() 方法注册路由\n");
        sb.append(" */\n");
        sb.append("public class ").append(className).append(" {\n");
        sb.append("    public static void register(com.mohanlv.router.RouterManager manager) {\n");
        
        List<? extends Element> routeList = new ArrayList<>(routes);
        for (Element element : routeList) {
            TypeElement typeElement = (TypeElement) element;
            
            AnnotationMirror foundMirror = getAnnotationMirror(typeElement, ANNOTATION_CLASS);
            if (foundMirror == null) continue;
            
            String path = getAnnotationValue(foundMirror, "path");
            String qualifiedName = typeElement.getQualifiedName().toString();
            
            if (path.isEmpty()) {
                processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.WARNING,
                    "@Route path cannot be empty: " + qualifiedName,
                    element
                );
                continue;
            }
            
            sb.append("        manager.registerInternal(\"").append(extractPath(path)).append("\", ")
               .append(qualifiedName).append(".class);\n");
        }
        
        sb.append("    }\n");
        sb.append("}\n");
        
        return sb.toString();
    }
    
    private AnnotationMirror getAnnotationMirror(TypeElement typeElement, String annotationClass) {
        for (AnnotationMirror mirror : typeElement.getAnnotationMirrors()) {
            if (mirror.getAnnotationType().toString().equals(annotationClass)) {
                return mirror;
            }
        }
        return null;
    }
    
    private String extractPath(String fullPath) {
        // 从 oneandroid://host/path 提取 host/path
        int idx = fullPath.indexOf("://");
        if (idx != -1) {
            return fullPath.substring(idx + 3);
        }
        return fullPath;
    }
    
    @SuppressWarnings("unchecked")
    private String getAnnotationValue(AnnotationMirror annotation, String name) {
        Map<String, String> values = new java.util.HashMap<>();
        for (Map.Entry<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> entry : annotation.getElementValues().entrySet()) {
            String key = entry.getKey().getSimpleName().toString();
            String value = entry.getValue().toString();
            if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            values.put(key, value);
        }
        return values.getOrDefault(name, "");
    }
}