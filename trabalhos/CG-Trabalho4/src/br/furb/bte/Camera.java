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

    private static final int TELEPORT_MARGIN = 5;

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
	    robot.setAutoDelay(0);
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
	if (!e.isControlDown()) {
	    teleportMouse(e);
	}
    }

    private boolean teleportMouse(MouseEvent e) {
	if (robot != null && !e.isShiftDown()) {
	    return false;
	}
	int absX = e.getXOnScreen();
	int absY = e.getYOnScreen();
	Point screenLocation = tela.getLocationOnScreen();
	Point newMouse = new Point();
	int limiteEsquerda = screenLocation.x + tela.getWidth();
	newMouse.y = absY;
	if (absX + TELEPORT_MARGIN >= limiteEsquerda) {
	    newMouse.x = screenLocation.x + TELEPORT_MARGIN + 1;
	} else if (absX - TELEPORT_MARGIN <= screenLocation.x) {
	    newMouse.x = limiteEsquerda - TELEPORT_MARGIN - 1;
	} else {
	    int limiteBaixo = screenLocation.y + tela.getHeight();
	    newMouse.x = absX;
	    if (absY + TELEPORT_MARGIN >= limiteBaixo) {
		newMouse.y = screenLocation.y + TELEPORT_MARGIN + 1;
	    } else if (absY - TELEPORT_MARGIN <= screenLocation.y) {
		newMouse.y = limiteBaixo - TELEPORT_MARGIN - 1;
	    } else {
		return false;
	    }
	}
	robot.mouseMove(newMouse.x, newMouse.y);
	oldMouse.X = newMouse.x - screenLocation.x;
	oldMouse.Y = newMouse.y - screenLocation.y;
	return true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
	mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent newMouse) {
	if (!newMouse.isControlDown()) {
	    if (teleportMouse(newMouse)) {
		return;
	    }

	    double offsetX = oldMouse.X - newMouse.getX();
//	    double offsetY = oldMouse.Y - newMouse.getY();

	    double scaleX = tela.getWidth() / 3;
//	    double scaleY = tela.getHeight();

	    offsetX /= -scaleX;
//	    offsetY /= scaleY;

	    // TODO: talvez movimentar proporcionalmente um pouco a câmera...

	    setTransformacao(getTransformacao().transformMatrix(new Transformacao().atribuirRotacaoY(offsetX)));
	    //		setTransformacao(getTransformacao().transformMatrix(new Transformacao().atribuirRotacaoX(offsetY)));
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
