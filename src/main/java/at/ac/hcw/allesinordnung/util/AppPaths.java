package at.ac.hcw.allesinordnung.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class AppPaths {
    private AppPaths() {}

    public static Path baseDir() {
        String home = System.getProperty("user.home");
        return Paths.get(home, "AllesInOrdnung");
    }

    public static Path collectionFile() {
        return baseDir().resolve("data").resolve("collection.json");
    }
}
