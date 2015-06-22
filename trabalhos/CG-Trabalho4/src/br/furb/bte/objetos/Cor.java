package br.furb.bte.objetos;

public class Cor {

    public float r;
    public float g;
    public float b;

    /**
     * @param r
     *            Red
     * @param g
     *            Green
     * @param b
     *            Blue
     */
    public Cor(float r, float g, float b) {
	this.r = r;
	this.g = g;
	this.b = b;
    }
    @Override
    public String toString() {
        return String.format("r=%f, g=%f, b=%f", r, g, b);
    }

}
