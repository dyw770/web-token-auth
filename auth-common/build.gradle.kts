description = "公共库"

plugins {
    id("java-library")
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    
    compileOnly("jakarta.servlet:jakarta.servlet-api")
    compileOnly("jakarta.validation:jakarta.validation-api")
    compileOnly("org.springframework.security:spring-security-config")
    
    compileOnly("org.projectlombok:lombok")
    compileOnly("com.github.therapi:therapi-runtime-javadoc")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("com.github.therapi:therapi-runtime-javadoc-scribe")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    
    compileOnly("com.baomidou:mybatis-plus-core")
    compileOnly("com.baomidou:mybatis-plus-extension")
    
    api("org.apache.commons:commons-lang3")
    
    api(project(":auth-annotation"))
}