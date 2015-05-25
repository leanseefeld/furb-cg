package br.furb.bte;

import java.util.List;
import javax.media.opengl.GL;

public abstract class Poligono extends ObjetoGrafico {

    protected final List<Ponto> pontos;
    protected int primitiva;
    private int largura;

    public Poligono(GL gl) {
	super(gl);
	this.primitiva = GL.GL_LINE_LOOP;
	this.pontos = this.criarPontos();
	this.largura = 1;
	this.criarBBox();
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

    /**
     * Monta a BBox em torno do objeto
     */
    public void criarBBox() {
	super.bbox = new BBox();
	super.bbox.setMaiorX(Integer.MIN_VALUE);
	super.bbox.setMenorX(Integer.MAX_VALUE);
	super.bbox.setMaiorY(Integer.MIN_VALUE);
	super.bbox.setMenorY(Integer.MAX_VALUE);
	for (Ponto ponto : pontos) {
	    if (ponto.X > this.bbox.getMaiorX())
		this.bbox.setMaiorX(ponto.X);
	    if (ponto.X < this.bbox.getMenorX())
		this.bbox.setMenorX(ponto.X);
	    if (ponto.Y > this.bbox.getMaiorY())
		this.bbox.setMaiorY(ponto.Y);
	    if (ponto.Y < this.bbox.getMenorY())
		this.bbox.setMenorY(ponto.Y);
	}
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
		    gl.glVertex2d(ponto.X, ponto.Y);
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
}
