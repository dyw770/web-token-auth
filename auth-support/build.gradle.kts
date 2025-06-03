plugins {
    id("java-library")
}

dependencies {
    api(project(":auth-annotation"))
    api(project(":auth-common"))

    compileOnly("org.springframework:spring-expression")

    compileOnly("org.slf4j:slf4j-api")
    compileOnly("org.springframework.boot:spring-boot-starter-aop")
    compileOnly("org.springframework:spring-context")
    compileOnly("org.springframework.security:spring-security-config")
    compileOnly("org.springframework.security:spring-security-web")
    compileOnly("org.springframework:spring-webmvc")
    compileOnly("jakarta.servlet:jakarta.servlet-api")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.springframework:spring-context")
    testImplementation("org.aspectj:aspectjweaver")
    testImplementation("org.slf4j:slf4j-simple")
}

