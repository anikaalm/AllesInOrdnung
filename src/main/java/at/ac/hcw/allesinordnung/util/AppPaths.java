
// AppPaths.java
package at.ac.hcw.allesinordnung.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class AppPaths {
    private AppPaths() {}

    public static Path collectionFile() {
        // Start vom aktuellen Arbeitsverzeichnis
        Path dir = Paths.get(System.getProperty("user.dir")).toAbsolutePath();

        // Bis zu 5 Ebenen nach oben nach data/collection.json suchen
        for (int i = 0; i < 5 && dir != null; i++, dir = dir.getParent()) {
            Path candidate = dir.resolve("data").resolve("collection.json");
            if (Files.exists(candidate)) {
                return candidate;
            }
        }
        // Fallback: user.dir/data/collection.json
        return Paths.get(System.getProperty("user.dir"), "data", "collection.json").toAbsolutePath();
    }
}
