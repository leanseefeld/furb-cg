package br.furb;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

public abstract class ObjetoGrafico {
	protected List<ObjetoGrafico> objetosGraficos;
	protected Cor cor;
	protected GL gl;
	protected BBox bbox;
	protected Transformacao transformacao;
	/**
	 * Objeto pai
	 */
	protected ObjetoGrafico parent;

	/**
	 * Inclui uma transformação para o objeto
	 * 
	 * @param transformacao
	 */
	public void addTransformacao(Transformacao transformacao) {
		this.transformacao = this.transformacao.transformMatrix(transformacao);
	}

	public Transformacao getTransformacao() {
		return transformacao;
	}

	public ObjetoGrafico getParent() {
		return parent;
	}

	public void setParent(ObjetoGrafico objetoGrafico) {
		this.parent = objetoGrafico;
	}

	/**
	 * Adiciona um objeto filho
	 * 
	 * @param poligono
	 */
	public void addFilho(Poligono poligono) {
		this.objetosGraficos.add(poligono);
		poligono.setParent(this);
	}

	public ObjetoGrafico(GL gl) {
		super();
		this.objetosGraficos = new ArrayList<ObjetoGrafico>();
		this.gl = gl;
		this.transformacao = new Transformacao();
		this.cor = new Cor(0f, 0f, 0f);
	}

	/**
	 * Desenha o objeto
	 */
	public void desenhar() {
		if (this.isSelected()) {
			desenharSelecao();
		}

		for (ObjetoGrafico objetoGrafico : objetosGraficos) {
			objetoGrafico.desenhar();
		}
	}

	/**
	 * Desenha o quadrado de seleção do objeto
	 */
	private void desenharSelecao() {
		if (this.bbox != null) {
			gl.glPointSize(4);
			if (Mundo.EstadoAtual == Estado.Visualizacao)
				gl.glColor3f(1.0f, 0.0f, 0.0f);
			else if (Mundo.EstadoAtual == Estado.Edicao) {
				gl.glColor3f(0.0f, 1.0f, 0.0f);
			} else {
				gl.glColor3f(0.0f, 0.0f, 1.0f);
			}
			gl.glBegin(GL.GL_POINTS);
			{
				gl.glVertex2d(this.bbox.getMaiorX(), this.bbox.getMaiorY());
				gl.glVertex2d(this.bbox.getMenorX(), this.bbox.getMaiorY());
				gl.glVertex2d(this.bbox.getMaiorX(), this.bbox.getMenorY());
				gl.glVertex2d(this.bbox.getMenorX(), this.bbox.getMenorY());

				int pontoMeioX = this.bbox.getMenorX() + ((this.bbox.getMaiorX() - this.bbox.getMenorX()) / 2);

				int pontoMeioY = this.bbox.getMenorY() + ((this.bbox.getMaiorY() - this.bbox.getMenorY()) / 2);

				gl.glVertex2d(this.bbox.getMaiorX(), pontoMeioY);
				gl.glVertex2d(this.bbox.getMenorX(), pontoMeioY);
				gl.glVertex2d(pontoMeioX, this.bbox.getMaiorY());
				gl.glVertex2d(pontoMeioX, this.bbox.getMenorY());
			}
			gl.glEnd();

			gl.glPointSize(1);
			gl.glBegin(GL.GL_POINTS);
			{
				for (int i = this.bbox.getMenorX(); i < this.bbox.getMaiorX(); i += 10) {
					gl.glVertex2d(i, this.bbox.getMenorY());
					gl.glVertex2d(i, this.bbox.getMaiorY());
				}

				for (int i = this.bbox.getMenorY(); i < this.bbox.getMaiorY(); i += 10) {
					gl.glVertex2d(this.bbox.getMenorX(), i);
					gl.glVertex2d(this.bbox.getMaiorX(), i);
				}
			}
			gl.glEnd();
		}
	}

	/**
	 * Seleciona um objeto em o ponto se encontre dentro
	 * 
	 * @param ponto
	 * @return retorna o objeto selecionado <br>
	 *         retorna NULL se não encontrar nenhum objeto
	 */
	public ObjetoGrafico selecionarObjeto(Ponto ponto) {
		ponto = transformacao.transformPointInverse(ponto);

		// Primeiro verifica se algum dos filhos foi selecionado
		ObjetoGrafico objetoSelecionado = null;
		for (ObjetoGrafico objetoGrafico : objetosGraficos) {
			if ((objetoSelecionado = objetoGrafico.selecionarObjeto(ponto)) != null) {
				return objetoSelecionado;
			}
		}

		// Se nenhum filho foi selecionado, então verifica se o objeto atual foi
		// selecionado
		if (this.bbox.estaDentro(ponto)) {
			if (this.malhaSelecionada(ponto)) {
				Mundo.objetoSelecionado = this;
				return this;
			}
		}

		return null;
	}

	/**
	 * Validação extra que deve ser implementada por quem extender essa classe
	 * abstrada
	 * 
	 * @param pontoBusca
	 * @return
	 */
	public abstract boolean malhaSelecionada(Ponto pontoBusca);

	/**
	 * Indica se o objeto está selecionado
	 * 
	 * @return
	 */
	public boolean isSelected() {
		return Mundo.objetoSelecionado == this;
	}

	public void removerFilho(ObjetoGrafico objeto) {
		this.objetosGraficos.remove(objeto);
	}

	/**
	 * Transforma o ponto para o ponto de origem considerando todas as
	 * transformações dos objetos pais
	 * 
	 * @param ponto
	 * @return
	 */
	public Ponto inverseTransformRecursive(Ponto ponto) {
		if (this.parent != null)
			ponto = this.parent.inverseTransformRecursive(ponto);

		return this.transformacao.transformPointInverse(ponto);
	}
}
