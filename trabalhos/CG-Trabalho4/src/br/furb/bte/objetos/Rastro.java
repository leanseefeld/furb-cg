package br.furb.bte.objetos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.media.opengl.GL;
import br.furb.bte.Parametros;

public class Rastro extends Poligono {

    private static final int PONTO_MAIS_ALTO = 40;
    private static final int PONTO_MAIS_BAIXO = 0;
    private List<Ponto> rastro;
    private int tamanhoRastro;

    public Rastro(Cor cor, int tamanhoRastro) {
	this.cor = cor;
	this.primitiva = GL.GL_QUAD_STRIP;
	this.tamanhoRastro = tamanhoRastro;
	this.rastro = new ArrayList<Ponto>();
    }

    /**
     * Aumenta o rastro para a uma novação posição
     * @param novaPosicao
     */
    public void arrastar(Ponto novaPosicao) {
	rastro.add(novaPosicao);
	if (rastro.size() > tamanhoRastro) {
	    rastro.remove(0);
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
		for (Ponto ponto : rastro) {
		    gl.glVertex3d(ponto.x, PONTO_MAIS_BAIXO, ponto.z);
		    gl.glVertex3d(ponto.x, PONTO_MAIS_ALTO, ponto.z);
		}
	    }
	    gl.glEnd();
	    gl.glEnable(GL.GL_CULL_FACE);
	    
	    if(Parametros.DESENHAR_BBOX)
	    {
		List<BBox> bboxes = this.getBBoxes();
		for (BBox bBox : bboxes) {
		    bBox.draw(gl);
		}
	    }

	    renderizarFilhos(gl);
	}
	gl.glPopMatrix();
	return false;
    }

    /**
     * Retorna a BBox de cada reta do rastro
     * @return
     */
    public List<BBox> getBBoxes() {
	List<BBox> bboxes = new ArrayList<BBox>();
	int numeroMinimoPontos = 2;
	int limite = rastro.size() - numeroMinimoPontos;
	for (int i = 0; i < limite; i++) {
	    Ponto pontoA = rastro.get(i);
	    Ponto pontoB = rastro.get(++i);

	    int diferencaX = pontoA.x - pontoB.x;
	    int diferenaZ = pontoA.z - pontoB.z;

	    //Busca o ultimo ponto da reta
	    Ponto pontoC;
	    i++;
	    while (i < limite) {
		pontoC = rastro.get(i);
		if ((pontoB.x - pontoC.x) != diferencaX || (pontoB.z - pontoC.z) != diferenaZ) {
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
