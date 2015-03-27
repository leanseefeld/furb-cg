package br.furb.n2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Main implements GLEventListener, KeyListener {
	private float ortho2D_minX = -400.0f, ortho2D_maxX = 400.0f,
			ortho2D_minY = -400.0f, ortho2D_maxY = 400.0f;
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	private static final int DESLOCAMENTO = 10;

	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
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

		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glPointSize(3f);
		gl.glBegin(GL.GL_POINTS);
		for (int i = 0; i < 360; i += 5) {
			gl.glVertex2d(RetornaX(i, 100), RetornaY(i, 100));
		}
		gl.glEnd();

		gl.glFlush();
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

	// <<< --- EVENTOS DE TECLADO
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
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	// --- EVENTOS DE TECLADO --- >>>
}
