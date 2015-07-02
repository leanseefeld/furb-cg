package br.furb.bte;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.glu.GLU;
import br.furb.bte.objetos.Arena;
import br.furb.bte.objetos.Cor;
import br.furb.bte.objetos.Moto;
import br.furb.bte.objetos.Mundo;
import com.sun.opengl.util.j2d.TextRenderer;

public class Tela extends GLCanvas implements GLEventAdapter {

    private class RenderLoop extends Thread {

	public RenderLoop() {
	    super("RenderLoop");
	}

	@Override
	public void run() {
	    while (true) {
		try {
		    if (animando || jogando || executandoPasso) {
			if (jogando) {
			    fireGameplayEvent(l -> l.beforePlay());
			    Thread.sleep(Parametros.RENDER_INTERVAL);
			    executarComportamentos();
			}
			glDrawable.display();
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

    private final Collection<GameplayListener> listeners;

    private static final long serialVersionUID = 1L;
    private static final int NEAR = 1;
    private static final int FAR = 2000;
    private static final TextRenderer TEXT_RENDERER = new TextRenderer(new Font("SansSerif", Font.BOLD, 18), true,
	    false);

    // ========== RECURSOS DO CANVAS ==========
    private int largura = 800;
    private int altura = 600;
    private GL gl;
    private GLU glu;
    private GLAutoDrawable glDrawable;
    private boolean atualizarVisualizacao;
    private boolean perspectiveMode = true;
    private final float[] posicaoLuz = { 0, 100, 0, 0,1f };
    private final float[] direcaoLuz = { 0, -1, 0 };
    private final float[] luzAmbiente = { 0.2f, 0.2f, 0.2f, 0f };

    // ========== OBJETOS GRÁFICOS ==========
    private Mundo mundo;
    private Moto moto1;
    private Moto moto2;
    private Optional<Moto> motoJogador = Optional.empty();
    private Arena arena;
    private Camera camera;

    // ========== CONTROLES DE EXECUÇÃO ==========
    private EstadoJogo estadoJogo;
    private boolean jogando;
    private boolean animando = true;
    private boolean executandoPasso;

    private final RenderLoop renderLoop;

    public Tela(GLCapabilities capabilities) {
	super(capabilities);
	setPreferredSize(new Dimension(largura, altura));
	renderLoop = new RenderLoop();
	listeners = new ArrayList<>();
	addGLEventListener(this);
    }

    public void addGameplayListener(GameplayListener l) {
	listeners.add(l);
    }

    public void removeGameplayListener(GameplayListener l) {
	listeners.remove(l);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
	glDrawable = drawable;
	gl = drawable.getGL();
	glu = new GLU();

	gl.glEnable(GL.GL_DEPTH_TEST);
	gl.glEnable(GL.GL_CULL_FACE);

	gl.glEnable(GL.GL_POINT_SMOOTH);
	gl.glEnable(GL.GL_POLYGON_SMOOTH);
	gl.glEnable(GL.GL_LINE_SMOOTH);

	gl.glHint(GL.GL_POINT_SMOOTH_HINT, GL.GL_NICEST);
	gl.glHint(GL.GL_POLYGON_SMOOTH_HINT, GL.GL_NICEST);
	gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);

	gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, posicaoLuz, 0);
	gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPOT_DIRECTION, direcaoLuz, 0);
	gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, luzAmbiente, 0);
	gl.glEnable(GL.GL_LIGHT0);
	gl.glEnable(GL.GL_LIGHTING);
	gl.glShadeModel(GL.GL_SMOOTH);

	glDrawable.setGL(new DebugGL(gl));
	gl.glClearColor(0f, 0f, 0f, 1.0f);

	mundo = new Mundo();
	camera = new Camera(this);

	addComponentListener(new ComponentAdapter() {

	    @Override
	    public void componentResized(ComponentEvent e) {
		if (arena != null) {
		    render();
		}
	    }
	});
	fireGameplayEvent(l -> l.afterInit());

	reset();
	renderLoop.start();
    }

    private void fireGameplayEvent(Consumer<GameplayListener> consumer) {
	for (GameplayListener l : listeners) {
	    consumer.accept(l);
	}
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

	animando = camera.atualizar(glu);
	animando |= mundo.renderizar(gl);
	if (Parametros.DESENHAR_SRU) {
	    desenhaSRU(gl);
	}

	TEXT_RENDERER.beginRendering(largura, altura);
	{
	    TEXT_RENDERER.setColor(1, 1, 1, 1);
	    int meioLargura = largura / 2;
	    int meioAltura = altura / 2;
	    if (estadoJogo != null && !jogando) {
		TEXT_RENDERER.draw(estadoJogo.getMensagemFimDeJogo(), meioLargura - 25, meioAltura);
		TEXT_RENDERER.draw("R para reiniciar", meioLargura - 50, meioAltura - 20);
	    } else {
		if (jogando) {
		    TEXT_RENDERER.draw("Rodando", 0, altura - 25);
		} else {
		    TEXT_RENDERER.draw("Pausado", meioLargura - 25, meioAltura);
		    TEXT_RENDERER.draw("Espaço para continuar", meioLargura - 80, meioAltura - 20);
		}
	    }
	}
	TEXT_RENDERER.endRendering();
    }

