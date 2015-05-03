package br.furb;

public class Ponto {
	public int W = 1;
	public int X;
	public int Y;
	
	public Ponto(int x, int y)
	{
		this.X = x;
		this.Y = y;
	}
	
	public Ponto(double x, double y)
	{
		this.X = (int)x;
		this.Y = (int)y;
	}
	
	@Override
	public Ponto clone()
	{
		return new Ponto(this.X, this.Y);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "X: " + this.X + " Y:" + this.Y;
	}
}
