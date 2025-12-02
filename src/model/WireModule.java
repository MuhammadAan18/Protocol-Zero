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

        String serialBomb = serial;
        int sumDigits = 0;

        for (int i = 0; i < serialBomb.length(); i++) {
            char c = serialBomb.charAt(i);
            if (Character.isDigit(c)) {
                sumDigits += (c - '0');
            }
        }

        int sumVowel = 0;
        for (int i = 0; i < serialBomb.length(); i++) {
            char v = serialBomb.charAt(i);
            if (v == 'A' ||v == 'I'|| v == 'U' || v == 'E' || v == 'O' ) {
                sumVowel++;
            }
        }

        // jumlahkan total angka + vowel
        int total = sumDigits + sumVowel;
        
        // menetapkan correct wire index
        int wire = wires.size();

        // correctWireIndex = total % wire;

        int sisa = total;
        while (sisa > wire) { //atur selisih
            sisa -= wire;
        }

        int kabelKe;

        if (sisa == 0) {
            kabelKe = total;       // sisa 0 artinya kabel terakhir, berarti dia kabel ke 6
        } else {
            kabelKe = sisa;    // 1..n-1 = kabel 1..n-1
        }        

        correctWireIndex = kabelKe - 1; //ngakalin supaya sisa dapat dicek di index pada arraynya

        for (int i = 0; i < wire; i++) {
            wires.get(i).setCorrectToCut(i == correctWireIndex);  // jika i itu true
        }
    }
}
