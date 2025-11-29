package model;

public class WireModule extends BombModule {
    private int correctWireIndex;

	public WireModule(int correctWireIndex) {
		super("WIRE");
        this.correctWireIndex = correctWireIndex;
	}

    public boolean cutWire(int index) {
        boolean ok = (index == correctWireIndex);
        if (ok) {
            solvedStatus = true;
        }
        return ok;
    }

    public int getCorrectWireIndex() {
        return correctWireIndex;
    }
}
