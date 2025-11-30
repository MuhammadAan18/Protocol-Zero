package model;

import java.util.List;

public class WireModule extends BombModule {
    private final List<Wire> wires;
    private int correctWireIndex = -1;

	public WireModule(List<Wire> wires) {
		super("WIRE");
        this.wires = wires;
	}

    public List<Wire> getWires() {
        return wires;
    }
    
    public int getCorrectWireIndex() {
        return correctWireIndex;
    }
    
    public boolean cutWire(int index) {
        if (index < 0 || index >= wires.size()) return false;

        Wire w = wires.get(index);
        if (w.isCut()) return false;

        w.cut();

        boolean ok = (index == correctWireIndex);
        if (ok) {
            solvedStatus = true;
        }
        return ok;
    }

    @Override
    public void applySerialRules(String serial) {
        if (serial == null) {
            throw new IllegalArgumentException("Serial tidak boleh null");
        }
        if (wires == null || wires.isEmpty()) {
            throw new IllegalStateException("WireModule tanpa kabel");
        }

        String upperSerial = serial.toUpperCase();

        // 1. Jumlahkan semua angka di serial
        int sumDigits = 0;

        for (int i = 0; i < upperSerial.length(); i++) {
            char c = upperSerial.charAt(i);
            if (Character.isDigit(c)) {
                sumDigits += (c - '0');
            }
        }

        // 2. jumlahkan  huruf vokal
        int sumVowel = 0;
        for (int i = 0; i < upperSerial.length(); i++) {
            char v = upperSerial.charAt(i);
            if (v == 'A' ||v == 'I'|| v == 'U' || v == 'E' || v == 'O' ) {
                sumVowel++;
            }
        }

        // jumlahkan total angka + vowel
        int total = sumDigits + sumVowel;
        
        // menetapkan correct wire index
        int wire = wires.size();
        correctWireIndex = total % wire;

        for (int i = 0; i < wire; i++) {
            wires.get(i).setCorrectToCut(i == correctWireIndex);
    }
        }
}
