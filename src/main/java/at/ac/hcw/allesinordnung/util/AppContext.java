
// src/main/java/at/ac/hcw/allesinordnung/util/AppContext.java
package at.ac.hcw.allesinordnung.util;

import at.ac.hcw.allesinordnung.manager.CollectionManager;

public final class AppContext {

    private static CollectionManager manager;

    private AppContext() {}

    public static synchronized CollectionManager getManager() {
        if (manager == null) {
            String filePath = AppPaths.collectionFile().toString();
            manager = new CollectionManager(filePath);
            manager.initStorage();
            System.out.println(">> [AppContext] Verwende gemeinsame Datei: " + filePath);
            System.out.println(">> [AppContext] Geladene Medien: " + manager.showAllMedia().size());
        }
        return manager;
    }
}
