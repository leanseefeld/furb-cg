package br.furb.bte;

import javax.media.opengl.GL;

public class Mundo extends ObjetoGrafico {

    public static ObjetoGrafico objetoSelecionado;
    public static Ponto pontoSelecionado;

    public Mundo(GL gl, int maxX, int minX, int maxY, int minY) {
	super(gl);
	super.bbox = new BBox(maxX, minX, maxY, minY);
    }

    @Override
    public void desenhar() {
	super.desenhar();
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
