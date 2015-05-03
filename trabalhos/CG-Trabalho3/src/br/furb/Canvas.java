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

public class Canvas implements GLEventListener, MouseMotionListener, MouseListener, KeyListener {
	private float ortho2D_minX = -400.0f, ortho2D_maxX = 400.0f, ortho2D_minY = -400.0f, ortho2D_maxY = 400.0f;
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	private Mundo mundo;
	private Ponto mouseReal = new Ponto(0, 0);
	private Poligono novoObjeto;

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

		this.desenhaPonteiro();
	}

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
			gl.glVertex2d(mouseReal.X, -mouseReal.Y);
		}
		gl.glEnd();
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		switch (Mundo.EstadoAtual) {
		case Visualizacao:
			Ponto pontoSelecionado = new Ponto(arg0.getX(), arg0.getY());
			ajustarPonto(pontoSelecionado);
			// Não vai selecionar corretamente se o objeto sofreram translação
			// O X está invertido
			// Ao inverter o sinal de X, passa a funcionar a seleção de objetos
			// translatados.
			// Mas deixa de funcionar para objetos rotacionados
			mundo.selecionarObjeto(pontoSelecionado);
			break;
		case Criacao:
			break;
		case Edicao:
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
		glDrawable.display();
	}

	private void ajustarPonto(Ponto ponto) {
		ponto.X = (ponto.X * 2 - 400);
		ponto.Y = (ponto.Y * 2 - 400);
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
	public boolean verificaEdicao(KeyEvent e) {
		boolean reconheceu = true;
		Transformacao trans = new Transformacao();
		int peso = 1;
		if (e.isControlDown()) {
			peso = 5;
		}

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

		if (reconheceu) {
			this.mundo.getObjetoSelecionado().setTransformacao(trans);
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
	public boolean verificaVisualizacao(KeyEvent e) {
		boolean reconheceu = true;
		switch (e.getKeyCode()) {
		// Novo objeto
		case KeyEvent.VK_N:
			novoObjeto = new Poligono(gl);
			Mundo.EstadoAtual = Estado.Criacao;
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
		case KeyEvent.VK_DELETE:
			if (!this.mundo.getObjetoSelecionado().equals(this.mundo))
				this.mundo.removeObjetoSelecionado();
			break;
		default:
			reconheceu = false;
		}
		return reconheceu;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		boolean reconheceu = false;

		if (Mundo.EstadoAtual == Estado.Visualizacao) {
			reconheceu = verificaVisualizacao(e);
		} else if (Mundo.EstadoAtual == Estado.Edicao) {
			reconheceu = verificaEdicao(e);
		}

		if (reconheceu) {
			glDrawable.display();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
