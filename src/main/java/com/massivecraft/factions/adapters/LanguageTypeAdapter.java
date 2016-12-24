package com.massivecraft.factions.adapters;

import com.google.gson.*;
import com.massivecraft.factions.Language;

import java.lang.reflect.Type;

import static java.util.Arrays.stream;

public class LanguageTypeAdapter implements JsonDeserializer<Language>, JsonSerializer<Language> {

    @Override
    public Language deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();



        return null;
    }

    @Override
    public JsonElement serialize(Language src, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        stream(Language.values()).forEach(message -> {
            String key = message.name().toLowerCase().replace("_", "-");
            object.add(key, context.serialize(message.getMessages()).getAsJsonArray());
        });

        return object;
    }
}
