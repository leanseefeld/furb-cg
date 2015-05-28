package br.furb.bte;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;

public class Rastro extends Poligono {

    private static final int TAMANHO_RASTRO = 100;

    public Rastro(GL gl, Cor cor) {
	super(gl);
	this.cor = cor;
	this.primitiva = GL.GL_LINE_STRIP;
	this.setLarguraLinha(5);
    }

    @Override
    protected List<Ponto> criarPontos() {
	List<Ponto> pontos = new ArrayList<>(4);
	return pontos;
    }

    public void arrastar(Ponto novaPosicao) {
	this.pontos.add(novaPosicao);
	if (this.pontos.size() > TAMANHO_RASTRO) {
	    this.pontos.remove(0);
	}
    }
}
