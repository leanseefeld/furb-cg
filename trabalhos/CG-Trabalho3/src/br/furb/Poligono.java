package br.furb;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

public class Poligono extends ObjetoGrafico {
	private static final int RANGE_SELECIONAR_PONTO = 15;
	private List<Ponto> pontos;
	private int primitiva;

	public Poligono(GL gl) {
		super(gl);
		this.primitiva = GL.GL_LINE_LOOP;
		this.pontos = new ArrayList<Ponto>();
	}

	public int getPrimitiva() {
		return primitiva;
	}

	public void setPrimitiva(int primitiva) {
		this.primitiva = primitiva;
	}

	public void addPonto(Ponto ponto) {
		Ponto pontoTrans = this.inverseTransformRecursive(ponto);
		this.pontos.add(pontoTrans);
	}

	/**
	 * Monta a BBox em torno do objeto
	 */
	private void criarBBox() {
		super.bbox = new BBox();
		super.bbox.setMaiorX(Integer.MIN_VALUE);
		super.bbox.setMenorX(Integer.MAX_VALUE);
		super.bbox.setMaiorY(Integer.MIN_VALUE);
		super.bbox.setMenorY(Integer.MAX_VALUE);
		for (Ponto ponto : pontos) {
			if (ponto.X > this.bbox.getMaiorX())
				this.bbox.setMaiorX(ponto.X);
			if (ponto.X < this.bbox.getMenorX())
				this.bbox.setMenorX(ponto.X);
			if (ponto.Y > this.bbox.getMaiorY())
				this.bbox.setMaiorY(ponto.Y);
			if (ponto.Y < this.bbox.getMenorY())
				this.bbox.setMenorY(ponto.Y);
		}
	}

	public void concluir() {
		criarBBox();
	}

	public void RotacionarX(int graus) {

	}

	public void RotacionarY(int graus) {

	}

	public void Mover(int x, int y) {

	}

	public void Escalonar(int quantidade) {

	}

	/**
	 * Destaca o ponto selecionado
	 */
	public void desenharPontoSelecionado() {
		if (Mundo.pontoSelecionado != null)
			for (Ponto ponto : this.pontos) {
				if (ponto == Mundo.pontoSelecionado) {
					gl.glPointSize(7);
					gl.glColor3f(0f, 0f, 0f);
					gl.glBegin(GL.GL_POINTS);
					{
						gl.glVertex2d(ponto.X, ponto.Y);
					}
					gl.glEnd();
				}
			}
	}

	public void desenhar() {
		gl.glLineWidth(3);
		gl.glColor3f(cor.R, cor.G, cor.B);

		gl.glPushMatrix();
		{
			gl.glMultMatrixd(transformacao.getMatriz(), 0);

			gl.glBegin(primitiva);
			{
				for (Ponto ponto : pontos) {
					gl.glVertex2d(ponto.X, ponto.Y);
				}
			}
			gl.glEnd();

			if (this.isSelected()) {
				if (Mundo.EstadoAtual == Estado.Edicao)
					destacarPontos();
				desenharPontoSelecionado();
			}

			super.desenhar();
		}
		gl.glPopMatrix();
	}

	private void destacarPontos() {
		for (Ponto ponto : this.pontos) {
			gl.glPointSize(7);
			gl.glColor3f(0.2f, 0.2f, 1f);
			gl.glBegin(GL.GL_POINTS);
			{
				gl.glVertex2d(ponto.X, ponto.Y);
			}
			gl.glEnd();
		}
	}

	@Override
	public boolean malhaSelecionada(Ponto pontoBusca) {
		int quantidade = 0;
		Ponto pontoA = null;
		Ponto pontoB = null;
		for (int i = 0; i < pontos.size(); i++) {
			pontoA = pontos.get(i);
			if (i + 1 < pontos.size())
				pontoB = pontos.get(i + 1);
			else
				pontoB = pontos.get(0);

			if (pontoA.Y == pontoB.Y) {
				// Não consigera por enquanto
			} else {
				// Verifica se o ponto está entre o Y dos pontos A e B
				float t = ((float) pontoBusca.Y - (float) pontoA.Y) / ((float) pontoB.Y - (float) pontoA.Y);
				if (t > 0 && t < 1) {
					// Verifica se o ponto está a direita da intersecção ou a
					// esquerda e considera apenas um dos lados
					float XInterseccao = getPontoIntermediario(pontoA.X, pontoB.X, t);
					if (XInterseccao > pontoBusca.X)
						quantidade++;
				}
			}
		}
		return (quantidade % 2) != 0;
	}

	/**
	 * Seleciona um ponto do objeto
	 * 
	 * @param pontoSelecionado
	 */
	public Ponto selecionarPonto(Ponto pontoSelecionado) {
		System.out.println("Busca : " + pontoSelecionado.toString());
		Ponto pontoTrans = this.inverseTransformRecursive(pontoSelecionado.clone());
		System.out.println("BTrans: " + pontoTrans.toString());
		for (Ponto ponto : this.pontos) {
			if (Math.abs(ponto.X - pontoTrans.X) < RANGE_SELECIONAR_PONTO && Math.abs(ponto.Y - pontoTrans.Y) < RANGE_SELECIONAR_PONTO) {
				Mundo.pontoSelecionado = ponto;
				return ponto;
			}
		}
		Mundo.pontoSelecionado = null;
		return null;
	}

	private float getPontoIntermediario(int a, int b, float peso) {
		return a + (b - a) * peso;
	}

	public boolean temPontos() {
		return this.pontos.size() > 0;
	}

	public void removerPonto(Ponto ponto) {
		this.pontos.remove(ponto);
	}
}
