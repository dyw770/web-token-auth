plugins {
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
}


group = "cn.dyw"
version = "0.0.1-SNAPSHOT"

allprojects {

    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
        }
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("java")
        plugin("io.spring.dependency-management")
    }

    // 配置java版本
    plugins.withType(JavaPlugin::class.java) {
        extensions.getByType(JavaPluginExtension::class.java).apply {
            toolchain {
                languageVersion = JavaLanguageVersion.of(17)
            }
        }

        configurations.named("compileOnly") {
            extendsFrom(
                configurations.named("annotationProcessor").get()
            )
        }
    }
    
    tasks.withType<Test> {
        useJUnitPlatform()
        enabled = hasProperty("enableTest")
    }
    
    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
    }
    
    dependencyManagement {
        dependencies {
            imports {
                mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
                mavenBom("com.baomidou:mybatis-plus-bom:3.5.11")
            }
            
            dependency("com.alibaba:druid-spring-boot-3-starter:1.2.22")
            
            dependency("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")
            
            dependency("com.github.therapi:therapi-runtime-javadoc:0.15.0")
            dependency("com.github.therapi:therapi-runtime-javadoc-scribe:0.15.0")
        }
    }
}