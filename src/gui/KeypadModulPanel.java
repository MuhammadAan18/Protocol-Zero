package gui;

import model.Keypad;
import model.KeypadModule;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class KeypadModulPanel extends JPanel {

    private final KeypadModule modul;
    private final Runnable onStrike;

    private final JLabel titleLabel = new JLabel("KEYPADS");
    private JButton[] keypadButtons;

    private ImageIcon iconIdle;
    private ImageIcon iconPressed;

    public KeypadModulPanel(KeypadModule modul, Runnable onStrike) {
        this.modul = modul;
        this.onStrike = onStrike;
        loadIcons();
        initUI();
    }

    private void loadIcons() {
    	iconIdle    = loadScaledIcon("assets/keypad/keypad_idle.png", 160, 160);
    	iconPressed = loadScaledIcon("assets/keypad/keypad_pressed.png", 160, 160);
	}

    private ImageIcon loadScaledIcon(String path, int w, int h) {
	    try {
    	    ImageIcon raw = new ImageIcon(path);
        	Image scaled = raw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
	        return new ImageIcon(scaled);
	    } catch (Exception e) {
    	    return null;
	    }
	}

	private void initUI() {
    	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    	setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    	setOpaque(false);

    	titleLabel.setFont(Theme.BUTTON_FONT);
    	titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	titleLabel.setForeground(Theme.TEXT_COLOR);
    	add(titleLabel);

		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    	row1.setOpaque(false);
    	JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    	row2.setOpaque(false);

    	List<Keypad> displayKeypads = modul.getDisplayKeypads();
    	keypadButtons = new JButton[displayKeypads.size()];

    	for (int i = 0; i < displayKeypads.size(); i++) {
        	Keypad k = displayKeypads.get(i);

        	JButton btn = new JButton(k.getLabel());
        	btn.setFont(Theme.TITLE_FONT);
        	btn.setHorizontalTextPosition(SwingConstants.CENTER);
        	btn.setVerticalTextPosition(SwingConstants.CENTER);
        	btn.setForeground(Theme.BACKGROUND);
        	btn.setContentAreaFilled(false);
        	btn.setBorderPainted(false);
        	btn.setFocusPainted(false);
        	btn.setOpaque(false);
			Dimension size = new Dimension(160, 160);
        	btn.setPreferredSize(size);
        	btn.setMinimumSize(size);
        	btn.setMaximumSize(size);

        	if (iconIdle != null) {
            	btn.setIcon(iconIdle);
        	}

        	final int index = i;
        	btn.addActionListener(e -> handlePress(index));

        	keypadButtons[i] = btn;
			if (i < 2) {
				row1.add(btn);
			} else {
				row2.add(btn);
			}
			add(row1);
			add(row2);
		}
	}


    private void handlePress(int index) {
    	JButton btn = keypadButtons[index];

    	// ubah visual tombol yang ditekan menjadi pressed
	    if (iconPressed != null) {
    	    btn.setIcon(iconPressed);
	    }

	    boolean ok = modul.pressAtIndex(index);

    	if (ok) {
	        titleLabel.setText("CORRECT");
    	    titleLabel.setForeground(Color.GREEN);
	        btn.setEnabled(false);
    	    if (iconPressed != null) {
        	    btn.setDisabledIcon(iconPressed);
        	}

	        if (modul.isSolvedStatus()) {
    	        titleLabel.setText("MODULE SOLVED");
        	    titleLabel.setForeground(new Color(0, 255, 0));
            	disableAllButtons();
        	}
    	} else {
        	titleLabel.setText("STRIKE");
        	titleLabel.setForeground(Color.RED);
        	enableAllButtons();
	        resetIconsToIdle();

        	if (onStrike != null) {
        	    onStrike.run();
        	}
    	}
	}

    private void disableAllButtons() {
        for (JButton btn : keypadButtons) {
            btn.setEnabled(false);
        }
    }

    private void enableAllButtons() {
        for (JButton btn : keypadButtons) {
            btn.setEnabled(true);
        }
    }

	private void resetIconsToIdle() {
    	if (iconIdle == null) return;
    	for (JButton btn : keypadButtons) {
        	btn.setIcon(iconIdle);
    	}
	}
}
