package br.furb.commons;

public class Cor {
	private float Red;
	private float Greend;
	private float Blue;

	public float getRed() {
		return Red;
	}

	public void setRed(float red) {
		Red = red;
	}

	public float getGreend() {
		return Greend;
	}

	public void setGreend(float greend) {
		Greend = greend;
	}

	public float getBlue() {
		return Blue;
	}

	public void setBlue(float blue) {
		Blue = blue;
	}

	public Cor(float red, float greend, float blue) {
		Red = red;
		Greend = greend;
		Blue = blue;
	}
}
