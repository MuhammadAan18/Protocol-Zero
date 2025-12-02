package model;

public class ScoreEntry {
    private int scoreId;
    private int userId;
    private int bombId;
    private int strikeLeft;
    private int timeLeft;
    private String username;
    private String gameScore;

    public int getScoreId() { 
		return scoreId; 
	}

    public void setScoreId(int scoreId) { 
		this.scoreId = scoreId; 
	}

    public String getUsername() { 
		return username; 
	}

	public void setUsername(String name) {
		this.username = name;
	}

    public int getUserId() { 
		return userId; 
	}
        
	public void setUserId(int userId) { 
		this.userId = userId; 
	}

    public int getBombId() { 
		return bombId; 
	}
        
	public void setBombId(int bombId) { 
		this.bombId = bombId; 
	}

    public int getStrikeLeft() { 
		return strikeLeft; 
	}
        
	public void setStrikeLeft(int strikeLeft) { 
		this.strikeLeft = strikeLeft; 
	}

    public int getTimeLeft() { 
		return timeLeft; 
	}

    public void setTimeLeft(int timeLeft) { 
		this.timeLeft = timeLeft; 
	}

    public String getGameScore() { 
		return gameScore; 
	}

    public void setGameScore(String gameScore) { 
		this.gameScore = gameScore; 
	}
}