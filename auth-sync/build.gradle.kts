plugins {
    id("java-library")
}

dependencies {
    api(project(":auth-common"))
    
    api("org.springframework.boot:spring-boot-starter-data-redis")
    api("com.fasterxml.jackson.core:jackson-databind")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")
}