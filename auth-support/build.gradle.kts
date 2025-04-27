plugins {
    id("java-library")
}

dependencies {
    api(project(":auth-annotation"))
    api(project(":auth-common"))
    
    api("org.springframework:spring-expression")
    api("org.springframework:spring-aop")

    api("org.apache.commons:commons-lang3")
    
    implementation("org.slf4j:slf4j-api")
    
    compileOnly("org.springframework:spring-context")
    compileOnly("org.springframework:spring-security")
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

