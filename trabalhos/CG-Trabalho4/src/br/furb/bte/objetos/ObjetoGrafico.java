package br.furb.bte.objetos;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;

public abstract class ObjetoGrafico {

    protected List<ObjetoGrafico> objetosGraficos;

    /**
     * Objeto pai
     */
    protected ObjetoGrafico parent;

    public ObjetoGrafico getParent() {
	return parent;
    }

    public void setParent(ObjetoGrafico objetoGrafico) {
	this.parent = objetoGrafico;
    }

    /**
     * Adiciona um objeto filho
     * 
     * @param poligono
     */
    public void addFilho(Poligono poligono) {
	this.objetosGraficos.add(poligono);
	poligono.setParent(this);
    }

    public ObjetoGrafico() {
	this.objetosGraficos = new ArrayList<ObjetoGrafico>();
    }

    /**
     * Renderiza este objeto no {@code drawable} informado, tendo como retorno a indicação se será
     * necessário renderizar este objeto novamente no futuro (como quando há uma animação
     * ocorrendo).
     * 
     * @param gl
     *            {@link GL gl} para renderizar este objeto.
     * @return {@code true} se este objeto precisará ser renderizado novamente nos próximos quadros
     *         (como quando está sendo animado).
     */
    public boolean renderizar(GL gl) {
	renderizarFilhos(gl);
	return false;
    }

    protected boolean renderizarFilhos(GL gl) {
	boolean mustRenderAgain = false;
	for (ObjetoGrafico objetoGrafico : objetosGraficos) {
	    mustRenderAgain |= objetoGrafico.renderizar(gl);
	}
	return mustRenderAgain;
    }

    public void removerFilho(ObjetoGrafico objeto) {
	this.objetosGraficos.remove(objeto);
    }

    public void removerTodosFilhos() {
	this.objetosGraficos.clear();
    }
}
