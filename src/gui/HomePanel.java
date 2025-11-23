package gui;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel{
	private final Main mainFrame;

	public HomePanel(Main mainFrame) {
		this.mainFrame = mainFrame;
		initUI();
	}

	private void initUI() {
		setLayout(new BorderLayout());
		setBackground(Theme.BACKGROUND);
		
		// untuk title gamemya
		JLabel titleLabel = new JLabel("Protocol Zero", SwingConstants.CENTER);
		titleLabel.setFont(Theme.TITLE_FONT);
		titleLabel.setForeground(Theme.COLOR_TITLE);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
		add(titleLabel, BorderLayout.NORTH);

		// button untuk game, panduan, dan keluar
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

		JButton btnStart = new JButton("Mulai Permainan");
		JButton btnGuide = new JButton("Panduan Permainan");
		JButton btnExit = new JButton("Keluar");

		Dimension btnSize = new Dimension(300, 50);
		JButton[] buttonList = {btnStart, btnGuide, btnExit};

        for (JButton b : buttonList) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(btnSize);
            b.setPreferredSize(btnSize);
            b.setFont(Theme.BUTTON_FONT);
        }

		// Komposisi dari tombolnya
		buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(btnStart);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // jarak 20
        buttonPanel.add(btnGuide);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(btnExit);
        buttonPanel.add(Box.createVerticalGlue());

        add(buttonPanel, BorderLayout.CENTER);


		btnExit.addActionListener(e -> {
			int result = JOptionPane.showConfirmDialog(
            this,
            "Apakah kamu yakin ingin keluar?",
            "Konfirmasi Keluar",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
    		);

			if (result == JOptionPane.YES_OPTION) {
				mainFrame.exitGame();
			}
		});
	}
}