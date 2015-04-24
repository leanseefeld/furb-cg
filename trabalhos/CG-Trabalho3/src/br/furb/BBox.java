package br.furb;

public class BBox {
	private int maiorX;
	private int menorX;
	private int maiorY;
	private int menorY;

	public int getMaiorX() {
		return maiorX;
	}

	public int getMenorX() {
		return menorX;
	}

	public int getMaiorY() {
		return maiorY;
	}

	public int getMenorY() {
		return menorY;
	}

	public void setMaiorX(int maiorX) {
		this.maiorX = maiorX;
	}

	public void setMenorX(int menorX) {
		this.menorX = menorX;
	}

	public void setMaiorY(int maiorY) {
		this.maiorY = maiorY;
	}

	public void setMenorY(int menorY) {
		this.menorY = menorY;
	}

	public BBox(int maiorX, int menorX, int maiorY, int menorY) {
		super();
		this.maiorX = maiorX;
		this.menorX = menorX;
		this.maiorY = maiorY;
		this.menorY = menorY;
	}

	public BBox() {
		super();
	}

	public Ponto getPontoCentral() {
		return new Ponto((this.maiorX + this.maiorX) / 2, (this.menorX + this.menorY) / 2);
	};

	public boolean estaDentro(Ponto ponto) {
		if (ponto.X > this.menorX && ponto.X < this.maiorX && ponto.Y > this.menorY && ponto.Y < this.maiorY) {
			return true;
		}
		return false;
	}
}
