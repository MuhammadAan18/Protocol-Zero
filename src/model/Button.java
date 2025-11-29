package model;

public class Button {
	private final String color;
	private final String label;

	public Button(String warna, String label) {
		this.color = warna;
		this.label = label;
	}
	
	public String getColor() {
        return color;
    }

    public String getLabel() {
        return label;
    }
}
