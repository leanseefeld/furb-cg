package br.furb.bte;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;

public class Moto extends Poligono {

    private int angulo;
    private static final int VELOCIDADE = 10;

    public Moto(GL gl, Ponto pontoInicial, int anguloIncial) {
	super(gl);
	this.primitiva = GL.GL_QUADS;
	this.cor = new Cor(1, 0, 0);
	this.girar(0);
	this.angulo = anguloIncial;

	Transformacao transTranslacao = new Transformacao();
	transTranslacao.atribuirTranslacao(pontoInicial.X, pontoInicial.Y);
	
	Transformacao transRotacao = new Transformacao();
	transRotacao.atribuirRotacao(Math.toRadians(anguloIncial));
	
	//this.transformacao = transRotacao.transformMatrix(transTranslacao);
	this.transformacao = transTranslacao.transformMatrix(transRotacao);
    }

    @Override
    protected List<Ponto> criarPontos() {
	List<Ponto> pontos = new ArrayList<>(4);
	
	pontos.add(new Ponto(+17, +4));
	pontos.add(new Ponto(+17, -4));
	pontos.add(new Ponto(-19, -6));
	pontos.add(new Ponto(-19, +6));
	
	return pontos;
    }

    public void girar(int graus) {
	this.angulo = (this.angulo += graus) % 360;
	Transformacao trans = new Transformacao();
	trans.atribuirRotacao(Math.toRadians(graus));
	this.addRotacao(trans);
    }

    public void andar() {
	int moverX = (int)Math.cos(Math.toRadians(this.angulo)) * VELOCIDADE;
	int moverY = (int)Math.sin(Math.toRadians(this.angulo)) * VELOCIDADE;
	Transformacao trans = new Transformacao();
	trans.atribuirTranslacao(moverX, moverY);
	this.addMovimentacao(trans);
    }

    public void setAngulo(int angulo) {
	int grausGirar = angulo - this.angulo;
	this.girar(grausGirar); 
	this.angulo = angulo;
    }
}
