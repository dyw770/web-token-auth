description = "授权 db 模块，实现数据库存储"

plugins {
    id("java-library")
}

dependencies {
    api(project(":auth-common"))

    api("com.baomidou:mybatis-plus-spring-boot3-starter")
    api("com.baomidou:mybatis-plus-jsqlparser")

    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.security:spring-security-core")
    api("org.springframework.security:spring-security-web")
    api("org.springframework.security:spring-security-config")
    api("org.springframework.boot:spring-boot-starter-validation")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")
    compileOnly("com.github.therapi:therapi-runtime-javadoc")
    annotationProcessor("com.github.therapi:therapi-runtime-javadoc-scribe")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("com.baomidou:mybatis-plus-generator")
    testImplementation("org.freemarker:freemarker")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.alibaba:druid-spring-boot-3-starter")
    testImplementation("com.mysql:mysql-connector-j")
    testImplementation(project(":auth-cache"))
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}