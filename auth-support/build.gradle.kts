plugins {
    id("java-library")
}

dependencies {
    api(project(":auth-annotation"))
    api(project(":auth-common"))
    
    api("org.springframework:spring-expression")
    api("org.springframework:spring-aop")
    compileOnly("org.springframework:spring-context")

    api("org.apache.commons:commons-lang3")
    
    implementation("org.slf4j:slf4j-api")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.springframework:spring-context")
    testImplementation("org.aspectj:aspectjweaver")
    testImplementation("org.slf4j:slf4j-simple")
}

