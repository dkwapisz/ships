package io.project.ships.menu;

public class UserStatistics {
    private int uid;
    private int played;
    private int victories;
    private float winRate;
    private int shots;
    private int onTarget;
    private float accuracy;

    public UserStatistics(int uid, int played, int victories, float winRate, int shots, int onTarget, float accuracy) {
        this.uid = uid;
        this.played = played;
        this.victories = victories;
        this.winRate = winRate;
        this.shots = shots;
        this.onTarget = onTarget;
        this.accuracy = accuracy;
    }

    public void updatePlayed() {
        this.played += 1;
    }

    public void updateVictories() {
        this.victories += 1;
    }

    public void updateWinRate() {
        if (this.played != 0) {
            this.winRate = this.victories / this.played;
        } else {
            this.winRate = 0;
        }
    }

    public void updateShots() {
        this.shots += 1;
    }

    public void updateOnTarget() {
        this.onTarget += 1;
    }

    public void updateAccuracy() {
        if (this.shots != 0) {
            this.accuracy = this.onTarget / this.shots;
        } else {
            this.accuracy = 0;
        }
    }

    public int getUid() {
        return uid;
    }

    public int getPlayed() {
        return played;
    }

    public int getVictories() {
        return victories;
    }

    public float getWinRate() {
        return winRate;
    }

    public int getShots() {
        return shots;
    }

    public int getOnTarget() {
        return onTarget;
    }

    public float getAccuracy() {
        return accuracy;
    }
}