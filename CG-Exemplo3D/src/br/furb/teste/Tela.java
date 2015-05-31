package br.furb.teste;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import com.sun.opengl.util.GLUT;

public class Tela extends GLCanvas implements GLEventListener, MouseMotionListener, MouseListener, KeyListener {

    private static final long serialVersionUID = 1L;

    private int largura = 400;
    private int altura = 400;
    private GL gl;
    private GLU glu;
    private GLAutoDrawable glDrawable;
    private GLUT glut;
    private float rotacaoCubo;
    private Ponto olho;
    private Ponto para;

    public Tela() {
	addGLEventListener(this);
	addMouseMotionListener(this);
	addMouseListener(this);
	addKeyListener(this);
	setPreferredSize(new Dimension(largura, altura));
    }

    @Override
    public void init(GLAutoDrawable drawable) {
	System.out.println(" --- init ---");
	glDrawable = drawable;
	gl = drawable.getGL();
	glut = new GLUT();

	gl.glEnable(GL.GL_DEPTH_TEST);
	gl.glEnable(GL.GL_CULL_FACE);

	glu = new GLU();
	glDrawable.setGL(new DebugGL(gl));
	gl.glClearColor(0f, 0f, 0f, 1.0f);

	this.olho = new Ponto(100, 100, 500);
	this.para = new Ponto(0, 0, 0);
    }

    @Override
    public void display(GLAutoDrawable arg0) {
	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	SRU();
	rotacaoCubo += 0.16f;
	desenharCubo(rotacaoCubo);
	gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
	gl.glMatrixMode(GL.GL_PROJECTION);
	gl.glLoadIdentity();
	float h = (float) arg0.getHeight() / (float) arg0.getWidth();
	glu.gluPerspective(60, h, 1, 1000);
	gl.glMatrixMode(GL.GL_MODELVIEW);
	gl.glLoadIdentity();
	glu.gluLookAt(olho.X, olho.Y, olho.Z, para.X, para.Y, para.Z, 0, 1, 0);
    }

    @Override
    public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {

    }

    private void desenharCubo(float rotacao) {
	gl.glColor3d(1, 1, 1);
	gl.glPushMatrix();
	{
	    //	    gl.glTranslatef(40f, 40f, 40f);
	    //	    gl.glRotated(rotacao, 1, 1, 1);
	    gl.glScaled(100, 100, 100);
	    glut.glutWireCube(1);
	}
	gl.glPopMatrix();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
	// TODO: talvez movimentar proporcionalmente um pouco a câmera...
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

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

    /**
     * Desenha os eixos da Tela
     */
    public void SRU() {
	gl.glLineWidth(1.0f);

	// eixo x
	gl.glColor3f(1.0f, 0.0f, 0.0f);
	gl.glBegin(GL.GL_LINES);
	{
	    gl.glVertex3f(0, 0f, 0f);
	    gl.glVertex3f(200f, 0f, 0f);
	}
	gl.glEnd();

	// eixo y
	gl.glColor3f(0.0f, 1.0f, 0.0f);
	gl.glBegin(GL.GL_LINES);
	{
	    gl.glVertex3f(0f, 0f, 0f);
	    gl.glVertex3f(0f, 200f, 0f);
	}
	gl.glEnd();

	// eixo z
	gl.glColor3f(0.0f, 0.0f, 1.0f);
	gl.glBegin(GL.GL_LINES);
	{
	    gl.glVertex3f(0f, 0f, 0f);
	    gl.glVertex3f(0f, 0f, 200f);
	}
	gl.glEnd();
    }
}
