package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimonModule extends BombModule {

    private final List<SimonColor> flashOrder = new ArrayList<>();
    private int currentInputIndex = 0;

    private boolean serialHasVowel = false;
    private int strikeCount = 0;          // diupdate dari GamePanel

    private final Random random = new Random();

    // konfigurasi panjang sequence
    private final int startLength;
    private final int targetLength;

    public SimonModule(int startLength, int targetLength) {
        super("SIMON SAYS");

        // jaga-jaga: nggak boleh < 1 atau target < start
        if (startLength < 1) startLength = 1;
        if (targetLength < startLength) targetLength = startLength;

        this.startLength  = startLength;
        this.targetLength = targetLength;

        // generate sequence awal sesuai startLength
        for (int i = 0; i < this.startLength; i++) {
            addRandomColor();
        }
    }

    @Override
    public void applySerialRules(String serial) {
        // Cek apakah serial punya huruf vokal
        if (serial == null) {
            serialHasVowel = false;
            return;
        }
        serialHasVowel = serial.toUpperCase().matches(".*[AEIOU].*");
    }

    // ==== API untuk GamePanel / GUI ====

    /** Dipanggil ketika jumlah strike di bomb berubah. */
    public void setStrikeCount(int strikeCount) {
        // Aturan Simon asli: 0, 1, atau 2+ (kita clamp ke 0..2)
        if (strikeCount < 0) strikeCount = 0;
        if (strikeCount > 2) strikeCount = 2;
        this.strikeCount = strikeCount;
    }

    /** Urutan warna yang sedang dipakai untuk animasi blink. */
    public List<SimonColor> getFlashOrder() {
        return new ArrayList<>(flashOrder);  // return copy biar aman
    }

    /** Index input berikutnya yang diharapkan player. */
    public int getCurrentInputIndex() {
        return currentInputIndex;
    }

    /**
     * Dipanggil saat pemain menekan salah satu warna.
     * @return true jika input benar, false jika salah (GamePanel kasih strike).
     */
public boolean pressColor(SimonColor input) {
    if (solvedStatus) {
        return true;  // sudah solved, anggap selalu "benar"
    }

    SimonColor flashColor = flashOrder.get(currentInputIndex);
    SimonColor expected   = mapFlashToPress(flashColor);

    boolean ok = (input == expected);

    if (ok) {
        currentInputIndex++;

        if (currentInputIndex >= flashOrder.size()) {

            if (flashOrder.size() >= targetLength) {
                // sequence sudah sepanjang target → modul selesai
                solvedStatus = true;
                currentInputIndex = 0;
            } else {
                // belum target → tambahkan warna baru
                addRandomColor();
                currentInputIndex = 0;
            }
        }
    } else {
        // salah → reset progres input (ulang dari awal urutan)
        currentInputIndex = 0;
    }

    return ok;
}


    // ==== LOGIKA INTERNAL ====

    /** Tambah 1 warna random di akhir sequence. */
    private void addRandomColor() {
        SimonColor[] values = SimonColor.values();
        SimonColor next = values[random.nextInt(values.length)];
        flashOrder.add(next);
    }

    /**
     * Mapping warna berkedip → warna yang harus ditekan
     * berdasarkan apakah serial punya vokal dan jumlah strike.
     */
    private SimonColor mapFlashToPress(SimonColor flash) {
        if (serialHasVowel) {
            // SERIAL PUNYA VOKAL
            switch (strikeCount) {
                case 0:
                    return switch (flash) {
                        case RED    -> SimonColor.BLUE;
                        case BLUE   -> SimonColor.RED;
                        case GREEN  -> SimonColor.YELLOW;
                        case YELLOW -> SimonColor.GREEN;
                    };
                case 1:
                    return switch (flash) {
                        case RED    -> SimonColor.YELLOW;
                        case BLUE   -> SimonColor.GREEN;
                        case GREEN  -> SimonColor.BLUE;
                        case YELLOW -> SimonColor.RED;
                    };
                default: // 2 atau lebih
                    return switch (flash) {
                        case RED    -> SimonColor.GREEN;
                        case BLUE   -> SimonColor.BLUE;
                        case GREEN  -> SimonColor.YELLOW;
                        case YELLOW -> SimonColor.BLUE;
                    };
            }
        } else {
            // SERIAL TIDAK PUNYA VOKAL
            switch (strikeCount) {
                case 0:
                    return switch (flash) {
                        case RED    -> SimonColor.BLUE;
                        case BLUE   -> SimonColor.YELLOW;
                        case GREEN  -> SimonColor.GREEN;
                        case YELLOW -> SimonColor.RED;
                    };
                case 1:
                    return switch (flash) {
                        case RED    -> SimonColor.RED;
                        case BLUE   -> SimonColor.BLUE;
                        case GREEN  -> SimonColor.YELLOW;
                        case YELLOW -> SimonColor.GREEN;
                    };
                default: // 2 atau lebih
                    return switch (flash) {
                        case RED    -> SimonColor.YELLOW;
                        case BLUE   -> SimonColor.RED;
                        case GREEN  -> SimonColor.BLUE;
                        case YELLOW -> SimonColor.BLUE;
                    };
            }
        }
    }
}
