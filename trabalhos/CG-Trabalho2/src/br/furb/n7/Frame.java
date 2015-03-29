package br.furb.n7;

import java.awt.BorderLayout;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Main renderer = new Main();

	private int janelaLargura = 400, janelaAltura = 400;
	private GLCanvas canvas;

	public Frame() {
		super("Execício N7");
		setBounds(300, 250, janelaLargura, janelaAltura + 22);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		GLCapabilities glCaps = new GLCapabilities();
		glCaps.setRedBits(8);
		glCaps.setBlueBits(8);
		glCaps.setGreenBits(8);
		glCaps.setAlphaBits(8);

		canvas = new GLCanvas(glCaps);
		add(canvas, BorderLayout.CENTER);
		canvas.addGLEventListener(renderer);
		canvas.requestFocus();
	}
	
	@Override
	public void setVisible(boolean arg0) {
		super.setVisible(arg0);
		canvas.requestFocus();
	}

	public static void main(String[] args) {
		new Frame().setVisible(true);
	}

}
