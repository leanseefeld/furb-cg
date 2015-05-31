package br.furb.bte;

public class Ponto {

    public int W = 1;
    public int X;
    public int Y;
    public int Z;

    public Ponto(int x, int y, int z) {
	this.X = x;
	this.Y = y;
	this.Z = z;
    }

    public Ponto(double x, double y, double z) {
	this.X = (int) x;
	this.Y = (int) y;
	this.Z = (int) z;
    }

    @Override
    public Ponto clone() {
	return new Ponto(this.X, this.Y, this.Z);
    }

    @Override
    public String toString() {
	// TODO Auto-generated method stub
	return "X: " + this.X + " Y:" + this.Y + " Z:" + this.Z;
    }

    public boolean mesmaPosicao(Ponto ponto) {
	return this.X == ponto.X && this.Y == ponto.Y && this.Z == ponto.Z;
    }
}
