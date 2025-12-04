package gui;

import java.awt.*;
import java.io.File;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;

import dao.*;
import model.*;

public class GamePanel extends JPanel {
    private final Main mainApp;

    private final JLabel serialNumber = new JLabel("Serial", SwingConstants.LEFT);
    private final JLabel timer        = new JLabel("Timer",  SwingConstants.CENTER);
	private final JLabel strikeLabel  = new JLabel("", SwingConstants.RIGHT);
    private final JLabel penaltyLabel = new JLabel("", SwingConstants.CENTER);

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

	private Thread bombTimerThread;
	private volatile boolean timerRunning = false;
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

    // load bomb
    public void loadBomb(Bomb bomb) {
        this.currentBomb   = bomb;
        this.currentStrikes = 0;

		// Stop thread sebelumnya jika masih berjalan
		timerRunning = false;
		if (bombTimerThread != null && bombTimerThread.isAlive()) {
			try {
				bombTimerThread.interrupt();
				bombTimerThread.join(100); // tunggu maksimal 100ms
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
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

        //  HUD atas 
        setSerialText("SERIAL : " + bomb.getSerial());
        setTimerText(formatTime(bomb.getTimeLimit()));  // misal timeLimit dalam detik
        setStrikeStatus(0);

        //  Isi grid modul 
        if (grid == null) return; // jaga-jaga

        grid.removeAll();

        for (BombModule m : bomb.getModules()) {
            grid.add(createPanelForModule(m));
        }

        while (grid.getComponentCount() < 4) {
            JPanel filler = new JPanel();
            filler.setOpaque(false);
            grid.add(filler);
        }

        grid.revalidate();
        grid.repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon("assets/bg/bg_game.png").getImage(), 0, 0, getWidth(), getHeight(), this);
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

    // setup ui
    private void initUI() {
        setLayout(new BorderLayout());
        setOpaque(true); 
		setBackground(Color.BLACK);

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

        penaltyLabel.setForeground(Color.RED);
        penaltyLabel.setFont(Theme.BUTTON_FONT);
        penaltyLabel.setVisible(false);
        center.add(penaltyLabel);

        return hud;
    }

    private JComponent modulGrid() {
        grid = new JPanel(new GridLayout(2, 2, 8, 8));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(0, 10, 0, 10));
        return grid;
    }

    private JComponent buttonControl() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(5, 10, 5, 10));

        createButton(btnBack);
        createButton(btnDefuse);
        createButton(btnManual);

        JPanel leftContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftContainer.setOpaque(false);
        leftContainer.add(btnBack);
        bottom.add(leftContainer, BorderLayout.WEST);

        JPanel centerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerContainer.setOpaque(false);
        centerContainer.add(btnDefuse);
        bottom.add(centerContainer, BorderLayout.CENTER); 

        JPanel rightContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightContainer.setOpaque(false);
        rightContainer.add(btnManual);
        bottom.add(rightContainer, BorderLayout.EAST); 

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
        btnBack.addActionListener(e -> {
            stopTickSound();
            mainApp.showHome();
        }); 
        btnDefuse.addActionListener(e -> checkAllModulesSolved());
        btnManual.addActionListener(e -> mainApp.showManual());
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

        if (timerRunning && remainingSeconds > 0) {
            remainingSeconds -= 30;
            showPenaltyText("-30"); 
            
            if (remainingSeconds < 0) {
                remainingSeconds = 0;
            }

            // update tampilan timer
            setTimerText(formatTime(remainingSeconds));

            // sesuaikan sound normal / fast tick
            if (remainingSeconds > 10) {
                if (fastTickActive) {
                    playNormalTickLoop();
                }
            } else {
                if (!fastTickActive) {
                    playFastTickLoop();
                }
            }

            // jika waktu habis setelah penalti → langsung meledak
            if (remainingSeconds <= 0) {
                timerRunning = false;
                if (bombTimerThread != null) {
                    bombTimerThread.interrupt();
                }
                handleTimeUp();
                return; // stop di sini supaya tidak lanjut ke logika strike max
            }
        }

