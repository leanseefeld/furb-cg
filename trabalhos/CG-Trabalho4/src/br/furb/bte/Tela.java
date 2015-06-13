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
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;

public class Tela //
	extends GLCanvas //
	implements GLEventListener, //
	KeyListener {

    private static final int NEAR = 1;
    private static final int FAR = 2000;
    private static final Font FONT = new Font("SansSerif", Font.BOLD, 18);
    private static final long serialVersionUID = 1L;
    private static final int TAMANHO_ARENA = 500;

    private int largura = 800;
    private int altura = 600;
    private GL gl;
    public GLU glu;
    private GLAutoDrawable glDrawable;
    private Mundo mundo;
    private Moto moto1;
    private Moto moto2;
    private Arena arena;
    private Transformacao transformacaoMundo;
    private boolean executarComportamentos;
    private final RenderLoop renderLoop;
    private Estado estado;
    private final Camera camera;
    private boolean perspectiveMode = true;
    private boolean atualizarVisualizacao = false;
    private final float[] posicaoLuz = { 50, 50, 100, 0f };

    public Tela() {
	setPreferredSize(new Dimension(largura, altura));

	transformacaoMundo = new Transformacao();
	estado = Estado.PAUSADO;
	camera = new Camera(this);

	renderLoop = new RenderLoop();
	addGLEventListener(this);
	addKeyListener(this);
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

    private void definirVisualizacao() {
	gl.glMatrixMode(GL.GL_PROJECTION);
	gl.glLoadIdentity();
	if (perspectiveMode) {
	    float aspect = largura / (float) altura;
	    glu.gluPerspective(60, aspect, NEAR, FAR);
	} else {
	    gl.glOrtho(-largura, largura, -altura, altura, NEAR, FAR);
	}
	gl.glMatrixMode(GL.GL_MODELVIEW);
	gl.glLoadIdentity();
	
	gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, posicaoLuz, 0);
	gl.glEnable(GL.GL_LIGHT0);
	gl.glEnable(GL.GL_LIGHTING);
	//	gl.glShadeModel(GL.GL_SMOOTH);
	gl.glShadeModel(GL.GL_FLAT);
	//	gl.glEnable(GL.GL_COLOR_MATERIAL);
	//	gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	this.altura = height;
	this.largura = width;
	definirVisualizacao();
    }

    private void drawCube(float xS, float yS, float zS) {
	float[] red = { 1f, 0f, 0f, 1f };
	gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, red, 0);

	gl.glPushMatrix();
	{
	    gl.glScalef(xS, yS, zS);
	    new GLUT().glutSolidCube(1.0f);
	}
	gl.glPopMatrix();

    }

    @Override
    public void display(GLAutoDrawable arg0) {
	if (atualizarVisualizacao) {
	    atualizarVisualizacao = false;
	    definirVisualizacao();
	}
	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	gl.glLoadIdentity();
//	transformacaoMundo = camera.absorverTransformacao(transformacaoMundo);
//	gl.glPushMatrix();
//	{
//	    gl.glMultMatrixd(transformacaoMundo.getMatriz(), 0);


	camera.atualizaPosicaoCamera();
	    mundo.desenhar();
	    drawCube(30, 100, 30);
	    SRU();
	    //	    new GLUT().glutSolidCube(100);
	    //	    gl.glFlush();
//	}
//	gl.glPopMatrix();
//
	TextRenderer text = new TextRenderer(FONT);
	text.beginRendering(largura, altura);
	{
	    text.setColor(1, 1, 1, 1);
	    switch (estado) {
		case PAUSADO:
		    text.draw("Pausado", (largura / 2) - 25, (altura / 2));
		    text.draw("Espaço para continuar", largura / 2 - 80, (altura / 2) - 20);
		    break;
		case DERROTADO:
		    text.draw("Perdeu", (largura / 2) - 25, altura / 2);
		    text.draw("R para reiniciar", largura / 2 - 50, (altura / 2) - 20);
		    break;
		case RODANDO:
		    text.draw("Rodando", 0, altura - 25);
		    break;
		case VITORIOSO:
		    text.draw("Venceu", (largura / 2) - 25, altura / 2);
		    text.draw("R para reiniciar", (largura / 2) - 50, (altura / 2) - 20);
		    break;
		case EMPATADO:
		    text.draw("Empatou", (largura / 2) - 25, altura / 2);
		    text.draw("R para reiniciar", (largura / 2) - 50, (altura / 2) - 20);
		    break;
	    }
	}
	text.endRendering();
    }

    private void alterarEstado(Estado novoEstado) {
	this.estado = novoEstado;
	executarComportamentos = novoEstado == Estado.RODANDO;
	render();
    }

    private void reset() {
	alterarEstado(Estado.PAUSADO);
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

    private void trataControleMotos(KeyEvent e) {
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
		moto1.addAngulo(direita);
		break;
	    case KeyEvent.VK_RIGHT:
		moto2.addAngulo(direita);
		break;
	    case KeyEvent.VK_A:
		moto1.addAngulo(esquerda);
		break;
	    case KeyEvent.VK_LEFT:
		moto2.addAngulo(esquerda);
		break;
	    case KeyEvent.VK_R:
		reset();
		break;
	    default:
	}
    }

    private void trataControleJogo(KeyEvent e) {
	switch (e.getKeyCode()) {
	    case KeyEvent.VK_SPACE:
		if (estado == Estado.RODANDO) {
		    alterarEstado(Estado.PAUSADO);
		} else {
		    alterarEstado(Estado.RODANDO);
		}
		break;
	    case KeyEvent.VK_X:
		executarComportamentos();
		break;
	    default:
	}
    }

    private void trataControleCenario(KeyEvent e) {
	switch (e.getKeyCode()) {
	    case KeyEvent.VK_1:
		perspectiveMode = !perspectiveMode;
		atualizarVisualizacao = true;
		render();
		//glDrawable.display();
	    default:
	}
    }

    @Override
    public void keyPressed(KeyEvent e) {
	trataControleMotos(e);
	trataControleJogo(e);
	trataControleCenario(e);
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
		alterarEstado(Estado.EMPATADO);
	    } else if (teveColisao1) {
		alterarEstado(Estado.DERROTADO);
	    } else {
		alterarEstado(Estado.VITORIOSO);
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

    public void render() {
	// força que só execute quando o RenderLoop chegar no wait(), garantindo que 
	// o comportamento vai terminar de executar antes de renderizar novamente
	synchronized (renderLoop) {
	    glDrawable.display();
	    renderLoop.notify();
	}
    }

    private class RenderLoop extends Thread {

	@Override
	public void run() {
	    while (true) {
		try {
		    if (executarComportamentos) {
			executarComportamentos();
			glDrawable.display();
			Thread.sleep(50);
		    } else {
			synchronized (this) {
			    wait();
			}
		    }
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
    }
}
