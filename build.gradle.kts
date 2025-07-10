plugins {
    id("java")
}

group = "ru.netology"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {


    // Библиотека для работы с CSV
    implementation("com.opencsv:opencsv:5.7.1")
    implementation("com.google.code.gson:gson:2.8.9")

    // Библиотека для работы с XML
    implementation("javax.xml.bind:jaxb-api:2.3.1")

    // Lombok (для сокращения кода)
    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")

    // Тестирование (JUnit 5)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
}

tasks.test {
    useJUnitPlatform()
}