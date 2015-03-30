package br.furb.commons;

public class Cor {
	private double Red;
	private double Greend;
	private double Blue;

	public double getRed() {
		return Red;
	}

	public void setRed(double red) {
		Red = red;
	}

	public double getGreend() {
		return Greend;
	}

	public void setGreend(double greend) {
		Greend = greend;
	}

	public double getBlue() {
		return Blue;
	}

	public void setBlue(double blue) {
		Blue = blue;
	}

	public Cor(double red, double greend, double blue) {
		Red = red;
		Greend = greend;
		Blue = blue;
	}
}
