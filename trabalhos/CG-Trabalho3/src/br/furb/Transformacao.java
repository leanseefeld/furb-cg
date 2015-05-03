package br.furb;

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
	 * Atribui os valores de Translacao a matriz de Transformacao. Elemento
	 * Neutro eh 0 (zero).
	 * 
	 * @param x
	 * @param y
	 */
	public void atribuirTranslacao(double x, double y) {
		atribuirIdentidade();
		matriz[12] = x;
		matriz[13] = y;
	}

	/**
	 * Atribui o valor de Escala a matriz de Transformacao. Elemento Neutro é 1.
	 * 
	 * @param x
	 * @param y
	 */
	public void atribuirEscala(double x, double y) {
		atribuirIdentidade();
		matriz[0] = x;
		matriz[5] = y;
	}

	/**
	 * Atribui o valor de Rotacao (angulo) no eixo Z a matriz de Transformacao.
	 * Elemento Neutro eh 0 (zero).
	 * 
	 * @param radians
	 */
	public void atribuirRotacao(double radians) {
		atribuirIdentidade();
		matriz[0] = Math.cos(radians);
		matriz[4] = -Math.sin(radians);
		matriz[1] = Math.sin(radians);
		matriz[5] = Math.cos(radians);
	}

	public Ponto transformPoint(Ponto point) {
		Ponto pointResult = new Ponto(//
				matriz[0] * point.X + matriz[4] * point.Y + matriz[12] * point.W, //
				matriz[1] * point.X + matriz[5] * point.Y + matriz[13] * point.W);
		return pointResult;
	}

	/**
	 * Move o ponto para a posição de origem
	 * @param point 
	 * @return ponto na posição de origem
	 */
	public Ponto transformPointInverse(Ponto point) {
		System.out.println("-------------------");
		this.exibeMatriz();
		Ponto pointResult = point.clone();
		System.out.println("Antes: " + pointResult.toString());

		// Diminiu o ponto para tamnho de origem
		if (matriz[12] != 0) {
			pointResult.X -= (matriz[12] * point.W);
		}
		if (matriz[13] != 0) {
			pointResult.Y += (matriz[13] * point.W);
		}
		// Move o ponto para local de origem
		pointResult.X = (int) (pointResult.X / matriz[0]);
		pointResult.Y = (int) (pointResult.Y / matriz[5]); 
		
		//TODO: Tratar a rotação
		/*pointResult.X += (matriz[4] == 0d ? 0d : -(-(matriz[4]) * pointResult.Y));
		pointResult.Y += (matriz[1] == 0d ? 0d : -(-(matriz[1]) * pointResult.X));*/

		System.out.println("Depois: " + pointResult.toString());
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
		System.out.println("______________________");
		System.out.println("|" + GetElement(0) + " | " + GetElement(4) + " | " + GetElement(8) + " | " + GetElement(12));
		System.out.println("|" + GetElement(1) + " | " + GetElement(5) + " | " + GetElement(9) + " | " + GetElement(13));
		System.out.println("|" + GetElement(2) + " | " + GetElement(6) + " | " + GetElement(10) + " | " + GetElement(14));
		System.out.println("|" + GetElement(3) + " | " + GetElement(7) + " | " + GetElement(11) + " | " + GetElement(15));
	}

}
