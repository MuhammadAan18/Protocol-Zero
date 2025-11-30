package gui;

import model.Button;
import model.ButtonModule;
import model.ButtonModule.ButtonAction;

import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonModulPanel extends JPanel {

    private final ButtonModule modul;
    private final Runnable onStrike;
    private JButton mainButtonVisual;

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

        holdTimeLabel = new JLabel("Hold: 0.00 s", SwingConstants.CENTER);
        holdTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        holdTimeLabel.setForeground(Theme.COLOR_TITLE);
        holdTimeLabel.setFont(Theme.BUTTON_FONT);

        add(holdTimeLabel, BorderLayout.NORTH);

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
        mainButtonVisual.setEnabled(true);

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
        boolean success = false;
        ButtonAction required = modul.getRequiredAction();

        if (durationMs < TAP_THRESHOLD_MS) { //tap
            if (required == ButtonAction.TAP) {
                success = modul.tap();
            } else {
                success = false; // hold = strike
            }
        } else { // hold
            if (required == ButtonAction.TAP) { //tap = strike
                success = false;
            } else {
                success = modul.solveByHoldSeconds(roundedSeconds);
            }
        }

        if (success) {
            holdTimeLabel.setText("MODULE SOLVED");
            holdTimeLabel.setForeground(Color.GREEN);
            mainButtonVisual.setEnabled(false);
        } else {
            holdTimeLabel.setText("STRIKE");
            holdTimeLabel.setForeground(Color.RED);
            if (onStrike != null) {
                onStrike.run();
            }
        }
    }

    private void startHoldTimer() {
        pressStartTime = System.currentTimeMillis();

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
    }
}