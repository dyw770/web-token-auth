description = "公共库"

dependencies {
    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    compileOnly("com.github.therapi:therapi-runtime-javadoc")
    annotationProcessor("com.github.therapi:therapi-runtime-javadoc-scribe")
}