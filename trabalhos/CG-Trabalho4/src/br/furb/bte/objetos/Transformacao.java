package br.furb.bte.objetos;

public class Transformacao {

    static final double DEG_TO_RAD = 0.017453292519943295769236907684886;
    private double[] matriz = new double[16];

    public Transformacao() {
	this.atribuirIdentidade();
    }

    public double[] getMatriz() {
	return matriz;
    }

    public void setMatriz(double[] matriz) {
	this.matriz = matriz;
    }

    public double getTrasnlacaoX() {
	return this.matriz[12];
    }

    public double getTrasnlacaoY() {
	return this.matriz[13];
    }

    /**
     * Atribui os valores de uma matriz Identidade a matriz de Transformacao.
     */
    public void atribuirIdentidade() {
	for (int i = 0; i < 16; ++i) {
	    matriz[i] = 0d;
	}
	matriz[0] = matriz[5] = matriz[10] = matriz[15] = 1.0;
    }

    /**
     * Atribui os valores de Translacao a matriz de Transformacao. Elemento Neutro eh 0 (zero).
     * 
     * @param x
     * @param y
     * @param z
     */
    public void atribuirTranslacao(double x, double y, double z) {
	atribuirIdentidade();
	matriz[12] = x;
	matriz[13] = y;
	matriz[14] = z;
    }

    /**
     * Atribui o valor de Escala a matriz de Transformacao. Elemento Neutro é 1.
     * 
     * @param x
     * @param y
     * @param z
     */
    public Transformacao atribuirEscala(double x, double y, double z) {
	atribuirIdentidade();
	matriz[0] = x;
	matriz[5] = y;
	matriz[10] = z;
	return this;
    }

    /**
     * Atribui o valor de Rotacao (angulo) no eixo Z a matriz de Transformacao. Elemento Neutro eh 0
     * (zero).
     * 
     * @param radians
     */
    public Transformacao atribuirRotacaoZ(double radians) {
	atribuirIdentidade();
	matriz[0] = Math.cos(radians);
	matriz[4] = -Math.sin(radians);
	matriz[1] = Math.sin(radians);
	matriz[5] = Math.cos(radians);
	return this;
    }

    /**
     * Atribui o valor de Rotacao (angulo) no eixo X a matriz de Transformacao. Elemento Neutro eh 0
     * (zero).
     * 
     * @param radians
     */
    public Transformacao atribuirRotacaoX(double radians) {
	atribuirIdentidade();
	//TODO Implementar
	matriz[5] = Math.cos(radians);
	matriz[6] = -Math.sin(radians);
	matriz[9] = Math.sin(radians);
	matriz[10] = Math.cos(radians);
	return this;
    }
    
    /**
     * Atribui o valor de Rotacao (angulo) no eixo X a matriz de Transformacao. Elemento Neutro eh 0
     * (zero).
     * 
     * @param radians
     */
    public Transformacao atribuirRotacaoY(double radians) {
	atribuirIdentidade();
	matriz[0] = Math.cos(radians);
	matriz[2] = -Math.sin(radians);
	matriz[8] = Math.sin(radians);
	matriz[10] = Math.cos(radians);
	return this;
    }
    
    public Ponto transformPoint(Ponto point) {
	Ponto pointResult = new Ponto(//
		matriz[0] * point.x + matriz[4] * point.y + matriz[8] * point.z + matriz[12] * point.w, //
		matriz[1] * point.x + matriz[5] * point.y + matriz[9] * point.z + matriz[13] * point.w,
		matriz[2] * point.x + matriz[6] * point.y + matriz[10] * point.z + matriz[14] * point.w );
	return pointResult;
    }

    /**
     * Move o ponto para a posição de origem
     * 
     * @param point
     * @return ponto na posição de origem
     */
    public Ponto transformPointInverse(Ponto point) {
	Ponto pointResult = point.clone();

	// Diminiu o ponto para tamnho de origem
	if (matriz[12] != 0) {
	    pointResult.x -= (matriz[12] * pointResult.w);
	}
	if (matriz[13] != 0) {
	    pointResult.y -= (matriz[13] * pointResult.w);
	}

	// Move o ponto para local de origem
	pointResult.x = (int) (pointResult.x / matriz[0]);
	pointResult.y = (int) (pointResult.y / matriz[5]);

	//TODO: Tratar a rotação
	/*pointResult.X += (matriz[4] == 0d ? 0d : pointResult.Y) / matriz[4];
	pointResult.Y += (matriz[1] == 0d ? 0d : pointResult.X / matriz[1]);
	*/
	return pointResult;
    }

    public Transformacao transformMatrix(Transformacao t) {
	Transformacao result = new Transformacao();
	for (int i = 0; i < 16; ++i)
	    result.matriz[i] = //
	    /*    */matriz[i % 4] * t.matriz[i / 4 * 4] + //
		    matriz[(i % 4) + 4] * t.matriz[i / 4 * 4 + 1] + //
		    matriz[(i % 4) + 8] * t.matriz[i / 4 * 4 + 2] + //
		    matriz[(i % 4) + 12] * t.matriz[i / 4 * 4 + 3];
	return result;
    }

    public double GetElement(int index) {
	return matriz[index];
    }

    public void SetElement(int index, double value) {
	matriz[index] = value;
    }

    public void exibeMatriz() {
	System.out.println(this.toString());
    }

    public void exibeMatriz(double[] matriz) {
	System.out.println("______________________");
	System.out.println("|" + matriz[0] + " | " + matriz[4] + " | " + matriz[8] + " | " + matriz[12]);
	System.out.println("|" + matriz[1] + " | " + matriz[5] + " | " + matriz[9] + " | " + matriz[13]);
	System.out.println("|" + matriz[2] + " | " + matriz[6] + " | " + matriz[10] + " | " + matriz[14]);
	System.out.println("|" + matriz[3] + " | " + matriz[7] + " | " + matriz[11] + " | " + matriz[15]);
    }

    @Override
    public String toString() {
	String saida = "";
	saida += "______________________\r\n";
	saida += ("|" + matriz[0] + " | " + matriz[4] + " | " + matriz[8] + " | " + matriz[12] + "\r\n");
	saida += ("|" + matriz[1] + " | " + matriz[5] + " | " + matriz[9] + " | " + matriz[13] + "\r\n");
	saida += ("|" + matriz[2] + " | " + matriz[6] + " | " + matriz[10] + " | " + matriz[14] + "\r\n");
	saida += ("|" + matriz[3] + " | " + matriz[7] + " | " + matriz[11] + " | " + matriz[15] + "\r\n");
	return saida;
    };

}
