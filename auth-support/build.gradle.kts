plugins {
    id("java-library")
}

dependencies {
    implementation(project(":auth-annotation"))
    implementation("org.springframework:spring-expression")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")
}

