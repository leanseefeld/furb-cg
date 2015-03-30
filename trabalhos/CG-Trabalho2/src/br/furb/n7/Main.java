package br.furb.n7;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import br.furb.commons.Circulo2D;
import br.furb.commons.Cor;
import br.furb.commons.Ponto2D;

public class Main implements GLEventListener, MouseMotionListener, KeyListener,
		MouseListener {
	private float ortho2D_minX = -400.0f, ortho2D_maxX = 400.0f,
			ortho2D_minY = -400.0f, ortho2D_maxY = 400.0f;
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	private Ponto2D ultimaMovimentacao = new Ponto2D();
	private final static double AJUSTE_MOUSE = 1.1d;
	private static final float DESLOCAMENTO = 10;
	private Circulo2D circuloMaior;
	private Circulo2D circuloMenor;
	private final static Cor COR_DENTRO_BBOX = new Cor(0d, 1d, 1d);
	private final static Cor COR_FORA_BBOX = new Cor(1d, 0d, 1d);
	private final static Cor COR_FORA_CIRCULO = new Cor(1d, 1d, 0d);

	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");

		circuloMaior = new Circulo2D(new Ponto2D(), 200);
		circuloMenor = new Circulo2D(new Ponto2D(), 50);

		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		System.out.println("Espa�o de desenho com tamanho: "
				+ drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void SRU() {
		// eixo x
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glLineWidth(1.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2f(-200.0f, 0.0f);
		gl.glVertex2f(200.0f, 0.0f);
		gl.glEnd();
		// eixo y
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2f(0.0f, -200.0f);
		gl.glVertex2f(0.0f, 200.0f);
		gl.glEnd();
	}

	// exibicaoPrincipal
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(ortho2D_minX, ortho2D_maxX, ortho2D_minY, ortho2D_maxY);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		SRU();

		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glLineWidth(2f);
		desenhaCirculo(200, circuloMaior.getLocalizacao().getX(), circuloMaior
				.getLocalizacao().getY());

		gl.glColor3f(1.0f, 0.0f, 1.0f);
		gl.glLineWidth(2f);
		desenhaCirculo(50, circuloMenor.getLocalizacao().getX(), circuloMenor
				.getLocalizacao().getY());

		gl.glColor3f(0f, 0f, 0f);
		gl.glPointSize(6f);
		gl.glBegin(GL.GL_POINTS);
		{
			gl.glVertex2d(circuloMenor.getLocalizacao().getX(), circuloMenor
					.getLocalizacao().getY());
		}
		gl.glEnd();

		gl.glFlush();
	}

	public void desenhaCirculo(int raio, double centroX, double centroY) {
		gl.glBegin(GL.GL_LINE_STRIP);
		for (int i = 0; i <= 360; i += 5) {
			gl.glVertex2d(RetornaX(i, raio) + centroX, RetornaY(i, raio)
					+ centroY);
		}
		gl.glEnd();
	}

	public double RetornaX(double angulo, double raio) {
		return (raio * Math.cos(Math.PI * angulo / 180.0));
	}

	public double RetornaY(double angulo, double raio) {
		return (raio * Math.sin(Math.PI * angulo / 180.0));
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {

	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		ultimaMovimentacao.setX(arg0.getX());
		ultimaMovimentacao.setY(arg0.getY());
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		boolean reconheceu = true;
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_E:
			ortho2D_minX -= DESLOCAMENTO;
			ortho2D_maxX -= DESLOCAMENTO;
			break;
		case KeyEvent.VK_D:
			ortho2D_minX += DESLOCAMENTO;
			ortho2D_maxX += DESLOCAMENTO;
			break;
		case KeyEvent.VK_C:
			ortho2D_minY += DESLOCAMENTO;
			ortho2D_maxY += DESLOCAMENTO;
			break;
		case KeyEvent.VK_B:
			ortho2D_minY -= DESLOCAMENTO;
			ortho2D_maxY -= DESLOCAMENTO;
			break;

		case KeyEvent.VK_O:
			if (Math.sqrt(Math.pow((ortho2D_maxY - ortho2D_minY), 2)) <= 1600) {
				ortho2D_minX -= DESLOCAMENTO;
				ortho2D_maxX += DESLOCAMENTO;
				ortho2D_minY -= DESLOCAMENTO;
				ortho2D_maxY += DESLOCAMENTO;
			}
			break;

		case KeyEvent.VK_I:
			if (Math.sqrt(Math.pow((ortho2D_maxY - ortho2D_minY), 2)) >= 400) {
				ortho2D_minX += DESLOCAMENTO;
				ortho2D_maxX -= DESLOCAMENTO;
				ortho2D_minY += DESLOCAMENTO;
				ortho2D_maxY -= DESLOCAMENTO;
				break;
			}
		default:
			reconheceu = false;
			break;
		}
		if (reconheceu) {
			glDrawable.display();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		double mvtoX = e.getX() - ultimaMovimentacao.getX();
		double mvtoY = e.getY() - ultimaMovimentacao.getY();

		double novoX = mvtoX + (mvtoX * AJUSTE_MOUSE);
		double novoY = -(mvtoY + (mvtoY * AJUSTE_MOUSE));

		if (circuloMenor.EstaDentroDe(circuloMaior)) {
			circuloMenor.getLocalizacao().MoverX(novoX);
			circuloMenor.getLocalizacao().MoverY(novoY);

			ultimaMovimentacao.setLocation(e.getX(), e.getY());
		}

		glDrawable.display();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
