package br.furb.bte.objetos;

public class Cor {

    public static final int R = 0;
    public static final int G = 1;
    public static final int B = 2;

    private final float[] componentes;

    /**
     * @param r
     *            Red
     * @param g
     *            Green
     * @param b
     *            Blue
     */
    public Cor(float r, float g, float b) {
	componentes = new float[] { r, g, b };
    }

    public float getR() {
	return componentes[R];
    }

    public void setR(float r) {
	componentes[R] = r;
    }

    public float getG() {
	return componentes[G];
    }

    public void setG(float g) {
	componentes[G] = g;
    }

    public float getB() {
	return componentes[B];
    }

    public void setB(float b) {
	componentes[B] = b;
    }

}
