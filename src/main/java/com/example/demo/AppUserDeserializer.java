package com.example.demo;

import com.google.gson.*;

import java.lang.reflect.Type;

public class AppUserDeserializer implements JsonDeserializer<AppUser> {
    @Override
    public AppUser deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String connectionId = jsonObject.get("ConnectionId").isJsonNull() ? null : jsonObject.get("ConnectionId").getAsString();
        boolean isCurrentlyPlaying = jsonObject.get("IsCurrentlyPlaying").getAsBoolean();
        String id = jsonObject.get("Id").getAsString();
        String userName = jsonObject.get("UserName").getAsString();

        AppUser appUser = new AppUser(id, userName);
        appUser.setConnectionId(connectionId);
        appUser.setCurrentlyPlaying(isCurrentlyPlaying);

        return appUser;
    }
}
