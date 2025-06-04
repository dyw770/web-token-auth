plugins {
    id("java-library")
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-cache")
    api("org.springframework.boot:spring-boot-starter-data-redis")
    api("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.apache.commons:commons-pool2")
    
    implementation(project(":auth-common"))
    implementation("org.springframework:spring-web")

    compileOnly("org.projectlombok:lombok")
    compileOnly("org.springframework.boot:spring-boot-autoconfigure")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}
