package model;

public class Wire {
	private final String color;
    private boolean cut;
    private boolean correctToCut; // ngecek kabel yang dicut bener apa ndak

	public Wire(String warna, boolean cut, boolean kabelBenar) {
		this.color = warna;
		this.cut = cut;
		this.correctToCut = kabelBenar;
	}

	public String getWireColor() {
		return color;
	}

	public boolean isCut() {
		return cut;
	}

	public void cut() {
		this.cut = true;
	}

	public boolean isCorrectToCut() {
		return correctToCut;
	}

	public void setCorrectToCut(boolean correctToCut) {
        this.correctToCut = correctToCut;
    }
}
