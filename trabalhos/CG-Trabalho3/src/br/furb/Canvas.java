package br.furb;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.SwingUtilities;

public class Canvas implements GLEventListener, MouseMotionListener, MouseListener, KeyListener {
	private float ortho2D_minX = -400.0f, ortho2D_maxX = 400.0f, ortho2D_minY = -400.0f, ortho2D_maxY = 400.0f;
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	private Mundo mundo;
	private Ponto mouseReal = new Ponto(0, 0);
	private Poligono novoObjeto;
	private Ponto pontoAnterior;
	private Ponto pontoPosterior;

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
		this.desenhaPonteiro();
	}

	/**
	 * Desenha o rastro do mouse ao criar um objeto
	 */
	private void desenhaRastro() {
		if (Mundo.EstadoAtual == Estado.Criacao) {
			if (pontoAnterior != null && pontoPosterior != null) {
				gl.glLineWidth(2);
				gl.glColor3f(0f, 0f, 1f);
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
		System.out.println("Espaço de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		mundo = new Mundo(gl, (int) ortho2D_maxX, (int) ortho2D_minX, (int) ortho2D_maxY, (int) ortho2D_minY);

		Poligono poFilho = new Poligono(gl);
		poFilho.cor = new Cor(1, 1, 0);
		mundo.addFilho(poFilho);

		poFilho.addPonto(new Ponto(30, 200));
		poFilho.addPonto(new Ponto(-30, 200));
		poFilho.addPonto(new Ponto(-30, -200));
		poFilho.addPonto(new Ponto(30, -200));

		poFilho.concluir();

		Mundo.EstadoAtual = Estado.Visualizacao;
	}

	public void desenhaPonteiro() {
		gl.glPointSize(2);
		gl.glColor3f(0f, 0f, 1f);
		gl.glBegin(GL.GL_POINTS);
		{
			gl.glVertex2d(mouseReal.X, mouseReal.Y);
		}
		gl.glEnd();
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
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
				this.pontoAnterior = this.pontoPosterior;
				this.pontoPosterior = pontoSelecionado.clone();
				this.novoObjeto.addPonto(this.pontoAnterior);
			}
			break;
		case Edicao:
			if (mundo.objetoSelecionado instanceof Poligono)
				((Poligono) mundo.objetoSelecionado).selecionarPonto(pontoSelecionado);
			break;
		}

		glDrawable.display();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		this.mouseReal.X = arg0.getX();
		this.mouseReal.Y = arg0.getY();
		ajustarPonto(this.mouseReal);

		if (this.pontoPosterior != null) {
			this.pontoPosterior.X = this.mouseReal.X;
			this.pontoPosterior.Y = this.mouseReal.Y;
		}
		glDrawable.display();
	}

	private void ajustarPonto(Ponto ponto) {
		ponto.X = (ponto.X * 2 - 400);
		ponto.Y = -(ponto.Y * 2 - 400);
	}

	@Override
	public void keyTyped(KeyEvent e) {

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
				trans.atribuirEscala(1 + ((double) peso / 100), 1 + ((double) peso / 100));
				break;
			case KeyEvent.VK_SUBTRACT:
				trans.atribuirEscala(1 - ((double) peso / 100), 1 - ((double) peso / 100));
				break;

			case KeyEvent.VK_ESCAPE:
				// TODO Implementar
				Mundo.EstadoAtual = Estado.Visualizacao;
				break;
			case KeyEvent.VK_ENTER:
				// TODO Implementar
				Mundo.EstadoAtual = Estado.Visualizacao;
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
					this.mundo.objetoSelecionado.parent.removerFilho(Mundo.objetoSelecionado);
				}
				break;
			case KeyEvent.VK_ESCAPE:
				Mundo.pontoSelecionado = null;
				break;
			default:
				reconheceu = false;
			}
		}

		if (reconheceu) {
			this.mundo.getObjetoSelecionado().addTransformacao(trans);
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
			novoObjeto = new Poligono(gl);
			novoObjeto.setPrimitiva(GL.GL_LINE_STRIP);
			Mundo.EstadoAtual = Estado.Criacao;
			if (this.mundo.getObjetoSelecionado() != null)
				this.mundo.getObjetoSelecionado().addFilho(novoObjeto);
			else
				this.mundo.addFilho(novoObjeto);
			break;
		// Editar objeto selecionado
		case KeyEvent.VK_E:
			if (!mundo.getObjetoSelecionado().equals(mundo)) {
				Mundo.EstadoAtual = Estado.Edicao;
			} else {
				System.out.println("Selecione um objeto para editar");
			}
			break;
		// Deletar objeto selecionado
		case KeyEvent.VK_DELETE:
			if (!this.mundo.getObjetoSelecionado().equals(this.mundo))
				this.mundo.removeObjetoSelecionado();
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
	public void keyReleased(KeyEvent e) {

	}
}
