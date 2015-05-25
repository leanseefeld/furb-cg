package br.furb.bte;

import java.util.ArrayList;
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

    /**
     * Verifica se a linha que forma entre os pontos A e B passa por dentro da BBOX
     * 
     * @param pontoA
     * @param pontoB
     */
    public boolean estaSendoCruzadoPor(Ponto pontoA, Ponto pontoB) {

	List<Ponto> pontosVerificacao = new ArrayList<>();
	pontosVerificacao.add(new Ponto(this.getMaiorX(), this.getMaiorY()));
	pontosVerificacao.add(new Ponto(this.getMaiorX(), this.getMenorY()));
	pontosVerificacao.add(new Ponto(this.getMenorX(), this.getMenorY()));
	pontosVerificacao.add(new Ponto(this.getMenorX(), this.getMaiorY()));

	Ponto pontoBBoxA = null;
	Ponto pontoBBoxB = null;
	for (int i = 0; i < pontosVerificacao.size(); i++) {
	    pontoBBoxA = pontosVerificacao.get(i);
	    if (i + 1 < pontosVerificacao.size())
		pontoBBoxB = pontosVerificacao.get(i + 1);
	    else
		pontoBBoxB = pontosVerificacao.get(0);
	    
	    //TODO: E agora?

	}

	return false;
    }
}
