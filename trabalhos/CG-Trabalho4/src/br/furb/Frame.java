package br.furb;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
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
		Frame frame = new Frame();
		frame.setVisible(true);
		UIUtils.centerOnScreen(frame);
		
		mostrarInstrucoes(frame);
	}

	private static void mostrarInstrucoes(Frame frame) {
		StringBuilder sb = new StringBuilder();
		try (Scanner sc = new Scanner(new File("res/instrucoes.txt"))) {
			while(sc.hasNext()) {
				sb.append(sc.nextLine()).append('\n');
			}
			JOptionPane.showMessageDialog(frame, sb.toString(), "Instruções", JOptionPane.INFORMATION_MESSAGE);
		} catch (FileNotFoundException e) {
			new RuntimeException(
					"Não foi possível mostrar as instruções de uso", e)
					.printStackTrace();
		}

	}

}
