package br.furb;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

public abstract class ObjetoGrafico {
	protected List<ObjetoGrafico> objetosGraficos;
	protected Cor cor;
	protected GL gl;
	protected boolean selecionado;
	protected BBox bbox;

	public ObjetoGrafico(GL gl) {
		super();
		this.objetosGraficos = new ArrayList<ObjetoGrafico>();
		this.gl = gl;
		this.selecionado = false;
	}

	public void desenhar() {
		if (this.isSelected()) {
			desenharSelecao();
		}

		for (ObjetoGrafico objetoGrafico : objetosGraficos) {
			objetoGrafico.desenhar();
		}
	}

	private void desenharSelecao() {
		if (this.bbox != null) {
			gl.glPointSize(4);
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glBegin(GL.GL_POINTS);
			{
				gl.glVertex2d(this.bbox.getMaiorX(), this.bbox.getMaiorY());
				gl.glVertex2d(this.bbox.getMenorX(), this.bbox.getMaiorY());
				gl.glVertex2d(this.bbox.getMaiorX(), this.bbox.getMenorY());
				gl.glVertex2d(this.bbox.getMenorX(), this.bbox.getMenorY());

				int pontoMeioX = this.bbox.getMenorX()
						+ ((this.bbox.getMaiorX() - this.bbox.getMenorX()) / 2);

				int pontoMeioY = this.bbox.getMenorY()
						+ ((this.bbox.getMaiorY() - this.bbox.getMenorY()) / 2);

				gl.glVertex2d(this.bbox.getMaiorX(), pontoMeioY);
				gl.glVertex2d(this.bbox.getMenorX(), pontoMeioY);
				gl.glVertex2d(pontoMeioX, this.bbox.getMaiorY());
				gl.glVertex2d(pontoMeioX, this.bbox.getMenorY());
			}
			gl.glEnd();

			gl.glPointSize(1);
			gl.glColor3f(1.0f, 0.0f, 0.0f);
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

	public abstract boolean malhaSelecionada(Ponto pontoBusca);

	public final ObjetoGrafico selecionarObjeto(Ponto ponto) {
		if (this.bbox.estaDentro(ponto)) {
			ObjetoGrafico objetoSelecionado = null;
			for (ObjetoGrafico objetoGrafico : objetosGraficos) {
				if ((objetoSelecionado = objetoGrafico.selecionarObjeto(ponto)) != null) {
					return objetoSelecionado;
				}
			}
			if (malhaSelecionada(ponto)) {
				this.setSelected(true);
				return this;
			}
		}

		return null;
	}

	public void addFilho(Poligono poligono) {
		this.objetosGraficos.add(poligono);
		poligono.setSelected(true);
	}

	public boolean isSelected() {
		return selecionado;
	}

	public void setSelected(boolean isSelected) {
		this.selecionado = isSelected;
	}
}
