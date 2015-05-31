package br.furb.bte;

import java.util.List;
import javax.media.opengl.GL;

public abstract class Poligono extends ObjetoGrafico {

    protected final List<Ponto> pontos;
    protected int primitiva;
    private int largura;
    protected Cor cor;
    protected BBox bbox;
    protected Transformacao transformacao;

    public Poligono(GL gl) {
	super(gl);
	this.primitiva = GL.GL_LINE_LOOP;
	this.pontos = this.criarPontos();
	this.largura = 1;
	this.bbox = new BBox(this.pontos);
	this.transformacao = new Transformacao();
	this.cor = new Cor(0f, 0f, 0f);
    }

    public Cor getCor() {
	return cor;
    }

    public void setCor(Cor cor) {
	this.cor = cor;
    }

    protected abstract List<Ponto> criarPontos();

    protected void setLarguraLinha(int largura) {
	this.largura = largura;
    }

    public int getPrimitiva() {
	return primitiva;
    }

    public void setPrimitiva(int primitiva) {
	this.primitiva = primitiva;
    }

    @Override
    public void desenhar() {
	gl.glLineWidth(this.largura);
	gl.glColor3f(cor.R, cor.G, cor.B);

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

    /**
     * Retorna a BBox com a transformação do objeto aplicada
     * @return
     */
    public BBox getBBoxTransformada() {
	return this.bbox.transformar(this.transformacao);
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
