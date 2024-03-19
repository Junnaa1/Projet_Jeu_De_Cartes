package modele;

class Score {
    private String playerName;
    private long timeElapsed;

    public Score(String playerName, long timeElapsed) {
        this.playerName = playerName;
        this.timeElapsed = timeElapsed;
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }
}