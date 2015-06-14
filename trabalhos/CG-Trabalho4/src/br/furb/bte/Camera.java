package br.furb.bte;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.media.opengl.glu.GLU;
import br.furb.bte.objetos.Moto;
import br.furb.bte.objetos.Poligono;
import br.furb.bte.objetos.Ponto;

public class Camera implements MouseMotionListener, MouseListener, MouseWheelListener {

    private static final int VELOCIDADE_ROTACAO = 15;
    private static final int TELEPORT_MARGIN = 5;
    private static final double DISTANCIA = 700;
    private static final double ALTURA = 500;

    private final Ponto oldMouse;
    private final Tela tela;
    private Robot robot;
    private float incAnguloY;
    private float anguloCameraY;
    private int distancia = 0;
    private Moto moto;

    public Camera(Tela tela, boolean permiteControlar) {
	this.tela = tela;
	this.oldMouse = new Ponto(0, 0, 0);
	try {
	    robot = new Robot();
	    robot.setAutoDelay(0);
	} catch (AWTException e) {
	    e.printStackTrace();
	}
//	incAnguloY = 45;

	tela.addMouseWheelListener(this);

	if (permiteControlar) {
	    tela.addMouseMotionListener(this);
	    tela.addMouseListener(this);
	}
    }

    public void seguirMoto(Moto moto) {
	this.moto = moto;
    }

    public void atualizar(GLU glu) {
	final Poligono objObservado = moto == null ? tela.getArena() : moto;
	if (objObservado == null) {
	    return;
	}
	final Ponto pontoObservado = objObservado.getBBoxTransformada().getCentro();
	float anguloCameraYProximo = moto == null ? incAnguloY : moto.getAngulo() + 180;

	float diferencaAngulo = anguloCameraYProximo - anguloCameraY;
	System.out.println("atualizaPosicaoCamera - anguloCameraY: " + anguloCameraY);
	System.out.println("atualizaPosicaoCamera - anguloCameraYProximo: " + anguloCameraYProximo);
	System.out.println("atualizaPosicaoCamera - Diferenca: " + diferencaAngulo);
	if (diferencaAngulo != 0) {
	    if (diferencaAngulo < -VELOCIDADE_ROTACAO) {
		anguloCameraY -= VELOCIDADE_ROTACAO;
		System.out.println("diferencaAngulo < VELOCIDADE_ROTACAO");
	    } else if (diferencaAngulo > VELOCIDADE_ROTACAO) {
		System.out.println("diferencaAngulo > VELOCIDADE_ROTACAO");
		anguloCameraY += VELOCIDADE_ROTACAO;
	    } else {
		System.out.println("diferencaAngulo > 15 e < 15");
		anguloCameraY = anguloCameraYProximo;
	    }
	}

	//Calcula a rotação e aproximação em X
	double novoX = (distancia + DISTANCIA) * Math.cos(Math.toRadians(anguloCameraY));
	novoX += pontoObservado.x;

	//Calcula a rotação e aproximação em Z
	double novoZ = (distancia + DISTANCIA) * Math.sin(Math.toRadians(anguloCameraY));
	novoZ += pontoObservado.z;

	//Calcula a altura e aproximação em Y
	double catetoOposto = ALTURA - pontoObservado.y;
	double catetoAdjascente = ((DISTANCIA - pontoObservado.x) + (DISTANCIA - pontoObservado.z)) / 2;
	double tangente = catetoOposto / catetoAdjascente;
	double anguloY = Math.atan(tangente);
	double novoY = (distancia / DISTANCIA * ALTURA + ALTURA) * Math.sin(anguloY);

	glu.gluLookAt(novoX, novoY, novoZ, pontoObservado.x, pontoObservado.y, pontoObservado.z, 0, 1, 0);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
	distancia += e.getWheelRotation() * 20;
	tela.render();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
	oldMouse.x = e.getX();
	oldMouse.y = e.getY();
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
	oldMouse.x = newMouse.x - screenLocation.x;
	oldMouse.y = newMouse.y - screenLocation.y;
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

	    double offsetX = oldMouse.x - newMouse.getX();
	    //	    double offsetY = oldMouse.Y - newMouse.getY();

	    double scaleX = tela.getWidth() / 3;
	    //	    double scaleY = tela.getHeight();

	    offsetX /= -scaleX;
	    //	    offsetY /= scaleY;

	    incAnguloY += offsetX * 50;
	    System.out.println("mouseMoved: " + incAnguloY);
	    //	    atualizaPosicaoCamera();

	    tela.render();
	}
	oldMouse.x = newMouse.getX();
	oldMouse.y = newMouse.getY();
    }

}
