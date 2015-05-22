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
     * Inclui uma transformação para o objeto
     * 
     * @param transformacao
     */
    public void addTransformacao(Transformacao transformacao) {
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

    /**
     * Indica se o objeto está selecionado
     * 
     * @return
     */
    public boolean isSelected() {
	return Mundo.objetoSelecionado == this;
    }

    public void removerFilho(ObjetoGrafico objeto) {
	this.objetosGraficos.remove(objeto);
    }

    /**
     * Retorna a transformação da raíz até este ponto Ou seja, todas as transformações dos objetos
     * somadas até este ponto
     *
     * @return
     */
    public Transformacao getTransformacaoTotal() {
	Transformacao trans;
	if (this.parent != null)
	    trans = this.transformacao.transformMatrix(this.parent.getTransformacaoTotal());
	else {
	    trans = this.transformacao;
	}
	return trans;
    }

    /**
     * Transforma o ponto para o ponto de origem considerando todas as transformações dos objetos
     * pais
     * 
     * @param ponto
     * @return
     */
    public Ponto inverseTransformRecursive(Ponto ponto) {
	if (this.parent != null)
	    ponto = this.parent.inverseTransformRecursive(ponto);

	return this.transformacao.transformPointInverse(ponto);
    }
}
