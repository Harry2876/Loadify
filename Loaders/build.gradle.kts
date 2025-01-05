plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
    id("signing")
}


android {
    namespace = "com.hariomharsh.loaders"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar() // Optional: Include source JAR for publishing
            withJavadocJar() // Optional: Include Javadoc JAR for publishing
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    implementation(libs.ezyloaders.v102)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])


                groupId = "com.github.Harry2876"
                artifactId = "Loadify"
                version = "1.0.3"



                pom {
                    name.set("Loadify")
                    description.set("A library for custom loaders in Android.")
                    url.set("https://github.com/Harry2876/Loadify")

                    licenses {
                        license {
                            name.set("Apache License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.html")
                        }
                    }

                    developers {
                        developer {
                            id.set("Harry2876")
                            name.set("Hariom Harsh")
                            email.set("acbcharryxyz@gmail.com")
                        }
                    }

                    scm {
                        connection.set("scm:git:git://github.com/Harry2876/Loadify")
                        developerConnection.set("scm:git:ssh://github.com/Harry2876/Loadify")
                        url.set("https://github.com/Harry2876/Loadify")
                    }
                }
            }
        }
    }
}

