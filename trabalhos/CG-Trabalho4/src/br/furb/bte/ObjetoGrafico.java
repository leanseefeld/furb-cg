package br.furb.bte;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;

public abstract class ObjetoGrafico {

    protected List<ObjetoGrafico> objetosGraficos;
    protected Cor cor;
    protected GL gl;
    protected BBox bbox;
    protected Transformacao transformacao;
    /**
     * Objeto pai
     */
    protected ObjetoGrafico parent;

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

    public ObjetoGrafico getParent() {
	return parent;
    }

    public void setParent(ObjetoGrafico objetoGrafico) {
	this.parent = objetoGrafico;
    }

    public Cor getCor() {
	return cor;
    }

    public void setCor(Cor cor) {
	this.cor = cor;
    }

    /**
     * Adiciona um objeto filho
     * 
     * @param poligono
     */
    public void addFilho(Poligono poligono) {
	this.objetosGraficos.add(poligono);
	poligono.criarBBox();
	poligono.setParent(this);
    }

    public ObjetoGrafico(GL gl) {
	super();
	this.objetosGraficos = new ArrayList<ObjetoGrafico>();
	this.gl = gl;
	this.transformacao = new Transformacao();
	this.cor = new Cor(0f, 0f, 0f);
    }

    /**
     * Desenha o objeto
     */
    public void desenhar() {
	for (ObjetoGrafico objetoGrafico : objetosGraficos) {
	    objetoGrafico.desenhar();
	}
    }

    public void removerFilho(ObjetoGrafico objeto) {
	this.objetosGraficos.remove(objeto);
    }
}
