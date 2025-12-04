package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeypadModule extends BombModule {

    private final List<Keypad> displayKeypads; // 4 kata yang tampil di tombol (urutan acak)
    private final List<Keypad> solutionOrder;  // urutan benar sesuai kolom (atas ke bawah)
    private int currentIndex = 0;              // index jawaban berikutnya

    public KeypadModule(List<Keypad> columnSymbols) {
        super("KEYPADS");

        if (columnSymbols == null || columnSymbols.size() < 4) {
            throw new IllegalArgumentException("Kolom Keypads minimal harus berisi 4 kata.");
        }

        // Pilih 4 kata acak dari kolom untuk ditampilkan di modul
        List<Keypad> shuffled = new ArrayList<>(columnSymbols);
        Collections.shuffle(shuffled);
        this.displayKeypads = new ArrayList<>(shuffled.subList(0, 4)); // ambil 4 pertama setelah di-shuffle

        // Bentuk urutan jawaban yang benar berdasarkan urutan kolom
        this.solutionOrder = new ArrayList<>();
        for (Keypad k : columnSymbols) {
            if (displayKeypads.contains(k)) {
                solutionOrder.add(k);
            }
        }

        if (solutionOrder.size() != displayKeypads.size()) {
            throw new IllegalStateException("Solution order tidak konsisten dengan display keypads.");
        }
    }

    public List<Keypad> getDisplayKeypads() {
        return Collections.unmodifiableList(displayKeypads);
    }

    public boolean pressAtIndex(int index) {
        if (isSolvedStatus()) {
            // sudah solved, treat sebagai tidak salah (supaya tidak nambah strike)
            return true;
        }

        if (index < 0 || index >= displayKeypads.size()) {
            return false;
        }

        Keypad pressed  = displayKeypads.get(index);
        Keypad expected = solutionOrder.get(currentIndex);

        boolean ok = pressed.equals(expected);

        if (ok) {
            currentIndex++;
            if (currentIndex >= solutionOrder.size()) {
                solvedStatus = true; // field dari BombModule
            }
        } else {
            // kalau salah, reset progres
            currentIndex = 0;
        }

        return ok;
    }

    @Override
    public void applySerialRules(String serial) {
        // modul ini ndk pake serial number jadinya tidak dipakai
    }
}
