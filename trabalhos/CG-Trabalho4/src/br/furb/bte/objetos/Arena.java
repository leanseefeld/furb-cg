package br.furb.bte.objetos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.media.opengl.GL;

// TODO: por enquanto extende de Polígono só pra poder desenhar algo na tela
public class Arena extends Poligono {

    private short idProxMoto = 1;
    private int largura;
    private int altura;
    private final Map<Short, Moto> motos;

    public Arena(int largura, int altura) {
	this.primitiva = GL.GL_QUADS;
	this.cor = new Cor(0.1f, 0.5f, 0.5f);
	this.largura = largura;
	this.altura = altura;
	carregarPontos();
	motos = new HashMap<>(3);
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

    public void addMoto(Moto moto) {
	addFilho(moto);
	addFilho(moto.getRastro());
	motos.put(idProxMoto++, moto);
    }

    public Moto getMoto(short id) {
	return motos.get(id);
    }

}
