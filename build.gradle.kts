plugins {
    id("java")
    id("application")
    id("war")
}

group = "ru.kpfu.itis.amirova"
version = "1.0-SNAPSHOT"

val springVersion: String by project
val jakartaVersion: String by project
val hibernateVersion: String by project
val postgresVersion: String by project
val freemarkerVersion: String by project
val hikariVersion: String by project
val springDataVersion: String by project

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-webmvc:$springVersion")
    implementation("org.springframework:spring-jdbc:$springVersion")
    implementation("org.springframework:spring-orm:$springVersion")
    implementation("org.springframework:spring-context-support:$springVersion")
    implementation("jakarta.servlet:jakarta.servlet-api:$jakartaVersion")
    implementation("org.hibernate.orm:hibernate-core:$hibernateVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("org.freemarker:freemarker:$freemarkerVersion")
    implementation("com.zaxxer:HikariCP:${hikariVersion}")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("org.springframework.data:spring-data-jpa:$springDataVersion")

    testImplementation("org.springframework:spring-test:$springVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("com.h2database:h2:2.2.224")
    testImplementation("org.mockito:mockito-core:5.14.2")
}
application {
    mainClass = "ru.kpfu.itis.amirova.Main"
}

tasks.test {
    useJUnitPlatform()
}