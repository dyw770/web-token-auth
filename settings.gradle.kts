rootProject.name = "web-token-auth"

pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
        }
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        id("org.springframework.boot") version "3.4.4" apply false
        id("io.spring.dependency-management") version "1.1.7" apply false
        kotlin("jvm") version "2.1.20" apply false
        kotlin("plugin.spring") version "2.1.20" apply false
    }
}


include(":auth-demo")
include(":auth-common")
include(":auth-core")
include(":auth-db")
include(":auth-annotation")
include(":auth-support")
include(":auth-ui")