package br.furb.teste;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import com.sun.opengl.util.GLUT;

public class Tela //
	extends GLCanvas //
	implements GLEventListener, MouseMotionListener, //
	MouseListener, KeyListener, MouseWheelListener {

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
    private Ponto mouse;
    private Transformacao transformacaoMundo;

    public Tela() {
	addGLEventListener(this);
	addMouseMotionListener(this);
	addMouseListener(this);
	addKeyListener(this);
	addMouseWheelListener(this);
	setPreferredSize(new Dimension(largura, altura));

	olho = new Ponto(100, 100, 500);
	para = new Ponto(0, 0, 0);
	mouse = new Ponto(0, 0, 0);
	transformacaoMundo = new Transformacao();
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
    }

    @Override
    public void display(GLAutoDrawable arg0) {
	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	gl.glLoadIdentity();
	glu.gluLookAt(olho.X, olho.Y, olho.Z, para.X, para.Y, para.Z, 0, 1, 0);

	gl.glPushMatrix();
	{
	    gl.glMultMatrixd(this.transformacaoMundo.getMatriz(), 0);
	    SRU();
	    rotacaoCubo += 0.16f;
	    desenharCubo(rotacaoCubo);
	    desenharMoto();
	    gl.glFlush();

	}
	gl.glPopMatrix();

    }

    @Override
    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
	gl.glViewport(0, 0, arg0.getWidth(), arg0.getHeight());
	gl.glMatrixMode(GL.GL_PROJECTION);
	gl.glLoadIdentity();
	float h = (float) arg0.getHeight() / (float) arg0.getWidth();
	glu.gluPerspective(60, h, 1, 1000);
	gl.glMatrixMode(GL.GL_MODELVIEW);
	gl.glLoadIdentity();
    }

    private void desenharCubo(float rotacao) {
	gl.glPushMatrix();
	{
	    //	    gl.glLoadIdentity();
	    gl.glColor3d(1, 1, 1);
	    //	    gl.glTranslatef(40f, 40f, 40f);
	    //	    gl.glRotated(rotacao, 1, 1, 1);
	    gl.glScaled(100, 100, 100);
	    glut.glutWireCube(1);
	}
	gl.glPopMatrix();
    }

    private void desenharMoto() {
	List<Ponto> pontos = new ArrayList<Ponto>();
	//Cima
	pontos.add(new Ponto(+170, +40, +40));
	pontos.add(new Ponto(+170, +40, -40));
	pontos.add(new Ponto(-190, +40, -60));
	pontos.add(new Ponto(-190, +40, +60));

	//Frente
	pontos.add(new Ponto(-190, +40, -60));
	pontos.add(new Ponto(-190, 0, -60));
	pontos.add(new Ponto(-190, 0, +60));
	pontos.add(new Ponto(-190, +40, +60));

	//Traz
	pontos.add(new Ponto(+170, +40, +40));
	pontos.add(new Ponto(+170, 0, +40));
	pontos.add(new Ponto(+170, 0, -40));
	pontos.add(new Ponto(+170, +40, -40));

	//Lateral Esquerda
	pontos.add(new Ponto(+170, +40, +40));
	pontos.add(new Ponto(-190, +40, +60));
	pontos.add(new Ponto(-190, 0, +60));
	pontos.add(new Ponto(+170, 0, +40));

	//Lateral Direita
	pontos.add(new Ponto(+170, 0, -40));
	pontos.add(new Ponto(-190, 0, -60));
	pontos.add(new Ponto(-190, +40, -60));
	pontos.add(new Ponto(+170, +40, -40));

	//Baixo
	pontos.add(new Ponto(-190, +00, +60));
	pontos.add(new Ponto(-190, +00, -60));
	pontos.add(new Ponto(+170, +00, -40));
	pontos.add(new Ponto(+170, +00, +40));

	gl.glColor3f(1.0f, 0.0f, 0.0f);
	gl.glBegin(GL.GL_QUADS);
	{
	    for (Ponto ponto : pontos) {
		gl.glVertex3d(ponto.X, ponto.Y, ponto.Z);
	    }
	}
	gl.glEnd();

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
	mouse.X = e.getX();
	mouse.Y = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
	double qtdX = mouse.X - e.getX();
	double qtdY = mouse.Y - e.getY();
	
	qtdX = -qtdX/100;
	qtdY = qtdY/100;
	
	mouse.X = e.getX();
	mouse.Y = e.getY();
	// TODO: talvez movimentar proporcionalmente um pouco a câmera...
	
	
	transformacaoMundo = transformacaoMundo.transformMatrix(new Transformacao().atribuirRotacaoY(qtdX));
	transformacaoMundo = transformacaoMundo.transformMatrix(new Transformacao().atribuirRotacaoX(qtdY));
	glDrawable.display();
	
	
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

    @Override
    public void mouseWheelMoved(MouseWheelEvent arg0) {
	this.olho.X += arg0.getWheelRotation() * this.olho.X / 10;
	this.olho.Y += arg0.getWheelRotation() * this.olho.Y / 10;
	this.olho.Z += arg0.getWheelRotation() * this.olho.Z / 10;
	glDrawable.display();
    }
}
