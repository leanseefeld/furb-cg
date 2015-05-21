package br.furb;

import javax.media.opengl.GL;

public class Mundo extends ObjetoGrafico {
	public static ObjetoGrafico objetoSelecionado;
	public static Ponto pontoSelecionado;
	public static Estado EstadoAtual;

	public Mundo(GL gl, int maxX, int minX, int maxY, int minY) {
		super(gl);
		super.bbox = new BBox(maxX, minX, maxY, minY);
	}

	@Override
	public void desenhar() {
		super.desenhar();
	}

	@Override
	public boolean malhaSelecionada(Ponto pontoBusca) {
		// Não possui malha(pontos), então retorna true sempre
		return true;
	}

	@Override
	public ObjetoGrafico selecionarObjeto(Ponto ponto) {
		Mundo.objetoSelecionado = super.selecionarObjeto(ponto);
		Mundo.pontoSelecionado = null;
		return Mundo.objetoSelecionado;
	}

	public static void setObjetoSelecionado(ObjetoGrafico objetoGrafico) {
		Mundo.pontoSelecionado = null;
		Mundo.objetoSelecionado = objetoGrafico;
	}

	public static ObjetoGrafico getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public static void removeObjetoSelecionado() {
		Mundo.objetoSelecionado.getParent().removerFilho(objetoSelecionado);
		Mundo.objetoSelecionado = null;
	}
}
