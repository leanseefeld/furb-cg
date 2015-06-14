package br.furb.bte.objetos;

import java.util.Arrays;
import java.util.List;

public class BBox {

    private Ponto maior;
    private Ponto menor;

    public BBox() {
	this.maior = new Ponto(0, 0, 0);
	this.menor = new Ponto(0, 0, 0);
    }

    public BBox(Ponto maior, Ponto menor) {
	this.maior = maior;
	this.menor = menor;
    }

    public BBox(int maiorX, int menorX, int maiorY, int menorY, int maiorZ, int menorZ) {
	this.maior = new Ponto(maiorX, maiorY, maiorZ);
	this.menor = new Ponto(menorX, menorY, menorZ);
    }

    public BBox(List<Ponto> pontos) {
	this.maior = new Ponto(0, 0, 0);
	this.menor = new Ponto(0, 0, 0);
	this.setMaiorX(Integer.MIN_VALUE);
	this.setMenorX(Integer.MAX_VALUE);
	this.setMaiorY(Integer.MIN_VALUE);
	this.setMenorY(Integer.MAX_VALUE);
	this.setMaiorZ(Integer.MIN_VALUE);
	this.setMenorZ(Integer.MAX_VALUE);
	for (Ponto ponto : pontos) {
	    if (ponto.X > this.getMaiorX())
		this.setMaiorX(ponto.X);
	    if (ponto.X < this.getMenorX())
		this.setMenorX(ponto.X);
	    if (ponto.Y > this.getMaiorY())
		this.setMaiorY(ponto.Y);
	    if (ponto.Y < this.getMenorY())
		this.setMenorY(ponto.Y);
	    if (ponto.Z > this.getMaiorZ())
		this.setMaiorZ(ponto.Z);
	    if (ponto.Z < this.getMenorZ())
		this.setMenorZ(ponto.Z);
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

    public int getMaiorZ() {
	return maior.Z;
    }

    public int getMenorZ() {
	return menor.Z;
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

    public void setMaiorZ(int maiorZ) {
	this.maior.Z = maiorZ;
    }

    public void setMenorZ(int menorZ) {
	this.menor.Z = menorZ;
    }

    /**
     * Verifica se o ponto está dentro da BBox
     * 
     * @param ponto
     * @return
     */
    public boolean estaDentro(Ponto ponto) {
	if (ponto.X > this.menor.X && ponto.X < this.maior.X //
		&& ponto.Y > this.menor.Y && ponto.Y < this.maior.Y// 
		&& ponto.Z > this.menor.Z && ponto.Z < this.maior.Z) {
	    return true;
	}
	return false;
    }

    public Ponto getCentro() {
	return new Ponto(//
		(this.getMaiorX() + this.getMenorX()) / 2, //
		(this.getMaiorY() + this.getMenorY()) / 2, //
		(this.getMaiorZ() + this.getMenorZ()) / 2);
    }

    /**
     * Returna true se uma parte de ambos os BBox estão no mesmo espaço
     * 
     * @param bbox
     * @return
     */
    public boolean estaColidindo(BBox bbox) {
	if (this.getMenorX() > bbox.getMaiorX())
	    return false;
	if (this.getMaiorX() < bbox.getMenorX())
	    return false;
	if (this.getMenorY() > bbox.getMaiorY())
	    return false;
	if (this.getMaiorY() < bbox.getMenorY())
	    return false;
	if (this.getMenorZ() > bbox.getMaiorZ())
	    return false;
	if (this.getMaiorZ() < bbox.getMenorZ())
	    return false;
	return true;
    }

    /**
     * Retorna true se esse BBox sobre o bbox recebido por parametro. Ou seja, se está encima
     * 
     * @param bbox
     * @return
     */
    public boolean estaSob(BBox bbox) {
	if (this.getMenorX() >= bbox.getMenorX() && this.getMaiorX() <= bbox.getMaiorX()
		&& this.getMenorZ() >= bbox.getMenorZ() && this.getMaiorZ() <= bbox.getMaiorZ())
	    return true;
	return false;
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

    public Ponto getMaior() {
	return this.maior;
    }

    public Ponto getMenor() {
	return this.menor;
    }

    public BBox transformar(Transformacao transformacao) {
	Ponto maiorTrans = transformacao.transformPoint(this.maior);
	Ponto menorTrans = transformacao.transformPoint(this.menor);
	return new BBox(maiorTrans, menorTrans);
    }
}
