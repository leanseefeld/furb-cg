package br.furb.commons;

public class Circulo2D {

	private Ponto2D localizacao;
	private int raio;
	// para otimizacao
	private double raioQuadrado;

	public Ponto2D getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(Ponto2D localizacao) {
		this.localizacao = localizacao;
	}

	public int getRaio() {
		return raio;
	}

	public double getRaioQuadrado() {
		return raioQuadrado;
	}
	
	public void setRaio(int raio) {
		this.raio = raio;
		this.raioQuadrado = Math.pow(raio, 2);
	}

	public Circulo2D(Ponto2D localizacao, int raio) {
		this.localizacao = localizacao;
		this.setRaio(raio);
	}
}
