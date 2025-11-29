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

        // 1. Jumlahkan semua digit di serial
        int sumDigits = 0;
        boolean hasDigit = false;

        for (int i = 0; i < upperSerial.length(); i++) {
            char c = upperSerial.charAt(i);
            if (Character.isDigit(c)) {
                sumDigits += (c - '0');
                hasDigit = true;
            }
        }

        if (!hasDigit) {
            sumDigits = 0; // kalau tidak ada digit sama sekali
        }

        // 2. Cek apakah ada huruf vokal
        boolean hasVowel = upperSerial.matches(".*[AEIOU].*");
        if (hasVowel) {
            sumDigits += 1;
        }

        // 3. Tentukan index kabel: 0..(jumlah kabel-1)
        int n = wires.size();
        int baseIndex = sumDigits;
        correctWireIndex = baseIndex % n;

        // 4. Optional: set flag di Wire (kalau mau dipakai)
        for (int i = 0; i < n; i++) {
            wires.get(i).setCorrectToCut(i == correctWireIndex);
        }
    }

    // private int extractLastDigit(String serial) {
    //     for (int i = serial.length() - 1; i >= 0; i--) {
    //         char c = serial.charAt(i);
    //         if (Character.isDigit(c)) {
    //             return c - '0';
    //         }
    //     }
    //     return -1; // tidak ada digit
    // }
}
