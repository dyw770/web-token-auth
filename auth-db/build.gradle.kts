description = "授权 db 模块，实现数据库存储"

plugins {
    id("java-library")
}

dependencies {
    api(project(":auth-common"))

    api("com.baomidou:mybatis-plus-spring-boot3-starter")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.apache.commons:commons-lang3")
    api("org.springframework.security:spring-security-core")
    
    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")
    
    testImplementation("com.baomidou:mybatis-plus-generator")
    testImplementation("org.freemarker:freemarker")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.alibaba:druid-spring-boot-3-starter")
    testImplementation("com.mysql:mysql-connector-j")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    enabled = false
}