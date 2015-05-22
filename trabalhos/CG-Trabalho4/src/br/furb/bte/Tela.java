package br.furb.bte;

import static java.lang.Math.abs;
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

public class Tela extends GLCanvas implements GLEventListener, MouseMotionListener, MouseListener, KeyListener {

    private static final long serialVersionUID = 1L;

    private float ortho2D_minX = -400.0f, ortho2D_maxX = 400.0f, ortho2D_minY = -400.0f, ortho2D_maxY = 400.0f;
    private int ortho2D_w = (int) (abs(ortho2D_maxX) - ortho2D_minX);
    private int ortho2D_h = (int) (abs(ortho2D_maxY) - ortho2D_minY);
    private GL gl;
    private GLU glu;
    private GLAutoDrawable glDrawable;
    private Mundo mundo;

    public Tela() {
	addGLEventListener(this);
	addMouseMotionListener(this);
	addMouseListener(this);
	addKeyListener(this);
	setPreferredSize(new Dimension(ortho2D_w, ortho2D_h));
    }

    @Override
    public void display(GLAutoDrawable arg0) {
	gl.glClear(GL.GL_COLOR_BUFFER_BIT);
	gl.glMatrixMode(GL.GL_PROJECTION);
	gl.glLoadIdentity();
	glu.gluOrtho2D(ortho2D_minX, ortho2D_maxX, ortho2D_minY, ortho2D_maxY);
	gl.glMatrixMode(GL.GL_MODELVIEW);
	gl.glLoadIdentity();

	SRU();

	mundo.desenhar();

    }

    /**
     * Desenha os eixos da Tela
     */
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

    @Override
    public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {

    }

    @Override
    public void init(GLAutoDrawable drawable) {
	System.out.println(" --- init ---");
	glDrawable = drawable;
	gl = drawable.getGL();
	glu = new GLU();
	glDrawable.setGL(new DebugGL(gl));
	System.out.println("Espaço de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
	gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	mundo = new Mundo(gl, (int) ortho2D_maxX, (int) ortho2D_minX, (int) ortho2D_maxY, (int) ortho2D_minY);

	Arena arena = new Arena(gl);
	arena.cor = new Cor(1, 1, 0);

	mundo.addFilho(arena);
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
	boolean reconheceu = false;
	// TODO: reconhecer setas do teclado e controlar a motoca
	if (reconheceu) {
	    glDrawable.display();
	}
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
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

}
