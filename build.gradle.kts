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
    mainClass.set("at.ac.hcw.allesinordnung.HelloApplication") //  JavaFX Start
    mainClass.set("at.ac.hcw.allesinordnung.demo.StorageDemo")
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
}
