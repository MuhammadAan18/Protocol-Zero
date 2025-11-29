package gui;

import model.*;
import dao.*;

import java.awt.*;
import java.sql.SQLException;

import javax.swing.*;

public class Main extends JFrame{
	private User currentUser;

	private DatabaseConnection db = new DatabaseConnection(); // buat inisialisasi DB
	private BombDAO bombDao = new BombDAO();
	private Bomb currentBomb;
	
	private final CardLayout cardLayout = new CardLayout(); // layout card buat pindah" panel
	private final JPanel panelContainer = new JPanel(cardLayout); // panel card yang digunakan

	private final LoginPanel loginPanel = new LoginPanel(this);
	private final HomePanel homePanel = new HomePanel(this);
	private final GamePanel gamePanel = new GamePanel(this);
	
	public Main() {
		setTitle("Protocol Zero");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setLocationRelativeTo(null);

		panelContainer.add(loginPanel, "LOGIN");
		panelContainer.add(homePanel, "HOME");
		panelContainer.add(gamePanel, "GAME");
		
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
		try {
        Bomb bomb = bombDao.loadRandomBomb();
        gamePanel.loadBomb(bomb);      
        cardLayout.show(panelContainer, "GAME");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(
                this,
                "Gagal memuat bomb: " + ex.getMessage(),
                "DB Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
	}

	public void showGuide() {
		GuideDialogs.showCyberpunk(this); //buat cara main
	}

	public void showManual() {
		GuideDialogs.showManual(this); //simpan buat guide nya
	}

	public DatabaseConnection getDb() {
		return db; 
	}

	public void startNewGame() {
		try {
			currentBomb = bombDao.loadRandomBomb();
			if (currentBomb != null) {
				gamePanel.loadBomb(currentBomb);
				cardLayout.show(panelContainer, "GAME");
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Tidak ada bomb di database!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	} 
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(Main::new);
	}
}
