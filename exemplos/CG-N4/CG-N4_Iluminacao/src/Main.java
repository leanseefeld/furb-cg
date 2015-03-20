/**
 * Objetivo: usar as transformações geométricas: translação, escala e rotação.
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
//import javax.media.opengl.glu.GLUquadric;

import com.sun.opengl.util.GLUT;

public class Main implements GLEventListener, KeyListener {
	private GL gl;
	private GLU glu;
	private GLUT glut;
	private GLAutoDrawable glDrawable;
	private double xEye, yEye, zEye;
	private double xCenter, yCenter, zCenter;
	private final double xUp = 0.0f, yUp = 1.0f, zUp = 0.0f;
	
    private float corRed[] = { 1.0f, 0.0f, 0.0f, 1.0f };
//    private float corGreen[] = { 0.0f, 1.0f, 0.0f, 1.0f };
//    private float corBlue[] = { 0.0f, 0.0f, 1.0f, 1.0f };
//    private float corWhite[] = { 1.0f, 1.0f, 1.0f, 1.0f };
//    private float corBlack[] = { 0.0f, 0.0f, 0.0f, 1.0f };

	//private static final int SIZE = 2;

	public void init(GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glut = new GLUT();
		glDrawable.setGL(new DebugGL(gl));

		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		xEye = 20.0f;
		yEye = 20.0f;
		zEye = 20.0f;
		xCenter = 0.0f;
		yCenter = 0.0f;
		zCenter = 0.0f;
		
	    float posLight[] = { 5.0f, 5.0f, 10.0f, 0.0f };
	    gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, posLight, 0);
	    gl.glEnable(GL.GL_LIGHT0);

	    gl.glEnable(GL.GL_CULL_FACE);
	    gl.glEnable(GL.GL_DEPTH_TEST);

	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	    gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(60, 1, 0.1, 100);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
		glu.gluLookAt(xEye, yEye, zEye, xCenter, yCenter, zCenter, xUp, yUp, zUp);
		Debug();
	}

	public void display(GLAutoDrawable drawable) {
		// gl = drawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
		glu.gluLookAt(xEye, yEye, zEye, xCenter, yCenter, zCenter, xUp, yUp, zUp);

		drawAxis();
		drawCube(2.0f,2.0f,2.0f);
		gl.glPushMatrix();
		gl.glTranslated(0.0f, 0.0f, 1.5f);
			drawCube(1.0f,1.0f,1.0f);
		gl.glPopMatrix();
//		drawCylinder(gl, 1.0f, 1.0f, 1.0f); // cilindro branco
		
		gl.glFlush();
	}
	
	private void drawCube(float xS, float yS, float zS) {
	    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, corRed, 0);
	    gl.glEnable(GL.GL_LIGHTING);

		gl.glPushMatrix();
			gl.glScalef(xS,yS,zS);
			glut.glutSolidCube(1.0f);
		gl.glPopMatrix();
		
		gl.glDisable(GL.GL_LIGHTING);
	}

//	private void drawCylinder(GL gl, float red, float green, float blue) {
//		gl.glColor3f(red, green, blue);
//
//		GLUquadric quad;
//		quad = glu.gluNewQuadric();
//		glu.gluQuadricDrawStyle(quad, GLU.GLU_FILL);
//		glu.gluQuadricOrientation(quad, GLU.GLU_OUTSIDE);
//		glu.gluQuadricNormals(quad, GLU.GLU_SMOOTH);
//		glu.gluQuadricTexture(quad, true);
//		glu.gluCylinder(quad, 0.25f, 0.25f, 1.0f, 60, 30);
//		glu.gluDeleteQuadric(quad);
//	}

	public void drawAxis() {
		// eixo X - Red
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(10.0f, 0.0f, 0.0f);
		gl.glEnd();
		// eixo Y - Green
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, 10.0f, 0.0f);
		gl.glEnd();
		// eixo Z - Blue
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, 0.0f, 10.0f);
		gl.glEnd();
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {

		case KeyEvent.VK_ESCAPE:
			System.exit(1);
		break;
		case KeyEvent.VK_1:
			xEye = 20.0f;
			yEye = 20.0f;
			zEye = 20.0f;
		break;
		case KeyEvent.VK_2:
			xEye = 20.0f;
			yEye = 0.0f;
			zEye = 0.0f;
			break;
		}
		glDrawable.display();	
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void Debug() {
	}

}
