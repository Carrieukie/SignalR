package com.example.demo;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

public class AppUser extends GamePlayer {
    private String connectionId;
    private boolean isCurrentlyPlaying;

    public AppUser() {
    }

    public AppUser(String id, String userName) {
        super(id, userName);
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public boolean isCurrentlyPlaying() {
        return isCurrentlyPlaying;
    }

    public void setCurrentlyPlaying(boolean currentlyPlaying) {
        isCurrentlyPlaying = currentlyPlaying;
    }
}

