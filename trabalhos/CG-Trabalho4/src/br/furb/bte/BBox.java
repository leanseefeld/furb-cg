package br.furb.bte;

import java.util.Arrays;
import java.util.List;

public class BBox {

    private Ponto maior;
    private Ponto menor;

    public BBox() {
	this.maior = new Ponto(0, 0);
	this.menor = new Ponto(0, 0);
    }

    public BBox(int maiorX, int menorX, int maiorY, int menorY) {
	this.maior = new Ponto(maiorX, maiorY);
	this.menor = new Ponto(menorX, menorY);
    }

    public BBox(List<Ponto> pontos) {
	this.maior = new Ponto(0, 0);
	this.menor = new Ponto(0, 0);
	this.setMaiorX(Integer.MIN_VALUE);
	this.setMenorX(Integer.MAX_VALUE);
	this.setMaiorY(Integer.MIN_VALUE);
	this.setMenorY(Integer.MAX_VALUE);
	for (Ponto ponto : pontos) {
	    if (ponto.X > this.getMaiorX())
		this.setMaiorX(ponto.X);
	    if (ponto.X < this.getMenorX())
		this.setMenorX(ponto.X);
	    if (ponto.Y > this.getMaiorY())
		this.setMaiorY(ponto.Y);
	    if (ponto.Y < this.getMenorY())
		this.setMenorY(ponto.Y);
	}
    }

    public int getMaiorX() {
	return maior.X;
    }

    public int getMenorX() {
	return menor.X;
    }

    public int getMaiorY() {
	return maior.Y;
    }

    public int getMenorY() {
	return menor.Y;
    }

    public void setMaiorX(int maiorX) {
	this.maior.X = maiorX;
    }

    public void setMenorX(int menorX) {
	this.menor.X = menorX;
    }

    public void setMaiorY(int maiorY) {
	this.maior.Y = maiorY;
    }

    public void setMenorY(int menorY) {
	this.menor.Y = menorY;
    }

    /**
     * Verifica se o ponto está dentro da BBox
     * 
     * @param ponto
     * @return
     */
    public boolean estaDentro(Ponto ponto) {
	if (ponto.X > this.menor.X && ponto.X < this.maior.X //
		&& ponto.Y > this.menor.Y && ponto.Y < this.maior.Y) {
	    return true;
	}
	return false;
    }

    /**
     * Verifica se o ponto está dentro da BBox
     * 
     * @param ponto
     * @return
     */
    public boolean estaDentro(Ponto ponto, Transformacao transformacao) {
	Ponto maiorTrans = transformacao.transformPoint(maior);
	Ponto menorTrans = transformacao.transformPoint(menor);
	if (ponto.X > menorTrans.X && ponto.X < maiorTrans.X //
		&& ponto.Y > menorTrans.Y && ponto.Y < maiorTrans.Y) {
	    return true;
	}
	return false;
    }

    public Ponto getCentro() {
	return new Ponto((this.getMaiorX() + this.getMenorX()) / 2, (this.getMaiorY() + this.getMenorY()) / 2);
    }

    public boolean estaColidindo(BBox bbox) {
	if (this.getMenorX() > bbox.getMaiorX())
	    return false;
	if (this.getMaiorX() < bbox.getMenorX())
	    return false;
	if (this.getMenorY() > bbox.getMaiorY())
	    return false;
	if (this.getMaiorY() < bbox.getMenorY())
	    return false;
	return true;
    }

    /**
     * Verifica se a linha que forma entre os pontos A e B passa por dentro da BBOX
     * 
     * @param pontoA
     * @param pontoB
     */
    public boolean estaSendoCruzadoPor(Ponto pontoA, Ponto pontoB) {
	BBox bboxLinha = new BBox(Arrays.asList(pontoA, pontoB));
	boolean estaColidindo = this.estaColidindo(bboxLinha);
	return estaColidindo;
    }
}
