package br.furb.bte.objetos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.media.opengl.GL;

// TODO: por enquanto extende de Polígono só pra poder desenhar algo na tela
public class Arena extends Poligono {

    private short idProxMoto = 1;
    private final Map<Short, Moto> motos;
    public static final int LARGURA = 1000;
    public static final int COMPRIMENTO = 1000;

    //    public static final int TAMANHO_ARENA = 1000;

    public Arena() {
	this.primitiva = GL.GL_QUADS;
	this.cor = new Cor(0.1f, 0.5f, 0.5f);
	carregarPontos();
	motos = new HashMap<>(3);
    }

    @Override
    protected List<Ponto> criarPontos() {
	List<Ponto> pontos = new ArrayList<Ponto>(4);
	pontos.add(new Ponto(+LARGURA / 2, 0, -COMPRIMENTO / 2));
	pontos.add(new Ponto(-LARGURA / 2, 0, -COMPRIMENTO / 2));
	pontos.add(new Ponto(-LARGURA / 2, 0, +COMPRIMENTO / 2));
	pontos.add(new Ponto(+LARGURA / 2, 0, +COMPRIMENTO / 2));
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
