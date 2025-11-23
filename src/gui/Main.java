package gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main extends JFrame{
	
	public Main() {
		setTitle("Protocol Zero");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setLocationRelativeTo(null);

		HomePanel homePanel = new HomePanel(this);
		setContentPane(homePanel);
		
		setVisible(true);
	}

	public void exitGame() {
    	System.exit(0);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(Main::new);
	}
}
