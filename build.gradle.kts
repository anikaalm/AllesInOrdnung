plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "at.ac.hcw"
version = "1.0-SNAPSHOT"


repositories {
    /*
    hier sagen wir Gradle, woher es externe Bibliotheken laden darf
    */
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("at.ac.hcw.allesinordnung")
    mainClass.set("at.ac.hcw.allesinordnung.App")
}


javafx {
    version = "17.0.6"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    /*
    JSON-Serialisierung, um unsere Medium-Liste in collection.json zu speichern und wieder zu laden.
    Gson macht Polymorphie möglich, gemeinsam mit MediumAdapter.
    Ohne diese Dependency könnte der Code nicht JSON lesen oder schreiben.
     */
    implementation("com.google.code.gson:gson:2.11.0")


    /*
    JUnit -> Für Tests (automatisches Prüfen von add, delete, load, save).
    So kann man die Methoden unabhängig von GUI testen
     */
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf(
        "--strip-debug",
        "--compress", "2",
        "--no-header-files",
        "--no-man-pages"
    ))

    launcher {
        name = "app"
    }

    tasks.register<JavaExec>("runHelloApp") {
        mainClass.set("at.ac.hcw.allesinordnung.controller.HelloApplication")
        classpath = sourceSets["main"].runtimeClasspath

        jvmArgs = listOf(
            "--module-path",
            "C:/Users/muham/.gradle/caches/modules-2/files-2.1/org.openjfx/javafx-controls/17.0.6/c95b460be3bc372060ff32d0c666c1233c3e8400/javafx-controls-17.0.6.jar;" +
                    "C:/Users/muham/.gradle/caches/modules-2/files-2.1/org.openjfx/javafx-fxml/17.0.6/5724aedc415683e62eeab3a3875550aa814c84fd/javafx-fxml-17.0.6-win.jar",
            "--add-modules", "javafx.controls,javafx.fxml"
        )

    }
}
