package gui;

import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;

public class LoginPanel extends JPanel {
    private final Main mainApp;
    private final JTextField userField = new JTextField();
    private final JPasswordField passField = new JPasswordField();
    private final JButton loginButton = new JButton("LOGIN");
    private final JButton registerButton = new JButton("REGISTER");
    private final JButton quitButton = new JButton("QUIT");

    public LoginPanel(Main mainApp) {
        this.mainApp = mainApp;

        setOpaque(false);
        setLayout(new GridBagLayout());

        HudPanel formBox = new HudPanel();
        formBox.setLayout(new BoxLayout(formBox, BoxLayout.Y_AXIS));
        formBox.setBorder(new EmptyBorder(40, 50, 40, 50)); 
        
        formBox.add(titleSection());
        formBox.add(Box.createVerticalStrut(30));
        formBox.add(formSection());
        formBox.add(Box.createVerticalStrut(30));
        formBox.add(buttons());

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        add(formBox, gc);

        addListeners();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon("assets/bg/bg_login.png").getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    private class HudPanel extends JPanel {

        public HudPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int cut = 20; 

            // buat B=bentuk kotak dari form
            Path2D.Double path = new Path2D.Double();
            path.moveTo(cut, 0);
            path.lineTo(w - cut, 0);
            path.lineTo(w, cut);
            path.lineTo(w, h - cut);
            path.lineTo(w - cut, h);
            path.lineTo(cut, h);
            path.lineTo(0, h - cut);
            path.lineTo(0, cut);
            path.closePath();

            // ackground Panel
            g2.setColor(Theme.PANEL_BG);
            g2.fill(path);
            g2.setColor(Theme.NEON_BLUE);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(path);

            // dekorasi Tambahan
            g2.setStroke(new BasicStroke(4f));
            g2.drawLine(w/2 - 40, 0, w/2 + 40, 0);
            g2.drawLine(w/2 - 40, h, w/2 + 40, h);
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(10, h-10, 30, h-10); 
            g2.drawLine(w-30, 10, w-10, 10); 
            g2.dispose();
        }
    }

    private JComponent titleSection() {
        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("PROTOCOL ZERO", SwingConstants.CENTER);
        title.setFont(Theme.BUTTON_FONT);
        title.setForeground(Theme.NEON_BLUE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JLabel subtitle = new JLabel("System Access Required", SwingConstants.CENTER);
        subtitle.setFont(new Font("Monospaced", Font.PLAIN, 12));
        subtitle.setForeground(Theme.TEXT_COLOR);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(title);
        box.add(subtitle);

        return box;
    }

    private JComponent formSection() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        styleSciFiField(userField);
        styleSciFiField(passField);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.gridy = 0;

        addRow(form, g, "USERNAME", userField);
        addRow(form, g, "PASSWORD", passField);

        return form;
    }

    private void styleSciFiField(JTextField f) {
        f.setPreferredSize(new Dimension(280, 35));
        f.setFont(new Font("Monospaced", Font.PLAIN, 14));
        f.setForeground(Color.WHITE);
        f.setCaretColor(Theme.NEON_BLUE);
        f.setOpaque(false); 
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.NEON_DARK, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        f.setBackground(new Color(0, 0, 0, 50));
    }

    private void addRow(JPanel form, GridBagConstraints g, String labelText, JComponent field) {
        g.gridx = 0;
        g.anchor = GridBagConstraints.EAST;
        JLabel label = new JLabel(labelText);
        label.setForeground(Theme.NEON_BLUE);
        label.setFont(new Font("Monospaced", Font.BOLD, 12));
        form.add(label, g);

        g.gridx = 1;
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        form.add(field, g);

        g.gridy++;
    }

    private JComponent buttons() {
        JPanel hbox = new JPanel();
        hbox.setOpaque(false);
        hbox.setLayout(new BoxLayout(hbox, BoxLayout.X_AXIS));

        JPanel vbox = new JPanel();
        vbox.setOpaque(false);
        vbox.setLayout(new BoxLayout(vbox, BoxLayout.Y_AXIS));

        styleSciFiButton(loginButton, true);
        styleSciFiButton(registerButton, true);
        styleSciFiButton(quitButton, false);

        // Ukuran tombol
        Dimension btnSize = new Dimension(140, 40);
        loginButton.setMaximumSize(btnSize);
        registerButton.setMaximumSize(btnSize);
        loginButton.setPreferredSize(btnSize);
        registerButton.setPreferredSize(btnSize);
        quitButton.setPreferredSize(new Dimension(280, 40));
        quitButton.setMaximumSize(new Dimension(296, 40));

        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        hbox.add(loginButton);
        hbox.add(Box.createHorizontalStrut(15));
        hbox.add(registerButton);

        hbox.setAlignmentX(Component.CENTER_ALIGNMENT);
        vbox.add(hbox);
        vbox.add(Box.createVerticalStrut(15));
        vbox.add(quitButton);
        vbox.setAlignmentX(Component.CENTER_ALIGNMENT);
        return vbox;
    }

    private void styleSciFiButton(JButton b, boolean isPrimary) {
        b.setFont(new Font("Monospaced", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        if (isPrimary) {
            b.setForeground(Color.BLACK);
            b.setBackground(Theme.NEON_BLUE);
            b.setOpaque(true); 
            b.setBorder(BorderFactory.createLineBorder(Theme.NEON_BLUE));
        } else {
            b.setForeground(Theme.NEON_BLUE);
            b.setOpaque(false);
            b.setBorder(BorderFactory.createLineBorder(Theme.NEON_BLUE));
        }

        // Efek Hover
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if(isPrimary) b.setBackground(Color.WHITE);
                else b.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }
            public void mouseExited(MouseEvent e) {
                if(isPrimary) b.setBackground(Theme.NEON_BLUE);
                else b.setBorder(BorderFactory.createLineBorder(Theme.NEON_BLUE));
            }
        });
    }

    private void addListeners() {
        loginButton.addActionListener(e -> processLogin());
        passField.addActionListener(e -> processLogin());
        registerButton.addActionListener(e -> processRegister());
        quitButton.addActionListener(e -> processQuit());
    }

    private void processLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("ACCESS DENIED", "Username and password required.");
            return;
        }

        try {
            User user = mainApp.getDb().loginUser(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "ACCESS GRANTED.", "System", JOptionPane.INFORMATION_MESSAGE);
                mainApp.setCurrentUsername(user);
                mainApp.showHome();
                resetField();
            } else {
                showError("LOGIN FAILED", "INVALID CREDENTIALS");
            }
        } catch (Exception ex) {
            showError("SYSTEM ERROR", ex.getMessage());
        }
    }

    private void processRegister() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("ERROR", "Fields cannot be empty.");
            return;
        }

        try {
            boolean ok = mainApp.getDb().registerUser(username, password);
            if (ok) {
                JOptionPane.showMessageDialog(this, "User Registered to Database.", "Success", JOptionPane.INFORMATION_MESSAGE);
                resetField();
            } else {
                showError("REGISTRATION FAILED", "Username already exists.");
            }
        } catch (Exception ex) {
            showError("SYSTEM ERROR", ex.getMessage());
        }
    }

    private void processQuit () {
        System.exit(0);
    }

    private void showError(String title, String msg) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    public void resetField() {
        userField.setText("");
        passField.setText("");
        userField.requestFocusInWindow();
    }
}