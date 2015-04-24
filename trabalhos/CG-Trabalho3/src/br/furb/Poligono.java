package br.furb;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

public class Poligono extends ObjetoGrafico {
	private Transformacao transformacao;
	private List<Ponto> pontos;
	private int primitiva;
	private Ponto pontoSelecionado;

	public Poligono(GL gl) {
		super(gl);
		this.primitiva = GL.GL_LINE_LOOP;
		this.pontos = new ArrayList<Ponto>();
	}

	public void addPonto(Ponto ponto) {
		this.pontos.add(ponto);
	}

	public void concluir() {
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

	public void RotacionarX(int graus) {

	}

	public void RotacionarY(int graus) {

	}

	public void Mover(int x, int y) {

	}

	public void Escalonar(int quantidade) {

	}

	public Ponto SelecionarPonto(int x, int y) {
		pontoSelecionado = null;
		return null;
	}

	public void desenhar() {
		gl.glLineWidth(2);
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

			super.desenhar();
		}
		gl.glPopMatrix();
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

	private float getPontoIntermediario(int a, int b, float peso) {
		return a + (b - a) * peso;
	}

	public void setTransformacao(Transformacao transformacao) {
		this.transformacao = transformacao;
	}
}
