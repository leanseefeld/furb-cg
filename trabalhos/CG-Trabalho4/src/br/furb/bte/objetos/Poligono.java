package br.furb.bte.objetos;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;

public abstract class Poligono extends ObjetoGrafico {

    private List<Ponto> pontos;
    protected int primitiva;
    protected Cor cor;
    protected BBox bbox;
    protected Transformacao transformacao;

    public Poligono() {
	this.transformacao = new Transformacao();
	this.cor = new Cor(0f, 0f, 0f);
    }

    protected List<Ponto> criarPontos() {
	return new ArrayList<Ponto>();
    }
    
    protected void carregarPontos() {
	this.pontos = criarPontos();
	this.bbox = new BBox(this.pontos);
    }

    public final Cor getCor() {
	return cor;
    }

    public final void setCor(Cor cor) {
	this.cor = cor;
    }

    public final int getPrimitiva() {
	return primitiva;
    }

    public final void setPrimitiva(int primitiva) {
	this.primitiva = primitiva;
    }

    @Override
    public boolean renderizar(GL gl) {
	float[] cor2 = { cor.r, cor.g, cor.b, 1f };
	gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, cor2, 0);

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
	    this.bbox.draw(gl);
	}
	gl.glPopMatrix();
	return super.renderizar(gl);
    }

    /**
     * Inclui um movimentação no objeto
     * 
     * @param transformacao
     */
    public final void addMovimentacao(Transformacao transformacao) {
	this.transformacao = transformacao.transformMatrix(this.transformacao);
    }

    /**
     * Inclui uma expansão no objeto
     * 
     * @param transformacao
     */
    public final void addExpansao(Transformacao transformacao) {
	this.transformacao = transformacao.transformMatrix(this.transformacao);
    }

    /**
     * Incluir uma rotação no objeto
     * 
     * @param transformacao
     */
    public final void addRotacao(Transformacao transformacao) {
	this.transformacao = this.transformacao.transformMatrix(transformacao);
    }

    public final Transformacao getTransformacao() {
	return transformacao;
    }

    public BBox getBBox() {
	return bbox;
    }
    
    /**
     * Retorna a BBox com a transformação do objeto aplicada
     * 
     * @return
     */
    public BBox getBBoxTransformada() {
	List<Ponto> pontosTrans = new ArrayList<Ponto>();
	for (Ponto ponto : this.pontos) {
	    pontosTrans.add(this.transformacao.transformPoint(ponto));
	}
	BBox bbox = new BBox(pontosTrans);
	return bbox;
    }
}
