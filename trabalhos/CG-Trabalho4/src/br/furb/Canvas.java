package br.furb;

import static java.lang.Math.abs;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.SwingUtilities;

public class Canvas implements GLEventListener, MouseMotionListener,
		MouseListener, KeyListener {
	private float ortho2D_minX = -400.0f, ortho2D_maxX = 400.0f,
			ortho2D_minY = -400.0f, ortho2D_maxY = 400.0f;
	private int ortho2D_w = (int) (abs(ortho2D_maxX) - ortho2D_minX);
	private int ortho2D_h = (int) (abs(ortho2D_maxY) - ortho2D_minY);
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	private Mundo mundo;
	private Poligono novoObjeto;
	private Ponto pontoAnterior;
	private Ponto pontoPosterior;
	private Ponto pontoPosicaoMouse;

	public Canvas(GLCanvas canvas) {
		canvas.addGLEventListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseListener(this);
		canvas.addKeyListener(this);
		canvas.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				ortho2D_w = e.getComponent().getWidth();
				ortho2D_h = e.getComponent().getHeight();
				ortho2D_minX = ortho2D_w / -2;
				ortho2D_maxX = ortho2D_w / 2;
				ortho2D_minY = ortho2D_h / -2;
				ortho2D_maxY = ortho2D_h / 2;
			}
		});
		canvas.setPreferredSize(new Dimension(ortho2D_w, ortho2D_h));
		pontoPosicaoMouse = new Ponto(0, 0);
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

		this.desenhaRastro();
	}

	/**
	 * Desenha o rastro do mouse ao criar um objeto
	 */
	private void desenhaRastro() {
		if (Mundo.EstadoAtual == Estado.Criacao) {
			if (pontoAnterior != null && pontoPosterior != null) {
				gl.glLineWidth(2);
				gl.glColor3f(1f, 0f, 0f);
				gl.glBegin(GL.GL_LINES);
				{
					gl.glVertex2d(pontoAnterior.X, pontoAnterior.Y);
					gl.glVertex2d(pontoPosterior.X, pontoPosterior.Y);
				}
				gl.glEnd();
			}
		}
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
		System.out.println("Espaço de desenho com tamanho: "
				+ drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		mundo = new Mundo(gl, (int) ortho2D_maxX, (int) ortho2D_minX,
				(int) ortho2D_maxY, (int) ortho2D_minY);

		Poligono po = new Poligono(gl);
		po.cor = new Cor(1, 1, 0);
		mundo.addFilho(po);

		po.addPonto(new Ponto(30, 200));
		po.addPonto(new Ponto(-30, 200));
		po.addPonto(new Ponto(-30, -200));
		po.addPonto(new Ponto(30, -200));
		po.setPrimitiva(GL.GL_LINE_LOOP);

		po.concluir();

		Mundo.EstadoAtual = Estado.Visualizacao;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Ponto pontoSelecionado = new Ponto(e.getX(), e.getY());
		ajustarPonto(pontoSelecionado);
		switch (Mundo.EstadoAtual) {
		case Visualizacao:
			mundo.selecionarObjeto(pontoSelecionado);
			break;
		case Criacao:
			if (SwingUtilities.isRightMouseButton(e)) {
				this.concluirCriacao();
			} else {
				if (!this.novoObjeto.temPontos()) {
					this.pontoAnterior = null;
					this.pontoPosterior = pontoSelecionado.clone();
				}

				if (e.isControlDown()) {
					movePontoParaPontoMaisProximo(this.novoObjeto,
							this.pontoPosterior, true);
				}

				this.pontoAnterior = this.pontoPosterior;
				this.pontoPosterior = pontoSelecionado.clone();

				this.novoObjeto.addPonto(this.pontoAnterior);

			}
			break;
		case Edicao:
			if (Mundo.objetoSelecionado instanceof Poligono)
				((Poligono) Mundo.objetoSelecionado)
						.selecionarPonto(pontoSelecionado);
			break;
		}

		glDrawable.display();
	}

	/**
	 * Move o ponto para o ponto mais proximo do objeto que está editado
	 * 
	 * @param ponto
	 *            ponto que terá suas cordenadas alteradas
	 * @param transformaParaPosicaoTela
	 *            O ponto retornado está com as coordenadas originais do objeto, <br>
	 *            ou seja, está sem as trasnformações.<br>
	 *            Ao passar True o ponto é trasnformado para exibir no local
	 *            correto da tela
	 */
	private void movePontoParaPontoMaisProximo(ObjetoGrafico objetoEditado,
			Ponto ponto, boolean transformaParaPosicaoTela) {
		if (objetoEditado instanceof Poligono) {

			Poligono pol = ((Poligono) objetoEditado);
			Ponto pontoMaisProximo = pol.getPontoMaisProximo(ponto);
			if (pontoMaisProximo != null) {
				if (transformaParaPosicaoTela) {
					Transformacao trans = pol.getTransformacaoTotal();
					pontoMaisProximo = trans.transformPoint(pontoMaisProximo);
				}

				ponto.X = pontoMaisProximo.X;
				ponto.Y = pontoMaisProximo.Y;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		if (Mundo.EstadoAtual == Estado.Edicao
				|| Mundo.EstadoAtual == Estado.Visualizacao) {
			if (Mundo.pontoSelecionado != null) {
				// Pega o ponto clicado
				Mundo.pontoSelecionado.X = e.getX();
				Mundo.pontoSelecionado.Y = e.getY();

				// Converte para o tamanho da tela
				ajustarPonto(Mundo.pontoSelecionado);
				
				if (!e.isControlDown()) {
					// Coloca o ponto na posição de origem
					Ponto pontoPosicaoOrigem = Mundo.objetoSelecionado
							.inverseTransformRecursive(Mundo.pontoSelecionado);
					Mundo.pontoSelecionado.X = pontoPosicaoOrigem.X;
					Mundo.pontoSelecionado.Y = pontoPosicaoOrigem.Y;
				} else {
					movePontoParaPontoMaisProximo(Mundo.getObjetoSelecionado(),
							Mundo.pontoSelecionado, false);
				}

				glDrawable.display();
			} else if (Mundo.objetoSelecionado != null
					&& Mundo.objetoSelecionado instanceof Poligono) {
				Ponto novaPosicao = new Ponto(e.getX(), e.getY());
				ajustarPonto(novaPosicao);

				Transformacao trans = new Transformacao();
				trans.atribuirTranslacao(//
						novaPosicao.X - pontoPosicaoMouse.X, //
						novaPosicao.Y - pontoPosicaoMouse.Y);
				Mundo.objetoSelecionado.addTransformacao(trans);
				this.pontoPosicaoMouse = novaPosicao;
				glDrawable.display();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.pontoPosicaoMouse.X = e.getX();
		this.pontoPosicaoMouse.Y = e.getY();
		ajustarPonto(this.pontoPosicaoMouse);

		if (this.pontoPosterior != null) {
			this.pontoPosterior.X = this.pontoPosicaoMouse.X;
			this.pontoPosterior.Y = this.pontoPosicaoMouse.Y;
		}

		if (Mundo.EstadoAtual == Estado.Criacao && e.isControlDown()) {
			movePontoParaPontoMaisProximo(this.novoObjeto, this.pontoPosterior,
					true);
		}

		if (glDrawable != null) {
			glDrawable.display();
		}
	}

	/**
	 * Ajusta o ponto para a posição na tela de acordo com as suas dimensões
	 * 
	 * @param ponto
	 */
	private void ajustarPonto(Ponto ponto) {
		ponto.X = (int) (-ortho2D_w / 2 + ponto.X);
		ponto.Y = (int) (ortho2D_h / 2 - ponto.Y);
	}

	/**
	 * Verifica o comando para edição e aplica sobre a transformação do objeto
	 * em edição
	 * 
	 * @param e
	 *            evento do teclado
	 * @return retorna true se houve edição
	 */
	public boolean verificarEdicao(KeyEvent e) {
		boolean reconheceu = true;
		Transformacao trans = new Transformacao();
		int peso = 1;
		if (e.isControlDown()) {
			peso = 5;
		}

		if (Mundo.pontoSelecionado == null) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				trans.atribuirTranslacao(0, 10 * peso);
				break;
			case KeyEvent.VK_DOWN:
				trans.atribuirTranslacao(0, -10 * peso);
				break;
			case KeyEvent.VK_LEFT:
				trans.atribuirTranslacao(-10 * peso, 0);
				break;
			case KeyEvent.VK_RIGHT:
				trans.atribuirTranslacao(10 * peso, 0);
				break;

			case KeyEvent.VK_1:
				trans.atribuirRotacao(0.01 * peso);
				break;
			case KeyEvent.VK_2:
				trans.atribuirRotacao(-0.01 * peso);
				break;

			case KeyEvent.VK_ADD:
				trans.atribuirEscala(1 + ((double) peso / 100),
						1 + ((double) peso / 100));
				break;
			case KeyEvent.VK_SUBTRACT:
				trans.atribuirEscala(1 - ((double) peso / 100),
						1 - ((double) peso / 100));
				break;

			case KeyEvent.VK_C:
				Mundo.objetoSelecionado.setCor(new Cor(
						new Random().nextFloat(), new Random().nextFloat(),
						new Random().nextFloat()));
				break;

			case KeyEvent.VK_ESCAPE:
			case KeyEvent.VK_ENTER:
				Mundo.EstadoAtual = Estado.Visualizacao;
				if (Mundo.objetoSelecionado instanceof Poligono) {
					((Poligono) Mundo.getObjetoSelecionado()).concluir();
				}
				break;
			default:
				reconheceu = false;
			}
		} else {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				Mundo.pontoSelecionado.Y += 1 * peso;
				break;
			case KeyEvent.VK_DOWN:
				Mundo.pontoSelecionado.Y -= 1 * peso;
				break;
			case KeyEvent.VK_LEFT:
				Mundo.pontoSelecionado.X -= 1 * peso;
				break;
			case KeyEvent.VK_RIGHT:
				Mundo.pontoSelecionado.X += 1 * peso;
				break;

			case KeyEvent.VK_DELETE:
				Poligono obj = (Poligono) Mundo.objetoSelecionado;
				obj.removerPonto(Mundo.pontoSelecionado);
				Mundo.pontoSelecionado = null;
				if (!obj.temPontos()) {
					Mundo.objetoSelecionado.parent
							.removerFilho(Mundo.objetoSelecionado);
				}
				break;
			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_ESCAPE:
				Mundo.pontoSelecionado = null;
				break;
			default:
				reconheceu = false;
			}
		}

		if (reconheceu) {
			Mundo.getObjetoSelecionado().addTransformacao(trans);
		}
		return reconheceu;
	}

	/**
	 * Verifica o comando para o estado de visualização
	 * 
	 * @param e
	 *            evento do teclado
	 * @return retorna true se houve algum comando válido
	 */
	public boolean verificarVisualizacao(KeyEvent e) {
		boolean reconheceu = true;
		switch (e.getKeyCode()) {
		// Novo objeto
		case KeyEvent.VK_N:
			if (Mundo.getObjetoSelecionado() == null)
				Mundo.setObjetoSelecionado(mundo);
			novoObjeto = new Poligono(gl);
			Mundo.EstadoAtual = Estado.Criacao;
			Mundo.getObjetoSelecionado().addFilho(novoObjeto);
			break;
		// Editar objeto selecionado
		case KeyEvent.VK_E:
			if (!Mundo.getObjetoSelecionado().equals(mundo)) {
				Mundo.EstadoAtual = Estado.Edicao;
			} else {
				System.out.println("Selecione um objeto para editar");
			}
			break;
		// Deletar objeto selecionado
		case KeyEvent.VK_DELETE:
			if (!Mundo.getObjetoSelecionado().equals(this.mundo))
				Mundo.removeObjetoSelecionado();
			break;
		default:
			reconheceu = false;
		}
		return reconheceu;
	}

	private boolean verificarCriacao(KeyEvent e) {
		boolean reconheceu = true;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ENTER:
			concluirCriacao();
			break;
		default:
			reconheceu = false;
		}
		return reconheceu;
	}

	private void concluirCriacao() {
		this.novoObjeto.concluir();
		Mundo.EstadoAtual = Estado.Visualizacao;
		this.novoObjeto.removerPonto(pontoPosterior);
		this.pontoPosterior = null;
		Mundo.setObjetoSelecionado(this.novoObjeto);
		this.novoObjeto = null;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		boolean reconheceu = false;

		switch (Mundo.EstadoAtual) {
		case Visualizacao:
			reconheceu = verificarVisualizacao(e);
			break;
		case Edicao:
			reconheceu = verificarEdicao(e);
			break;
		case Criacao:
			reconheceu = verificarCriacao(e);
			break;
		}

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
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
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
