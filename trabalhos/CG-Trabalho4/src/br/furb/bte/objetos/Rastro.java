package br.furb.bte.objetos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.media.opengl.GL;

public class Rastro extends Poligono {

    private static final int TAMANHO_RASTRO = 300;
    private static final int PONTO_MAIS_ALTO = 10;
    private static final int PONTO_MAIS_BAIXO = 0;

    public Rastro(Cor cor) {
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
    public boolean renderizar(GL gl) {
	gl.glColor3d(cor.r - 0.2, cor.g - 0.2, cor.b - 0.2);

	gl.glPushMatrix();
	{
	    gl.glMultMatrixd(transformacao.getMatriz(), 0);

	    gl.glDisable(GL.GL_CULL_FACE);
	    gl.glBegin(primitiva);
	    {
		for (Ponto ponto : getPontos()) {
		    gl.glVertex3d(ponto.x, PONTO_MAIS_BAIXO, ponto.z);
		    gl.glVertex3d(ponto.x, PONTO_MAIS_ALTO, ponto.z);
		}
	    }
	    gl.glEnd();
	    gl.glEnable(GL.GL_CULL_FACE);

	    renderizarFilhos(gl);
	}
	gl.glPopMatrix();
	return false;
    }

    public List<BBox> getBBoxes() {
	ListaPontos pontos = getPontos();
	List<BBox> bboxes = new ArrayList<BBox>();
	int numeroMinimoPontos = 2;
	for (int i = 0; i < pontos.tamanho() - numeroMinimoPontos; i++) {
	    Ponto pontoA = pontos.get(i);
	    Ponto pontoB = pontos.get(++i);

	    int diferenaX = pontoA.x - pontoB.x;
	    int diferenaZ = pontoA.z - pontoB.z;

	    //Busca o ultimo ponto da reta
	    Ponto pontoC;
	    i++;
	    while (i < pontos.tamanho() - numeroMinimoPontos) {
		pontoC = pontos.get(i);
		if ((pontoB.x - pontoC.x) != diferenaX || (pontoB.z - pontoC.z) != diferenaZ) {
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
