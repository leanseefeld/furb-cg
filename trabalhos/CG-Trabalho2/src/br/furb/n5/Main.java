package br.furb.n5;

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
	private int index = 0;
	int[] primitivas = { GL.GL_POINTS, GL.GL_LINES, GL.GL_LINE_LOOP,
			GL.GL_LINE_STRIP, GL.GL_TRIANGLES, GL.GL_TRIANGLE_FAN,
			GL.GL_TRIANGLE_STRIP, GL.GL_QUADS, GL.GL_QUAD_STRIP, GL.GL_POLYGON };

	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));

		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
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
		PrimitivasGeometricas(index);

		gl.glFlush();
	}

	public void SRU() {
		// eixo x
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glLineWidth(1.0f);
		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex2f(-200.0f, 0.0f);
			gl.glVertex2f(200.0f, 0.0f);
		}
		gl.glEnd();
		// eixo y
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex2f(0.0f, -200.0f);
			gl.glVertex2f(0.0f, 200.0f);
		}
		gl.glEnd();
	}

	public void PrimitivasGeometricas(int i) {
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glPointSize(6.0f);
		gl.glLineWidth(6.0f);
		gl.glBegin(definePrimitiva(i));
		{
			// Altera o desenhos dos 4 pontos
			// de acordo com a primitiva passada
			gl.glVertex2d(200, 200);
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glVertex2d(200, -200);
			gl.glColor3f(0.0f, 1.0f, 0.0f);
			gl.glVertex2d(-200, -200);
			gl.glColor3f(0.0f, 0.0f, 1.0f);
			gl.glVertex2d(-200, 200);
		}
		gl.glEnd();

	}

	private int definePrimitiva(int ind) {
		//if (ind < 1) {
		//	return GL.GL_POINTS;
		//} else {
			return primitivas[index];
		//}
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {

	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getExtendedKeyCode() == KeyEvent.VK_SPACE) {
			index++;
			if (index >= primitivas.length) {
				index = 0;
			}
			this.glDrawable.display();
			System.out.println("Primitiva: " + index);
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
}
