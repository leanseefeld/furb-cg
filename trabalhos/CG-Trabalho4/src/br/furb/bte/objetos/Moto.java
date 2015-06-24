package br.furb.bte.objetos;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;
import br.furb.bte.LeitorObjeto.OBJModel;
import br.furb.bte.LeitorObjeto.Tuple3;

public class Moto extends Poligono {

    private static OBJModel MODEL;
    private static BBox B_BOX;

    private int angulo;
    private Rastro rastro;
    private final Cor corColisao = new Cor(0, 0, 1);
    private final Cor corNormal;
    private final Transformacao ajuste;
    private final String nome;
    private static List<Ponto> PONTOS;

    public static final int TAMANHO_RASTRO = 300;
    public static final int VELOCIDADE = 5;
//    public static final int CIMA = -90;
    public static final int CIMA = 270;
    public static final int DIREITA = 0;
    public static final int BAIXO = 90;
    public static final int ESQUERDA = 180;
    
    //TODO descobrir o tamanho da BBox da moto para ajustar esses valores
    public static final int LARGURA = 10;
    public static final int COMPRIMENTO = 20;

    public Moto(String nome, int x, int z, Cor cor, GL gl) {
	this.nome = nome;
	this.cor = cor;
	this.corNormal = cor;
	this.rastro = new Rastro(cor, TAMANHO_RASTRO);
	this.ajuste = getTransformacaoDeAjuste();
	this.transformacao = this.transformacao.transformMatrix(new Transformacao().atribuirTranslacao(0, 5, 0))
		.transformMatrix(new Transformacao().atribuirRotacaoY(Math.toRadians(90)));
	setPosicao(x, z);

	getOrLoadModelAndBBox(gl);
	carregarPontos();
    }

    private static OBJModel getOrLoadModelAndBBox(GL gl) {
	if (MODEL == null) {
	    MODEL = new OBJModel("data/lightcycle-med", 30f, gl, true);
	    PONTOS = tupla3ToPonto(MODEL.getVerts());
	    B_BOX = new BBox(PONTOS);
	    System.out.println(B_BOX);
	}
	return MODEL;
    }
    
    @Override
    protected List<Ponto> criarPontos() {
	return new ArrayList<>(PONTOS);
    }

    public static List<Ponto> tupla3ToPonto(List<Tuple3> tuplas) {
	List<Ponto> pontos = new ArrayList<Ponto>();
	for (Tuple3 tuple3 : tuplas) {
	    pontos.add(new Ponto(tuple3.getX(), tuple3.getY(), tuple3.getZ()));
	}
	return pontos;
    }

    public String getNome() {
	return nome;
    }

    public float getAngulo() {
	return angulo;
    }

    public void setColidido(boolean isColidido) {
	this.cor = isColidido ? this.corColisao : this.corNormal;
    }

    private void setPosicao(int x, int z) {
	Transformacao transTranslacao = new Transformacao();
	transTranslacao.atribuirTranslacao(x, 0, z);
	this.addMovimentacao(transTranslacao);
    }

    private Transformacao getTransformacaoDeAjuste() {
	Transformacao transTranslacao = new Transformacao();
	transTranslacao.atribuirRotacaoX(Math.toRadians(90));
	return transTranslacao;
    }

    public void setRastro(Rastro rastro) {
	this.rastro = rastro;
    }

    public Rastro getRastro() {
	return rastro;
    }

    @Override
    public boolean renderizar(GL gl) {
	float[] cor2 = { cor.r, cor.g, cor.b, 1f };
	gl.glPushMatrix();
	{
	    Transformacao trans = new Transformacao();
	    trans.setMatriz(transformacao.getMatriz());
	    trans = trans.transformMatrix(ajuste);

	    gl.glMultMatrixd(trans.getMatriz(), 0);
	    MODEL.draw(gl);

	    B_BOX.draw(gl);
	}
	gl.glPopMatrix();
	gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, cor2, 0);
	return false;
    }

    public void mover() {
	int moverX = (int) Math.cos(Math.toRadians(this.angulo)) * VELOCIDADE;
	int moverZ = (int) Math.sin(Math.toRadians(this.angulo)) * VELOCIDADE;
	Transformacao trans = new Transformacao();
	trans.atribuirTranslacao(moverX, 0, moverZ);
	this.addMovimentacao(trans);
	this.rastro.arrastar(this.transformacao.transformPoint(B_BOX.getCentro()));
    }

    public boolean estaColidindo(Poligono objeto) {
	BBox bbox = this.getBBoxTransformada();
	BBox bbox2 = objeto.getBBoxTransformada();
	return bbox.estaColidindo(bbox2);
    }

    public boolean estaColidindo(Rastro rastro) {
	BBox bboxTransformado = getBBoxTransformada();

	boolean existeColisao = false;
	int qtdIgnorar = rastro == this.rastro ? 3 : 0;

	List<BBox> bboxes = rastro.getBBoxes();
	for (int i = 0; i < bboxes.size() - qtdIgnorar; i++) {
	    if (bboxTransformado.estaColidindo(bboxes.get(i))) {
		existeColisao = true;
		System.out.println("Moto " + this.getNome() + " colidiu com o BBox nº " + i);
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

    public void girar(int graus) {
	this.angulo += graus;
	Transformacao trans = new Transformacao();
	trans.atribuirRotacaoY(Math.toRadians(-graus));
	this.addRotacao(trans);
    }

    @Override
    public BBox getBBox() {
	return B_BOX;
    }

    public boolean estaNaVertical() {
	return getAngulo() == Moto.BAIXO || getAngulo() == Moto.CIMA;
    }

}
