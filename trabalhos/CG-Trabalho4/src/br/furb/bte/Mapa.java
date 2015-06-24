package br.furb.bte;

import br.furb.bte.objetos.Arena;
import br.furb.bte.objetos.Moto;
import br.furb.bte.objetos.Ponto;
import br.furb.bte.objetos.Rastro;

public class Mapa {

    public static final int OBSTACULO = -1;
    public static final int MOTO1 = 1;
    public static final int MOTO2 = 2;
    public static final int VAZIO = 0;

    private final Moto moto1;
    private final Moto moto2;
    private final int unidadeMedida = Moto.VELOCIDADE * 5;
    private final int tamanhoXMapa = Arena.LARGURA / unidadeMedida;
    private final int tamanhoZMapa = Arena.COMPRIMENTO / unidadeMedida;
    /**
     * Esse ajuste é necessário pois a arena não inicia na posição 0, 0 do mapa
     */
    private final int ajusteCoordenadasX = Arena.LARGURA / 2;
    private final int ajusteCoordenadasZ = Arena.COMPRIMENTO / 2;
    private int[][] mapa;

    public Mapa(Moto moto1, Moto moto2) {
	this.moto1 = moto1;
	this.moto2 = moto2;
    }

    public int[][] getMatriz() {
	return this.mapa;
    }

    public int[][] contruirMapa() {
	inserirBordas();
	inserirRastros(moto1.getRastro());
	inserirRastros(moto2.getRastro());
	inserirMoto(moto1, MOTO1);
	inserirMoto(moto2, MOTO2);

	System.out.println(this.toString());

	return this.mapa;
    }

    private void inserirBordas() {
	mapa = new int[tamanhoXMapa][tamanhoXMapa];

	for (int i = 0; i < tamanhoXMapa; i++) {
	    mapa[i][0] = OBSTACULO;
	    mapa[i][tamanhoZMapa - 1] = OBSTACULO;
	}

	for (int i = 0; i < tamanhoZMapa; i++) {
	    mapa[0][i] = OBSTACULO;
	    mapa[tamanhoXMapa - 1][i] = OBSTACULO;
	}
    }

    private void inserirMoto(Moto moto, int valorMoto) {
	Ponto pontoMoto1 = moto.getBBoxTransformada().getCentro();
	int largura = (Moto.LARGURA / unidadeMedida / 2);
	int comprimento = (int) (Moto.COMPRIMENTO / unidadeMedida / 2);

	for (int i = -largura; i <= largura; i++) {
	    for (int j = -comprimento; j <= comprimento; j++) {
		int x = (pontoMoto1.x + ajusteCoordenadasX) / unidadeMedida;
		int z = (pontoMoto1.z + ajusteCoordenadasZ) / unidadeMedida;
		//TODO Verificar se não é o contrário
		if (moto.estaNaVertical())
		    mapa[validaX(x + i)][validaZ(z + j)] = valorMoto;
		else
		    mapa[validaX(x + j)][validaZ(z + i)] = valorMoto;

	    }
	}
    }

    public int validaX(int x) {
	if (x < 0)
	    return 0;
	if (x > tamanhoXMapa)
	    return tamanhoXMapa - 1;
	return x;
    }

    public int validaZ(int z) {
	if (z < 0)
	    return 0;
	if (z > tamanhoZMapa)
	    return tamanhoZMapa - 1;
	return z;
    }

    private void inserirRastros(Rastro rastro) {
	for (Ponto ponto : rastro.getRastro()) {
	    final int x = validaX((ponto.x + ajusteCoordenadasX) / unidadeMedida);
	    final int z = validaZ((ponto.z + ajusteCoordenadasZ) / unidadeMedida);
	    mapa[x][z] = OBSTACULO;
	}
    }

    @Override
    public String toString() {
	StringBuilder str = new StringBuilder();
	str.append("Mapa: \r\n");
	for (int i = 0; i < mapa.length; i++) {
	    for (int j = 0; j < mapa[i].length; j++) {
		switch (mapa[i][j]) {
		    case OBSTACULO:
			str.append('#');
			break;
		    case MOTO1:
			str.append('1');
			break;
		    case MOTO2:
			str.append('2');
			break;
		    default:
			str.append(' ');
		}
	    }
	    str.append("\r\n");
	}
	return str.toString();
    }
}
