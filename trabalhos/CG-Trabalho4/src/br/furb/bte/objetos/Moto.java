package br.furb.bte.objetos;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;
import br.furb.bte.LeitorObjeto.OBJModel;
import br.furb.bte.LeitorObjeto.Tuple3;

public class Moto extends Poligono {

    private int angulo;
    private Rastro rastro;
    private final Cor corColisao = new Cor(0, 0, 1);
    private final Cor corNormal;
    private final BBox bbox;
    private final OBJModel moto;
    private final Transformacao ajuste;
    private final String nome;

    private static final int TAMANHO_RASTRO = 300;
    private static final int VELOCIDADE = 5;

    public static final int CIMA = -90;
    public static final int DIREITA = 0;
    public static final int BAIXO = 90;
    public static final int ESQUERDA = 180;

    public Moto(String nome, int x, int z, Cor cor, GL gl) {
	this.nome = nome;
	this.cor = cor;
	this.corNormal = cor;
	this.rastro = new Rastro(cor, TAMANHO_RASTRO);
	this.ajuste = getTransformacaoDeAjuste();
	this.transformacao = this.transformacao.transformMatrix(new Transformacao().atribuirTranslacao(0, 7, 0));
	getTransformacaoDeAjuste();
	setPosicao(x, z);

	this.moto = new OBJModel("data/moto", 30f, gl, true);
	bbox = new BBox(tupla3ToPonto(moto.getVerts()));

	//	bbox = new BBox(25, -25, 50, -50, 50, -50);
    }

    public List<Ponto> tupla3ToPonto(List<Tuple3> tuplas) {
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
	//	this.transformacao = this.transformacao.transformMatrix(transTranslacao);
	//	ajusteMoto = transTranslacao;
	return transTranslacao;
	//	this.addMovimentacao(transTranslacao);
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
	gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, cor2, 0);
	gl.glPushMatrix();
	{
	    Transformacao trans = new Transformacao();
	    trans.setMatriz(transformacao.getMatriz());
	    trans = trans.transformMatrix(ajuste);

	    gl.glMultMatrixd(trans.getMatriz(), 0);
	    this.moto.draw(gl);

	    this.bbox.draw(gl);
	}
	gl.glPopMatrix();
	return false;
    }

    @Override
    protected List<Ponto> criarPontos() {
	List<Ponto> pontos = new ArrayList<>();

	//	for (Tuple3 tup : moto.getVerts()) {
	//	    pontos.add(new Ponto(tup.getX(), tup.getY(), tup.getZ()));
	//	}
	//		}else{
	//			// Cima
	//			pontos.add(new Ponto(+17, +10, +4));
	//			pontos.add(new Ponto(+17, +10, -4));
	//			pontos.add(new Ponto(-19, +10, -6));
	//			pontos.add(new Ponto(-19, +10, +6));
	//	
	//			// Baixo
	//			pontos.add(new Ponto(-19, +0, +6));
	//			pontos.add(new Ponto(-19, +0, -6));
	//			pontos.add(new Ponto(+17, +0, -4));
	//			pontos.add(new Ponto(+17, +0, +4));
	//	
	//			// Frente
	//			pontos.add(new Ponto(-19, +10, -6));
	//			pontos.add(new Ponto(-19, +0, -6));
	//			pontos.add(new Ponto(-19, +0, +6));
	//			pontos.add(new Ponto(-19, +10, +6));
	//	
	//			// Traz
	//			pontos.add(new Ponto(+17, +10, +4));
	//			pontos.add(new Ponto(+17, +0, +4));
	//			pontos.add(new Ponto(+17, +0, -4));
	//			pontos.add(new Ponto(+17, +10, -4));
	//	
	//			// Lateral Esquerda
	//			pontos.add(new Ponto(+17, +10, +4));
	//			pontos.add(new Ponto(-19, +10, +6));
	//			pontos.add(new Ponto(-19, +0, +6));
	//			pontos.add(new Ponto(+17, +0, +4));
	//	
	//			// Lateral Direita
	//			pontos.add(new Ponto(+17, +0, -4));
	//			pontos.add(new Ponto(-19, +0, -6));
	//			pontos.add(new Ponto(-19, +10, -6));
	//			pontos.add(new Ponto(+17, +10, -4));
	//		}
	return pontos;
    }

    public void mover() {
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
	return this.bbox;
    }

    @Override
    public BBox getBBoxTransformada() {
	return this.bbox.getTransformado(this.transformacao);
    }
    //    public boolean renderizar(GL gl) {
    //	float[] cor2 = { cor.r, cor.g, cor.b, 1f };
    //	gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, cor2, 0);
    //
    //	// gl.glColor3f(cor.R, cor.G, cor.B);
    //
    //	gl.glPushMatrix();
    //	{
    //	    gl.glMultMatrixd(transformacao.getMatriz(), 0);
    //
    //	    gl.glBegin(primitiva);
    //	    {
    //		if (teste) {
    //		    System.out.println("Testando2 2 2 2 2");
    //		}
    //		for (Ponto ponto : getPontos()) {
    //		    gl.glVertex3d(ponto.x, ponto.y, ponto.z);
    //		}
    //
    //		//moto.draw(gl);
    //	    }
    //	    gl.glEnd();
    //	}
    //	gl.glPopMatrix();
    //	// return super.renderizar(gl);
    //	teste = !teste;
    //	return true;
    //    }

    //    @Override
    //    public BBox getBBoxTransformada() {
    //	// TODO: Ver forma mais performática de fazer a colisão
    //	List<Ponto> pontosTrans = new ArrayList<Ponto>();
    //	for (Ponto ponto : getPontos()) {
    //	    pontosTrans.add(this.transformacao.transformPoint(ponto));
    //	}
    //	BBox bbox = new BBox(pontosTrans);
    //
    //	// return this.bbox.transformar(this.transformacao);
    //	return bbox;
    //    }

}
