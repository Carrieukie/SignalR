package com.example.demo

import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.TypeReference

//fun main() {
//    val input = "https://kadikombatapi.azurewebsites.net/kadi/e1e1665b-c2dc-4f0e-9666-f10c62c5e631"
//    val hubConnection = HubConnectionBuilder.create(input)
//        .build()
//
//    val playerListType = object : TypeReference<List<UserResponse>>() {}.type
//    hubConnection.on<List<UserResponse>>("ReceiveAvailablePlayers", { players ->
//    }, playerListType)
//    hubConnection.start().blockingAwait()
//
//}