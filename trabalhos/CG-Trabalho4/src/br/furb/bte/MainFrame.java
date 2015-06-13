package br.furb.bte;

import java.awt.BorderLayout;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private Tela canvas;

    public MainFrame() {
	super("Tron");
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	getContentPane().setLayout(new BorderLayout());

	GLCapabilities glCaps = new GLCapabilities();
	glCaps.setRedBits(8);
	glCaps.setBlueBits(8);
	glCaps.setGreenBits(8);
	glCaps.setAlphaBits(8);

	canvas = new Tela();
	add(canvas, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean b) {
	pack();
	super.setVisible(b);
	canvas.requestFocus();
    }

    public static void main(String[] args) {
	UIUtils.changeLookAndFeelIfPossible(UIUtils.SupportedLookAndFeel.SYSTEM_DEFAULT);
	MainFrame frame = new MainFrame();
	frame.setVisible(true);
	UIUtils.centerOnScreen(frame);
    }

}
