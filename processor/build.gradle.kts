plugins {
    kotlin("jvm")
    alias(libs.plugins.com.google.devtools.ksp)
    id("maven-publish")
}
java {
    publishing {
        withSourcesJar()
        withJavadocJar()
    }
}
ksp {
    arg("autoserviceKsp.verify", "true")
    arg("autoserviceKsp.verbose", "true")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.m3u.extension"
                artifactId = "processor"
                version = libs.versions.extension.get()

                from(components["java"])
            }
        }
    }
}

dependencies {
    implementation(project(":annotation"))

    implementation(libs.symbol.processing.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
    implementation(libs.auto.service.annotations)

    ksp(libs.auto.service.ksp)
}