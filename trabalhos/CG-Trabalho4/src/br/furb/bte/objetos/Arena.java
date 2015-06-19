package br.furb.bte.objetos;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;

// TODO: por enquanto extende de Polígono só pra poder desenhar algo na tela
public class Arena extends Poligono {

    private int largura;
    private int altura;
    
    public Arena(int largura, int altura) {
	this.primitiva = GL.GL_QUADS;
	this.cor = new Cor(0.1f, 0.5f, 0.5f);
	this.largura = largura;
	this.altura = altura;
	carregarPontos();
    }

    @Override
    protected List<Ponto> criarPontos() {
	List<Ponto> pontos = new ArrayList<>(4);
	pontos.add(new Ponto(+largura, 0, -altura));
	pontos.add(new Ponto(-largura, 0, -altura));
	pontos.add(new Ponto(-largura, 0, +altura));
	pontos.add(new Ponto(+largura, 0, +altura));
	return pontos;
    }
}
