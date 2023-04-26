package com.example.demo;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.microsoft.signalr.Action1;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SignalRClientConn {

    static HubConnection kadiHubConnection;
    static HubConnection gameHubConnection;

    static AppUser currentUser;

    static String baseUrl = "http://localhost:5088/";

    // this list does not contain the current user
    static List<GamePlayer> availablePlayers;


    public static void main(String[] args) {

        // get choice from user whether login or register
        int choice = getChoice();

        // if choice is 1 then login
        if (choice == 1) {
            // login
            currentUser = login();
        } else {
            // register
            currentUser = register();
        }

        System.out.println("Welcome " + currentUser.getUserName());

        // kadi hub url : /kadi/{userId}
        String kadiHubLocalUrl = baseUrl + "kadi/" + currentUser.getId();

        connectToKadiHub(kadiHubLocalUrl);

        // get game choice options
        int gameChoice = getGameChoice();
        
        // if choice is 1 then start a new game
        if(gameChoice ==1){
            System.out.println("Getting available players...");
            getAvailablePlayers();

            while (availablePlayers == null || availablePlayers.size() == 0) {
                System.out.println("No players available, trying again in 5 seconds...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getAvailablePlayers();
            }

            List<GamePlayer> selectedPlayers = selectPlayers();
            initializeGame(selectedPlayers);
        }
        else {
            // wait to be added to a game
            System.out.println("Waiting to be added to a game...");
        }
    }

    private static void initializeGame(List<GamePlayer> selectedPlayers) {
        System.out.println("Initializing game with " + selectedPlayers.size() + " players");

        List<String> playerIds = new ArrayList<>(selectedPlayers.size() + 1);
        selectedPlayers.stream()
                .map(GamePlayer::getId)
                .forEach(playerIds::add);

        playerIds.add(currentUser.getId());


        // convert playerIds to json
        String playerIdsJson = new Gson().toJson(playerIds);

        // http post request to /baseurl/api/games
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "api/games"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(playerIdsJson))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // if response is successful
            if (response.statusCode() != 200 && response.statusCode() != 201) {
                System.out.println("Error creating game");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
    private static List<GamePlayer> selectPlayers(){
        // get a comma separated list of player indexes+1 from user
        System.out.println("Please select players to play with by entering their numbers separated by commas");

        Scanner scanner = new Scanner(System.in);

        String selection = scanner.nextLine();

        List<Integer> indices = Arrays.stream(selection.split(","))
                .map(Integer::parseInt)
                .toList();

        return indices.stream()
                .map(i -> availablePlayers.get(i - 1))
                .collect(Collectors.toList());

    }
    private static void getAvailablePlayers() {
        // invoke getAvailablePlayers method on kadi hub
        kadiHubConnection.send("GetAvailablePlayers");
    }

    private static int getGameChoice(){
        System.out.println("Do you want to start a new game or wait to be added to a game?");
        System.out.println("1. Start a new game\n2. Wait to be added to a game");
        
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        if (choice == 1 || choice == 2) {
            return choice;
        }
        System.out.println("Invalid choice");
        return getGameChoice();
    }


    private static AppUser register(){
         while (true){

             // get username from user
             Scanner scanner = new Scanner(System.in);
             System.out.println("Please enter your username:");
             String username = scanner.nextLine();

             AppUser user = new AppUser();
             user.setUserName(username);

             String playerJson = new Gson().toJson(user);
             HttpRequest request = HttpRequest.newBuilder()
                     .uri(URI.create(baseUrl + "api/users"))
                     .header("Content-Type", "application/json")
                     .POST(HttpRequest.BodyPublishers.ofString(playerJson))
                     .build();

             HttpClient client = HttpClient.newHttpClient();
             HttpResponse<String> response = null;
             try {
                 response = client.send(request, HttpResponse.BodyHandlers.ofString());

             } catch (IOException | InterruptedException e) {
                 throw new RuntimeException(e);
             }

             if (response.statusCode() == 201) {
                 String responseJson = response.body();

                 Gson gson = new GsonBuilder()
                         .registerTypeAdapter(AppUser.class, new AppUserDeserializer())
                         .create();

                 user = gson.fromJson(responseJson, AppUser.class);
                 return user;
             }
             else
             {
                    System.out.println("Registration failed: " + response.body());
             }
         }
    }
    private static AppUser login(){
        while (true){
            // get username from user
            System.out.println("Enter Username:");
            Scanner scanner = new Scanner(System.in);
            String userName = scanner.nextLine();

            // make a get request to log in url at baseurl/api/users/usr/{username}
            try {
                URI loginUrl = URI.create(baseUrl + "api/users/usr/" + userName);
                HttpClient client = HttpClient.newHttpClient();

                HttpResponse<String> response = client.send(
                        HttpRequest.newBuilder()
                                .uri(loginUrl)
                                .GET()
                                .build(),
                        HttpResponse.BodyHandlers.ofString()
                );

                if (response.statusCode() != 200) {
                    String errorJson = response.body();
                    System.out.println("Login failed: " + errorJson);
                    continue;
                }

                String playerJson = response.body();

                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(AppUser.class, new AppUserDeserializer())
                        .create();

                AppUser user = gson.fromJson(playerJson, AppUser.class);
                return user;

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    private static int getChoice() {
        // get choice from user whether login or register
        System.out.println("1. Login \n2. Register");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        // choice is not 1 or 2 then ask again to choose login or register
        while (choice != 1 && choice != 2) {
            System.out.println("Invalid Entry!\n1. Login \n2. Register");
            choice = scanner.nextInt();
        }

        return choice;
    }

    private static void connectToKadiHub(String kadiHubLocalUrl) {
        // create hub connection
        kadiHubConnection  = HubConnectionBuilder.create(kadiHubLocalUrl)
                .build();

        Type playerListType = new TypeToken<List<AppUser>>() {}.getType();

        // register callback for ReceiveAvailablePlayers
        // Call receive available players method
        kadiHubConnection.on("ReceiveAvailablePlayers", SignalRClientConn::receiveAvailablePlayers,  playerListType);


        // register callback to receive game id
        // Call receive game id method
        kadiHubConnection.on("ReceiveGameId", SignalRClientConn::receiveGameId, String.class);

        // start hub connection
        kadiHubConnection.start();
    }

    private static void receiveGameId(String gameId) {
        // print game id
        System.out.println("Received Game id: " + gameId);

        // connect to game hub
        connectToGameHub(gameId);
    }

    private static void connectToGameHub(String gameId) {
        // create hub connection /games/{gameId}/{userId}
        gameHubConnection  = HubConnectionBuilder.create("http://localhost:5088/games/" + gameId + "/" + currentUser.getId())
                .build();

        // Register callback to begin game, the call back receives a GameInfo object
        // Call begin game method
        gameHubConnection.on("BeginGame", (Action1<GameInfo>) SignalRClientConn::beginGame, GameInfo.class);

        // Start hub connection
        gameHubConnection.start();
    }

    private static void beginGame(GameInfo gameInfo) {
        // print game info in a formatted table
        System.out.println("Game Info: ");
        System.out.println("Players: ");
        for (GamePlayer player : gameInfo.getPlayers()) {
            System.out.println("1. " + player.getUserName());
        }
        System.out.println("Current Player: " + gameInfo.getCurrentPlayerId());
        System.out.println("Game Playing Card: " + gameInfo.getGamePlayingCard());
        System.out.println("Players Wako Kadi: ");
        for (String player : gameInfo.getPlayersWakoKadi()) {
            System.out.println("1. " + player);
        }
        System.out.println("Discard Pile Count: " + gameInfo.getDiscardPileCount());
        System.out.println("Draw Pile Count: " + gameInfo.getDrawPileCount());
        System.out.println("Winner Id: " + gameInfo.getWinnerId());
        System.out.println("Ordered Player Id: ");
        for (String player : gameInfo.getOrderedPlayerId()) {
            System.out.println("1. " + player);
        }

    }


    private static void receiveAvailablePlayers(List<AppUser> players) {

        // clear available players list if not null. if null then create a new list
        if (availablePlayers != null) {
            availablePlayers.clear();
        } else {
            availablePlayers = new ArrayList<>();
        }


        // add players to available players list and skip current user
        for (AppUser player : players) {
            if (!player.getId().equals(currentUser.getId())) {
                availablePlayers.add(player);
            }
        }


        if(availablePlayers.size() == 0)
            return;

        // print a list of all available players
        System.out.println("Online & Available Players: ");
        for (GamePlayer player : availablePlayers) {
            System.out.println("1. " + player.getUserName());
        }


    }

}

