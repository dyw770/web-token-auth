plugins {
    id("java-library")
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-cache")
    api("org.springframework.boot:spring-boot-starter-data-redis")
    
    implementation(project(":auth-common"))
    
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}
