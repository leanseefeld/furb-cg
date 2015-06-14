package br.furb.bte;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.glu.GLU;
import br.furb.bte.objetos.Arena;
import br.furb.bte.objetos.Cor;
import br.furb.bte.objetos.Moto;
import br.furb.bte.objetos.Mundo;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;

public class Tela extends GLCanvas implements GLEventAdapter {

    private class RenderLoop extends Thread {

	@Override
	public void run() {
	    while (true) {
		try {
		    if (executando) {
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

    private static final long serialVersionUID = 1L;
    private static final int NEAR = 1;
    private static final int FAR = 2000;
    private static final TextRenderer TEXT_RENDERER = new TextRenderer(new Font("SansSerif", Font.BOLD, 18));
    private static final int TAMANHO_ARENA = 500;

    // ========== RECURSOS DO CANVAS ==========
    private int largura = 800;
    private int altura = 600;
    private GL gl;
    public GLU glu;
    private GLAutoDrawable glDrawable;
    private boolean atualizarVisualizacao;
    private boolean perspectiveMode = true;
    private final float[] posicaoLuz = { 50, 50, 100, 0 };
    // ========== OBJETOS GRÁFICOS ==========
    private Mundo mundo;
    private Moto moto1;
    private Moto moto2;
    private Arena arena;
    private Camera camera;

    private ModoCamera modoCamera;

    // ========== CONTROLES DE EXECUÇÃO ==========
    private EstadoJogo estadoJogo;
    private boolean executando;

    private final RenderLoop renderLoop;

    public Tela() {
	setPreferredSize(new Dimension(largura, altura));
	modoCamera = ModoCamera.SEGUE_MOTO;
	camera = new Camera(this, modoCamera == ModoCamera.VISAO_MAPA);

	renderLoop = new RenderLoop();
	addGLEventListener(this);

	addKeyListener(new KeyAdapter() {

	    @Override
	    public void keyPressed(KeyEvent e) {
		@SuppressWarnings("unused")
		boolean reconheceu = trataControleMotos(e) || trataControleJogo(e) || trataControleCenario(e);
	    }
	});

	addComponentListener(new ComponentAdapter() {

	    @Override
	    public void componentResized(ComponentEvent e) {
		render();
	    }
	});
    }

    @Override
    public void init(GLAutoDrawable drawable) {
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

    private void configurarCanvas() {
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
	//		gl.glShadeModel(GL.GL_SMOOTH);
	gl.glShadeModel(GL.GL_FLAT);
	//		gl.glEnable(GL.GL_COLOR_MATERIAL);
	gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	this.altura = height;
	this.largura = width;
	setPreferredSize(new Dimension(largura, altura));
	configurarCanvas();
    }

    @Override
    public void display(GLAutoDrawable arg0) {
	if (atualizarVisualizacao) {
	    atualizarVisualizacao = false;
	    configurarCanvas();
	}
	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	gl.glLoadIdentity();
	//	transformacaoMundo = camera.absorverTransformacao(transformacaoMundo);
	//	gl.glPushMatrix();
	//	{
	//	    gl.glMultMatrixd(transformacaoMundo.getMatriz(), 0);

	if (modoCamera == ModoCamera.SEGUE_MOTO) {
	    if (this.moto1 != null) {
		camera.setPontoObservacao(this.moto1.getBBoxTransformada().getCentro());
		camera.setAngulo(this.moto1.getAngulo() + 180);
	    }
	}
	camera.atualizaPosicaoCamera();
	mundo.desenhar();
	drawCube(30, 100, 30);
	desenhaSRU(gl);
	//	    new GLUT().glutSolidCube(100);
	//	    gl.glFlush();
	//	}
	//	gl.glPopMatrix();
	//
	TEXT_RENDERER.beginRendering(largura, altura);
	{
	    TEXT_RENDERER.setColor(1, 1, 1, 1);
	    int meioLargura = largura / 2;
	    int meioAltura = altura / 2;
	    if (estadoJogo != null) {
		TEXT_RENDERER.draw(estadoJogo.getMensagemFimDeJogo(), meioLargura - 25, meioAltura);
		TEXT_RENDERER.draw("R para reiniciar", meioLargura - 50, meioAltura - 20);
	    } else {
		if (executando) {
		    TEXT_RENDERER.draw("Rodando", 0, altura - 25);
		} else {
		    TEXT_RENDERER.draw("Pausado", meioLargura - 25, meioAltura);
		    TEXT_RENDERER.draw("Espaço para continuar", meioLargura - 80, meioAltura - 20);
		}
	    }
	}
	TEXT_RENDERER.endRendering();
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

    private void alterarEstadoJogo(EstadoJogo novoEstado) {
	this.estadoJogo = novoEstado;
	render();
    }

    private void alterarExecucao(boolean executar) {
	this.executando = executar;
	render();
    }

    private void reset() {
	alterarExecucao(false);
	alterarEstadoJogo(null);
	mundo.removerTodosFilhos();
	arena = new Arena(gl, TAMANHO_ARENA, TAMANHO_ARENA);
	moto1 = new Moto(gl, arena.getBBox().getMenorX() + 50, 0, Moto.DIREITA, new Cor(1, 0, 0));
	arena.addFilho(moto1);
	arena.addFilho(moto1.getRastro());

	moto2 = new Moto(gl, arena.getBBox().getMaiorX() - 50, 0, Moto.ESQUERDA, new Cor(0, 1, 0));
	arena.addFilho(moto2);
	arena.addFilho(moto2.getRastro());

	mundo.addFilho(arena);
    }

    private boolean trataControleMotos(KeyEvent e) {
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
		return false;
	}
	return true;
    }

    private boolean trataControleJogo(KeyEvent e) {
	switch (e.getKeyCode()) {
	    case KeyEvent.VK_SPACE:
		alterarExecucao(!executando);
		break;
	    case KeyEvent.VK_X:
		executarComportamentos();
		break;
	    default:
		return false;
	}
	return true;
    }

    private boolean trataControleCenario(KeyEvent e) {
	if (e.getKeyCode() == KeyEvent.VK_1) {
	    perspectiveMode = !perspectiveMode;
	    atualizarVisualizacao = true;
	    render();
	    return true;
	}
	return false;
    }

    private void executarComportamentos() {
	this.moto1.mover();
	this.moto2.mover();

	boolean teveColisao1 = false;
	boolean teveColisao2 = false;
	teveColisao1 = verificaColisaoMoto(moto1);
	teveColisao2 = verificaColisaoMoto(moto2);
	if (teveColisao1 || teveColisao2) {
	    alterarExecucao(false);
	    if (teveColisao1 && teveColisao2) {
		alterarEstadoJogo(EstadoJogo.EMPATADO);
	    } else if (teveColisao1) {
		alterarEstadoJogo(EstadoJogo.DERROTADO);
	    } else {
		alterarEstadoJogo(EstadoJogo.VITORIOSO);
	    }
	}
    }

    private boolean verificaColisaoMoto(Moto moto) {
	boolean colidido = moto.estaColidindo(moto1.getRastro()) //
		|| moto.estaColidindo(moto2.getRastro()) //
		|| moto.estaColidindo(getInimigo(moto)) //
		|| !moto.estaSobre(arena);

	moto.setColidido(colidido);
	return colidido;
    }

    private Moto getInimigo(Moto moto) {
	return moto == this.moto1 ? moto2 : moto1;
    }

    public void render() {
	// força que só execute quando o RenderLoop chegar no wait(), garantindo que 
	// o comportamento vai terminar de executar antes de renderizar novamente
	synchronized (renderLoop) {
	    glDrawable.display();
	    renderLoop.notify();
	}
    }

    /**
     * Desenha os eixos do Sistema de Referência Universal
     */
    public static void desenhaSRU(GL gl) {
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
