package com.example.demo;

import com.google.gson.reflect.TypeToken;
import com.microsoft.signalr.Action1;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import java.lang.reflect.Type;
import java.util.List;

public class SignalRClientConn {

    public static void main(String[] args) {

        String localUrl = "http://localhost:5088/kadi/171d5950-9251-4e51-a82f-0086b0a02a25";
        HubConnection hubConnection = HubConnectionBuilder.create(localUrl)
                .build();

        Type playerListType = new TypeToken<List<UserResponse>>() {}.getType();
        hubConnection.on("ReceiveAvailablePlayers", (Action1<List<UserResponse>>) (players) -> {
            System.out.println("The players in this game are " + players.size());
            System.out.println(hubConnection.getConnectionId());
        },  playerListType);

        //This is a blocking call
        hubConnection.start().blockingAwait();

    }

}

