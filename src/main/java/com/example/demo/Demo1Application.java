package com.example.demo;

import com.google.gson.reflect.TypeToken;
import com.microsoft.signalr.Action1;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Type;
import java.util.List;

@SpringBootApplication
public class Demo1Application {

    public static void main(String[] args) {

        String input = "https://kadikombatapi.azurewebsites.net/kadi/e1e1665b-c2dc-4f0e-9666-f10c62c5e631";

        HubConnection hubConnection = HubConnectionBuilder.create(input)
                .build();

        Type playerListType = new TypeToken<List<UserResponse>>() {}.getType();
        hubConnection.on("ReceiveAvailablePlayers", (Action1<List<UserResponse>>) (players) -> {
            System.out.println("The players in this game are " + players.size());
            System.out.println(hubConnection.getConnectionId());
        },  playerListType);

        //This is a blocking call
        hubConnection.start().doOnComplete(() -> {

                }
        );

    }

}

class UserResponse {
    private String ConnectionId;
    private String UserName;
    private Boolean IsCurrentlyPlaying;
    private String Id;

    public UserResponse() {
    }

    public UserResponse(String connectionId, String userName, Boolean isCurrentlyPlaying, String id) {
        ConnectionId = connectionId;
        UserName = userName;
        IsCurrentlyPlaying = isCurrentlyPlaying;
        Id = id;
    }

    public String getConnectionId() {
        return ConnectionId;
    }

    public void setConnectionId(String connectionId) {
        ConnectionId = connectionId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Boolean getCurrentlyPlaying() {
        return IsCurrentlyPlaying;
    }

    public void setCurrentlyPlaying(Boolean currentlyPlaying) {
        IsCurrentlyPlaying = currentlyPlaying;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}

