package br.furb.bte.objetos;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;

public abstract class Poligono extends ObjetoGrafico {

    private ListaPontos pontos;
    protected int primitiva;
    protected Cor cor;
    protected BBox bbox;
    protected Transformacao transformacao;

    public Poligono() {
	this.transformacao = new Transformacao();
	this.cor = new Cor(0f, 0f, 0f);
    }

    protected abstract List<Ponto> criarPontos();

    public void setPontos(List<Ponto> pontos) {
	this.pontos = new ListaPontos(pontos);
	this.bbox = new BBox(pontos);
    }

    public void addPonto(Ponto ponto) {
	this.pontos.add(ponto);
    }

    public ListaPontos getPontos() {
	return this.pontos;
    }

    public int getNumeroPontos() {
	return pontos.tamanho();
    }

    public Cor getCor() {
	return cor;
    }

    public void setCor(Cor cor) {
	this.cor = cor;
    }

    public int getPrimitiva() {
	return primitiva;
    }

    public void setPrimitiva(int primitiva) {
	this.primitiva = primitiva;
    }

    @Override
    public boolean renderizar(GL gl) {
	float[] cor2 = { cor.r, cor.g, cor.b, 1f };
	gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, cor2, 0);

	//		gl.glColor3f(cor.R, cor.G, cor.B);

	gl.glPushMatrix();
	{
	    gl.glMultMatrixd(transformacao.getMatriz(), 0);

	    gl.glBegin(primitiva);
	    {
		for (Ponto ponto : pontos) {
		    gl.glVertex3d(ponto.x, ponto.y, ponto.z);
		}
	    }
	    gl.glEnd();

	}
	gl.glPopMatrix();
	return super.renderizar(gl);
    }

    public boolean temPontos() {
	return !this.pontos.vazio();
    }

    public void removerPonto(int index) {
	this.pontos.remover(index);
    }

    /**
     * Retorna a BBox com a transformação do objeto aplicada
     * 
     * @return
     */
    public BBox getBBoxTransformada() {
	//TODO: Ver forma mais performática de fazer a colisão
	List<Ponto> pontosTrans = new ArrayList<Ponto>();
	for (Ponto ponto : this.pontos) {
	    pontosTrans.add(this.transformacao.transformPoint(ponto));
	}
	BBox bbox = new BBox(pontosTrans);

	//	return this.bbox.transformar(this.transformacao);
	return bbox;
    }

    /**
     * Inclui um movimentação no objeto
     * 
     * @param transformacao
     */
    public void addMovimentacao(Transformacao transformacao) {
	this.transformacao = transformacao.transformMatrix(this.transformacao);
    }

    /**
     * Inclui uma expansão no objeto
     * 
     * @param transformacao
     */
    public void addExpansao(Transformacao transformacao) {
	this.transformacao = transformacao.transformMatrix(this.transformacao);
    }

    /**
     * Incluir uma rotação no objeto
     * 
     * @param transformacao
     */
    public void addRotacao(Transformacao transformacao) {
	this.transformacao = this.transformacao.transformMatrix(transformacao);
    }

    public Transformacao getTransformacao() {
	return transformacao;
    }

    public BBox getBBox() {
	return bbox;
    }

}
