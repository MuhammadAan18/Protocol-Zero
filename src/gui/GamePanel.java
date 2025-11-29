package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.*;

public class GamePanel extends JPanel {
    private final Main mainApp;

    private final JLabel serialNumber = new JLabel("Serial", SwingConstants.LEFT);
    private final JLabel timer        = new JLabel("Timer",  SwingConstants.CENTER);
	private final JLabel strikeLabel  = new JLabel("", SwingConstants.RIGHT);
    private final ImageIcon[] strike = {
		loadStrikeIcon("assets/strikes/0strikes.png"),
		loadStrikeIcon("assets/strikes/1strikes.png"),
		loadStrikeIcon("assets/strikes/2strikes.png"),
		loadStrikeIcon("assets/strikes/3strikes.png")
	};

    private final JButton btnManual = new JButton("MANUAL");
    private final JButton btnDefuse = new JButton("DEFUSE");
    private final JButton btnBack   = new JButton("BACK");

    private JPanel grid;
    private Bomb currentBomb;
    private int currentStrikes = 0;

    public GamePanel(Main main) {
		setBackground(Theme.BACKGROUND);
        this.mainApp = main;
        initUI();
        initActions();
    }

    // ================== LOAD BOMB ==================
    public void loadBomb(Bomb bomb) {
        this.currentBomb   = bomb;
        this.currentStrikes = 0;

		if (bomb == null) {
            serialNumber.setText("NO BOMB");
            timer.setText("--:--");
            strikeLabel.setIcon(null);
            strikeLabel.setText("");
            if (grid != null) {
                grid.removeAll();
                grid.revalidate();
                grid.repaint();
            }
            return;
        }

        // --- HUD atas ---
        setSerialText("SERIAL : " + bomb.getSerial());
        setTimerText(formatTime(bomb.getTimeLimit()));  // misal timeLimit dalam detik
        setStrikeStatus(0);

        // --- Isi grid modul ---
        if (grid == null) return; // jaga-jaga

        grid.removeAll();

        for (BombModule m : bomb.getModules()) {
            grid.add(createPanelForModule(m));
        }

        // kalau modul kurang dari 4, isi filler supaya layout 2x2 tetap rapi
        while (grid.getComponentCount() < 4) {
            JPanel filler = new JPanel();
            filler.setOpaque(false);
            grid.add(filler);
        }

        grid.revalidate();
        grid.repaint();
    }

    private String formatTime(int totalSeconds) { //buat format wkatu
        int m = totalSeconds / 60;
        int s = totalSeconds % 60;
        return String.format("%02d:%02d", m, s);
    }

    private JComponent createPanelForModule(BombModule module) { // bikin panel GUI untuk tiap module
        // contoh kalau module wire
        if (module instanceof WireModule wire) {
            return new WireModulPanel(wire, this::registerStrike);
        }

        // fallback sementara: panel teks nama module
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setBorder(BorderFactory.createLineBorder(Theme.NEON_BLUE));
        p.add(new JLabel(module.getmodulName()));
        return p;
    }

    // ================== UI SETUP ==================
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setOpaque(true); // nanti ganti 
		setBackground(Color.BLACK);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(statusPanel(), BorderLayout.NORTH);
        add(modulGrid(),   BorderLayout.CENTER);
        add(buttonControl(), BorderLayout.SOUTH);
    }

    private JComponent statusPanel() {
        JPanel hud = new JPanel(new BorderLayout());
        hud.setOpaque(false);
        hud.setBorder(new EmptyBorder(5, 10, 5, 10));

        serialNumber.setForeground(Theme.NEON_BLUE);
        serialNumber.setFont(Theme.TITLE_FONT);
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        left.add(serialNumber);

        timer.setForeground(Theme.NEON_BLUE);
        timer.setFont(Theme.TITLE_FONT);
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        center.setOpaque(false);
        center.add(timer);

        strikeLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        right.add(strikeLabel);

        hud.add(left, BorderLayout.WEST);
        hud.add(center, BorderLayout.CENTER);
        hud.add(right, BorderLayout.EAST);

        return hud;
    }

    private JComponent modulGrid() {
        grid = new JPanel(new GridLayout(2, 2, 16, 16));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(10, 20, 10, 20));
        return grid;
    }

    private JComponent buttonControl() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottom.setOpaque(false);

        bottom.add(btnBack);
        bottom.add(btnDefuse);
        bottom.add(btnManual);

        return bottom;
    }

	private ImageIcon loadStrikeIcon(String path) {
    	ImageIcon raw = new ImageIcon(path);
    	Image scaled = raw.getImage().getScaledInstance(120, 60, Image.SCALE_SMOOTH);
    	return new ImageIcon(scaled);
	}

    private void initActions() {
        btnBack.addActionListener(e -> mainApp.showHome());
        btnDefuse.addActionListener(e -> {
            // nanti diisi logika cek semua modul, atau langsung pakai checkAllModulesSolved()
            checkAllModulesSolved();
        });
        btnManual.addActionListener(e -> mainApp.showGuide());
    }

    // ================== HUD SETTER ==================
    public void setSerialText(String serial) {
        serialNumber.setText(serial);
    }

    public void setTimerText(String text) {
        timer.setText(text);
    }

    public void setStrikeStatus(int strikes) {
        this.currentStrikes = strikes;
        updateStrikeHud();
    }

    // ================== STRIKE & STATUS ==================
    private void updateStrikeHud() {
        if (currentBomb == null) {
            strikeLabel.setIcon(null);
            strikeLabel.setText("");
            return;
        }

        int max = currentBomb.getMaxStrikes(); // contoh: 3
        int used = Math.min(currentStrikes, max); // berapa strike yang sudah kena (0..max)
        int iconIndex = Math.min(used, strike.length - 1); // clamp ke 0..3

        strikeLabel.setText("");
        strikeLabel.setIcon(strike[iconIndex]);
    }

	// Dipanggil dari modul ketika player salah (misal WireModulPanel)
    private void registerStrike() {
        if (currentBomb == null) return;

        currentStrikes++;
        updateStrikeHud();

        if (currentStrikes >= currentBomb.getMaxStrikes()) {
            JOptionPane.showMessageDialog(
                    this,
                    "BOMB DETONATED.\nMission failed.",
                    "Explosion",
                    JOptionPane.ERROR_MESSAGE
            );
            mainApp.showHome();
        }
    }

    private void checkAllModulesSolved() {
        if (currentBomb == null) return;

        boolean allSolved = currentBomb.getModules().stream().allMatch(BombModule::isSolvedStatus);

        if (allSolved) {
            JOptionPane.showMessageDialog(
                    this,
                    "BOMB DEFUSED.\nMission complete.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            mainApp.showHome();
        } else {
			JOptionPane.showMessageDialog(
                    this,
                    "Some modules are still active.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
		}
    }
}
