plugins {
    kotlin("jvm")
    id("maven-publish")
}
java {
    publishing {
        withSourcesJar()
        withJavadocJar()
    }
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.m3u.extension"
                artifactId = "annotation"
                version = libs.versions.extension.get()

                from(components["java"])
            }
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
}