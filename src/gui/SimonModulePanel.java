package gui;

import model.SimonColor;
import model.SimonModule;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimonModulePanel extends JPanel {

    // pre-game (lampu pertama kedip)
    private javax.swing.Timer attractTimer;
    private boolean preGame = true;

    private final SimonModule modul;
    private final Runnable onStrike;

    private final JLabel titleLabel = new JLabel("SIMON SAYS");

    // 4 "tombol" (pakai icon)
    private JButton btnRed;
    private JButton btnBlue;
    private JButton btnGreen;
    private JButton btnYellow;

    // aset icon
    private ImageIcon iconIdle;
    private ImageIcon iconRed;
    private ImageIcon iconBlue;
    private ImageIcon iconGreen;
    private ImageIcon iconYellow;

    // playback sequence
    private javax.swing.Timer sequenceTimer;
    private int seqIndex = 0;
    private boolean seqOnPhase = true; // true = lampu ON, false = padam

    private boolean acceptingInput = false;
    private int lastKnownSequenceLength = 0;

    // ukuran tombol/icon
    private static final int ICON_SIZE = 100;

    public SimonModulePanel(SimonModule modul, Runnable onStrike) {
        this.modul = modul;
        this.onStrike = onStrike;
        loadIcons();
        initUI();
        startAttractMode();  
    }

    // ====== LOAD ICONS DARI FILESYSTEM ======
    
    private void loadIcons() {
        iconIdle   = loadScaledIcon("assets/lamp/lamp_idle.png",   ICON_SIZE, ICON_SIZE);
        iconRed    = loadScaledIcon("assets/lamp/lamp_red.png",    ICON_SIZE, ICON_SIZE);
        iconBlue   = loadScaledIcon("assets/lamp/lamp_blue.png",   ICON_SIZE, ICON_SIZE);
        iconGreen  = loadScaledIcon("assets/lamp/lamp_green.png",  ICON_SIZE, ICON_SIZE);
        iconYellow = loadScaledIcon("assets/lamp/lamp_yellow.png", ICON_SIZE, ICON_SIZE);
    }

    private ImageIcon loadScaledIcon(String path, int w, int h) {
        try {
            ImageIcon raw = new ImageIcon(path); 
            Image scaled = raw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.err.println("Gagal load icon (filesystem): " + path);
            return null;
        }
    }

    // ====== INIT UI ======

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setOpaque(false);

        // HEADER
        titleLabel.setFont(Theme.BUTTON_FONT); // Pastikan Theme ada di projectmu
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Theme.TEXT_COLOR);
        add(titleLabel);

        add(Box.createVerticalStrut(8));
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);

        btnBlue   = createSimonButton(SimonColor.BLUE);
        btnRed    = createSimonButton(SimonColor.RED);
        btnYellow = createSimonButton(SimonColor.YELLOW);
        btnGreen  = createSimonButton(SimonColor.GREEN);

        // Layout Grid
        gbc.gridx = 1; gbc.gridy = 0; center.add(btnBlue, gbc);
        gbc.gridx = 0; gbc.gridy = 1; center.add(btnYellow, gbc);
        gbc.gridx = 2; gbc.gridy = 1; center.add(btnRed, gbc);
        gbc.gridx = 1; gbc.gridy = 2; center.add(btnGreen, gbc);

        add(center);
    }

    // --- BAGIAN YANG DIPERBAIKI ---
    private JButton createSimonButton(SimonColor color) {
        JButton btn = new JButton();
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setMargin(new Insets(0, 0, 0, 0));

        // Set default icon idle
        if (iconIdle != null) {
            btn.setIcon(iconIdle);
            btn.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
        }

        // PERBAIKAN: Ambil dulu icon warnanya sebelum dipakai
        ImageIcon activeIcon = getIconForColor(color);

        if (activeIcon != null) {
            // Swing otomatis ganti gambar saat mouse ditahan
            btn.setPressedIcon(activeIcon);
        }

        // Action Listener tetap ada, logika validasi ada di handleColorPress
        btn.addActionListener(e -> handleColorPress(color));
        return btn;
    }
    // -----------------------------

    // ====== HANDLE INPUT PEMAIN ======

    private void handleColorPress(SimonColor color) {
        // Kita tidak disable tombol, jadi tombol selalu bisa diklik.
        // Tapi kita cegah logic berjalan jika sedang playback (!acceptingInput)
        // atau jika modul sudah selesai.
        if (!acceptingInput || modul.isSolvedStatus()) {
            return;
        }

        // ==== PHASE 1: PRE-GAME (WARNA PERTAMA KEDIP) ====
        if (preGame) {
            preGame = false;
            if (attractTimer != null && attractTimer.isRunning()) {
                attractTimer.stop();
            }
            
            resetAllIcons(); // Pastikan semua balik ke idle dulu
            acceptingInput = false; 

            startSequencePlayback();
            return; 
        }

        // ==== PHASE 2: GAME NORMAL ====
        highlightOnly(color);
        boolean ok = modul.pressColor(color);

        if (ok) {
            titleLabel.setText("CORRECT");
            titleLabel.setForeground(new Color(0, 200, 0));

            if (modul.isSolvedStatus()) {
                titleLabel.setText("MODULE SOLVED");
                titleLabel.setForeground(new Color(0, 255, 0));
                acceptingInput = false;
                showSolvedIcons();
            } else {
                List<SimonColor> seq = modul.getFlashOrder();
                if (seq.size() != lastKnownSequenceLength && modul.getCurrentInputIndex() == 0) {
                    // Sequence bertambah panjang -> Replay sequence baru
                    startSequencePlayback();
                } else {
                    Timer restoreTimer = new Timer(300, e -> {
                        if (acceptingInput) setAllButtonsActive(); 
                    });
                    restoreTimer.setRepeats(false);
                    restoreTimer.start();
                }
            }
        } else {
            titleLabel.setText("STRIKE");
            titleLabel.setForeground(Color.RED);
            if (onStrike != null) onStrike.run();
            // Ulang sequence yang sama
            startSequencePlayback();
        }
    }

    // ====== PLAYBACK SEQUENCE (BLINK DENGAN ICON) ======

    private void startSequencePlayback() {
        acceptingInput = false; // Block input user lewat logic

        if (sequenceTimer != null && sequenceTimer.isRunning()) {
            sequenceTimer.stop();
        }

        List<SimonColor> seq = modul.getFlashOrder();
        lastKnownSequenceLength = seq.size();

        resetAllIcons(); // Semua jadi idle/gelap

        seqIndex = 0;
        seqOnPhase = true;

        sequenceTimer = new javax.swing.Timer(400, e -> {
            List<SimonColor> currentSeq = modul.getFlashOrder();
            
            // Cek jika sequence selesai diputar
            if (currentSeq.isEmpty() || seqIndex >= currentSeq.size()) {
                // resetAllIcons(); // Pastikan semua mati
                sequenceTimer.stop();
                
                acceptingInput = true; // BUKA BLOCK INPUT
                
                titleLabel.setText("REPEAT THE SEQUENCE");
                titleLabel.setForeground(Theme.TEXT_COLOR);

                setAllButtonsActive();
                return;
            }

            SimonColor c = currentSeq.get(seqIndex);
            JButton btn = getButtonForColor(c);

            if (seqOnPhase) {
                // FASE ON: Ganti icon tombol target jadi berwarna
                setButtonIconForColor(btn, c);
            } else {
                // FASE OFF: Kembalikan icon tombol target jadi idle
                setButtonIdleIcon(btn);
                seqIndex++; // Pindah ke warna berikutnya
            }

            seqOnPhase = !seqOnPhase;
        });

        sequenceTimer.setInitialDelay(500); // Delay sedikit sebelum mulai
        sequenceTimer.start();
        titleLabel.setText("WATCH THE SEQUENCE");
        titleLabel.setForeground(Theme.TEXT_COLOR);
    }

    // ====== ATTRACT MODE (WARNA PERTAMA KEDIP TERUS) ======

    private void startAttractMode() {
        preGame = true;
        acceptingInput = true; // Boleh diklik untuk start game

        if (attractTimer != null && attractTimer.isRunning()) {
            attractTimer.stop();
        }

        List<SimonColor> seq = modul.getFlashOrder();
        if (seq.isEmpty()) return;

        SimonColor first = seq.get(0);
        JButton btn = getButtonForColor(first);

        resetAllIcons(); // Pastikan semua idle

        final boolean[] on = {false};

        attractTimer = new javax.swing.Timer(500, e -> {
            if (on[0]) {
                setButtonIdleIcon(btn); // Kedip Mati
            } else {
                setButtonIconForColor(btn, first); // Kedip Nyala
            }
            on[0] = !on[0];
        });

        attractTimer.setInitialDelay(0);
        attractTimer.start();

        titleLabel.setText("PRESS THE BLINKING LAMP");
        titleLabel.setForeground(Theme.TEXT_COLOR);
    }

    // ====== HELPER ICON & BUTTON ======

    private JButton getButtonForColor(SimonColor c) {
        return switch (c) {
            case RED    -> btnRed;
            case BLUE   -> btnBlue;
            case GREEN  -> btnGreen;
            case YELLOW -> btnYellow;
        };
    }

    private ImageIcon getIconForColor(SimonColor c) {
        return switch (c) {
            case RED    -> iconRed;
            case BLUE   -> iconBlue;
            case GREEN  -> iconGreen;
            case YELLOW -> iconYellow;
        };
    }

    private void setButtonIconForColor(JButton btn, SimonColor c) {
        ImageIcon active = getIconForColor(c);
        if (active != null) {
            btn.setIcon(active);
            btn.setDisabledIcon(active); 
        }
    }

    private void setButtonIdleIcon(JButton btn) {
        if (iconIdle != null) {
            btn.setIcon(iconIdle);
            btn.setDisabledIcon(iconIdle); 
        }
    }

    private void resetAllIcons() {
        setButtonIdleIcon(btnRed);
        setButtonIdleIcon(btnBlue);
        setButtonIdleIcon(btnGreen);
        setButtonIdleIcon(btnYellow);
    }

    private void showSolvedIcons() {
        if (iconRed != null)    btnRed.setIcon(iconRed);
        if (iconBlue != null)   btnBlue.setIcon(iconBlue);
        if (iconGreen != null)  btnGreen.setIcon(iconGreen);
        if (iconYellow != null) btnYellow.setIcon(iconYellow);
    }

    // --- METHOD BARU: Nyalakan SEMUA tombol (Ready state) ---
    private void setAllButtonsActive() {
        setButtonIconForColor(btnRed, SimonColor.RED);
        setButtonIconForColor(btnBlue, SimonColor.BLUE);
        setButtonIconForColor(btnGreen, SimonColor.GREEN);
        setButtonIconForColor(btnYellow, SimonColor.YELLOW);
    }

    // --- METHOD BARU: Nyalakan SATU tombol, sisanya IDLE (Focus state) ---
    private void highlightOnly(SimonColor target) {
        // Matikan semua dulu
        resetAllIcons(); 
        
        // Nyalakan hanya yang ditarget
        JButton targetBtn = getButtonForColor(target);
        setButtonIconForColor(targetBtn, target);
    }
}