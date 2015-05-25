package br.furb.bte;

import java.awt.BorderLayout;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main extends JFrame {

    private static final long serialVersionUID = 1L;
    private Tela canvas;

    public Main() {
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
	setResizable(false);
    }

    @Override
    public void setVisible(boolean b) {
	canvas.requestFocus();
	pack();
	super.setVisible(b);
    }

    public static void main(String[] args) {
	UIUtils.changeLookAndFeelIfPossible(UIUtils.SupportedLookAndFeel.SYSTEM_DEFAULT);
	Main frame = new Main();
	frame.setVisible(true);
	UIUtils.centerOnScreen(frame);
    }

}
