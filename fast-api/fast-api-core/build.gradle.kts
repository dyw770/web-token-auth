description = "fast api 核心模块"

plugins {
    id("java-library")
}

dependencies {
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    compileOnly("jakarta.validation:jakarta.validation-api")

    api("com.baomidou:mybatis-plus-spring-boot3-starter")
    api("org.apache.commons:commons-lang3")
    api("commons-beanutils:commons-beanutils")

    api("com.alibaba:druid")

    testImplementation("junit:junit")
    
}