package at.ac.hcw.allesinordnung.persistence;

import at.ac.hcw.allesinordnung.model.Medium;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonFileStorage {

    private final Gson gson;      // Gson mit Adapter
    private final File file;

    // Konstruktor
    public JsonFileStorage(String filePath) {
        this.file = new File(filePath);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Medium.class, new MediumAdapter()) // <-- Hier den Adapter eintragen
                .setPrettyPrinting()
                .create();
    }

    // ----------------------------------------
    // save() Methode
    public void save(List<Medium> media) {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(media, writer);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern: " + e.getMessage());
        }
    }

    // ----------------------------------------
    // load() Methode
    public List<Medium> load() {
        if (!file.exists()) return new ArrayList<>();

        try (Reader reader = new FileReader(file)) {
            Type listType = new com.google.gson.reflect.TypeToken<List<Medium>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            System.err.println("Fehler beim Laden: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
