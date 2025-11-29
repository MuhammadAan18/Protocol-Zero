package gui;

import model.User;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HomePanel extends JPanel{
	private final Main mainFrame;
    private final JLabel usserLabel = new JLabel("", SwingConstants.CENTER);
	private final JButton btnStart = new JButton("START MISSION");
	private final JButton btnGuide = new JButton("GUIDE MISSION");
	private final JButton btnExit = new JButton("ABORT");

	public HomePanel(Main mainFrame) {
		this.mainFrame = mainFrame;
		initUI();
	}

	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image bg = new ImageIcon("assets/bg/bg_home.png").getImage();
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }

	private void initUI() {
        setLayout(null); //pake absolut layout buat atur tombol secara manual

        usserLabel.setFont(Theme.USSER_FONT);
        usserLabel.setForeground(Theme.NEON_BLUE);
        usserLabel.setOpaque(false);

        createButton(btnStart);
        createButton(btnGuide);
        createButton(btnExit);

        add(usserLabel);
        add(btnStart);
        add(btnGuide);
        add(btnExit);

        btnStart.addActionListener(e -> mainFrame.showGamePanel());
        btnGuide.addActionListener(e -> mainFrame.showGuide());
        btnExit.addActionListener(e -> mainFrame.showLogin());

		addComponentListener(new java.awt.event.ComponentAdapter() {
        
        @Override
        public void componentResized(java.awt.event.ComponentEvent e) {
            int btnWidth  = 540; // atur lebar
            int btnHeight = 38; // atur tinggi
            int baseY = 655; // atur horizontal
            int centerX = (getWidth() - btnWidth) / 2 - 65; //atur vertikal
            int gap   = 50;

            btnStart.setBounds(centerX, baseY, btnWidth, btnHeight);
            btnGuide.setBounds(centerX, baseY + gap, btnWidth, btnHeight);
            btnExit.setBounds(centerX, baseY + 2 * gap, btnWidth, btnHeight);

            int labelWidth = getWidth();
            int labelHeight = 50;
            int labelY = 480;
            int labelx = -60;

            usserLabel.setBounds(labelx, labelY, labelWidth, labelHeight);

            repaint();
        }});
    }

    public void updateUsername() {
        User user = mainFrame.getCurrentUsername();
        if (user != null) {
            usserLabel.setText("WELCOME AGENT " + user.getUsername().toUpperCase());
        }
    }

    private JButton createButton(JButton b) {
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setForeground(new Color(0, 255, 255));
        b.setFont(Theme.TITLE_FONT);
        b.setBorder(BorderFactory.createLineBorder(Theme.NEON_BLUE));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        //hover
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(Theme.NEON_BLUE);
                b.setForeground(Theme.COLOR_TITLE);
                b.setBorder(BorderFactory.createLineBorder(Theme.COLOR_TITLE));
            }
            public void mouseExited(MouseEvent e) {
                b.setBackground(Theme.NEON_DARK);
                b.setForeground(new Color(0, 255,255));
                b.setBorder(BorderFactory.createLineBorder(Theme.NEON_BLUE));
            }
        });
        return b;
    }
}