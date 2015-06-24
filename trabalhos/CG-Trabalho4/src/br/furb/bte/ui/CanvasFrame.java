package br.furb.bte.ui;

import java.awt.BorderLayout;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import br.furb.bte.Tela;
import br.furb.bte.controle.Controlador;

public class CanvasFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private Tela canvas;

    public CanvasFrame(String title, Controlador controlador) {
	super(title);
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	getContentPane().setLayout(new BorderLayout());

	GLCapabilities glCaps = new GLCapabilities();
	glCaps.setRedBits(8);
	glCaps.setBlueBits(8);
	glCaps.setGreenBits(8);
	glCaps.setAlphaBits(8);
	glCaps.setSampleBuffers(true);
	canvas = new Tela(glCaps);
	add(canvas, BorderLayout.CENTER);
	controlador.associarTela(canvas);
	pack();
    }
    
    @Override
    public void setVisible(boolean b) {
	super.setVisible(b);
	canvas.requestFocus();
    }

}
