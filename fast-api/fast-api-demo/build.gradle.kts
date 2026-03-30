plugins {
    id("java")
}

group = "cn.dyw"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":fast-api:fast-api-server"))

    implementation("com.alibaba:druid-spring-boot-3-starter")
    implementation("com.mysql:mysql-connector-j")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")
    compileOnly("com.github.therapi:therapi-runtime-javadoc")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
}

tasks.test {
    useJUnitPlatform()
}