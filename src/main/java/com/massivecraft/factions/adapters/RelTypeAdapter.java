package com.massivecraft.factions.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.massivecraft.factions.struct.Rel;

import java.lang.reflect.Type;

public class RelTypeAdapter implements JsonDeserializer<Rel> {

    @Override
    public Rel deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return Rel.parse(json.getAsString());
    }
}
