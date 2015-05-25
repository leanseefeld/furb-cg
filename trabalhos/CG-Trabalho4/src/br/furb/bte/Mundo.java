package br.furb.bte;

import javax.media.opengl.GL;

public class Mundo extends ObjetoGrafico {

    public Mundo(GL gl, int maxX, int minX, int maxY, int minY) {
	super(gl);
	super.bbox = new BBox(maxX, minX, maxY, minY);
    }

    @Override
    public void desenhar() {
	super.desenhar();
    }
}
