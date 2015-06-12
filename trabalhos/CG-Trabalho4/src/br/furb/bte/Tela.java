package br.furb.bte;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import com.sun.opengl.util.j2d.TextRenderer;

public class Tela //
	extends GLCanvas //
	implements GLEventListener, //
	KeyListener {

    private static final long serialVersionUID = 1L;
    private static final int TAMANHO_ARENA = 500;

    private int largura = 800;
    private int altura = 800;
    private GL gl;
    private GLU glu;
    private GLAutoDrawable glDrawable;
    private Ponto olho;
    private Ponto para;
    private Mundo mundo;
    private Moto moto1;
    private Moto moto2;
    private Arena arena;
    private Transformacao transformacaoMundo;
    private boolean executarComportamentos;
    private final RenderLoop renderLoop;
    private Estado estado;
    private final Camera camera;

    public Tela() {
	addGLEventListener(this);
	addKeyListener(this);
	setPreferredSize(new Dimension(largura, altura));

	olho = new Ponto(500, 500, 1000);
	para = new Ponto(0, 0, 0);
	transformacaoMundo = new Transformacao();
	estado = Estado.Pausado;

	camera = new Camera(this);
	renderLoop = new RenderLoop();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
	System.out.println(" --- init ---");
	glDrawable = drawable;
	gl = drawable.getGL();

	gl.glEnable(GL.GL_DEPTH_TEST);
	gl.glEnable(GL.GL_CULL_FACE);

	glu = new GLU();
	glDrawable.setGL(new DebugGL(gl));
	gl.glClearColor(0f, 0f, 0f, 1.0f);

	mundo = new Mundo(gl);

	reset();
	renderLoop.start();
    }

    @Override
    public void display(GLAutoDrawable arg0) {
	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

	transformacaoMundo = camera.absorverTransformacao(transformacaoMundo);
	gl.glPushMatrix();
	{
	    gl.glMultMatrixd(transformacaoMundo.getMatriz(), 0);

	    mundo.desenhar();
	    SRU();
	    //	    new GLUT().glutSolidCube(100);
	    //	    gl.glFlush();
	}
	gl.glPopMatrix();

	TextRenderer text = new TextRenderer(new Font("SansSerif", Font.BOLD, 18));
	text.beginRendering(largura, altura);
	{
	    text.setColor(1, 1, 1, 1);
	    switch (estado) {
		case Pausado:
		    text.draw("Pausado", (largura / 2) - 25, (altura / 2));
		    text.draw("Espaço para continuar", largura / 2 - 80, (altura / 2) - 20);
		    break;
		case Perdeu:
		    text.draw("Perdeu", (largura / 2) - 25, altura);
		    text.draw("R para reiniciar", largura / 2 - 50, (altura / 2) - 20);
		    break;
		case Rodando:
		    text.draw("Rodando", 0, altura - 25);
		    break;
		case Venceu:
		    text.draw("Venceu", (largura / 2) - 25, altura);
		    text.draw("R para reiniciar", (largura / 2) - 50, (altura / 2) - 20);
		    break;
		case Empatou:
		    text.draw("Empatou", (largura / 2) - 25, altura);
		    text.draw("R para reiniciar", (largura / 2) - 50, (altura / 2) - 20);
		    break;
	    }
	}
	text.endRendering();
    }

    @Override
    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
	gl.glViewport(0, 0, arg0.getWidth(), arg0.getHeight());
	gl.glMatrixMode(GL.GL_PROJECTION);
	gl.glLoadIdentity();
	float h = (float) arg0.getHeight() / (float) arg0.getWidth();
	glu.gluPerspective(60, h, 1, 2000);
	gl.glMatrixMode(GL.GL_MODELVIEW);
	gl.glLoadIdentity();
	glu.gluLookAt(olho.X, olho.Y, olho.Z, para.X, para.Y, para.Z, 0, 1, 0);
    }

    private void reset() {
	this.estado = Estado.Pausado;
	this.executarComportamentos = false;
	this.mundo.removerTodosFilhos();
	arena = new Arena(gl, TAMANHO_ARENA, TAMANHO_ARENA);
	moto1 = new Moto(gl, this.arena.bbox.getMenorX() + 50, 0, Moto.DIREITA, new Cor(1, 0, 0));
	arena.addFilho(moto1);
	arena.addFilho(moto1.getRastro());

	moto2 = new Moto(gl, this.arena.bbox.getMaiorX() - 50, 0, Moto.ESQUERDA, new Cor(0, 1, 0));
	arena.addFilho(moto2);
	arena.addFilho(moto2.getRastro());

	mundo.addFilho(arena);
    }

    @Override
    public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
	boolean reconheceu = true;
	int direita = 90;
	int esquerda = -90;
	switch (e.getKeyCode()) {
	//	    case KeyEvent.VK_W:
	//		this.moto1.setAngulo(Moto.CIMA);
	//		break;
	//	    case KeyEvent.VK_UP:
	//		this.moto2.setAngulo(Moto.CIMA);
	//		break;
	//	    case KeyEvent.VK_D:
	//		this.moto1.setAngulo(Moto.DIREITA);
	//		break;
	//	    case KeyEvent.VK_RIGHT:
	//		this.moto2.setAngulo(Moto.DIREITA);
	//		break;
	//	    case KeyEvent.VK_S:
	//		this.moto1.setAngulo(Moto.BAIXO);
	//		break;
	//	    case KeyEvent.VK_DOWN:
	//		this.moto2.setAngulo(Moto.BAIXO);
	//		break;
	//	    case KeyEvent.VK_A:
	//		this.moto1.setAngulo(Moto.ESQUERDA);
	//		break;
	//	    case KeyEvent.VK_LEFT:
	//		this.moto2.setAngulo(Moto.ESQUERDA);
	//		break;

	    case KeyEvent.VK_D:
		this.moto1.addAngulo(direita);
		break;
	    case KeyEvent.VK_RIGHT:
		this.moto2.addAngulo(direita);
		break;
	    case KeyEvent.VK_A:
		this.moto1.addAngulo(esquerda);
		break;
	    case KeyEvent.VK_LEFT:
		this.moto2.addAngulo(esquerda);
		break;
	    case KeyEvent.VK_R:
		this.reset();
		break;
	    case KeyEvent.VK_SPACE:
		if (executarComportamentos) {
		    executarComportamentos = false;
		    estado = Estado.Pausado;
		} else {
		    executarComportamentos = true;
		    estado = Estado.Rodando;
		}
		reconheceu = false;
		break;
	    case KeyEvent.VK_X:
		executarComportamentos();
		break;
	    default:
		reconheceu = false;
	}
	//	if (reconheceu) {
	//	    glDrawable.display();
	//	}
    }

    private void executarComportamentos() {
	this.moto1.mover();
	this.moto2.mover();

	boolean teveColisao1 = false;
	boolean teveColisao2 = false;
	teveColisao1 = verificaColisaoMoto(moto1);
	teveColisao2 = verificaColisaoMoto(moto2);
	if (teveColisao1 || teveColisao2) {
	    this.executarComportamentos = false;
	    if (teveColisao1 && teveColisao2) {
		this.estado = Estado.Empatou;
	    } else if (teveColisao1) {
		this.estado = Estado.Perdeu;
	    } else {
		this.estado = Estado.Venceu;
	    }
	}
    }

    private boolean verificaColisaoMoto(Moto moto) {
	boolean teveColisao = false;
	teveColisao |= moto.estaColidindo(moto1.getRastro());
	teveColisao |= moto.estaColidindo(moto2.getRastro());
	teveColisao |= moto.estaColidindo(getEnemy(moto));
	teveColisao |= !moto.estaSobre(arena);

	if (teveColisao)
	    moto.setColisao();
	else
	    moto.setNormal();
	return teveColisao;
    }

    private Moto getEnemy(Moto moto) {
	if (moto == this.moto1)
	    return moto2;
	else
	    return moto1;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

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

    private class RenderLoop extends Thread {

	@Override
	public void run() {
	    while (true) {
		try {
		    if (executarComportamentos) {
			executarComportamentos();
		    }
		    glDrawable.display();
		    Thread.sleep(50);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
    }
}
