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
    private Moto moto1;
    private Moto moto2;

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

	mundo.desenhar();

	SRU();
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
	this.moto1 = new Moto(gl, new Ponto(-100, 0), Moto.DIREITA, new Cor(1, 0, 0));
	arena.addFilho(moto1);
	arena.addFilho(this.moto1.getRastro());

	this.moto2 = new Moto(gl, new Ponto(+100, 0), Moto.ESQUERDA, new Cor(0, 1, 0));
	arena.addFilho(moto2);
	arena.addFilho(this.moto2.getRastro());

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
	boolean reconheceu = true;
	switch (e.getKeyCode()) {
	    case KeyEvent.VK_W:
		this.moto2.setAngulo(Moto.CIMA);
		break;
	    case KeyEvent.VK_UP:
		this.moto1.setAngulo(Moto.CIMA);
		break;
	    case KeyEvent.VK_D:
		this.moto2.setAngulo(Moto.DIREITA);
		break;
	    case KeyEvent.VK_RIGHT:
		this.moto1.setAngulo(Moto.DIREITA);
		break;
	    case KeyEvent.VK_S:
		this.moto2.setAngulo(Moto.BAIXO);
		break;
	    case KeyEvent.VK_DOWN:
		this.moto1.setAngulo(Moto.BAIXO);
		break;
	    case KeyEvent.VK_A:
		this.moto2.setAngulo(Moto.ESQUERDA);
		break;
	    case KeyEvent.VK_LEFT:
		this.moto1.setAngulo(Moto.ESQUERDA);
		break;
	    case KeyEvent.VK_SPACE:
		this.executarComportamentos();
		break;
	    default:
		reconheceu = false;
	}
	if (reconheceu) {
	    glDrawable.display();
	}
    }

    private void executarComportamentos() {
	this.moto1.mover();
	this.moto2.mover();

	boolean teveColisao = false;
	teveColisao |= this.moto1.verificaColisao(this.moto1.getRastro());
	teveColisao |= this.moto1.verificaColisao(this.moto2.getRastro());
	teveColisao |= this.moto2.verificaColisao(this.moto1.getRastro());
	teveColisao |= this.moto2.verificaColisao(this.moto2.getRastro());
	if (teveColisao) {
	    System.out.println("COLISÃO");
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
}
