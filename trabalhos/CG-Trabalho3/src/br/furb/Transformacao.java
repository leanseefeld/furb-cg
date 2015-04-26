package br.furb;

public class Transformacao {
	private double[] matriz = new double[] {//
	/*    */1, 0, 0, 0,//
			0, 1, 0, 0,//
			0, 0, 1, 0,//
			0, 0, 0, 1 };

	public double[] getMatriz() {
		return matriz;
	}

	public void setMatriz(double[] matriz) {
		this.matriz = matriz;
	}

	public double getTrasnlacaoX() {
		return this.matriz[12];
	}

	public double getTrasnlacaoY() {
		return this.matriz[13];
	}
}
