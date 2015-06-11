package br.furb.bte;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;

public abstract class ObjetoGrafico {

    protected List<ObjetoGrafico> objetosGraficos;
    protected GL gl;
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

    public ObjetoGrafico(GL gl) {
	super();
	this.objetosGraficos = new ArrayList<ObjetoGrafico>();
	this.gl = gl;
    }

    /**
     * Desenha o objeto
     */
    public void desenhar() {
	desenharFilhos();
    }
    
    protected void desenharFilhos() {
	for (ObjetoGrafico objetoGrafico : objetosGraficos) {
	    objetoGrafico.desenhar();
	}
    }

    public void removerFilho(ObjetoGrafico objeto) {
	this.objetosGraficos.remove(objeto);
    }

    public void removerTodosFilhos() {
	this.objetosGraficos.clear();
    }
}
