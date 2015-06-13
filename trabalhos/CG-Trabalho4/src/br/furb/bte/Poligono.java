package br.furb.bte;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;

public abstract class Poligono extends ObjetoGrafico {

    private List<Ponto> pontos;
    protected int primitiva;
    protected Cor cor;
    protected BBox bbox;
    protected Transformacao transformacao;

    public Poligono(GL gl) {
	super(gl);
	this.transformacao = new Transformacao();
	this.cor = new Cor(0f, 0f, 0f);
    }

    protected abstract List<Ponto> criarPontos();

    public void setPontos(List<Ponto> pontos) {
	this.pontos = pontos;
	this.bbox = new BBox(this.pontos);
    }

    public void addPonto(Ponto ponto) {
	this.pontos.add(ponto);
    }

    public List<Ponto> getPontos() {
	return this.pontos;
    }

    public int getNumeroPontos() {
	return this.pontos.size();
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
    public void desenhar() {
	float[] cor2 = { cor.R, cor.G, cor.B, 1f };
	gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, cor2, 0);

//		gl.glColor3f(cor.R, cor.G, cor.B);

	gl.glPushMatrix();
	{
	    gl.glMultMatrixd(transformacao.getMatriz(), 0);

	    gl.glBegin(primitiva);
	    {
		for (Ponto ponto : pontos) {
		    gl.glVertex3d(ponto.X, ponto.Y, ponto.Z);
		}
	    }
	    gl.glEnd();

	    super.desenhar();
	}
	gl.glPopMatrix();
    }

    public boolean temPontos() {
	return this.pontos.size() > 0;
    }

    public void removerPonto(Ponto ponto) {
	this.pontos.remove(ponto);
    }

    public void removerPonto(int index) {
	this.pontos.remove(index);
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

}
