package model;

public abstract class BombModule {
    protected final String modulName;
    protected boolean solvedStatus = false;

    protected BombModule(String modulName) {	
        this.modulName = modulName;
    }

    public String getmodulName() {
		return modulName; 
	}
    
	public boolean isSolvedStatus() {
		return solvedStatus; 
	}

    // dipanggil setelah bomb di-load untuk set rule berdasarkan serial
    public abstract void applySerialRules(String serial);
}
