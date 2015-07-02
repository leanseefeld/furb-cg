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
    
    /**
     * @param r
     *            Red
     * @param g
     *            Green
     * @param b
     *            Blue
     */
    public Cor(double r, double g, double b) {
	this.r = (float)r;
	this.g = (float)g;
	this.b = (float)b;
    }

    @Override
    public String toString() {
	return String.format("r=%f, g=%f, b=%f", r, g, b);
    }

}
