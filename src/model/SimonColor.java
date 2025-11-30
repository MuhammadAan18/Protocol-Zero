package model;

public enum SimonColor {
    RED("RED"),
    BLUE("BLUE"),
    GREEN("GREEN"),
    YELLOW("YELLOW");

    private final String label;

    SimonColor(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
