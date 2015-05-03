package br.furb;

import java.awt.BorderLayout;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Canvas renderer = new Canvas();
	private int janelaLargura = 400, janelaAltura = 400;

	public Frame() {
		super("Trabalho 3");
		setBounds(300, 250, janelaLargura, janelaAltura + 22);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		GLCapabilities glCaps = new GLCapabilities();
		glCaps.setRedBits(8);
		glCaps.setBlueBits(8);
		glCaps.setGreenBits(8);
		glCaps.setAlphaBits(8);

		GLCanvas canvas = new GLCanvas(glCaps);
		add(canvas, BorderLayout.CENTER);
		canvas.addGLEventListener(renderer);
		canvas.addMouseMotionListener(renderer);
		canvas.addMouseListener(renderer);
		canvas.addKeyListener(renderer);
		canvas.requestFocus();
	}

	public static void main(String[] args) {
		new Frame().setVisible(true);
	}

}
