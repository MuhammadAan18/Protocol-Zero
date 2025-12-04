package gui;

import javax.swing.*;
import java.awt.*;

public class CountDownPanel extends JPanel {

    private JLabel label;
    private Thread countdownThread;
    private volatile boolean countdownRunning = false;
    private int count;

    public CountDownPanel() {
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        label = new JLabel("3");
        label.setFont(Theme.COUNTDOWN_FONT);
        label.setForeground(Theme.NEON_BLUE);

        add(label, new GridBagConstraints());
    }

    public void startCountdown(Runnable onFinish) {
        // Stop thread sebelumnya jika masih berjalan
        countdownRunning = false;
        if (countdownThread != null && countdownThread.isAlive()) {
            try {
                countdownThread.interrupt();
                countdownThread.join(100); // tunggu maksimal 100ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        count = 3;
        label.setText(String.valueOf(count));
        countdownRunning = true;

        // Buat Runnable untuk countdown logic
        Runnable countdownTask = () -> {
            try {
                while (countdownRunning && count > 0) { 
                    Thread.sleep(1000); // tunggu 1 detik
                    
                    if (!countdownRunning) break; // cek lagi setelah sleep
                    
                    count--;
                    
                    // Update UI di EDT (Event Dispatch Thread)
                    SwingUtilities.invokeLater(() -> {
                        if (count > 0) {
                            label.setText(String.valueOf(count));
                        } else {
                            // Countdown selesai
                            label.setText("START DEFUSE");
                            countdownRunning = false;
                            onFinish.run();
                        }
                    });
                }
            } catch (InterruptedException e) {
                // Thread di-interrupt, stop countdown
                Thread.currentThread().interrupt();
                countdownRunning = false;
            }
        };

        // Buat dan start thread
        countdownThread = new Thread(countdownTask);
        countdownThread.setDaemon(true); // thread akan auto-stop saat app close
        countdownThread.start();
    }
}
