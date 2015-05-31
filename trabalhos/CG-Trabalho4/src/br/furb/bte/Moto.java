package br.furb.bte;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;

public class Moto extends Poligono {

    /**
     * Angulo atual da moto
     */
    private int angulo;
    /**
     * indica qual será o angulo aplicado na proxima execução
     */
    private int anguloProximo;
    private Rastro rastro;
    private final Cor corColisao = new Cor(0, 0, 1);
    private final Cor corNormal;
    private static final int VELOCIDADE = 10;
    private static final int MAX_ROTACAO = 90;
    public static final int CIMA = -90;
    public static final int DIREITA = 0;
    public static final int BAIXO = 90;
    public static final int ESQUERDA = 180;

    public Moto(GL gl, int x, int z, int anguloIncial, Cor cor) {
	super(gl);
	this.transformacao = new Transformacao();
	this.primitiva = GL.GL_QUADS;
	this.cor = cor;
	this.corNormal = cor;
	this.rastro = new Rastro(gl, cor);
	this.setPosicao(x, z);
	this.anguloProximo = anguloIncial;
	this.girar();
    }

    public void setColisao() {
	this.cor = this.corColisao;
    }

    public void setNormal() {
	this.cor = this.corNormal;
    }

    private void setPosicao(int x, int z) {
	Transformacao transTranslacao = new Transformacao();
	transTranslacao.atribuirTranslacao(x, 0, z);
	this.addMovimentacao(transTranslacao);
    }

    public void setRastro(Rastro rastro) {
	this.rastro = rastro;
    }

    public Rastro getRastro() {
	return rastro;
    }

    @Override
    protected List<Ponto> criarPontos() {
	List<Ponto> pontos = new ArrayList<>();

	//Cima
	pontos.add(new Ponto(+17, +4, +4));
	pontos.add(new Ponto(+17, +4, -4));
	pontos.add(new Ponto(-19, +4, -6));
	pontos.add(new Ponto(-19, +4, +6));

	//Baixo
	pontos.add(new Ponto(-19, +0, +6));
	pontos.add(new Ponto(-19, +0, -6));
	pontos.add(new Ponto(+17, +0, -4));
	pontos.add(new Ponto(+17, +0, +4));

	//Frente
	pontos.add(new Ponto(-19, +4, -6));
	pontos.add(new Ponto(-19, +0, -6));
	pontos.add(new Ponto(-19, +0, +6));
	pontos.add(new Ponto(-19, +4, +6));

	//Traz
	pontos.add(new Ponto(+17, +4, +4));
	pontos.add(new Ponto(+17, +0, +4));
	pontos.add(new Ponto(+17, +0, -4));
	pontos.add(new Ponto(+17, +4, -4));

	//Lateral Esquerda
	pontos.add(new Ponto(+17, +4, +4));
	pontos.add(new Ponto(-19, +4, +6));
	pontos.add(new Ponto(-19, +0, +6));
	pontos.add(new Ponto(+17, +0, +4));

	//Lateral Direita
	pontos.add(new Ponto(+17, +0, -4));
	pontos.add(new Ponto(-19, +0, -6));
	pontos.add(new Ponto(-19, +4, -6));
	pontos.add(new Ponto(+17, +4, -4));


	return pontos;
    }

    public void mover() {
	this.girar();
	int moverX = (int) Math.cos(Math.toRadians(this.angulo)) * VELOCIDADE;
	int moverZ = (int) Math.sin(Math.toRadians(this.angulo)) * VELOCIDADE;
	Transformacao trans = new Transformacao();
	trans.atribuirTranslacao(moverX, 0, moverZ);
	this.addMovimentacao(trans);

	this.rastro.arrastar(this.transformacao.transformPoint(this.bbox.getCentro()));
    }

    public boolean estaColidindo(Poligono objeto) {
	BBox bbox = this.getBBoxTransformada();
	BBox bbox2 = objeto.getBBoxTransformada();
	return bbox.estaColidindo(bbox2);
    }

    public boolean estaColidindo(Rastro rastro) {
	BBox bboxTransformado = getBBoxTransformada();

	boolean existeColisao = false;

	Ponto pontoA = null;
	Ponto pontoB = null;
	//	Ponto pontoBAux = null;
	int qtdIgnorar = rastro == this.rastro ? 5 : 0;
	for (int i = 1; i < rastro.pontos.size() - qtdIgnorar; i++) {

	    pontoA = rastro.pontos.get(i - 1);
	    pontoB = rastro.pontos.get((i));

	    //	    i++;
	    //	    if (pontoA.X == pontoB.X) {
	    //		while (i < rastro.pontos.size() - qtdIgnorar) {
	    //		    pontoBAux = rastro.pontos.get((i) % rastro.pontos.size());
	    //		    if (pontoA.X != pontoBAux.X) {
	    //			break;
	    //		    }
	    //		    pontoB = pontoBAux;
	    //		    i++;
	    //		}
	    //	    } else {
	    //		while (i < rastro.pontos.size() - qtdIgnorar) {
	    //		    pontoBAux = rastro.pontos.get((i) % rastro.pontos.size());
	    //		    if (pontoA.Y == pontoBAux.Y) {
	    //			break;
	    //		    }
	    //		    pontoB = pontoBAux;
	    //		    i++;
	    //		}
	    //	    }
	    //	    i--;

	    if (bboxTransformado.estaSendoCruzadoPor(pontoA, pontoB)) {
		existeColisao = true;
		break;
	    }
	}
	if (existeColisao)
	    this.cor = this.corColisao;
	else
	    this.cor = this.corNormal;
	return existeColisao;
    }

    public boolean estaSobre(Poligono objeto) {
	BBox bbox = this.getBBoxTransformada();
	BBox bbox2 = objeto.getBBoxTransformada();
	return bbox.estaSob(bbox2);
    }
    
    public void girar() {
	int graus = this.anguloProximo - this.angulo;
	this.angulo = this.anguloProximo;
	Transformacao trans = new Transformacao();
	trans.atribuirRotacaoY(Math.toRadians(graus));
	this.addRotacao(trans);
	this.rastro.arrastar(this.transformacao.transformPoint(this.bbox.getCentro()));
    }

    public void setAngulo(int angulo) {
	int grausGirar = angulo - this.angulo;
	if (grausGirar < -180 || grausGirar > 180)
	    grausGirar = -(grausGirar % 180);
	if (grausGirar >= -MAX_ROTACAO && grausGirar <= MAX_ROTACAO) {
	    this.anguloProximo = angulo;
	}
    }
}
