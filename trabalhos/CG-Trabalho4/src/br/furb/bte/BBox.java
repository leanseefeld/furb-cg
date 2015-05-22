package br.furb.bte;

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
}
