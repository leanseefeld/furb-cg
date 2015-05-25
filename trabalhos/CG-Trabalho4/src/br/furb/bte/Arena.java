package br.furb.bte;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;

// TODO: por enquanto extende de Polígono só pra poder desenhar algo na tela
public class Arena extends Poligono {

    public Arena(GL gl) {
	super(gl);
	this.primitiva = GL.GL_QUADS;
	this.cor = new Cor(1, 1, 0);
    }

    @Override
    protected List<Ponto> criarPontos() {
	List<Ponto> pontos = new ArrayList<>(4);
	pontos.add(new Ponto(+300, +300));
	pontos.add(new Ponto(-300, +300));
	pontos.add(new Ponto(-300, -300));
	pontos.add(new Ponto(+300, -300));
	return pontos;
    }
}
