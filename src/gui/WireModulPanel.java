package gui;

import model.WireModule;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WireModulPanel extends JPanel {
	private final WireModule modul;
	private final Runnable onStrike;
    private final JButton[] wireButtons = new JButton[6];
    private final JLabel statusLabel = new JLabel("CUT THE CORRECT WIRE", SwingConstants.CENTER);
	private static final int WIRE_WIDTH  = 120;
    private static final int WIRE_HEIGHT = 420;

	private final ImageIcon[] wireIconsUncut = {
    	new ImageIcon("assets/wires/merah_uncut.png"),
    	new ImageIcon("assets/wires/kuning_uncut.png"),
    	new ImageIcon("assets/wires/hijau_uncut.png"),
    	new ImageIcon("assets/wires/biru_uncut.png"),
    	new ImageIcon("assets/wires/pink_uncut.png"),
    	new ImageIcon("assets/wires/ungu_uncut.png")
	};

	private final ImageIcon[] wireIconsCut = {
    	new ImageIcon("assets/wires/merah_cut.png"),
    	new ImageIcon("assets/wires/kuning_cut.png"),
    	new ImageIcon("assets/wires/hijau_cut.png"),
    	new ImageIcon("assets/wires/biru_cut.png"),
    	new ImageIcon("assets/wires/pink_cut.png"),
    	new ImageIcon("assets/wires/ungu_cut.png")
	};

	public WireModulPanel(WireModule module, Runnable onStrike) {
		this.modul = module;
		this.onStrike = onStrike;
		initUI();
	}

	private void initUI() {
		setOpaque(false);
		setLayout(new BorderLayout(8,8));
		setBorder(new EmptyBorder(10,10,10,10));

		JPanel wiresRow = new JPanel(new GridLayout(1, 6, 8, 0));
        wiresRow.setOpaque(false);

        for (int i = 0; i < 6; i++) {
    		JButton b = new JButton();

			ImageIcon scaled = resizeIcon(wireIconsUncut[i], WIRE_WIDTH, WIRE_HEIGHT);
    		b.setIcon(scaled); // pake gambar kabel UNCUT

    		b.setHorizontalAlignment(SwingConstants.CENTER);
    		b.setVerticalAlignment(SwingConstants.CENTER);

			//untuk ukuran kabel
			b.setPreferredSize(new Dimension(WIRE_WIDTH, WIRE_HEIGHT));
            b.setMinimumSize(new Dimension(WIRE_WIDTH, WIRE_HEIGHT));
            b.setMaximumSize(new Dimension(WIRE_WIDTH, WIRE_HEIGHT));

    		styleWireButton(b);
    		final int index = i;
    		b.addActionListener(e -> handleCut(index));

    		wireButtons[i] = b;
    		wiresRow.add(b);
		}

        add(wiresRow, BorderLayout.CENTER);

        statusLabel.setForeground(Theme.TEXT_COLOR);
        statusLabel.setFont(Theme.LABEL_FONT);
        add(statusLabel, BorderLayout.NORTH);
	}

	private void styleWireButton(JButton b) {
        b.setFocusPainted(false);
    	b.setContentAreaFilled(false);
    	b.setBorderPainted(false);           
		b.setOpaque(false);
    	b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		// hover
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (b.isEnabled()) {
					b.setBorderPainted(true);
                    b.setBorder(BorderFactory.createLineBorder(Theme.COLOR_TITLE));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (b.isEnabled()) {
                    b.setBorderPainted(false);
                }
            }
        });
    }

	// helper untuk scale icon
    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resized = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }

    // ketika user klik salah satu kabel
    private void handleCut(int index) {

		if (modul.isSolvedStatus()) { // cek modul udah solved atau belum
            return;
        }

		wireButtons[index].setEnabled(false); // jika sudah di klik, tidak bisa diklik lagi
    	boolean success = modul.cutWire(index); //ngecek logika apakah ini kabel yang benar
	    ImageIcon cutScaled = resizeIcon(wireIconsCut[index], WIRE_WIDTH, WIRE_HEIGHT); // ngubah icon
    	wireButtons[index].setIcon(cutScaled);
		wireButtons[index].setDisabledIcon(cutScaled);

    	if (success) {
            // Kalau jawaban benar 
            statusLabel.setText("MODULE SOLVED");
            statusLabel.setForeground(Color.GREEN);

            for (int i = 0; i < wireButtons.length; i++) {
            	if (wireButtons[i] == null) continue;
            	// nagtur kabel lain biar tetap tidak kedisabled
            	wireButtons[i].setEnabled(true);
        	}
        } else {
            // Kalau salah 
            wireButtons[index].setEnabled(false);
            statusLabel.setText("WRONG WIRE! STRIKE +1");
            statusLabel.setForeground(Color.RED);

            if (onStrike != null) {
                onStrike.run();  // lapor ke GamePanel untuk tambah strike
            }
        }
	}
}
