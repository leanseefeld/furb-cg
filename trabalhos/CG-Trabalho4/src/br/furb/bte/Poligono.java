package br.furb.bte;

import java.util.ArrayList;
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
	this.bbox = new BBox(this.pontos);
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

    public BBox getBBoxTransformada() {
	List<Ponto> pontosTransformados = new ArrayList<Ponto>(this.pontos.size());
	for (Ponto ponto : this.pontos) {
	    pontosTransformados.add(this.transformacao.transformPoint(ponto));
	}
	BBox bboxTransformado = new BBox(pontosTransformados);
	return bboxTransformado;
    }
}
