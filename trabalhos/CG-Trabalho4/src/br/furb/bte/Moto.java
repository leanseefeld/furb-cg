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
    public static final int CIMA = 90;
    public static final int DIREITA = 0;
    public static final int BAIXO = -90;
    public static final int ESQUERDA = 180;

    public Moto(GL gl, Ponto pontoInicial, int anguloIncial, Cor cor) {
	super(gl);
	this.transformacao = new Transformacao();
	this.primitiva = GL.GL_QUADS;
	this.cor = cor;
	this.corNormal = cor;
	this.rastro = new Rastro(gl, cor);
	this.setPosicao(pontoInicial);
	this.anguloProximo = anguloIncial;
	this.girar();
    }

    private void setPosicao(Ponto pontoInicial) {
	Transformacao transTranslacao = new Transformacao();
	transTranslacao.atribuirTranslacao(pontoInicial.X, pontoInicial.Y);
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
	List<Ponto> pontos = new ArrayList<>(4);

	pontos.add(new Ponto(+17, +4));
	pontos.add(new Ponto(+17, -4));
	pontos.add(new Ponto(-19, -6));
	pontos.add(new Ponto(-19, +6));

	return pontos;
    }

    public void mover() {
	this.girar();
	int moverX = (int) Math.cos(Math.toRadians(this.angulo)) * VELOCIDADE;
	int moverY = (int) Math.sin(Math.toRadians(this.angulo)) * VELOCIDADE;
	Transformacao trans = new Transformacao();
	trans.atribuirTranslacao(moverX, moverY);
	this.addMovimentacao(trans);

	this.rastro.arrastar(this.transformacao.transformPoint(this.bbox.getCentro()));
    }

    public boolean verificarColisao(Moto moto) {
	return this.getBBoxTrasnformada().estaColidindo(moto.getBBoxTrasnformada());
    }
    
    public boolean verificarColisao(Rastro rastro) {
	BBox bboxTransformado = getBBoxTrasnformada();
	
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

    public BBox getBBoxTrasnformada() {
	List<Ponto> pontosTransformados = new ArrayList<Ponto>(super.pontos.size());
	for (Ponto ponto : super.pontos) {
	    pontosTransformados.add(this.transformacao.transformPoint(ponto));
	}
	BBox bboxTransformado = new BBox(pontosTransformados);
	return bboxTransformado;
    }

    public void girar() {
	int graus = this.anguloProximo - this.angulo;
	this.angulo = this.anguloProximo;
	Transformacao trans = new Transformacao();
	trans.atribuirRotacao(Math.toRadians(graus));
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

    public void setColisao() {
	this.cor = this.corColisao;
    }

    public void setNormal() {
	this.cor = this.corNormal;
    }
}
