package com.example.demo;

import com.google.gson.annotations.SerializedName;

import java.util.*;

public class GameInfo {
    private List<GamePlayer> players = new ArrayList<>();
    private List<CardType> playerCards = new ArrayList<>();
    private Map<String, Integer> playersCardsCount = new HashMap<>();
    private String currentPlayerId;
    private CardType gamePlayingCard;
    private List<String> playersWakoKadi = new ArrayList<>();
    private int discardPileCount;
    private int drawPileCount;
    private String winnerId;
    private LinkedList<String> orderedPlayerId = new LinkedList<>();
    private boolean isComplete;

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<GamePlayer> players) {
        this.players = players;
    }

    public List<CardType> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(List<CardType> playerCards) {
        this.playerCards = playerCards;
    }

    public Map<String, Integer> getPlayersCardsCount() {
        return playersCardsCount;
    }

    public void setPlayersCardsCount(Map<String, Integer> playersCardsCount) {
        this.playersCardsCount = playersCardsCount;
    }

    public String getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(String currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public CardType getGamePlayingCard() {
        return gamePlayingCard;
    }

    public void setGamePlayingCard(CardType gamePlayingCard) {
        this.gamePlayingCard = gamePlayingCard;
    }

    public List<String> getPlayersWakoKadi() {
        return playersWakoKadi;
    }

    public void setPlayersWakoKadi(List<String> playersWakoKadi) {
        this.playersWakoKadi = playersWakoKadi;
    }

    public int getDiscardPileCount() {
        return discardPileCount;
    }

    public void setDiscardPileCount(int discardPileCount) {
        this.discardPileCount = discardPileCount;
    }

    public int getDrawPileCount() {
        return drawPileCount;
    }

    public void setDrawPileCount(int drawPileCount) {
        this.drawPileCount = drawPileCount;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    public LinkedList<String> getOrderedPlayerId() {
        return orderedPlayerId;
    }

    public void setOrderedPlayerId(LinkedList<String> orderedPlayerId) {
        this.orderedPlayerId = orderedPlayerId;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