        // 3. Update jumlah strike ke modul Simon
        for (BombModule m : currentBomb.getModules()) {
            if (m instanceof SimonModule simon) {
                simon.setStrikeCount(currentStrikes);
            }
        }

        // 4. Jika strike sudah mencapai batas → bomb meledak karena strike
        if (currentStrikes >= currentBomb.getMaxStrikes()) {
    		timerRunning = false;
    		if (bombTimerThread != null) {
    			bombTimerThread.interrupt();
    		}
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
			timerRunning = false;
			if (bombTimerThread != null) {
				bombTimerThread.interrupt();
			}
    		stopTickSound();

            // buat leaderboar
            int bombID = currentBomb.getbomb_Id();
            int maxStrikes = currentBomb.getMaxStrikes();
            int strikesLeft = Math.max(0, maxStrikes - currentStrikes);
            int timeLeft = Math.max(0, remainingSeconds);
            User currentUser = mainApp.getCurrentUsername();
            int user_id = 0;

            if (currentUser != null) {
                user_id = currentUser.getUserID();
            }
            
            ScoreDAO.insertScore(user_id, bombID, strikesLeft, timeLeft, maxStrikes);
            LeaderBoardDialog.show(mainApp);
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

	    // Stop thread sebelumnya jika masih berjalan
		timerRunning = false;
		if (bombTimerThread != null && bombTimerThread.isAlive()) {
			try {
				bombTimerThread.interrupt();
				bombTimerThread.join(100); // tunggu maksimal 100ms
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
    	
		playNormalTickLoop();
		timerRunning = true;

		// Buat Runnable untuk bomb timer logic
		Runnable timerTask = () -> {
			try {
				while (timerRunning && remainingSeconds > 0) {
					Thread.sleep(1000); // tunggu 1 detik
					
					if (!timerRunning) break; // cek lagi setelah sleep
					
					remainingSeconds--;
					
					// Update UI di EDT (Event Dispatch Thread)
					SwingUtilities.invokeLater(() -> {
						setTimerText(formatTime(remainingSeconds));

						// ganti ke fast ticking kalau sisa <= 10
						if (remainingSeconds > 10) {
							if (fastTickActive) {
								playNormalTickLoop();
							}
						} else {
							if (!fastTickActive) {
								playFastTickLoop();
							}
						}

						if (remainingSeconds <= 0) {
							timerRunning = false;
							handleTimeUp();
						}
					});
				}
			} catch (InterruptedException e) {
				// Thread di-interrupt, stop timer
				Thread.currentThread().interrupt();
				timerRunning = false;
			}
		};

		// Buat dan start thread
		bombTimerThread = new Thread(timerTask);
		bombTimerThread.setDaemon(true); // thread akan auto-stop saat app close
		bombTimerThread.start();
	}

    private void showPenaltyText(String text) {
        penaltyLabel.setText(text);
        penaltyLabel.setVisible(true);
        // Sembunyikan setelah 0.5 detik
        new Timer(500, e -> penaltyLabel.setVisible(false)).start();
    }

	private void handleTimeUp() {
    	timerRunning = false;
    	if (bombTimerThread != null) {
    		bombTimerThread.interrupt();
    	}
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

    public void showLeaderboardDialog() {
        List<ScoreEntry> scores = ScoreDAO.getTopScores(10);

    String[] columns = { "Rank", "User", "Bomb", "Strike", "Time", "Grade" };
    String[][] data = new String[scores.size()][columns.length];

    int rank = 1;
    for (int i = 0; i < scores.size(); i++) {
        ScoreEntry s = scores.get(i);
        data[i][0] = String.valueOf(rank++);
        data[i][1] = String.valueOf(s.getUserId());
        data[i][2] = String.valueOf(s.getBombId());
        data[i][3] = String.valueOf(s.getStrikeLeft());
        data[i][4] = String.valueOf(s.getTimeLeft());
        data[i][5] = s.getGameScore();
    }

    JTable table = new JTable(data, columns);
    table.setEnabled(false);
    table.setRowHeight(26);

    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setPreferredSize(new Dimension(600, 300));

    JOptionPane.showMessageDialog(
            null,
            scrollPane,
            "LEADERBOARD",
            JOptionPane.PLAIN_MESSAGE
    );
}
}