    private void alterarEstadoJogo(EstadoJogo novoEstado) {
	this.estadoJogo = novoEstado;
	render();
    }

    public void alterarExecucao(boolean executar) {
	this.jogando = executar;
	camera.setRotationLocked(executar);

	if (executar) {
	    fireGameplayEvent(l -> l.onResume());
	} else {
	    fireGameplayEvent(l -> l.onPause());
	}
	render();
    }

    public boolean isExecutando() {
	return jogando;
    }

    public void reset() {
	animando = false;

	mundo.removerTodosFilhos();
	arena = new Arena();
	moto1 = new Moto("Player 1", arena.getBBox().getMenorX() + 50, 0, new Cor(1, 0, 0), gl);

	camera.seguirMoto(moto1);
	arena.addMoto(moto1);

	moto2 = new Moto("Player 2", arena.getBBox().getMaiorX() - 50, 0, new Cor(0, 1, 0), gl);
	moto2.girar(180);
	arena.addMoto(moto2);

	mundo.addFilho(arena);
	jogando = false;
	estadoJogo = null;

	animando = true;

	fireGameplayEvent(l -> l.onReset());

	render();
    }

    public void setMotoJogador(Moto moto) {
	if (moto == null || moto != moto1 && moto != moto2) {
	    throw new IllegalArgumentException("a moto informada não faz parte do jogo");
	}
	this.motoJogador = Optional.ofNullable(moto);
    }

    public void executarComportamentos() {
	moto1.mover();
	moto2.mover();

	boolean teveColisao1 = false;
	boolean teveColisao2 = false;

	Moto motoJogadorReal = motoJogador.orElse(moto1);
	Moto inimigo = getInimigo(motoJogadorReal);

	teveColisao1 = verificaColisaoMoto(motoJogadorReal);
	teveColisao2 = verificaColisaoMoto(inimigo);
	if (teveColisao1 || teveColisao2) {
	    alterarExecucao(false);
	    camera.seguirMoto(teveColisao1 ? motoJogadorReal : inimigo);
	    if (teveColisao1 && teveColisao2) {
		alterarEstadoJogo(EstadoJogo.EMPATADO);
	    } else if (teveColisao1) {
		alterarEstadoJogo(EstadoJogo.DERROTADO);
	    } else {
		alterarEstadoJogo(EstadoJogo.VITORIOSO);
	    }

	    fireGameplayEvent(l -> l.onFinish());
	}
    }

    /**
     * Alterna o modo de visualização entre perspectiva e ortogonal.
     * 
     * @return {@code true} se passou para o modo de perspectiva; {@code false} se passou para o
     *         modo ortogonal
     */
    public boolean alternarPerspectiva() {
	perspectiveMode = !perspectiveMode;
	atualizarVisualizacao = true;
	return perspectiveMode;
    }

    private boolean verificaColisaoMoto(Moto moto) {
	boolean colidido = moto.estaColidindo(moto1.getRastro()) //
		|| moto.estaColidindo(moto2.getRastro()) //
		|| moto.estaColidindo(getInimigo(moto)) //
		|| !moto.estaSobre(arena);

	moto.setColidindo(colidido);
	return colidido;
    }

    private Moto getInimigo(Moto moto) {
	return moto == this.moto1 ? moto2 : moto1;
    }

    public void render() {
	// força que só execute quando o RenderLoop chegar no wait(), garantindo
	// que o
	// comportamento vai terminar de executar antes de renderizar novamente
	synchronized (renderLoop) {
	    glDrawable.display();
	    renderLoop.notify();
	}
    }

    public Camera getCamera() {
	return camera;
    }

    public Arena getArena() {
	return arena;
    }

    public boolean isGameOver() {
	return estadoJogo != null;
    }

    /**
     * Desenha os eixos do Sistema de ReferÃªncia Universal
     */
    public static void desenhaSRU(GL gl) {
	// gl.glEnable(GL.GL_COLOR_MATERIAL);

	gl.glDisable(GL.GL_LIGHTING);

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

	gl.glEnable(GL.GL_LIGHTING);
	// gl.glDisable(GL.GL_COLOR_MATERIAL);
    }

    public Mapa getMapa() {
	Mapa mapa = new Mapa(moto1, moto2);
	mapa.contruirMapa();
	return mapa;
    }

}
