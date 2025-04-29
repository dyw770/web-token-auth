description = "公共库"

plugins {
    id("java-library")
}

dependencies {
    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    compileOnly("com.github.therapi:therapi-runtime-javadoc")
    annotationProcessor("com.github.therapi:therapi-runtime-javadoc-scribe")
    compileOnly("org.springframework.security:spring-security-config")
    compileOnly("jakarta.servlet:jakarta.servlet-api")
    compileOnly("jakarta.validation:jakarta.validation-api")

    compileOnly("com.baomidou:mybatis-plus-core")
    compileOnly("com.baomidou:mybatis-plus-extension")
    
    api("org.apache.commons:commons-lang3")
}