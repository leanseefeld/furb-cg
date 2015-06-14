package br.furb.bte.objetos;

import object.Tuple3;

public class Ponto {

    public int w = 1;
    public int x;
    public int y;
    public int z;

    public Ponto(int x, int y, int z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }

    public Ponto(double x, double y, double z) {
	this.x = (int) x;
	this.y = (int) y;
	this.z = (int) z;
    }

    @Override
    public Ponto clone() {
	return new Ponto(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
	return "X: " + this.x + " Y:" + this.y + " Z:" + this.z;
    }

    public boolean mesmaPosicao(Ponto ponto) {
	return this.x == ponto.x && this.y == ponto.y && this.z == ponto.z;
    }

	public static Ponto Tuple3toPoint(Tuple3 tup) {
		return new Ponto(tup.getX(), tup.getY(), tup.getZ());
	}
}
