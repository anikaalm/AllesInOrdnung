
package at.ac.hcw.allesinordnung.persistence;

import at.ac.hcw.allesinordnung.model.Medium;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonFileStorage {

    private final Gson gson;      // Gson mit Adapter
    private final File file;

    // Konstruktor
    public JsonFileStorage(String filePath) {
        this.file = new File(filePath);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Medium.class, new MediumAdapter())
                .setPrettyPrinting()
                .create();
    }

    /** Ermöglicht dem CollectionManager den absoluten Pfad zu bekommen */
    public String getFilePath() {
        return file.getAbsolutePath();
    }

    /** Speichern in UTF‑8; Parent-Ordner bei Bedarf anlegen */
    public void save(List<Medium> media) {
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            try (Writer writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
                gson.toJson(media, writer);
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern: " + e.getMessage());
        }
    }

    /** Laden in UTF‑8; leere/korrupten Dateiinhalt robust behandeln */
    public List<Medium> load() {
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        try (Reader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            Type listType = new com.google.gson.reflect.TypeToken<List<Medium>>() {}.getType();
            List<Medium> list = gson.fromJson(reader, listType);
            return (list != null) ? list : new ArrayList<>();

        } catch (Exception e) {
            System.err.println("Fehler beim Laden (JSON ungültig/leer?): " + e.getMessage());
            return new ArrayList<>();
        }
    }
}