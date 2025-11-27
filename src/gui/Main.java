package gui;

import dao.DatabaseConnection;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main extends JFrame{

	private DatabaseConnection db = new DatabaseConnection(); // buat inisialisasi DB
	
	private final CardLayout cardLayout = new CardLayout(); // layout card buat pindah" panel
	private final JPanel panelContainer = new JPanel(cardLayout); // panel card yang digunakan

	private final LoginPanel loginPanel = new LoginPanel(this);
	private final HomePanel homePanel = new HomePanel(this);
	
	private int currentUserId = -1;
    private String currentUsername = null;
	
	public Main() {
		setTitle("Protocol Zero");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setLocationRelativeTo(null);

		panelContainer.add(loginPanel, "LOGIN");
		panelContainer.add(homePanel, "HOME");
		
		setContentPane(panelContainer); // panelContainer menjadi content utama frame 

		setVisible(true);
	}

	public int getCurrentUserId() {
        return currentUserId;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

	public void showLogin() {
        cardLayout.show(panelContainer, "LOGIN");
    }

    public void showHome() {
        cardLayout.show(panelContainer, "HOME");
    }

	public DatabaseConnection getDb() {
		return db; 
	}

	public void onLoginSuccess(int userId, String username) {
        this.currentUserId = userId;
        this.currentUsername = username;
        showHome();
    }

	public void showGuide() {
		GuideDialogs.showCyberpunk(this);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(Main::new);
	}
}
