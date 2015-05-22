package br.furb.bte;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;

public abstract class Poligono extends ObjetoGrafico {

    private final List<Ponto> pontos;
    private int primitiva;

    public Poligono(GL gl) {
	super(gl);
	this.primitiva = GL.GL_LINE_LOOP;
	this.pontos = new ArrayList<Ponto>();
    }

    protected abstract List<Ponto> criarPontos();

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
	gl.glLineWidth(3);
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

	    if (this.isSelected()) {
	    }

	    super.desenhar();
	}
	gl.glPopMatrix();
    }

    private void destacarPontos() {
	for (Ponto ponto : this.pontos) {
	    gl.glPointSize(7);
	    gl.glColor3f(0f, 1f, 1f);
	    gl.glBegin(GL.GL_POINTS);
	    {
		gl.glVertex2d(ponto.X, ponto.Y);
	    }
	    gl.glEnd();
	}
    }

    public Ponto getPontoMaisProximo(Ponto pontoSelecionado) {
	double menorDistancia = Double.MAX_VALUE;
	Ponto pontoMaisProximo = null;
	Ponto pontoTrans = this.inverseTransformRecursive(pontoSelecionado.clone());
	for (Ponto ponto : this.pontos) {
	    if (pontoSelecionado != ponto) {
		double distancia = Math.abs(ponto.X - pontoTrans.X) + Math.abs(ponto.Y - pontoTrans.Y);
		if (distancia < menorDistancia) {
		    menorDistancia = distancia;
		    pontoMaisProximo = ponto;
		}
	    }
	}
	return pontoMaisProximo;
    }

    private float getPontoIntermediario(int a, int b, float peso) {
	return a + (b - a) * peso;
    }

    public boolean temPontos() {
	return this.pontos.size() > 0;
    }

    public void removerPonto(Ponto ponto) {
	this.pontos.remove(ponto);
    }
}
