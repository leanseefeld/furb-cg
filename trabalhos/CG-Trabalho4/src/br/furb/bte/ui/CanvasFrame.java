package br.furb.bte.ui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import br.furb.bte.Tela;

public class CanvasFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private Tela canvas;

    public CanvasFrame(String title) {
	super(title);
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	getContentPane().setLayout(new BorderLayout());

	canvas = new Tela();
	add(canvas, BorderLayout.CENTER);
	pack();
    }

    @Override
    public void setVisible(boolean b) {
	super.setVisible(b);
	canvas.requestFocus();
    }

}
