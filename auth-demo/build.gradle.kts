description = "Spring Security Auth Demo"

plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":auth-core"))
    implementation(project(":auth-db"))

    implementation("com.alibaba:druid-spring-boot-3-starter")
    implementation("com.mysql:mysql-connector-j")
    
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")
    compileOnly("com.github.therapi:therapi-runtime-javadoc")
    annotationProcessor("com.github.therapi:therapi-runtime-javadoc-scribe")
    
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
