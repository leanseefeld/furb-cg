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
}
