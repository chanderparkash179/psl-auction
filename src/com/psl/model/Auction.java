package com.psl.model;

public class Auction {

    private int auctionId;
    private int playerId;
    private int teamId;
    private int categoryId;

    private double finalPrice;
    private String auctionRound;
    private String acquisitionType;
    private String status;

    private boolean isDeleted;

    public Auction() {}

    public Auction(int auctionId, int playerId, int teamId, int categoryId,
                   double finalPrice, String auctionRound,
                   String acquisitionType, String status, boolean isDeleted) {
        this.auctionId = auctionId;
        this.playerId = playerId;
        this.teamId = teamId;
        this.categoryId = categoryId;
        this.finalPrice = finalPrice;
        this.auctionRound = auctionRound;
        this.acquisitionType = acquisitionType;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getAuctionRound() {
        return auctionRound;
    }

    public void setAuctionRound(String auctionRound) {
        this.auctionRound = auctionRound;
    }

    public String getAcquisitionType() {
        return acquisitionType;
    }

    public void setAcquisitionType(String acquisitionType) {
        this.acquisitionType = acquisitionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}