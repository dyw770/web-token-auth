description = "fast api 对外服务模块"

plugins {
    id("java-library")
}

dependencies {
    api(project(":auth-common"))
    api(project(":fast-api:fast-api-core"))

    api("com.baomidou:mybatis-plus-jsqlparser")

    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-validation")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")
    compileOnly("com.github.therapi:therapi-runtime-javadoc")
    annotationProcessor("com.github.therapi:therapi-runtime-javadoc-scribe")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("com.baomidou:mybatis-plus-generator")
    testImplementation("org.freemarker:freemarker")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.alibaba:druid-spring-boot-3-starter")
    testImplementation("com.mysql:mysql-connector-j")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator")
}