//damit GSON weiß welche Unterklasse ein Medium in JSON ist
package at.ac.hcw.allesinordnung.persistence;

import at.ac.hcw.allesinordnung.model.Book;
import at.ac.hcw.allesinordnung.model.Cd;
import at.ac.hcw.allesinordnung.model.Dvd;
import at.ac.hcw.allesinordnung.model.Medium;
import com.google.gson.*;

import java.lang.reflect.Type;

public class MediumAdapter implements JsonSerializer<Medium>, JsonDeserializer<Medium> {

    @Override
    public JsonElement serialize(Medium src, Type typeOfSrc, JsonSerializationContext ctx) {
        // Gson nutzt den Runtime-Typ, daher reicht:
        return ctx.serialize(src);
    }

    @Override
    public Medium deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx)
            throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        JsonElement typeEl = obj.get("type");
        if (typeEl == null || typeEl.isJsonNull()) {
            throw new JsonParseException("Missing 'type' discriminator");
        }
        String t = typeEl.getAsString();
        switch (t) {
            case "BOOK": return ctx.deserialize(obj, Book.class);
            case "CD":   return ctx.deserialize(obj, Cd.class);
            case "DVD":  return ctx.deserialize(obj, Dvd.class);
            default: throw new JsonParseException("Unknown type: " + t);
        }
    }
}
