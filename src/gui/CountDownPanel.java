package gui;

import javax.swing.*;
import java.awt.*;

public class CountDownPanel extends JPanel {

    private JLabel label;
    private Timer countdownTimer;
    private int count;

    public CountDownPanel() {
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        label = new JLabel("3");
        label.setFont(new Font("Arial", Font.BOLD, 120));
        label.setForeground(Color.WHITE);

        add(label, new GridBagConstraints());
    }

    public void startCountdown(Runnable onFinish) {
        count = 3;
        label.setText(String.valueOf(count));

        if (countdownTimer != null) {
            countdownTimer.stop();
        }

        countdownTimer = new Timer(1000, e -> {
            count--;
            if (count > 0) {
                label.setText(String.valueOf(count));
            } else {
                ((Timer) e.getSource()).stop();
                onFinish.run();
            }
        });

        countdownTimer.start();
    }
}
