package br.furb.bte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.media.opengl.GL;

public class Rastro extends Poligono {

    private static final int TAMANHO_RASTRO = 500;
    private static final int PONTO_MAIS_ALTO = 10;
    private static final int PONTO_MAIS_BAIXO = 0;

    public Rastro(GL gl, Cor cor) {
	super(gl);
	this.cor = cor;
	this.primitiva = GL.GL_QUAD_STRIP;
	this.setPontos(this.criarPontos());
    }

    @Override
    protected List<Ponto> criarPontos() {
	return new ArrayList<>(TAMANHO_RASTRO);
    }

    public void arrastar(Ponto novaPosicao) {
	this.addPonto(novaPosicao);

	if (this.getNumeroPontos() > TAMANHO_RASTRO * 2) {
	    this.removerPonto(0);
	}
    }

    @Override
    public void desenhar() {
	gl.glColor3d(cor.R - 0.2, cor.G - 0.2, cor.B - 0.2);

	gl.glPushMatrix();
	{
	    gl.glMultMatrixd(transformacao.getMatriz(), 0);

	    gl.glBegin(primitiva);
	    {
		for (int i = this.getPontos().size() - 1; i >= 0; i--) {
		    gl.glVertex3d(this.getPontos().get(i).X, PONTO_MAIS_BAIXO, this.getPontos().get(i).Z);
		    gl.glVertex3d(this.getPontos().get(i).X, PONTO_MAIS_ALTO, this.getPontos().get(i).Z);
		}

		for (int i = 0; i < this.getPontos().size(); i++) {
		    gl.glVertex3d(this.getPontos().get(i).X, PONTO_MAIS_BAIXO, this.getPontos().get(i).Z);
		    gl.glVertex3d(this.getPontos().get(i).X, PONTO_MAIS_ALTO, this.getPontos().get(i).Z);
		}
	    }
	    gl.glEnd();

	    super.desenhar();
	}
	gl.glPopMatrix();
    }

    public List<BBox> getBBoxes() {
	List<Ponto> pontos = this.getPontos();
	List<BBox> bboxes = new ArrayList<BBox>();
	for (int i = 0; i < pontos.size() - 2; i += 2) {
	    BBox bbox = new BBox(Arrays.asList(//
		    pontos.get(i), //
		    pontos.get(i + 1))); //
	    bboxes.add(bbox);
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
	}
	return bboxes;
    }
}
