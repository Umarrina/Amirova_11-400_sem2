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


//    testImplementation(platform("org.junit:junit-bom:5.10.0"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "ru.kpfu.itis.amirova.Main"
}

tasks.test {
    useJUnitPlatform()
}