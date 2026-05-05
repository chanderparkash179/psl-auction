package com.psl.model;

public class PlayerCareer {

    private int careerId;
    private int playerId;

    private int pslMatches;
    private int battingInnings;
    private int totalRuns;
    private int highestScore;

    private double battingAvg;
    private double strikeRate;

    private int fifties;
    private int hundreds;

    private int wickets;
    private double economy;
    private double bowlingAvg;

    private String bestBowling;
    private String pslSeasonDebut;

    private boolean isDeleted;

    public PlayerCareer() {
    }

    public PlayerCareer(int careerId, int playerId, int pslMatches, int battingInnings,
                        int totalRuns, int highestScore, double battingAvg,
                        double strikeRate, int fifties, int hundreds,
                        int wickets, double economy, double bowlingAvg,
                        String bestBowling, String pslSeasonDebut, boolean isDeleted) {

        this.careerId = careerId;
        this.playerId = playerId;
        this.pslMatches = pslMatches;
        this.battingInnings = battingInnings;
        this.totalRuns = totalRuns;
        this.highestScore = highestScore;
        this.battingAvg = battingAvg;
        this.strikeRate = strikeRate;
        this.fifties = fifties;
        this.hundreds = hundreds;
        this.wickets = wickets;
        this.economy = economy;
        this.bowlingAvg = bowlingAvg;
        this.bestBowling = bestBowling;
        this.pslSeasonDebut = pslSeasonDebut;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters

    public int getCareerId() {
        return careerId;
    }

    public void setCareerId(int careerId) {
        this.careerId = careerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPslMatches() {
        return pslMatches;
    }

    public void setPslMatches(int pslMatches) {
        this.pslMatches = pslMatches;
    }

    public int getBattingInnings() {
        return battingInnings;
    }

    public void setBattingInnings(int battingInnings) {
        this.battingInnings = battingInnings;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public double getBattingAvg() {
        return battingAvg;
    }

    public void setBattingAvg(double battingAvg) {
        this.battingAvg = battingAvg;
    }

    public double getStrikeRate() {
        return strikeRate;
    }

    public void setStrikeRate(double strikeRate) {
        this.strikeRate = strikeRate;
    }

    public int getFifties() {
        return fifties;
    }

    public void setFifties(int fifties) {
        this.fifties = fifties;
    }

    public int getHundreds() {
        return hundreds;
    }

    public void setHundreds(int hundreds) {
        this.hundreds = hundreds;
    }

    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public double getEconomy() {
        return economy;
    }

    public void setEconomy(double economy) {
        this.economy = economy;
    }

    public double getBowlingAvg() {
        return bowlingAvg;
    }

    public void setBowlingAvg(double bowlingAvg) {
        this.bowlingAvg = bowlingAvg;
    }

    public String getBestBowling() {
        return bestBowling;
    }

    public void setBestBowling(String bestBowling) {
        this.bestBowling = bestBowling;
    }

    public String getPslSeasonDebut() {
        return pslSeasonDebut;
    }

    public void setPslSeasonDebut(String pslSeasonDebut) {
        this.pslSeasonDebut = pslSeasonDebut;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}