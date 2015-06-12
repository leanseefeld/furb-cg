package br.furb.bte;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Camera implements MouseMotionListener, MouseListener, MouseWheelListener {

    private final Ponto oldMouse;
    private final Tela tela;
    private Robot robot;
    private Transformacao $transformacao;

    public Camera(Tela tela) {
	this.tela = tela;
	this.oldMouse = new Ponto(0, 0, 0);
	tela.addMouseMotionListener(this);
	tela.addMouseListener(this);
	tela.addMouseWheelListener(this);
	try {
	    robot = new Robot();
	} catch (AWTException e) {
	    e.printStackTrace();
	}
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
	oldMouse.X = e.getX();
	oldMouse.Y = e.getY();
    }

    @Override
    public void mouseExited(MouseEvent e) {
	if (robot != null && e.isShiftDown() && !e.isControlDown()) {
	    int absX = e.getXOnScreen();
	    int absY = e.getYOnScreen();
	    Point screenLocation = tela.getLocationOnScreen();
	    int limiteEsquerda = screenLocation.x + tela.getWidth();
	    if (absX >= limiteEsquerda) {
		robot.mouseMove(screenLocation.x, absY);
	    } else if (absX <= screenLocation.x) {
		robot.mouseMove(limiteEsquerda, absY);
	    } else {
		int limiteBaixo = screenLocation.y + tela.getHeight();
		if (absY >= limiteBaixo) {
		    robot.mouseMove(absX, screenLocation.y);
		} else if (absY <= screenLocation.y) {
		    robot.mouseMove(absX, limiteBaixo);
		}
	    }
	}
    }

    @Override
    public void mousePressed(MouseEvent e) {
	oldMouse.X = e.getX();
	oldMouse.Y = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent newMouse) {
	if (!newMouse.isControlDown()) {
	    double offsetX = oldMouse.X - newMouse.getX();
	    double offsetY = oldMouse.Y - newMouse.getY();

	    double maxSize = tela.getWidth() / 3;

	    offsetX /= -maxSize;
	    offsetY /= 100;

	    // TODO: talvez movimentar proporcionalmente um pouco a câmera...

	    setTransformacao(getTransformacao().transformMatrix(new Transformacao().atribuirRotacaoY(offsetX)));
	    //	transformacaoMundo = transformacaoMundo.transformMatrix(new Transformacao().atribuirRotacaoX(qtdY));
	    tela.render();
	}
	oldMouse.X = newMouse.getX();
	oldMouse.Y = newMouse.getY();
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
