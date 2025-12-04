package model;

import java.util.List;

public class Bomb {
    private final int bomb_id;
    private final String serial;
    private final int maxStrikes;
    private final int timeLimit;
    private final List<BombModule> modules;

    public Bomb(int bomb_id, String serial, int maxStrikes, int timeLimit, List<BombModule> modules) {
        this.bomb_id = bomb_id;
        this.serial = serial;
        this.maxStrikes = maxStrikes;
        this.timeLimit = timeLimit;
        this.modules = modules;
    }

    public int getbomb_Id() {
		return bomb_id; 
	}
    
	    public String getSerial() {
		return serial; 
	}

    public int getMaxStrikes() { 
		return maxStrikes; 
	}

    public int getTimeLimit() { 
		return timeLimit; 
	}
	
    public List<BombModule> getModules() { 
		return modules; 
	}
}
