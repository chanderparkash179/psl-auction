package com.psl.model;

public class Team {

    private int teamId;
    private String teamName;
    private boolean isDeleted;

    public Team() {}

    public Team(int teamId, String teamName, boolean isDeleted) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.isDeleted = isDeleted;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}