package com.psl.model;

public class Player {

    private int playerId;
    private String playerName;
    private String nationality;
    private String primaryRole;
    private boolean isDeleted;

    public Player() {
    }

    public Player(int playerId, String playerName, String nationality, String primaryRole, boolean isDeleted) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.nationality = nationality;
        this.primaryRole = primaryRole;
        this.isDeleted = isDeleted;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPrimaryRole() {
        return primaryRole;
    }

    public void setPrimaryRole(String primaryRole) {
        this.primaryRole = primaryRole;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}