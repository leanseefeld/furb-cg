package br.furb.commons;

import java.awt.geom.Point2D;

public class Ponto2D extends Point2D {

	private double x;
	private double y;

	public void MoverX(double quantidade)
	{
		x += quantidade;
	}
	
	public void MoverY(double quantidade)
	{
		y += quantidade;
	}
	
	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setX(double x) {
		this.x = x;
	}

	@Override
	public void setLocation(double x, double y) {
		this.setX(x);
		this.setY(y);
	}

	public Ponto2D() {
	}

	public Ponto2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

}
