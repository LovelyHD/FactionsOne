package com.massivecraft.factions.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.massivecraft.factions.struct.FFlag;

import java.lang.reflect.Type;

public class FFlagTypeAdapter implements JsonDeserializer<FFlag> {

    @Override
    public FFlag deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return FFlag.parse(json.getAsString());
    }

}
