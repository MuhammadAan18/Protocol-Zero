package gui;

import model.*;
import dao.*;

import java.awt.*;
import java.sql.SQLException;

import javax.swing.*;

public class Main extends JFrame{
	private User currentUser;
	private Bomb currentBomb;
	private DatabaseConnection db = new DatabaseConnection(); // buat inisialisasi DB
	private BombDAO bombDao = new BombDAO();
	
	private final CardLayout cardLayout = new CardLayout(); // layout card buat pindah" panel
	private final JPanel panelContainer = new JPanel(cardLayout); // panel card yang digunakan
	private final LoginPanel loginPanel = new LoginPanel(this);
	private final HomePanel homePanel = new HomePanel(this);
	private final GamePanel gamePanel = new GamePanel(this);
	private final CountDownPanel countdownPanel = new CountDownPanel();
	
	
	public Main() {
		setTitle("Protocol Zero");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setLocationRelativeTo(null);

		panelContainer.add(loginPanel, "LOGIN");
		panelContainer.add(homePanel, "HOME");
		panelContainer.add(gamePanel, "GAME");
		panelContainer.add(countdownPanel, "COUNTDOWN");
		
		setContentPane(panelContainer); // panelContainer menjadi content utama frame 

		setVisible(true);
	}

	public void setCurrentUsername(User user) {
        this.currentUser = user;
		homePanel.updateUsername();
    }

    public User getCurrentUsername() {
        return currentUser;
    }

	public void showLogin() {
        cardLayout.show(panelContainer, "LOGIN");
    }

    public void showHome() {
		cardLayout.show(panelContainer, "HOME");
    }
	
	public void showGamePanel() {
        // Load bomb di background thread agar tidak freeze UI
        Thread loadBombThread = new Thread(() -> {
            try {
                Bomb loadedBomb = bombDao.loadRandomBomb();
                
                // Update UI di EDT setelah load selesai
                SwingUtilities.invokeLater(() -> {
                    if (loadedBomb == null) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Tidak ada bomb di database!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    
                    currentBomb = loadedBomb;
                    gamePanel.loadBomb(currentBomb);
                    cardLayout.show(panelContainer, "COUNTDOWN");
                    countdownPanel.startCountdown(() -> {
                        cardLayout.show(panelContainer, "GAME");
                        gamePanel.startBombTimer();
                    });
                });
            } catch (SQLException ex) {
                // Handle error di EDT juga
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            this,
                            "Gagal memuat bomb: " + ex.getMessage(),
                            "DB Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                });
            }
        });
        
        loadBombThread.setDaemon(true); // thread akan auto-stop saat app close
        loadBombThread.start();
    }

	public void showGuide() {
		GuideDialogs.showCyberpunk(this); //buat cara main
	}

	public void showManual() {
		GuideDialogs.showManual(this); //simpan buat guide nya
	}

	public void showLeaderboardDialog() {
    	LeaderBoardDialog.show(this);
	}

	public DatabaseConnection getDb() {
		return db; 
	}

	public void startNewGame() {
		// Load bomb di background thread agar tidak freeze UI
		Thread loadBombThread = new Thread(() -> {
			try {
				Bomb loadedBomb = bombDao.loadRandomBomb();
				
				// Update UI di EDT setelah load selesai
				SwingUtilities.invokeLater(() -> {
					if (loadedBomb != null) {
						currentBomb = loadedBomb;
						gamePanel.loadBomb(currentBomb);
						cardLayout.show(panelContainer, "GAME");
					} else {
						JOptionPane.showMessageDialog(
								this, 
								"Tidak ada bomb di database!", 
								"Error", 
								JOptionPane.ERROR_MESSAGE
						);
					}
				});
			} catch (SQLException ex) {
				// Handle error di EDT juga
				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(
							this, 
							"Gagal memuat bomb: " + ex.getMessage(), 
							"Error", 
							JOptionPane.ERROR_MESSAGE
					);
				});
			}
		});
		
		loadBombThread.setDaemon(true); // thread akan auto-stop saat app close
		loadBombThread.start();
	} 

	public static void main(String[] args) {
		SwingUtilities.invokeLater(Main::new);
	}
}
