package gui;

import java.awt.*;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;


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

	private javax.swing.Timer bombTimer;
	private int remainingSeconds;
	private Clip bombTickClip;
	private boolean fastTickActive = false;

    public GamePanel(Main main) {
		setBackground(Theme.BACKGROUND);
        this.mainApp = main;
        initUI();
        initActions();
		initSound();
    }

    // ================== LOAD BOMB ==================
    public void loadBomb(Bomb bomb) {
        this.currentBomb   = bomb;
        this.currentStrikes = 0;

		if (bombTimer != null) {
        	bombTimer.stop();
    	}

		if (bomb == null) {
            serialNumber.setText("NO BOMB");
            timer.setText("--:--");
            strikeLabel.setIcon(null);
            strikeLabel.setText("Null");
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
        
        JPanel p = new JPanel();
        // module wire
        if (module instanceof WireModule wire) {
            return new WireModulPanel(wire, this::registerStrike);
        }

        // module wire
        if (module instanceof ButtonModule buttonModule) {
            return new ButtonModulPanel(buttonModule, this::registerStrike);
        }

        // module keypad
        if (module instanceof KeypadModule keypadModule) {
            return new KeypadModulPanel(keypadModule, this::registerStrike);
        }

        // module simon
        if (module instanceof SimonModule simonModule) {
            return new SimonModulePanel(simonModule, this::registerStrike);
        }

        return p;
    }

    // ================== UI SETUP ==================
    private void initUI() {
        setLayout(new BorderLayout());
        setOpaque(true); // nanti ganti 
		setBackground(Color.BLACK);
        // setBorder(new EmptyBorder(20, 20, 20, 20));

        add(statusPanel(), BorderLayout.NORTH);
        add(modulGrid(),   BorderLayout.CENTER);
        add(buttonControl(), BorderLayout.SOUTH);
    }

    private JComponent statusPanel() {
        JPanel hud = new JPanel(new BorderLayout());
        hud.setOpaque(false);
        hud.setBorder(new EmptyBorder(5, 10, 5, 10));

        serialNumber.setForeground(Theme.COLOR_TITLE);
        serialNumber.setFont(Theme.BUTTON_FONT);
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        left.add(serialNumber);

        timer.setForeground(Theme.COLOR_TITLE);
        timer.setFont(Theme.BUTTON_FONT);
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
        grid = new JPanel(new GridLayout(2, 2, 8, 8));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(0, 10, 0, 10));
        return grid;
    }

    private JComponent buttonControl() {
        // Container utama untuk tombol (BorderLayout: WEST, CENTER, EAST)
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(5, 10, 5, 10));

        // Inisialisasi styling untuk semua tombol
        createButton(btnBack);
        createButton(btnDefuse);
        createButton(btnManual);

        // 1. KIRI (btnBack) - menggunakan FlowLayout.LEFT untuk memastikan padding kiri
        JPanel leftContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftContainer.setOpaque(false);
        leftContainer.add(btnBack);
        bottom.add(leftContainer, BorderLayout.WEST);

        // 2. TENGAH (btnDefuse) - menggunakan FlowLayout.CENTER agar tombol tidak stretch
        JPanel centerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerContainer.setOpaque(false);
        centerContainer.add(btnDefuse);
        bottom.add(centerContainer, BorderLayout.CENTER); // BARU: Tambahkan ke CENTER

        // 3. KANAN (btnManual) - menggunakan FlowLayout.RIGHT untuk memastikan padding kanan
        JPanel rightContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightContainer.setOpaque(false);
        rightContainer.add(btnManual);
        bottom.add(rightContainer, BorderLayout.EAST); // btnManual sudah ada di sini

        return bottom;
    }

    private JButton createButton(JButton b) {
        b.setOpaque(false);
        b.setBorder(null);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setForeground(Theme.COLOR_TITLE);
        b.setFont(Theme.BUTTON_FONT);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        //hover
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setForeground(Theme.NEON_BLUE);
            }
            public void mouseExited(MouseEvent e) {
                b.setForeground(Theme.TEXT_COLOR);
            }
        });
        return b;
    }

	private ImageIcon loadStrikeIcon(String path) {
    	ImageIcon raw = new ImageIcon(path);
    	Image scaled = raw.getImage().getScaledInstance(120, 60, Image.SCALE_SMOOTH);
    	return new ImageIcon(scaled);
	}

    private void initActions() {
        btnBack.addActionListener(e -> mainApp.showHome());
        btnDefuse.addActionListener(e -> checkAllModulesSolved());
        btnManual.addActionListener(e -> mainApp.showGuide());
    }

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

    private void updateStrikeHud() {
        if (currentBomb == null) {
            strikeLabel.setIcon(null);
            return;
        }

        int max = currentBomb.getMaxStrikes(); 
        int used = Math.min(currentStrikes, max); // berapa strike yang sudah kena (0..max)
        int iconIndex = Math.min(used, strike.length - 1); // clamp ke 0..3
        strikeLabel.setIcon(strike[iconIndex]);
    }

	// Dipanggil dari modul ketika player salah
    private void registerStrike() {
        if (currentBomb == null) return;

        currentStrikes++;
        updateStrikeHud();

        for (BombModule m : currentBomb.getModules()) { //khusus untu modul simon untuk ngeset jumlah strike nya
            if (m instanceof SimonModule simon) {
                simon.setStrikeCount(currentStrikes);
            }
        }

        if (currentStrikes >= currentBomb.getMaxStrikes()) {
    		if (bombTimer != null) bombTimer.stop();
    		playExplosionOnce();

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
			if (bombTimer != null) bombTimer.stop();
    		stopTickSound();
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

	public void startBombTimer() {
    	if (currentBomb == null) return;

    	remainingSeconds = currentBomb.getTimeLimit();
    	setTimerText(formatTime(remainingSeconds));

	    if (bombTimer != null) {
    	    bombTimer.stop();
	    }
    	playNormalTickLoop();

    	bombTimer = new Timer(1000, e -> {
        	remainingSeconds--;
        	setTimerText(formatTime(remainingSeconds));

        	// ganti ke fast ticking kalau sisa <= 10
        	if (remainingSeconds >10) {
            	if (fastTickActive) {
                	playNormalTickLoop();
            	}
        	} else {
            	if (!fastTickActive) {
                	playFastTickLoop();
            	}
        	}

        	if (remainingSeconds <= 0) {
            	((Timer) e.getSource()).stop();
            	handleTimeUp();
        	}
    	});
    	bombTimer.start();
	}

    public int getLastTimerDigit() {
        if (remainingSeconds < 0) return 0;
        return Math.abs(remainingSeconds) % 10;
    }

	private void handleTimeUp() {
    	if (bombTimer != null) bombTimer.stop();
    	stopTickSound();
    	JOptionPane.showMessageDialog(
            this,
            "TIME'S UP!\nBomb detonated.",
            "Explosion",
            JOptionPane.ERROR_MESSAGE
    	);
    	mainApp.showHome();
	}

	private void initSound() {
    	try {
	        File soundFile = new File("assets/sounds/tick.wav"); 
    	    AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
        	bombTickClip = AudioSystem.getClip();
	        bombTickClip.open(ais);
    	} catch (Exception ex) {
        	ex.printStackTrace();
	        bombTickClip = null;
    	}
	}

	private void playNormalTickLoop() {
    	if (bombTickClip == null) return;

    	fastTickActive = false;
    	bombTickClip.stop();

    	float frameRate = bombTickClip.getFormat().getFrameRate();

	    int startFrame = 0;
    	int endFrame   = (int) (7000 * frameRate / 1000); // 0 - 7 detik

	    bombTickClip.setLoopPoints(startFrame, endFrame);
    	bombTickClip.setFramePosition(startFrame);
    	bombTickClip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	private void playFastTickLoop() {
    	if (bombTickClip == null) return;

	    fastTickActive = true;
    	bombTickClip.stop();

	    float frameRate = bombTickClip.getFormat().getFrameRate();

    	int startFrame = 8000; //dari 8 detik
	    int endFrame   = (int) (13000 * frameRate / 1000); // sampai detik ke-14

    	bombTickClip.setLoopPoints(startFrame, endFrame);
	    bombTickClip.setFramePosition(startFrame);
    	bombTickClip.loop(Clip.LOOP_CONTINUOUSLY);
	}

    private void playExplosionOnce() {
        if (bombTickClip == null) return;

        try {
            bombTickClip.stop();

            float frameRate  = bombTickClip.getFormat().getFrameRate();
            int frameLength  = bombTickClip.getFrameLength();

            int startFrame = (int) (10000 * frameRate / 1000f);
            int endFrame = (int) (10500 * frameRate / 1000f);
            if (startFrame >= frameLength) {
                startFrame = 0;
            }

            bombTickClip.setLoopPoints(startFrame, endFrame);
            bombTickClip.setFramePosition(startFrame);
            bombTickClip.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	private void stopTickSound() {
    	if (bombTickClip != null && bombTickClip.isRunning()) {
        	bombTickClip.stop();
    	}
	}
}
