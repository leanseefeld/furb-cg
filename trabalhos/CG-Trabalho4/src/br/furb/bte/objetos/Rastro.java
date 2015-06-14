package br.furb.bte.objetos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.media.opengl.GL;

public class Rastro extends Poligono {

    private static final int TAMANHO_RASTRO = 300;
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

	    gl.glDisable(GL.GL_CULL_FACE);
	    gl.glBegin(primitiva);
	    {
		for (int i = 0; i < this.getPontos().size(); i++) {
		    gl.glVertex3d(this.getPontos().get(i).X, PONTO_MAIS_BAIXO, this.getPontos().get(i).Z);
		    gl.glVertex3d(this.getPontos().get(i).X, PONTO_MAIS_ALTO, this.getPontos().get(i).Z);
		}
	    }
	    gl.glEnd();
	    gl.glEnable(GL.GL_CULL_FACE);

	    desenharFilhos();
	}
	gl.glPopMatrix();
    }

    public List<BBox> getBBoxes() {
	List<Ponto> pontos = this.getPontos();
	List<BBox> bboxes = new ArrayList<BBox>();
	int numeroMinimoPontos = 2;
	for (int i = 0; i < pontos.size() - numeroMinimoPontos; i++) {
	    Ponto pontoA = pontos.get(i);
	    Ponto pontoB = pontos.get(++i);

	    int diferenaX = pontoA.X - pontoB.X;
	    int diferenaZ = pontoA.Z - pontoB.Z;

	    //Busca o ultimo ponto da reta
	    Ponto pontoC;
	    i++;
	    while (i < pontos.size() - numeroMinimoPontos) {
		pontoC = pontos.get(i);
		if ((pontoB.X - pontoC.X) != diferenaX || (pontoB.Z - pontoC.Z) != diferenaZ) {
		    break;
		}
		pontoB = pontoC;
		i++;
	    }
	    i--;

	    BBox bbox = new BBox(Arrays.asList(pontoA, pontoB));
	    bboxes.add(bbox);

	}
	return bboxes;
    }
}
