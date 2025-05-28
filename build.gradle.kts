plugins {
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
    kotlin("jvm") apply false
}

allprojects {

    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
        }
        mavenCentral()
    }

    group = "cn.dyw"
    version = "0.0.1-SNAPSHOT"
}

val nonJavaProjects = listOf("auth-ui")

subprojects {
    if (!nonJavaProjects.contains(project.name)) {
        apply {
            plugin("java")
            plugin("io.spring.dependency-management")
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

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.add("-Xjsr305=strict")
        }
    }

}

tasks.register("statistic") {
    val suffix = arrayOf("java", "xml", "vue", "ts", "kt")
    val ignore = arrayOf("node_modules", ".git", "target", "dist", "build", ".idea")
    val statistic = project.projectDir.walk()
        .onEnter { file ->
            ignore.none { it == file.name }
        }
        .filter { file ->
            file.isFile && suffix.any { file.extension == it }
        }
        .map {
            it.extension to it.readLines(charset = Charsets.UTF_8).count()
        }
        .groupBy { it.first }
        .map { (key, value) ->
            key to value.sumOf { it.second }
        }
        .toList()

    statistic.forEach { (key, value) -> 
        println("""
            文件类型:  $key, 代码行数: $value
        """.trimIndent())
    }
}