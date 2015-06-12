package br.furb.bte;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Camera implements MouseMotionListener, MouseListener, MouseWheelListener {

    private final Ponto mouse;
    private Transformacao $transformacao;
    private final Tela tela;

    public Camera(Tela tela) {
	this.tela = tela;
	this.mouse = new Ponto(0, 0, 0);
	tela.addMouseMotionListener(this);
	tela.addMouseListener(this);
	tela.addMouseWheelListener(this);
    }

    private Transformacao getTransformacao() {
	if ($transformacao == null) {
	    $transformacao = new Transformacao();
	}
	return $transformacao;
    }

    private Transformacao setTransformacao(Transformacao transformacao) {
	$transformacao = transformacao;
	return transformacao;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
	setTransformacao(getTransformacao().transformMatrix(//
		new Transformacao().atribuirEscala(//
			1 - (float) e.getWheelRotation() / 10, //
			1 - (float) e.getWheelRotation() / 10, //
			1 - (float) e.getWheelRotation() / 10)));
	tela.render();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
	mouse.X = e.getX();
	mouse.Y = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
	double qtdX = mouse.X - e.getX();
	double qtdY = mouse.Y - e.getY();

	qtdX = -qtdX / 100;
	qtdY = qtdY / 100;

	mouse.X = e.getX();
	mouse.Y = e.getY();
	// TODO: talvez movimentar proporcionalmente um pouco a câmera...

	setTransformacao(getTransformacao().transformMatrix(new Transformacao().atribuirRotacaoY(qtdX)));
	//	transformacaoMundo = transformacaoMundo.transformMatrix(new Transformacao().atribuirRotacaoX(qtdY));
	tela.render();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
	// TODO: talvez movimentar proporcionalmente um pouco a câmera...
    }

    public Transformacao absorverTransformacao(final Transformacao transformacao) {
	if ($transformacao != null) {
	    Transformacao transformMatrix = transformacao.transformMatrix($transformacao);
	    $transformacao = null;
	    return transformMatrix;
	}
	return transformacao;
    }

}
