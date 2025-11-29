package gui;

import model.Button;
import model.ButtonModule;
import model.ButtonModule.ButtonAction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonModulPanel extends JPanel {

    private final ButtonModule modul;
    private final Runnable onStrike;
    private JButton mainButtonVisual;
    private JLabel statusLabel;

    private static final int BTN_SIZE = 275;
    private static final long TAP_THRESHOLD_MS = 300; // < 300ms = TAP, >= 300ms = HOLD
    private JLabel holdTimeLabel;        // indikator waktu hold
    private javax.swing.Timer holdTimer; // timer Swing untuk update label
    private long pressStartTime = 0L;

    public ButtonModulPanel(ButtonModule modul, Runnable onStrike) {
        this.modul = modul;
        this.onStrike = onStrike;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setOpaque(false);

        // HEADER KECIL
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.setOpaque(false);
        
        statusLabel = new JLabel("Tekan tombol untuk mencoba.", SwingConstants.CENTER);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        northPanel.add(statusLabel);
        northPanel.setBorder(new EmptyBorder(10,0,10,0));
        holdTimeLabel = new JLabel("Hold: 0.00 s", SwingConstants.CENTER);
        holdTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        holdTimeLabel.setForeground(Color.ORANGE);
        holdTimeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        northPanel.add(holdTimeLabel);

        add(northPanel, BorderLayout.NORTH);

        // TOMBOL UTAMA (IKON + LABEL)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        mainButtonVisual = new JButton();
        mainButtonVisual.setFont(new Font("Arial", Font.BOLD, 18));

        Button buttonModel = modul.getButton();
        mainButtonVisual.setText(buttonModel.getLabel());

        ImageIcon btnIcon = loadButtonIcon(buttonModel.getColor());
        if (btnIcon != null) {
            mainButtonVisual.setIcon(btnIcon);
            mainButtonVisual.setDisabledIcon(btnIcon);
            mainButtonVisual.setHorizontalTextPosition(SwingConstants.CENTER);
            mainButtonVisual.setVerticalTextPosition(SwingConstants.CENTER);
        }

        mainButtonVisual.setContentAreaFilled(false);
        mainButtonVisual.setBorderPainted(false);
        mainButtonVisual.setFocusPainted(false);
        mainButtonVisual.setOpaque(false);
        mainButtonVisual.setMargin(new Insets(0, 0, 0, 0));
        mainButtonVisual.setForeground(Color.WHITE);

        mainButtonVisual.setPreferredSize(new Dimension(BTN_SIZE, BTN_SIZE));
        mainButtonVisual.setMinimumSize(new Dimension(BTN_SIZE, BTN_SIZE));
        mainButtonVisual.setMaximumSize(new Dimension(BTN_SIZE, BTN_SIZE));

        // Penting: harus enabled supaya menerima mouse event
        mainButtonVisual.setEnabled(true);


        // Mouse listener: bedakan TAP vs HOLD
        mainButtonVisual.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (modul.isSolvedStatus()) return;
                startHoldTimer();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (modul.isSolvedStatus()) return;
                stopHoldTimer();
                
                long duration = System.currentTimeMillis() - pressStartTime;
                double sec = duration / 1000.0;
                long roundedSec = Math.round(sec);
                holdTimeLabel.setText(String.format("Hold: %.2f s (≈ %d)", sec, roundedSec));
                handlePressAction(duration, roundedSec);
            }
        });

        centerPanel.add(mainButtonVisual);
        add(centerPanel, BorderLayout.CENTER);
    }

    private ImageIcon loadButtonIcon(String colorName) {
        if (colorName == null) return null;

        String path;
        switch (colorName.toUpperCase().trim()) {
            case "RED":
                path = "assets/buttons/red_button.png";
                break;
            case "YELLOW":
                path = "assets/buttons/yellow_button.png";
                break;
            case "GREEN":
                path = "assets/buttons/green_button.png";
                break;
            default:
                path = null;
        }

        if (path == null) return null;

        ImageIcon raw = new ImageIcon(path);
        Image img = raw.getImage().getScaledInstance(BTN_SIZE, BTN_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // ====== LOGIKA PRESS ======

    private void handlePressAction(long durationMs, long roundedSeconds) {
    boolean success;

        if (durationMs < TAP_THRESHOLD_MS) {
            // klik cepat → TAP
            success = modul.tap();
        } else {
            // hold → cek berdasar detik yang dibulatkan
            success = modul.solveByHoldSeconds(roundedSeconds);
        }

        if (success) {
            statusLabel.setText("BENAR! MODULE SOLVED.");
            statusLabel.setForeground(Color.GREEN);
            mainButtonVisual.setEnabled(false);
        } else {
            statusLabel.setText("SALAH AKSI! STRIKE +1");
            statusLabel.setForeground(Color.RED);
            if (onStrike != null) {
                onStrike.run();
            }
        }
    }


    private void startHoldTimer() {
        pressStartTime = System.currentTimeMillis();

    // kalau sudah ada timer lama, stop dulu
        if (holdTimer != null && holdTimer.isRunning()) {
            holdTimer.stop();
        }

        holdTimer = new javax.swing.Timer(50, e -> {
            long elapsedMs = System.currentTimeMillis() - pressStartTime;
            double sec = elapsedMs / 1000.0;
            holdTimeLabel.setText(String.format("Hold: %.2f s", sec));
        });
        holdTimer.start();
    }

    private void stopHoldTimer() {
        if (holdTimer != null && holdTimer.isRunning()) {
            holdTimer.stop();
        }

        // hitung lama hold terakhir
        long elapsedMs = System.currentTimeMillis() - pressStartTime;
        double sec = elapsedMs / 1000.0;

        // pembulatan: 1.23 -> 1, 1.51 -> 2, dll
        long rounded = Math.round(sec);

        // tampilkan dalam bentuk detik bulat
        holdTimeLabel.setText(String.format("Hold: %.2f s (≈ %d)", sec, rounded));
    }

    // getter opsional
    public boolean isSolved() {
        return modul.isSolvedStatus();
    }

    public ButtonAction getRequiredAction() {
        return modul.getRequiredAction();
    }
}