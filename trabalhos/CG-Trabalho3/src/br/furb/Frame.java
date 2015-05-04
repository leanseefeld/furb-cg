package br.furb;

import java.awt.BorderLayout;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
//	private int janelaLargura = 400, janelaAltura = 400;
	private GLCanvas canvas;

	public Frame() {
		super("Trabalho 3");
		setLocation(300, 250);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		GLCapabilities glCaps = new GLCapabilities();
		glCaps.setRedBits(8);
		glCaps.setBlueBits(8);
		glCaps.setGreenBits(8);
		glCaps.setAlphaBits(8);

		canvas = new GLCanvas(glCaps);
		new Canvas(canvas);
		add(canvas, BorderLayout.CENTER);
	}
	
	@Override
	public void setVisible(boolean b) {
		canvas.requestFocus();
//		setSize(canvas.getSize());
		pack();
		super.setVisible(b);
	}

	public static void main(String[] args) {
		Frame frame = new Frame();
		frame.setVisible(true);
	}

}
