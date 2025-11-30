package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Modul Keypads berbasis kata.
 * Logika:
 * - Dibentuk dari satu kolom (KeypadColumns.COLUMN_X).
 * - Modul memilih 4 kata dari kolom tersebut secara acak untuk ditampilkan.
 * - Urutan jawaban yang benar mengikuti urutan kata dalam kolom (atas -> bawah),
 *   tetapi hanya untuk kata-kata yang muncul di modul.
 */
public class KeypadModule extends BombModule {

    private final List<Keypad> displayKeypads; // 4 kata yang tampil di tombol (urutan acak)
    private final List<Keypad> solutionOrder;  // urutan benar sesuai kolom (atas -> bawah)
    private int currentIndex = 0;              // index jawaban berikutnya

    /**
     * Membangun modul dari satu kolom kata.
     * @param columnSymbols kolom penuh dari KeypadColumns (misal COLUMN_1, COLUMN_2, dll)
     */
    public KeypadModule(List<Keypad> columnSymbols) {
        super("KEYPADS");

        if (columnSymbols == null || columnSymbols.size() < 4) {
            throw new IllegalArgumentException("Kolom Keypads minimal harus berisi 4 kata.");
        }

        // 1) Pilih 4 kata acak dari kolom untuk ditampilkan di modul
        List<Keypad> shuffled = new ArrayList<>(columnSymbols);
        Collections.shuffle(shuffled);
        this.displayKeypads = new ArrayList<>(shuffled.subList(0, 4)); // ambil 4 pertama setelah di-shuffle

        // 2) Bentuk urutan jawaban yang benar berdasarkan urutan kolom (atas -> bawah)
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

    /**
     * Getter untuk 4 kata yang tampil di modul (untuk dipakai di GUI panel).
     */
    public List<Keypad> getDisplayKeypads() {
        return Collections.unmodifiableList(displayKeypads);
    }

    /**
     * Dipanggil ketika pemain menekan salah satu tombol keypad.
     *
     * @param index index tombol yang ditekan (0..3, sesuai urutan di displayKeypads)
     * @return true jika input benar (sesuai urutan solutionOrder), false jika salah.
     */
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
            // kalau salah, reset progres (optional).
            // Kalau tidak mau reset, tinggal hapus baris ini.
            currentIndex = 0;
        }

        return ok;
    }

    @Override
    public void applySerialRules(String serial) {
        // modul ini ndk pake serial number jadinya tidak dipakai
    }
}
