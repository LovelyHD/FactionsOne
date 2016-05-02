package com.massivecraft.factions.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.massivecraft.factions.struct.FPerm;
import java.lang.reflect.Type;

public class FPermTypeAdapter implements JsonDeserializer<FPerm> {

    @Override
    public FPerm deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return FPerm.parse(json.getAsString());
    }

}
